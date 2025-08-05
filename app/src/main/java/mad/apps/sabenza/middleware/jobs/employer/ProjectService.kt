package mad.apps.sabenza.middleware.jobs.employer

import io.reactivex.Single
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.api.JobsAPI
import mad.apps.sabenza.data.api.toEqualQueryList
import mad.apps.sabenza.data.api.toEqualQueryString
import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.data.model.jobs.EmployerProject
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.Project
import mad.apps.sabenza.data.model.jobs.ProjectStateEnum
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.service.MiddlewareService
import mad.apps.sabenza.middleware.rx.MiddlewareSingleObserver
import mad.apps.sabenza.state.action.*
import mad.apps.sabenza.state.models.EmployerModel
import mad.apps.sabenza.state.models.ProjectModel
import mad.apps.sabenza.state.util.EmployerStateUtil
import mad.apps.sabenza.state.util.MeStateUtil
import zendesk.suas.*

interface ProjectService : MiddlewareService

class ProjectServiceImpl(val jobsAPI: JobsAPI, val employerAPI: EmployerAPI) : ProjectService {
    var employer : Employer? = null

    override fun onAction(action: Action<*>, state: GetState, dispatcher: Dispatcher, continuation: Continuation) {
        when (action) {
            is EmployerUpdatedAction -> {
                this.employer = action.employer
                if (MeStateUtil.getMe(state)?.role == Role.EMPLOYER) {
                    refreshProjectsAndJobs(dispatcher)
                }
                continuation.next(action)
            }

            is RefreshProjectsAndJobsAction -> {
                if (checkForValidEmployer(state)) {
                    refreshProjectsAndJobs(dispatcher)
                }
                continuation.next(action)
            }

            is CreateNewProjectAction -> {
                if (checkForValidEmployer(state)) {
                    createNewProject(dispatcher, action.project)
                }
                continuation.next(action)
            }

            is ChangeProjectStateAction -> {
                changeProjectState(dispatcher, action.project, action.status)
                continuation.next(action)
            }

            is AddJobToProjectAction -> {
                attemptToAddJobToProject(dispatcher, action.job)
                continuation.next(action)
            }

            else -> continuation.next(action)
        }
    }

    private fun checkForValidEmployer(state: GetState): Boolean {
        val employerModel = state.state.getState(EmployerModel::class.java)

        if (employerModel != null && employerModel.hasEmployer) {
            this.employer = employerModel.employer
        }

        return  (employerModel != null && employer != null && MeStateUtil.getMe(state)?.role == Role.EMPLOYER )
    }

    private fun attemptToAddJobToProject(dispatcher: Dispatcher, job: Job) {
        jobsAPI.addJob(job)
                .flatMap { refreshEmployerProjects() }
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<List<EmployerProject>>(dispatcher) {
                    override fun onSuccess(t: List<EmployerProject>) {
                        dispatcher.dispatch(NewProjectsAvailableAction(t))
                    }
                })
    }

    private fun refreshProjectsAndJobs(dispatcher: Dispatcher) {
        if (employer != null) {
            refreshEmployerProjects()
                    .compose(NetworkTransformer())
                    .subscribe(object : MiddlewareSingleObserver<List<EmployerProject>>(dispatcher) {
                        override fun onSuccess(t: List<EmployerProject>) {
                            dispatcher.dispatch(NewProjectsAvailableAction(t))
                        }
                    })
        }
    }

    private fun createNewProject(dispatcher: Dispatcher, project: Project) {
        jobsAPI.addProject(project)
                .flatMap { refreshEmployerProjects() }
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<List<EmployerProject>>(dispatcher) {
                    override fun onSuccess(t: List<EmployerProject>) {
                        dispatcher.dispatch(NewProjectsAvailableAction(t))
                    }
                })
    }

    private fun changeProjectState(dispatcher: Dispatcher, project: Project, projectStatus: ProjectStateEnum) {
        jobsAPI.changeProjectState(project.id!!.toEqualQueryString(), projectStatus)
                .flatMap { refreshEmployerProjects() }
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<List<EmployerProject>>(dispatcher) {
                    override fun onSuccess(t: List<EmployerProject>) {
                        dispatcher.dispatch(NewProjectsAvailableAction(t))
                    }
                })
    }

    private fun refreshEmployerProjects(): Single<List<EmployerProject>> {
        return jobsAPI.listProjectsForEmployer(employerId = employer!!.id!!.toEqualQueryString())
                .flatMap {
                    val projects = it
                    jobsAPI.listJobsForProjects(it.map { it.id }.toEqualQueryList())
                            .map { Pair(projects, it) }
                }
                .map {
                    val projects = it.first
                    val jobs = it.second

                    projects.map {
                        val project = it
                        EmployerProject(project, jobs.filter { it.projectId == project.id })
                    }
                }
    }

}
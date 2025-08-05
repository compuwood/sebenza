package mad.apps.sabenza.middleware.jobs.employee

import io.reactivex.Observable
import mad.apps.sabenza.data.api.EqualsQueryString
import mad.apps.sabenza.data.api.JobsAPI
import mad.apps.sabenza.data.api.toEqualQueryString
import mad.apps.sabenza.data.model.employee.EmployeeSkill
import mad.apps.sabenza.data.model.jobs.EmployeeJobStateEnum
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.JobEmployee
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.service.MiddlewareService
import mad.apps.sabenza.middleware.rx.MiddlewareSingleObserver
import mad.apps.sabenza.state.action.*
import mad.apps.sabenza.state.models.EmployeeJobsModel
import mad.apps.sabenza.state.models.EmployeeModel
import mad.apps.sabenza.state.util.MeStateUtil
import zendesk.suas.Action
import zendesk.suas.Continuation
import zendesk.suas.Dispatcher
import zendesk.suas.GetState

interface EmployeeJobsService : MiddlewareService

class EmployeeJobsServiceImpl(val jobsAPI: JobsAPI) : EmployeeJobsService {
    override fun onAction(action: Action<*>, state: GetState, dispatcher: Dispatcher, continuation: Continuation) {
        when (action) {
            is LoginOrSignUpSuccessAction -> {
                updateAvailableEmployeeJobs(dispatcher, state.state.getState(EmployeeModel::class.java)!!.skills, action.me)
                continuation.next(action)
            }

            is RefreshEmployeeJobsAction -> {
                updateAvailableEmployeeJobs(dispatcher, state.state.getState(EmployeeModel::class.java)!!.skills, MeStateUtil.getMe(state))
                continuation.next(action)
            }

            is EmployeeUpdatedAction -> {
                getAppliedForJobs(dispatcher)
                continuation.next(action)
            }

            is RefreshLinkedJobsAction -> {
                getAppliedForJobs(dispatcher)
                continuation.next(action)
            }

            is EmployeeSkillsUpdatedAction -> {
                updateAvailableEmployeeJobs(dispatcher, action.skills, MeStateUtil.getMe(state))
                continuation.next(action)
            }

            is ApplyForJobAction -> {
                applyForJob(dispatcher, action.jobToApplyFor, state.state.getState(EmployeeModel::class.java)!!, state.state.getState(EmployeeJobsModel::class.java)!!)
                continuation.next(action)
            }

            is WithdrawFromJobAction -> {
                withdrawFromJob(dispatcher, action.jobToWithdrawFrom, state.state.getState(EmployeeModel::class.java)!!)
                continuation.next(action)
            }

            else -> continuation.next(action)
        }
    }

    private fun withdrawFromJob(dispatcher: Dispatcher, job: Job, employeeModel: EmployeeModel) {
        if (employeeModel.hasEmployee && employeeModel.employee != null) {
            jobsAPI.changeStatusOfEmployeeJob(
                    jobId = job.id!!.toEqualQueryString(),
                    reason = "Withdrawing",
                    state = EmployeeJobStateEnum.WITHDRAWN)
                    .flatMap {
                        jobsAPI.getEmployeeJobs()
                    }
                    .compose(NetworkTransformer())
                    .subscribe(object : MiddlewareSingleObserver<List<JobEmployee>>(dispatcher) {
                        override fun onSuccess(t: List<JobEmployee>) {
                            dispatcher.dispatch(JobStatusChangedAction(t))
                        }
                    })
        }
    }

    private fun applyForJob(dispatcher: Dispatcher, job: Job, employeeModel: EmployeeModel, employeeJobsModel: EmployeeJobsModel) {
        val jobAlreadyExists = employeeJobsModel.linkedJobs.map { it.jobId }.contains(job.id)

        if (employeeModel.hasEmployee && employeeModel.employee != null) {
            if (!jobAlreadyExists) {
                jobsAPI.applyForJobAsEmployee(JobEmployee(
                        jobId = job.id!!,
                        employeeId = employeeModel.employee.id!!,
                        skillId = job.skillId!!))
                        .flatMap {
                            jobsAPI.getEmployeeJobs()
                        }
                        .compose(NetworkTransformer())
                        .subscribe(object : MiddlewareSingleObserver<List<JobEmployee>>(dispatcher) {
                            override fun onSuccess(t: List<JobEmployee>) {
                                dispatcher.dispatch(JobSuccessfullyAppliedForAction(t))
                            }
                        })
            } else {
                val employeeJobId = employeeJobsModel.linkedJobs.find { it.jobId == job.id }
                jobsAPI.changeStatusOfEmployeeJob(
                        jobId = job.id!!.toEqualQueryString(),
                        reason = "Re-Applying",
                        state = EmployeeJobStateEnum.APPLIED)
                        .flatMap {
                            jobsAPI.getEmployeeJobs()
                        }
                        .compose(NetworkTransformer())
                        .subscribe(object : MiddlewareSingleObserver<List<JobEmployee>>(dispatcher) {
                            override fun onSuccess(t: List<JobEmployee>) {
                                dispatcher.dispatch(JobStatusChangedAction(t))
                            }
                        })
            }
        }
    }

    private fun getAppliedForJobs(dispatcher: Dispatcher) {
        jobsAPI.getEmployeeJobs()
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<List<JobEmployee>>(dispatcher) {
                    override fun onSuccess(jobs: List<JobEmployee>) {
                        dispatcher.dispatch(LinkedJobsUpdated(jobs))
                    }
                })
    }

    private fun updateAvailableEmployeeJobs(dispatcher: Dispatcher, skills: List<EmployeeSkill>, me: Me?) {
        if (me != null && me.role == Role.EMPLOYEE && skills.isNotEmpty()) {
            Observable.fromIterable(skills)
                    .map { it.skillId }
                    .flatMap { jobsAPI.listJobsForEmployeeSkill(EqualsQueryString(it)).toObservable() }
                    .toList()
                    .map {
                        val flatList: MutableList<Job> = ArrayList()
                        for (list in it) {
                            flatList.addAll(list)
                        }
                        flatList.toList()
                    }
                    .compose(NetworkTransformer())
                    .subscribe(object : MiddlewareSingleObserver<List<Job>>(dispatcher) {
                        override fun onSuccess(t: List<Job>) {
                            dispatcher.dispatch(NewEmployeeJobsAvailableAction(t))
                        }
                    })
        }
    }
}


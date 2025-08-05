package mad.apps.sabenza.ui.widget.tablayout.presenter

import mad.apps.sabenza.data.model.jobs.EmployeeJobStateEnum
import mad.apps.sabenza.data.model.jobs.ProjectPreviewModel
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.util.CurrencyUtil
import mad.apps.sabenza.framework.ui.ViewPresenter
import mad.apps.sabenza.state.models.EmployeeJobsModel
import mad.apps.sabenza.state.models.ProjectModel
import mad.apps.sabenza.state.util.RoleStateUtil
import mad.apps.sabenza.ui.employer.joblisting.JobListingScreen
import mad.apps.sabenza.ui.util.MissingFeature
import mad.apps.sabenza.ui.widget.tablayout.ProjectsJobsContainerView
import zendesk.suas.Store
import zendesk.suas.Subscription

class ProjectsJobsPresenter(store: Store) : ViewPresenter<ProjectsJobsContainerView>(store), ProjectsJobsPresenterInterface {

    override fun gotoSelectedJob(jobId: String, isEmployer: Boolean) {
        if (isEmployer) {
            routeTo(JobListingScreen.provideViewController().withViewArgs(Pair("jobid", jobId)))
        } else {
            routeTo(mad.apps.sabenza.ui.employee.joblisting.JobListingScreen.provideViewController().withViewArgs(Pair("jobid", jobId)))
        }
    }

    override fun getProjects(): List<ProjectPreviewModel> {
        return buildProjectPreviewModelList(store.state.getState(ProjectModel::class.java)!!)
    }

    private fun buildProjectPreviewModelList(projectModel: ProjectModel): List<ProjectPreviewModel> {
        return projectModel.projects.map { employerProject ->
            if (employerProject.linkedJobs.size == 1) {
                val job = employerProject.linkedJobs.first()
                ProjectPreviewModel(
                        jobId = job.id!!,
                        title = job.description!!,
                        description = job.description!!,
                        startDate = job.calculateStartDate(),
                        endDate = job.calculateEndDate(),
                        isProject = false,
                        isFilled = (job.filled == "1"),
                        budget = CurrencyUtil.convertCurrencyStringToDouble(job.rate!!).toInt()
                )
            } else if (employerProject.hasJobs()) {
                ProjectPreviewModel(
                        jobId = employerProject.project.id.toString()!!,
                        title = employerProject.project.description!!,
                        description = employerProject.project.description,
                        startDate = employerProject.calculateStartDate(),
                        endDate = employerProject.calculateEndDate(),
                        isProject = true,
                        isFilled = employerProject.isFilled(),
                        budget = employerProject.calculateBudget()
                )
            } else {
                null
            }
        }
                .filterNotNull()
    }

    private fun buildProjectPreviewModelList(employeeJobsModel: EmployeeJobsModel): List<ProjectPreviewModel> {
        val allJobs = employeeJobsModel.jobs
        val appliedJobs = employeeJobsModel.linkedJobs.filter { it.employeeJobState == EmployeeJobStateEnum.APPLIED }.filterNotNull()
        val confirmedJobs = employeeJobsModel.linkedJobs.filter { it.employeeJobState == EmployeeJobStateEnum.CONFIRMED }.filterNotNull()
        val availableJobs = employeeJobsModel.jobs
                .filter { !appliedJobs.map { it.jobId }.contains(it.id) }.filterNotNull()
                .filter { !confirmedJobs.map { it.jobId }.contains(it.id) }.filterNotNull()

        val list: MutableList<ProjectPreviewModel> = mutableListOf()

        list.addAll(
                appliedJobs.map {
                    val employeeJob = it
                    val job = allJobs.find { it.id == employeeJob.jobId }

                    if (job == null) {
                        null
                    } else {
                        ProjectPreviewModel(
                                jobId = job.id!!,
                                title = job.description!!,
                                description = job.description!!,
                                startDate = job.calculateStartDate(),
                                endDate = job.calculateEndDate(),
                                isProject = false,
                                isFilled = (job.filled == "1"),
                                budget = CurrencyUtil.convertCurrencyStringToDouble(job.rate!!).toInt(),
                                isAppliedFor = true)
                    }
                }.filterNotNull()
        )

        list.addAll(
                confirmedJobs.map {
                    val employeeJob = it
                    val job = allJobs.find { it.id == employeeJob.jobId }

                    if (job == null) {
                        null
                    } else {
                        ProjectPreviewModel(
                                jobId = job.id!!,
                                title = job.description!!,
                                description = job.description!!,
                                startDate = job.calculateStartDate(),
                                endDate = job.calculateEndDate(),
                                isProject = false,
                                isFilled = (job.filled == "1"),
                                budget = CurrencyUtil.convertCurrencyStringToDouble(job.rate!!).toInt(),
                                isConfirmed = true)
                    }
                }.filterNotNull())

        list.addAll(availableJobs.map { job ->
            ProjectPreviewModel(
                    jobId = job.id!!,
                    title = job.description!!,
                    description = job.description!!,
                    startDate = job.calculateStartDate(),
                    endDate = job.calculateEndDate(),
                    isProject = false,
                    isFilled = (job.filled == "1"),
                    budget = CurrencyUtil.convertCurrencyStringToDouble(job.rate!!).toInt(),
                    isForYou = true)
        })

        return list.toList()
    }


    var projectsSubscription: Subscription? = null
    var jobsSubscription: Subscription? = null

    override fun loaded() {
        if (RoleStateUtil.getRole(store) == Role.EMPLOYER) {
            projectsSubscription = store.addListener(ProjectModel::class.java, {
                view.projectsUpdated(buildProjectPreviewModelList(projectModel = it))
            })
            projectsSubscription?.addListener()
        } else if (RoleStateUtil.getRole(store) == Role.EMPLOYEE) {
            jobsSubscription = store.addListener(EmployeeJobsModel::class.java, {
                view.projectsUpdated(buildProjectPreviewModelList(it))
            })
            jobsSubscription?.informWithCurrentState()
            jobsSubscription?.addListener()
        }
    }

    override fun dropView() {
        super.dropView()
        projectsSubscription?.removeListener()
        jobsSubscription?.removeListener()
    }
}
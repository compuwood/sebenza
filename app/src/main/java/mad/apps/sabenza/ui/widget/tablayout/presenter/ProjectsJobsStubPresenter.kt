package mad.apps.sabenza.ui.widget.tablayout.presenter

import mad.apps.sabenza.data.model.jobs.ProjectPreviewModel
import mad.apps.sabenza.framework.ui.ViewPresenter
import mad.apps.sabenza.ui.employer.joblisting.JobListingScreen as EmployerJobListingScreen
import mad.apps.sabenza.ui.employee.joblisting.JobListingScreen as EmployeeJobListingScreen
import mad.apps.sabenza.ui.widget.tablayout.ProjectsJobsContainerView
import zendesk.suas.Store
import java.util.*


class ProjectsJobsStubPresenter(store: Store) : ViewPresenter<ProjectsJobsContainerView>(store), ProjectsJobsPresenterInterface {

    override fun gotoSelectedJob(jobId: String, isEmployer: Boolean) {
        if (isEmployer) {
            routeTo(EmployerJobListingScreen.provideViewController())
        } else {
            routeTo(EmployeeJobListingScreen.provideViewController())
        }
    }

    var startDate1 : Calendar = Calendar.getInstance()
    var endDate1 : Calendar = Calendar.getInstance()
    var startDate2 : Calendar = Calendar.getInstance()
    var endDate2 : Calendar = Calendar.getInstance()

    init {
        startDate1.set(2017,11,23)
        endDate1.set(2017,11,24)
        startDate2.set(2017,8,7)
        endDate2.set(2017,9,14)
    }

    val testModel = ProjectPreviewModel(
            jobId = "101",
            title = "Holiday Home Renovation",
            description = "I need a wall, a door and a mural painted for me. I also need someone to walk my dog to the pier and fetch me a cold beer",
            startDate = startDate1,
            endDate = endDate1,
            isProject = true,
            isAppliedFor = true,
            isForYou = true,
            budget = 1200
    )
    val testModel2 = ProjectPreviewModel(
            jobId = "102",
            title = "Interior Decorating",
            description = "I need a wall, a door and a mural painted for me. I also need someone to walk my dog to the pier and fetch me a cold beer",
            startDate = startDate2,
            endDate = endDate2,
            isFilled = true,
            isConfirmed = true,
            isForYou = true,
            budget = 10000
    )
    val demoList = listOf(testModel, testModel2, testModel2, testModel, testModel2)


    override fun getProjects(): List<ProjectPreviewModel> {
        return demoList
    }

    override fun loaded() {
//        view.projectsUpdated(demoList)
    }


}
package mad.apps.sabenza.ui.employer.addproject.listedjobs

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobScreen
import mad.apps.sabenza.ui.employer.addproject.projectpreview.ProjectPreviewScreen
import zendesk.suas.Store
import java.util.*

class ListedJobsStubPresenter(store : Store) : BasePresenter<ListedJobsViewInterface>(store), ListedJobsPresenterInterface {
    override fun editJob(jobId: Int) {
        routeTo(ChooseJobScreen.provideViewController())
    }

    override fun addJob() {
        routeTo(ChooseJobScreen.provideViewController())
    }

    override fun goToProjectPreview() {
        routeTo(ProjectPreviewScreen.provideViewController())
    }

    override fun getProjectJobsList(): List<JobPreviewModel> {
        var startDate: Calendar = Calendar.getInstance()
        var endDate: Calendar = Calendar.getInstance()
        val startTimeHour : Int = 8
        val startTimeMinute : Int = 0
        val endTimeHour : Int = 16
        val endTimeMinute : Int = 30
        startDate.set(2017,9,23)
        endDate.set(2017,9,24)

        val address = Address("The Hamptons, East End", "Long Island", "New York","90028-6102", "", "", "1")


        val job1 = JobPreviewModel(
                jobId = 1,
                title = "Painter",
                description = "I need a wall, a door and a mural painted for me. I also need someone to walk my dog to the pier and fetch me a cold beer",
                experience = 1,
                budget = 100,
                startDate = startDate,
                endDate = endDate,
                startTimeHour = startTimeHour,
                startTimeMinute = startTimeMinute,
                endTimeHour = endTimeHour,
                endTimeMinute = endTimeMinute,
                address = address

        )

        val job2 = JobPreviewModel(
                jobId = 1,
                title = "Panel Beater",
                description = "I need a panel beater to fix the tree-shaped dent on the front of my car",
                experience = 1,
                budget = 100,
                startDate = startDate,
                endDate = endDate,
                startTimeHour = startTimeHour,
                startTimeMinute = startTimeMinute,
                endTimeHour = endTimeHour,
                endTimeMinute = endTimeMinute,
                address = address
        )


        return listOf(job1,job2,job1)
    }

}
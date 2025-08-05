package mad.apps.sabenza.ui.employer.addproject.projectpreview

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import zendesk.suas.Store
import java.util.*

class ProjectPreviewStubPresenter(store: Store) : BasePresenter<ProjectPreviewViewInterface>(store), ProjectPreviewPresenterInterface {
    override fun getProjectName(): String {
        return "Holiday Home Renovation"
    }

    override fun getProjectDescription(): String {
        return "My place in the Hamptons needs attention. Kim and Kanye are coming over for labour day weekend and the place is a mess. I need help!"
    }

    override fun getProjectAddress(): Address {
        val address1 = Address("6834 Hollywood Blvd", "Los Angeles", "California","90028-6102", "", "", "1")
        return address1
    }

    override fun editProject() {
    }

    override fun postProject() {
        routeTo(ViewProjectsScreen.provideViewController())
    }

    override fun getProjectJobsList(): List<JobPreviewModel> {
        var startDate: Calendar = Calendar.getInstance()
        var endDate: Calendar = Calendar.getInstance()
        val startTimeHour: Int = 8
        val startTimeMinute: Int = 0
        val endTimeHour: Int = 16
        val endTimeMinute: Int = 30
        startDate.set(2017, 9, 23)
        endDate.set(2017, 9, 24)

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
                address = getProjectAddress()
        )

        val job2 = JobPreviewModel(
                jobId = 1,
                title = "Panel Beater",
                description = "I need a panel beater to fix the tree-shaped dent at the front of my car",
                experience = 1,
                budget = 100,
                startDate = startDate,
                endDate = endDate,
                startTimeHour = startTimeHour,
                startTimeMinute = startTimeMinute,
                endTimeHour = endTimeHour,
                endTimeMinute = endTimeMinute,
                address = getProjectAddress()
        )
        return listOf(job1, job2, job1)
    }
}
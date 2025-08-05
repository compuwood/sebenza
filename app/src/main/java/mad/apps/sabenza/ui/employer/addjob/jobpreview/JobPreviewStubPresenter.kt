package mad.apps.sabenza.ui.employer.addjob.jobpreview

import io.reactivex.Completable
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.rx.CompletableNetworkTransformer
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import zendesk.suas.Store
import java.util.*
import java.util.concurrent.TimeUnit

class JobPreviewStubPresenter(store: Store) : BasePresenter<JobPreviewViewInterface>(store), JobPreviewPresenterInterface {
    override fun updateWithJobViewModel(fetchViewArgument: AddJobViewModel) {
    }

    override fun getJobObject(): JobPreviewModel {

        val startDate: Calendar = Calendar.getInstance()
        val endDate: Calendar = Calendar.getInstance()
        val startTimeHour: Int = 8
        val startTimeMinute: Int = 0
        val endTimeHour: Int = 16
        val endTimeMinute: Int = 30
        startDate.set(2017, 9, 23)
        endDate.set(2017, 9, 24)

        val address = Address("The Hamptons, East End", "Long Island", "New York","90028-6102", "", "", "1")


        val job = JobPreviewModel(
                jobId = 1,
                title = "Painter",
                description = "My place in the Hamptons needs attention. Kim and Kanye are coming over for labour day weekend and the place is a mess. I need help!",
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

        return job
    }

    override fun postJob() {
        view.busy()
        Completable.complete()
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(CompletableNetworkTransformer())
                .subscribe {
                    view.success()
                }
    }

    override fun goToJobsFeed() {
        routeToNewTop(ViewProjectsScreen.provideViewController())
    }

}
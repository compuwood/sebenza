package mad.apps.sabenza.ui.employer.addproject.jobconfirm

import io.reactivex.Completable
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.rx.CompletableNetworkTransformer
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addproject.listedjobs.ListedJobsScreen
import zendesk.suas.Store
import java.util.*
import java.util.concurrent.TimeUnit

class JobConfirmStubPresenter(store : Store) : BasePresenter<JobConfirmViewInterface>(store), JobConfirmPresenterInterface {
    override fun confirmJob() {
        view.busy()
        Completable.complete()
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(CompletableNetworkTransformer())
                .subscribe {
                    view.success()
                }
    }

    override fun gotoListedJobScreen() {
        routeTo(ListedJobsScreen.provideViewController())
    }

    var startDate: Calendar = Calendar.getInstance()
    var endDate: Calendar = Calendar.getInstance()
    val startTimeHour : Int = 8
    val startTimeMinute : Int = 0
    val endTimeHour : Int = 16
    val endTimeMinute : Int = 30

    val address = Address("The Hamptons, East End", "Long Island", "New York","90028-6102", "", "", "1")

    init {
        startDate.set(2017,9,23)
        endDate.set(2017,9,24)
    }

    val testModel = JobPreviewModel(
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

    override fun getSelectedJob() : JobPreviewModel{
        return testModel
    }

}
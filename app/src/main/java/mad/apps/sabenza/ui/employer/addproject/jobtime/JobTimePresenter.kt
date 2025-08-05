package mad.apps.sabenza.ui.employer.addproject.jobtime

import io.reactivex.Completable
import mad.apps.sabenza.framework.rx.CompletableNetworkTransformer
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.employer.addjob.jobpreview.JobPreviewScreen
import mad.apps.sabenza.ui.employer.addproject.jobconfirm.JobConfirmScreen
import zendesk.suas.Store
import java.util.*
import java.util.concurrent.TimeUnit

class JobTimePresenter(store : Store) : BasePresenter<JobTimeViewInterface>(store), JobTimePresenterInterface {

    lateinit var addJobViewModel : AddJobViewModel

    override fun updateAddJobModel(model: AddJobViewModel) {
        this.addJobViewModel = model
    }

    override fun gotoJobConfirmScreen(job: Boolean) {
        if (job) {
            routeTo(JobPreviewScreen.provideViewController().withViewArgs(Pair(AddJobViewModel.argName(), addJobViewModel)))
        } else {
            routeTo(JobConfirmScreen.provideViewController())
        }
    }

    override fun saveTimeRange(startDate: Calendar, endDate: Calendar, startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) {
        view.busy()

        startDate.set(Calendar.HOUR, startHour)
        startDate.set(Calendar.MINUTE, startMinute)
        endDate.set(Calendar.HOUR, endHour)
        endDate.set(Calendar.MINUTE, endMinute)

        addJobViewModel.startDate = startDate
        addJobViewModel.endDate = endDate

        view.success()
    }

}
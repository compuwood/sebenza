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

class JobTimeStubPresenter(store : Store) : BasePresenter<JobTimeViewInterface>(store), JobTimePresenterInterface {
    override fun updateAddJobModel(model: AddJobViewModel) {

    }

    override fun gotoJobConfirmScreen(job: Boolean) {
        if (job) {
            routeTo(JobPreviewScreen.provideViewController())
        } else {
            routeTo(JobConfirmScreen.provideViewController())
        }
    }

    override fun saveTimeRange(startDate: Calendar, endDate: Calendar, startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) {
        view.busy()
        Completable.complete()
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(CompletableNetworkTransformer())
                .subscribe {
                    view.success()
                }
    }

}
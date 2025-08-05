package mad.apps.sabenza.ui.employer.addproject.jobtime

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.util.BusyViewInterface
import mad.apps.sabenza.ui.util.SuccessErrorViewInterface
import zendesk.suas.Store
import java.util.*

interface JobTimeViewInterface : ViewInterface, BusyViewInterface, SuccessErrorViewInterface {

}

interface JobTimePresenterInterface : PresenterInterface<JobTimeViewInterface> {
    fun saveTimeRange(startDate: Calendar, endDate: Calendar, startHour: Int, startMinute: Int, endHour: Int, endMinute: Int)
    fun gotoJobConfirmScreen(job: Boolean = false)
    fun updateAddJobModel(model: AddJobViewModel)
}

object JobTimeScreen : BaseScreen<JobTimeViewInterface, JobTimePresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return JobTimeViewController()
    }

    override fun providePresenter(store: Store): JobTimePresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            JobTimeStubPresenter(store)
        } else {
            JobTimePresenter(store)
        }
    }
}
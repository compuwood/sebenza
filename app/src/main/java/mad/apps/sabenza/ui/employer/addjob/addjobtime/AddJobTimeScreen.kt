package mad.apps.sabenza.ui.employer.addjob.addjobtime

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.employer.addproject.jobtime.JobTimePresenterInterface
import mad.apps.sabenza.ui.employer.addproject.jobtime.JobTimeStubPresenter
import mad.apps.sabenza.ui.employer.addproject.jobtime.JobTimeViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface AddJobTimeViewInterface : ViewInterface, BusyViewInterface {

}

object AddJobTimeScreen : BaseScreen<JobTimeViewInterface, JobTimePresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return AddJobTimeViewController()
    }

    override fun providePresenter(store: Store): JobTimePresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return JobTimeStubPresenter(store)
//        } else {
//            return JobTimePresenter(store)
//        }
    }
}
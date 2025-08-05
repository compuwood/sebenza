package mad.apps.sabenza.ui.employer.addjob.addjoblocation

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.employer.addproject.joblocation.JobLocationPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.joblocation.JobLocationStubPresenter
import mad.apps.sabenza.ui.employer.addproject.joblocation.JobLocationViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface AddJobLocationViewInterface : ViewInterface, BusyViewInterface {

}

object AddJobLocationScreen : BaseScreen<JobLocationViewInterface, JobLocationPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return AddJobLocationViewController()
    }

    override fun providePresenter(store: Store): JobLocationPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return JobLocationStubPresenter(store)
//        } else {
//            return JobLocationPresenter(store)
//        }
    }
}
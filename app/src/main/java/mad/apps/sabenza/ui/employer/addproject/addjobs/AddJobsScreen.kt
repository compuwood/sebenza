package mad.apps.sabenza.ui.employer.addproject.addjobs

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface AddJobsViewInterface : ViewInterface, BusyViewInterface {

}

interface AddJobsPresenterInterface : PresenterInterface<AddJobsViewInterface> {
    fun startAddJob()
}

object AddJobsScreen : BaseScreen<AddJobsViewInterface, AddJobsPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return AddJobsViewController()
    }

    override fun providePresenter(store: Store): AddJobsPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return AddJobsStubPresenter(store)
//        } else {
//            return ListedJobsPresenter(store)
//        }
    }
}
package mad.apps.sabenza.ui.employer.addproject.startwork

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.employer.StartWork.startwork.StartWorkStubPresenter
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface StartWorkViewInterface : ViewInterface, BusyViewInterface {

}

interface StartWorkPresenterInterface : PresenterInterface<StartWorkViewInterface> {
    fun startProject()
    fun startJob()
}

object StartWorkScreen : BaseScreen<StartWorkViewInterface, StartWorkPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return StartWorkViewController()
    }

    override fun providePresenter(store: Store): StartWorkPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
            return StartWorkStubPresenter(store)
//        } else {
//            return StartWorkPresenter(store)
//        }
    }
}
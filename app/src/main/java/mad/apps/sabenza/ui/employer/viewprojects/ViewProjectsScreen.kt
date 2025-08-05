package mad.apps.sabenza.ui.employer.viewprojects

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface ViewProjectsViewInterface : ViewInterface, BusyViewInterface {
}

interface ViewProjectsPresenterInterface : PresenterInterface<ViewProjectsViewInterface> {
    fun addProject()
}

object ViewProjectsScreen : BaseScreen<ViewProjectsViewInterface, ViewProjectsPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return ViewProjectsViewController()
    }

    override fun providePresenter(store: Store): ViewProjectsPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
            return ViewProjectsStubPresenter(store)
//        } else {
//            return ViewProjectsPresenter(store)
//        }
    }
}
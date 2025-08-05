package mad.apps.sabenza.ui.landing

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import zendesk.suas.Store

interface LandingViewInterface : ViewInterface {
}

interface LandingPresenterInterface : PresenterInterface<LandingViewInterface> {
    fun goToRoleSelectScreen()
}

object LandingScreen : BaseScreen<LandingViewInterface, LandingPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return LandingViewController()
    }

    override fun providePresenter(store: Store): LandingPresenterInterface {
        return LandingStubPresenter(store)
    }
}
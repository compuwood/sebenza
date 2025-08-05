package mad.apps.sabenza.ui.roleselect

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import zendesk.suas.Store

interface RoleSelectViewInterface : ViewInterface

interface RoleSelectPresenterInterface : PresenterInterface<RoleSelectViewInterface> {
    fun goToEmployerScreen()
    fun goToEmployeeScreen()
    fun goToLoginScreen()
}

object RoleSelectScreen : BaseScreen<RoleSelectViewInterface, RoleSelectPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return RoleSelectIntegrationViewController()
    }

    override fun providePresenter(store: Store): RoleSelectPresenterInterface {
        return RoleSelectStubPresenter(store)
    }
}
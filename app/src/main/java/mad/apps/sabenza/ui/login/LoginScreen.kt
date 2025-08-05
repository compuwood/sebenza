package mad.apps.sabenza.ui.login

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface LoginViewInterface : ViewInterface, BusyViewInterface {
    fun showLoginSuccess()
    fun showLoginError()
}

interface LoginPresenterInterface : PresenterInterface<LoginViewInterface> {
    fun resetPassword(email: String)
    fun attemptLogin(email: String, password: String)
}

object LoginScreen : BaseScreen<LoginViewInterface, LoginPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return LoginViewController()
    }

    override fun providePresenter(store: Store): LoginPresenterInterface {
        if (BuildConfig.IS_UI_BUILD) {
            return LoginStubPresenter(store)
        } else {
            return LoginPresenter(store)
        }
    }
}
package mad.apps.sabenza.ui.employer.signup.signup

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface SignUpViewInterface : ViewInterface, BusyViewInterface {
    fun failedSignUp(error: String)
    fun successfulSignUp()
}

interface SignUpPresenterInterface : PresenterInterface<SignUpViewInterface> {
    fun saveEmailAndPassword(email: String, password: String)
    fun goToCreateProfileWithEmail()
}

object SignUpScreen : BaseScreen<SignUpViewInterface, SignUpPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return SignUpViewController()
    }

    override fun providePresenter(store: Store): SignUpPresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            SignUpStubPresenter(store)
        } else {
            SignUpPresenter(store)
        }
    }
}
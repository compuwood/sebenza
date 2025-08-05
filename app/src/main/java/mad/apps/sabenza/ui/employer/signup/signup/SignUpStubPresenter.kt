package mad.apps.sabenza.ui.employer.signup.signup

import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.signup.createprofile.CreateProfileScreen
import zendesk.suas.Store

class SignUpStubPresenter(store : Store) : BasePresenter<SignUpViewInterface>(store), SignUpPresenterInterface {

    override fun saveEmailAndPassword(email: String, password: String) {
        view.successfulSignUp()
    }

    override fun goToCreateProfileWithEmail() {
        router().pushController(RouterTransaction.with(CreateProfileScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }
}
package mad.apps.sabenza.ui.employee.signup.signup

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employee.signup.createprofile.CreateProfileScreen
import zendesk.suas.Store

class SignUpStubPresenter(store : Store) : BasePresenter<SignUpViewInterface>(store), SignUpPresenterInterface {

    override fun saveEmailAndPassword(email: String, password: String) {
        view.successfulSignUp()
    }

    override fun goToCreateProfileWithEmail() {
        routeToNewTop(CreateProfileScreen.provideViewController())
    }
}
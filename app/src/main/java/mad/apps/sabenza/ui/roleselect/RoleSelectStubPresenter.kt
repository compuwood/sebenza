package mad.apps.sabenza.ui.roleselect

import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.login.LoginScreen
import zendesk.suas.Store
import mad.apps.sabenza.ui.employee.signup.signup.SignUpScreen as EmployeeSignUpScreen
import mad.apps.sabenza.ui.employer.signup.signup.SignUpScreen as EmployerSignUpScreen

class RoleSelectStubPresenter(store: Store) : BasePresenter<RoleSelectViewInterface>(store), RoleSelectPresenterInterface {

    override fun goToEmployerScreen() {
        router().pushController(RouterTransaction.with(EmployerSignUpScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }

    override fun goToEmployeeScreen() {
        router().pushController(RouterTransaction.with(EmployeeSignUpScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }

    override fun goToLoginScreen() {
        router().pushController(RouterTransaction.with(LoginScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }
}
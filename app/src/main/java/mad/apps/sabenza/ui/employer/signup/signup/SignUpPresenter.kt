package mad.apps.sabenza.ui.employer.signup.signup

import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.framework.rx.UIObservableTransformer
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.framework.rx.state.RxStateObserver
import mad.apps.sabenza.framework.rx.state.StateObserver
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.SignUpAsRoleRequestAction
import mad.apps.sabenza.state.models.LoginModel
import mad.apps.sabenza.ui.employer.signup.createprofile.CreateProfileScreen
import zendesk.suas.Store

class SignUpPresenter(store: Store) : BasePresenter<SignUpViewInterface>(store), SignUpPresenterInterface {

    var observer : RxStateObserver<LoginModel>? = null

    override fun saveEmailAndPassword(email: String, password: String) {
        observer = StateObserver.buildObserver<LoginModel> {
            if (it.inProgress) {
                view.busy()
            } else {
                view.idle()

                if (it.isSuccess) {
                    view.successfulSignUp()
                } else {
                    view.failedSignUp("Sign Up Failed")
                }
            }
        }

        RxStateBinder.dispatchAndBind(SignUpAsRoleRequestAction(username = email, password = password, role = Role.EMPLOYER),
                store,
                LoginModel::class.java)
                .compose(UIObservableTransformer())
                .subscribe(observer!!)
    }

    override fun goToCreateProfileWithEmail() {
        router().setRoot(RouterTransaction.with(CreateProfileScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }

}
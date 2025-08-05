package mad.apps.sabenza.ui.login

import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import io.reactivex.Completable
import mad.apps.sabenza.framework.rx.CompletableNetworkTransformer
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.signup.signup.SignUpScreen
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import zendesk.suas.Store
import java.util.concurrent.TimeUnit

class LoginStubPresenter(store : Store) : BasePresenter<LoginViewInterface>(store), LoginPresenterInterface {
    override fun resetPassword(email:String) {
        router().pushController(RouterTransaction.with(SignUpScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }

    override fun attemptLogin(email: String, password: String) {
        view.busy()
        Completable.complete()
                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(CompletableNetworkTransformer())
                .subscribe {
                    view.idle()
                    routeToReplaceCurrent(ViewProjectsScreen.provideViewController())
                }
    }

}
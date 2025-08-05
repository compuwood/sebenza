package mad.apps.sabenza.ui.login

import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.framework.ui.BaseViewController
import zendesk.suas.Store

class LoginPresenter(store: Store) : BasePresenter<LoginViewInterface>(store), LoginPresenterInterface {
    override fun dropView() {
        super.dropView()
    }

    override fun resetPassword(email: String) {

    }

    override fun attemptLogin(email: String, password: String) {
        view.busy()
        LoginStateInterpreter(store).dispatchLoginAndAwaitNextScreenOrFailure(email, password)
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<BaseViewController>() {

                    override fun onError(e: Throwable) {
                        view.idle()
                        view.showLoginError()
                    }

                    override fun onSuccess(t: BaseViewController) {
                        view.idle()
                        routeToNewTop(t)
                    }

                })
    }
}
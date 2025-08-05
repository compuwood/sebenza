package mad.apps.sabenza.framework.ui

import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import zendesk.suas.Store

abstract class BasePresenter<T : ViewInterface>(store: Store) : SuasPresenter(store), PresenterInterface<T> {

    var firstRun = true

    lateinit var view: T
        private set

    override fun takeView(baseViewController: T) {
        view = baseViewController
        if (firstRun) {
            firstRun = false
            init()
        }
        loaded()
    }

    override fun dropView() {

    }

    fun router(): Router {
        return view.getRouter()
    }

    protected open fun loaded() {

    }

    protected open fun init() {

    }

    protected open fun routeTo(viewController: BaseViewController) {
        router().pushController(
                RouterTransaction.with(viewController)
                        .pushChangeHandler(FadeChangeHandler())
                        .popChangeHandler(FadeChangeHandler())
        )
    }

    protected open fun routeTo(viewController: BaseViewController, pushHandler: AnimatorChangeHandler, popHandler: AnimatorChangeHandler) {
        router().pushController(
                RouterTransaction.with(viewController)
                        .pushChangeHandler(pushHandler)
                        .popChangeHandler(popHandler)
        )
    }

    protected open fun routeToNewTop(viewController: BaseViewController) {
        router().setRoot(
                RouterTransaction.with(viewController)
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler())
        )
    }

    protected open fun routeToNewTop(viewController: BaseViewController, pushHandler: AnimatorChangeHandler, popHandler: AnimatorChangeHandler) {
        router().setRoot(
                RouterTransaction.with(viewController)
                        .pushChangeHandler(pushHandler)
                        .popChangeHandler(popHandler)
        )
    }

    protected open fun routePop() {
        router().popCurrentController()
    }

    protected open fun routeToReplaceCurrent(viewController: BaseViewController) {
        router().replaceTopController(
                RouterTransaction.with(viewController)
                        .pushChangeHandler(FadeChangeHandler())
                        .popChangeHandler(FadeChangeHandler())
        )
    }

}

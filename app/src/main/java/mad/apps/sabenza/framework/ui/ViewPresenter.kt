package mad.apps.sabenza.framework.ui

import android.view.View
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import mad.apps.sabenza.SabenzaActivity
import mad.apps.sabenza.SabenzaActivity_MembersInjector
import zendesk.suas.Store

abstract class ViewPresenter<T : View>(store: Store) : SuasPresenter(store) {

    var firstRun = true

    lateinit var view : T
        private set

    open fun takeView(viewController : T) {
        view = viewController
        if (firstRun) {
            firstRun = false
            init()
        }
        loaded()
    }

    open fun dropView() {
    }

    protected open fun loaded() {

    }

    fun router() : Router {
        return (view.context as SabenzaActivity).router
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

    protected open fun routeToReplaceCurrent(viewController: BaseViewController) {
        router().replaceTopController(
                RouterTransaction.with(viewController)
                        .pushChangeHandler(FadeChangeHandler())
                        .popChangeHandler(FadeChangeHandler())
        )
    }

    protected open fun routePop() {
        router().popCurrentController()
    }






}
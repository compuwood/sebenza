package mad.apps.sabenza.middleware.rx

import com.noveogroup.android.log.LoggerManager
import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.middleware.MiddlewareError
import mad.apps.sabenza.state.action.MiddlewareErrorAction
import zendesk.suas.Dispatcher

abstract class MiddlewareSingleObserver<T: Any?>(val dispatcher: Dispatcher, val errorTag: MiddlewareError = MiddlewareError.UNKNOWN) : EnhancedSingleObserver<T>() {

    override fun onError(e: Throwable) {
        if (BuildConfig.DEBUG) {
            LoggerManager.getLogger().e(e)
        }

        if (!handleError(e)) {
            dispatcher.dispatch(MiddlewareErrorAction(e, errorTag))
        }
    }

    open fun handleError(e: Throwable) : Boolean {
        return false
    }
}
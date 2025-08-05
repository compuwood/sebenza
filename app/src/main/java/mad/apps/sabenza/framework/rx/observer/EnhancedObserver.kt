package mad.apps.sabenza.framework.rx.observer

import com.noveogroup.android.log.LoggerManager
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Cancellable
import io.reactivex.observers.DisposableObserver
import mad.apps.sabenza.framework.util.ExceptionTracker

abstract class EnhancedObserver<T> : DisposableObserver<T>() {

    override fun onError(error: Throwable) {
        ExceptionTracker.trackException(error)
        LoggerManager.getLogger().e(error)
        error(error)
    }

    open fun error(error: Throwable) {

    }
}
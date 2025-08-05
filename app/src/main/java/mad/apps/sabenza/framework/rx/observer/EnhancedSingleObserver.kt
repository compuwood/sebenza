package mad.apps.sabenza.framework.rx.observer

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import mad.apps.sabenza.framework.util.ExceptionTracker

abstract class EnhancedSingleObserver<T> : SingleObserver<T> {

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {
        ExceptionTracker.trackException(e)
    }


}
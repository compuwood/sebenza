package mad.apps.sabenza.framework

import io.reactivex.observers.TestObserver

abstract class SabenzaRPCTestObserver<T> : TestObserver<T>() {
    override fun onError(t: Throwable) {

    }

    abstract fun error(t: Throwable)
}
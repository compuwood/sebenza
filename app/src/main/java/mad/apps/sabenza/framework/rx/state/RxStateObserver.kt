package mad.apps.sabenza.framework.rx.state

import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import mad.apps.sabenza.framework.redux.RxModel

abstract class RxStateObserver<T: RxModel> : DisposableObserver<T>() {
    override fun onComplete() {

    }

    override fun onError(e: Throwable) {

    }
}

abstract class RxSingleStateObserver<T: RxModel> : DisposableSingleObserver<T>() {

    override fun onError(e: Throwable) {

    }
}

object StateObserver {

    fun <T : RxModel> buildObserver(doOnNext : (t: T) -> Unit) : RxStateObserver<T> {
        return object : RxStateObserver<T>() {
            override fun onNext(t: T) {
                doOnNext(t)
            }
        }
    }

    fun <T : RxModel> buildObserver(doOnNext : (t: T) -> Unit, doOnError : (e : Throwable) -> Unit) : RxStateObserver<T> {
        return object : RxStateObserver<T>() {
            override fun onNext(t: T) {
                doOnNext(t)
            }

            override fun onError(e: Throwable) {
                doOnError(e)
            }
        }
    }


    fun <T : RxModel> singleObserver(doOnSuccess : (t: T) -> Unit) : RxSingleStateObserver<T> {
        return object : RxSingleStateObserver<T>() {
            override fun onSuccess(t: T) {
                doOnSuccess(t)
            }
        }
    }

    fun <T : RxModel> singleObserver(doOnSuccess : (t: T) -> Unit, doOnError : (e : Throwable) -> Unit) : RxSingleStateObserver<T> {
        return object : RxSingleStateObserver<T>() {
            override fun onSuccess(t: T) {
                doOnSuccess(t)
            }

            override fun onError(e: Throwable) {
                doOnError(e)
            }
        }
    }

}
package mad.apps.sabenza.framework.rx.state

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import mad.apps.sabenza.framework.redux.Action
import mad.apps.sabenza.framework.redux.RxModel
import mad.apps.sabenza.state.models.ErrorModel
import zendesk.suas.*

class RxStateListener<T : RxModel> : Listener<T> {

    private val subject: PublishSubject<T> = PublishSubject.create()

    override fun update(state: T) {
        subject.onNext(state)
    }

    fun bind(): Observable<T> {
        return subject
    }

    fun single(): Single<T> {
        return subject.singleOrError()
    }

}

object RxStateBinder {

    fun <T : RxModel> bindState(store: Store, clazz: Class<T>, catchErrors : Boolean = true, emitFirstState: Boolean = false): Observable<T> {
        var subscription: Subscription? = null
        var hasError = false

        val observable = Observable.create<T> { subscriber ->
            subscription = store.addListener({ oldState, newState ->

                val stateChange = !(oldState.getState(clazz)?.equals(newState.getState(clazz)) ?: true)
                val errorChange = !(oldState.getState(ErrorModel::class.java)?.equals(newState.getState(ErrorModel::class.java)) ?: true)
                hasError = errorChange ?: false
                (stateChange || errorChange )

            }, { state: State ->

                if (hasError) {
                    val errorState = state.getState(ErrorModel::class.java)
                    subscriber.onError( errorState?.mostRecentError ?: Throwable("Something went wrong in the rx binder"))
                } else {
                    subscriber.onNext(state.getState(clazz)!!)
                }

            })

            subscription?.addListener()
        }
        return observable.doOnTerminate { subscription?.removeListener() }
    }

    fun <T : RxModel> bindSingleState(store: Store, clazz: Class<T>, catchErrors: Boolean = true, emitFirstState: Boolean = false): Single<T> {
        var subscription: Subscription? = null
        var hasError = false

        val single = Single.create<T> { subscriber ->
            subscription = store.addListener({ oldState, newState ->

                val stateChange = !(oldState.getState(clazz)?.equals(newState.getState(clazz)) ?: true)
                val errorChange = !(oldState.getState(ErrorModel::class.java)?.equals(newState.getState(ErrorModel::class.java)) ?: true)
                hasError = errorChange ?: false
                (stateChange || errorChange )

            }, { state: State ->

                if (hasError) {
                    val errorState = state.getState(ErrorModel::class.java)
                    subscriber.onError( errorState?.mostRecentError ?: Throwable("Something went wrong in the rx binder"))
                } else {
                    subscriber.onSuccess(state.getState(clazz)!!)
                }

            })

            subscription?.addListener()
        }

        return single.doAfterTerminate { subscription?.removeListener() }
    }

    fun <T : RxModel> dispatchAndBind(action: Action<*>, store: Store, clazz: Class<T>): Observable<T> {
        val observable = bindState(store, clazz)
        return observable.doOnSubscribe { store.dispatch(action) }
    }

    fun <T : RxModel> dispatchAndBindForResult(action: Action<*>, store: Store, clazz: Class<T>): Single<T> {
        val single = bindSingleState(store, clazz)
        return single.doOnSubscribe { store.dispatch(action) }
    }

}


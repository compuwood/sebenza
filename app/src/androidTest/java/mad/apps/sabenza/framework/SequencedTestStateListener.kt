package mad.apps.sabenza.framework

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SequencedTestStateListener<T>(initialState: T?, vararg sequencedStateTesterArg: StateTester<T>) : TestStateListener<T>(initialState, *sequencedStateTesterArg) {

    private val stateSubject : PublishSubject<Pair<Int, T>> = PublishSubject.create()
    private var count = 0;

    override fun update(state: T) {
        super.update(state)
        if (!failure && !complete) {
            stateSubject.onNext(Pair(count++, state))
        }
    }

    fun stateStream() : Observable<Pair<Int, T>> = stateSubject

}
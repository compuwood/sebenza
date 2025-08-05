package mad.apps.sabenza.framework

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import zendesk.suas.Listener

open class TestStateListener<T>(val initialState: T?, vararg sequencedStateTesterArg: StateTester<T>) : Listener<T> {

    private val sequencedStateTesters: List<StateTester<T>> = sequencedStateTesterArg.toList()
    private val max = sequencedStateTesters.size
    private var index = 0
    private var previousState = initialState

    protected var complete = false
    protected var failure = false
    protected var error: String = ""


    override fun update(state: T) {
        if (!complete && !failure) {
            val tester = sequencedStateTesters[index++]
            val stateResult = tester.test(state, previousState!!, initialState!!)
            previousState = state

            if (!stateResult) {
                index--
                error = tester.getError(state, index)

                complete = true
                failure = true
            }

            if (index == max) {
                complete = true
            }


        }
    }

    fun awaitCompletion(timeout: Long): Boolean {
        var waitSubscriber = TestObserver<Any>()


        Completable.create {
            var time: Long = 0
            while (!complete && !failure && (time < timeout)) {
                time += 100
                Thread.sleep(100)
            }

            if (time >= timeout) {
                error = "Timeout Occured waiting for response"
                failure = true
            }

            it.onComplete()
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(waitSubscriber)


        return waitSubscriber.awaitTerminalEvent()
    }

    fun awaitCompletionAndResult() : Result = awaitCompletionAndResult(5000)

    fun awaitCompletionAndResult(timeout: Long): Result {
        val waitComplete = awaitCompletion(timeout)
        return Result(error, waitComplete && completed() && success())
    }


    fun completed(): Boolean {
        return complete
    }

    fun success(): Boolean {
        return !failure && index == max
    }


}

class Result(val error : String, val isSuccess: Boolean)

interface StateTester<T> {
    fun test(newState: T, previousState: T, initialState: T): Boolean
    fun getError(stateResult: T, index: Int) = "Failed testing model " + stateResult.toString() + " at step: " + index
}

object StateTestCaseBuilder {

    fun <T> buildTestCase(error: String, testCase: (newState: T, previousState: T, initialState: T) -> Boolean): StateTester<T> {
        return object : StateTester<T> {
            override fun test(newState: T, previousState: T, initialState: T): Boolean {
                return testCase(newState, previousState, initialState)
            }

            override fun getError(stateResult: T, index: Int): String = error+ "\n" + stateResult.toString()
        }
    }

    fun <T> buildTestCase(error: String, testCase: (newState: T) -> Boolean): StateTester<T> {
        return object : StateTester<T> {
            override fun test(newState: T, previousState: T, initialState: T): Boolean {
                return testCase(newState)
            }

            override fun getError(stateResult: T, index: Int): String = error + "\n" + stateResult.toString()
        }
    }

    fun <T> buildTestCase(testCase: (newState: T) -> Boolean): StateTester<T> {
        return object : StateTester<T> {
            override fun test(newState: T, previousState: T, initialState: T): Boolean {
                return testCase(newState)
            }
        }
    }

    fun <T> buildTestCase(testCase: (newState: T, previousState: T, initialState: T) -> Boolean): StateTester<T> {
        return object : StateTester<T> {
            override fun test(newState: T, previousState: T, initialState: T): Boolean {
                return testCase(newState, previousState, initialState)
            }
        }
    }

    fun <T> buildTestCase(error: (result: T) -> String,
                          testCase: (newState: T, previousState: T, initialState: T) -> Boolean): StateTester<T> {
        return object : StateTester<T> {
            override fun test(newState: T, previousState: T, initialState: T): Boolean {
                return testCase(newState, previousState, initialState)
            }

            override fun getError(stateResult: T, index: Int): String {
                return error(stateResult) + "\n" + stateResult.toString()
            }
        }

    }

    fun <T> buildTestCase(error: (result: T) -> String,
                          testCase: (newState: T) -> Boolean): StateTester<T> {
        return object : StateTester<T> {
            override fun test(newState: T, previousState: T, initialState: T): Boolean {
                return testCase(newState)
            }

            override fun getError(stateResult: T, index: Int): String {
                return error(stateResult) + "\n" + stateResult.toString()
            }
        }
    }

}
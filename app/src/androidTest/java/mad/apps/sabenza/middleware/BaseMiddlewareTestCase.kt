package mad.apps.sabenza.middleware

import android.support.test.runner.AndroidJUnit4
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.Project
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.rpc.calls.login.LoginResponse
import mad.apps.sabenza.framework.StateTestCaseBuilder
import mad.apps.sabenza.framework.TestStateListener
import mad.apps.sabenza.framework.rx.CompletableNetworkTransformer
import mad.apps.sabenza.middleware.logging.StateLoggingTransformer
import mad.apps.sabenza.state.action.LoginAsRoleRequestAction
import mad.apps.sabenza.state.action.LoginRequestAction
import mad.apps.sabenza.state.models.LoginModel
import org.junit.Assert
import org.junit.runner.RunWith
import zendesk.suas.Store
import javax.inject.Inject

open abstract class BaseMiddlewareTestCase : BaseDaggerTestCase() {

    @Inject
    lateinit var loggingTransformer: StateLoggingTransformer

    fun loggedInState(store : Store) {
        val testStateListener = TestStateListener(
                store.state.getState(LoginModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Failed As Login Is Not In Progress",
                        testCase = { newState -> newState.inProgress }),


                StateTestCaseBuilder.buildTestCase(
                        error = { result -> "Login Failed with error: " + result.error },
                        testCase = { newState -> (newState.isSuccess && !newState.inProgress && newState.isLoggedIn) })
        )

        store.addListener(LoginModel::class.java, testStateListener)

        store.dispatch(buildLoginAction())

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)

        stabilise()
    }

    fun loggedInEmployerState(store : Store) {
        val testStateListener = TestStateListener(
                store.state.getState(LoginModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Failed As Login Is Not In Progress",
                        testCase = { newState -> newState.inProgress }),


                StateTestCaseBuilder.buildTestCase(
                        error = { result -> "Login Failed with error: " + result.error },
                        testCase = { newState -> (newState.isSuccess && !newState.inProgress && newState.isLoggedIn) })
        )

        store.addListener(LoginModel::class.java, testStateListener)

        store.dispatch(buildEmployerLoginAction())

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)

        stabilise()
    }


    fun randomLoggedInState(store : Store) {
        val me = successfullyRandomSignUp()

        val testStateListener = TestStateListener(
                store.state.getState(LoginModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Failed As Login Is Not In Progress",
                        testCase = { newState -> newState.inProgress }),


                StateTestCaseBuilder.buildTestCase(
                        error = { result -> "Login Failed with error: " + result.error },
                        testCase = { newState -> (newState.isSuccess && !newState.inProgress && newState.isLoggedIn) })
        )

        store.addListener(LoginModel::class.java, testStateListener)

        store.dispatch(LoginRequestAction(username = me.email!!, password = basePassword))

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)

        stabilise()
    }

    fun randomLoggedInEmployerState(store : Store) {
        val me = successfullyRandomSignUp()

        val testStateListener = TestStateListener(
                store.state.getState(LoginModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Failed As Login Is Not In Progress",
                        testCase = { newState -> newState.inProgress }),


                StateTestCaseBuilder.buildTestCase(
                        error = { result -> "Login Failed with error: " + result.error },
                        testCase = { newState -> (newState.isSuccess && !newState.inProgress && newState.isLoggedIn) })
        )

        store.addListener(LoginModel::class.java, testStateListener)

        store.dispatch(LoginAsRoleRequestAction(username = me.email!!, password = basePassword, role = Role.EMPLOYER))

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)

        stabilise()
    }


    private fun stabilise() {
        var waitSubscriber = TestObserver<Any>()

        Completable.create {
            Thread.sleep(2500)
            it.onComplete() }
                .compose(CompletableNetworkTransformer())
                .subscribe(waitSubscriber)

        waitSubscriber.awaitTerminalEvent()
    }

    protected fun filterStates(classes : List<Class<*>>) {
        loggingTransformer.filterAllExcept(classes)
    }

    protected fun filterState(clazz : Class<*>) {
        loggingTransformer.filterAllExcept(listOf(clazz))
    }

    fun buildEmployerLoginAction() : LoginRequestAction {
        return buildLoginAction().copy(username = baseUsername + "employer" + "@email.com")
    }

}
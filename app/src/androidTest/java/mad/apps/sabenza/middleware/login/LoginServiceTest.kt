package mad.apps.sabenza.middleware.login

import android.support.test.runner.AndroidJUnit4
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.dependancy.PreferencesProvider
import mad.apps.sabenza.framework.StateTestCaseBuilder
import mad.apps.sabenza.framework.TestStateListener
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.middleware.logging.StateLoggingTransformer
import mad.apps.sabenza.state.action.LoginRequestAction
import mad.apps.sabenza.state.action.SignUpAsRoleRequestAction
import mad.apps.sabenza.state.action.SignUpRequestAction
import mad.apps.sabenza.state.models.LoginModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import zendesk.suas.Dispatcher
import zendesk.suas.Store
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class LoginServiceTest : BaseDaggerTestCase() {

    @Inject
    lateinit var loginService: LoginService

    @Inject
    lateinit var preferencesProvider: PreferencesProvider

    @Inject
    lateinit var dispatcher: Dispatcher

    @Inject
    lateinit var store: Store

    @Inject
    lateinit var loggingTransformer : StateLoggingTransformer

    @Before
    override fun setupDependencies() {
        component.inject(this)
        loggingTransformer.filterAllExcept(listOf(LoginModel::class.java))
    }

    @Test
    fun testFailedLogin() {
        val testStateListener = TestStateListener(
                store.state.getState(LoginModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Failed As Login Is Not In Progress",
                        testCase = { newState -> newState.inProgress }),


                StateTestCaseBuilder.buildTestCase(
                        error = "Login Succeeded When It Should Have Failed",
                        testCase = { newState -> (!newState.isSuccess && !newState.inProgress && !newState.isLoggedIn) })
        )

        store.addListener(LoginModel::class.java, testStateListener)

        dispatcher.dispatch(LoginRequestAction(username = "androidTestUser2@email.com", password = "androidTestPass123"))

        val result = testStateListener.awaitCompletionAndResult(2000)
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun testSuccessfulLogin() {
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

        dispatcher.dispatch(buildLoginAction())

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun testNewSignUp() {
        val testStateListener = TestStateListener(
                store.state.getState(LoginModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Failed As Sign Up Is Not In Progress",
                        testCase = { newState -> newState.inProgress }
                ),

                StateTestCaseBuilder.buildTestCase(
                        error = { result -> "Failed To Successfully Sign Up New Account with Error: " + result.error },
                        testCase = { newState -> newState.isSuccess && !newState.inProgress && newState.isLoggedIn }
                )
        )

        store.addListener(LoginModel::class.java, testStateListener)

        dispatcher.dispatch(SignUpRequestAction(username = "newuser" + TestStringHelper.nextPrintableString(4) + "@email.com", password = "newpassword"))

        val result = testStateListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun testExistingSignUp() {
        val testStateListener = TestStateListener(
                store.state.getState(LoginModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Failed As Sign Up Is Not In Progress",
                        testCase = { newState -> newState.inProgress }
                ),

                StateTestCaseBuilder.buildTestCase(
                        error = "Account Signed Up Succesfully when it should have failed",
                        testCase = { newState -> !newState.isSuccess && !newState.inProgress && !newState.isLoggedIn }
                )
        )

        store.addListener(LoginModel::class.java, testStateListener)

        dispatcher.dispatch(SignUpRequestAction(username = "androidTestUser2@email.com", password = "newpassword"))

        val result = testStateListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun testSignUpAsEmployer() {
        val testStateListener = TestStateListener(
                store.state.getState(LoginModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Failed As Sign Up Is Not In Progress",
                        testCase = { newState -> newState.inProgress }
                ),

                StateTestCaseBuilder.buildTestCase(
                        error = { result -> "Failed To Successfully Sign Up New Account with Error: " + result.error },
                        testCase = { newState ->
                            newState.isSuccess
                                    && !newState.inProgress
                                    && newState.isLoggedIn
                                    && newState.me!!.role == Role.EMPLOYER
                        }
                )
        )

        store.addListener(LoginModel::class.java, testStateListener)

        dispatcher.dispatch(SignUpAsRoleRequestAction(
                username = "newuser" + TestStringHelper.nextPrintableString(4) + "@email.com",
                password = "newpassword",
                role = Role.EMPLOYER))

        val result = testStateListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)
    }

}
package mad.apps.sabenza.api.login

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.rpc.calls.RPCEndpointError
import mad.apps.sabenza.data.rpc.calls.login.LoginRequest
import mad.apps.sabenza.data.rpc.calls.login.LoginResponse
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.data.rpc.calls.role.EmployeeRoleRequest
import mad.apps.sabenza.data.rpc.calls.role.EmployerRoleRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpResponse
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginTest : BaseDaggerTestCase() {

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testSuccessfulSignUp() {
        val suffix = TestStringHelper.nextPrintableString(4)
        val username = baseUsername + suffix
        val testSubscriber = TestObserver<SignUpResponse>()

        sabenzaAPI.signup(request = SignUpRequest(
                email = username + "@email.com",
                password = basePassword))
                .compose(NetworkTransformer<SignUpResponse>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertComplete()

        assert(testSubscriber.values().size == 1)
        assert(testSubscriber.values().first().me?.username == username)
    }


    @Test
    fun testExistingSignUp() {
        val username = baseUsername
        val testSubscriber = TestObserver<SignUpResponse>()

        sabenzaAPI.signup(request = SignUpRequest(
                email = username + "@email.com",
                password = basePassword))
                .compose(NetworkTransformer<SignUpResponse>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertError(HttpException::class.java)
        assert(testSubscriber.errors().first() is HttpException)

        val exception: HttpException = testSubscriber.errors().first() as HttpException

        val endpointError = RPCEndpointError(exception)
        assert(endpointError.valid())
        assert(endpointError.errorResponse?.code == "23505")
    }

    @Test
    fun testExistingEmployerSignUp() {
        val username = baseUsername + "employer"
        val testSubscriber = TestObserver<Any>()

        sabenzaAPI.signup(request = SignUpRequest(
                email = username + "@email.com",
                password = basePassword))
                .flatMap {
                    sabenzaAPI.switchRole(EmployerRoleRequest())
                }
                .compose(NetworkTransformer<Any>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertError(HttpException::class.java)
    }

    @Test
    fun testSuccessfulLogin() {
        val testSubscriber = TestObserver<LoginResponse>()

        sabenzaAPI.login(LoginRequest(
                email = baseUsername + "@email.com",
                password = basePassword
        ))
                .compose(NetworkTransformer<LoginResponse>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        assert(testSubscriber.values().first().me?.username == baseUsername)

        testSubscriber.assertComplete()
    }

    @Test
    fun testFailedLogin() {
        val testSubscriber = TestObserver<LoginResponse>()

        sabenzaAPI.login(LoginRequest(
                email = baseUsername + "@email.com",
                password = "fakePassword!"
        ))
                .compose(NetworkTransformer<LoginResponse>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertError(HttpException::class.java)
        assert(testSubscriber.errors().first() is HttpException)

        val exception: HttpException = testSubscriber.errors().first() as HttpException

        val endpointError = RPCEndpointError(exception)
        assert(endpointError.valid())
        assert(endpointError.errorResponse?.code == "P0001")
    }

    @Test
    fun testSuccessfulEmployerLogin() {
        val testSubscriber = TestObserver<LoginResponse>()

        sabenzaAPI.login(LoginRequest(
                email = baseUsername + "employer" + "@email.com",
                password = basePassword
        ))
                .map {
                    httpProvider.token = it.token!!
                    it
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        assert(testSubscriber.values().first().me?.username == baseUsername + "employer")
        assert(testSubscriber.values().first().me?.role == Role.EMPLOYER)

        testSubscriber.assertComplete()
    }


    @Test
    fun testGetInvalidMe() {
        val testSubscriber = TestObserver<Me>()

        sabenzaAPI.fetchMe()
                .compose(NetworkTransformer<Me>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertNotComplete()

        val exception: HttpException = testSubscriber.errors().first() as HttpException

        val endpointError = RPCEndpointError(exception)
        assert(endpointError.valid())
        assert(endpointError.errorResponse?.code == "42501")
    }

    @Test
    fun testGetValidAuth() {
        loginToReceiveToken()
        val testSubscriber = TestObserver<Me>()

        sabenzaAPI.fetchMe()
                .compose(NetworkTransformer<Me>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertComplete()
    }

    @Test
    fun testSwitchRoles() {
        loginToReceiveToken()

        var testSubscriber = TestObserver<Any>()

        sabenzaAPI.switchRole(EmployerRoleRequest())
                .compose(NetworkTransformer<Any>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertComplete()

        testSubscriber = TestObserver<Any>()

        sabenzaAPI.switchRole(EmployeeRoleRequest())
                .compose(NetworkTransformer<Any>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertComplete()

        testSubscriber = TestObserver<Any>()

        sabenzaAPI.switchRole(EmployeeRoleRequest())
                .compose(NetworkTransformer<Any>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertComplete()
    }

    @Test
    fun refreshToken() {
        successfullyLogin()

        val testSubscriber = TestObserver<String>()

        sabenzaAPI.refreshTokenPost()
                .compose(NetworkTransformer())
                .map {
                    httpProvider.token = it
                    it
                }
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    fun loginToReceiveToken() {
        val testSubscriber = TestObserver<LoginResponse>()

        sabenzaAPI.login(LoginRequest(
                email = baseUsername + "@email.com",
                password = basePassword
        ))
                .compose(NetworkTransformer<LoginResponse>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        if (testSubscriber.values().first().token != null) {
            httpProvider.token = testSubscriber.values().first().token!!
        }
    }

    @Test
    fun breakAnAcccount() {
        baseUsername = baseUsername + TestStringHelper.nextPrintableString(4)
        val testSubscriber = TestObserver<SignUpResponse>()

        sabenzaAPI.signup(request = SignUpRequest(
                email = baseUsername + "@email.com",
                password = basePassword))
                .compose(NetworkTransformer<SignUpResponse>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        testSuccessfulLogin();
        testSwitchRoles()
        testSuccessfulLogin();
    }

    @Test
    fun breakAnAcccountWithSwitchRole() {
        baseUsername = baseUsername + TestStringHelper.nextPrintableString(4)
        val testSubscriber = TestObserver<SignUpResponse>()

        sabenzaAPI.signup(request = SignUpRequest(
                email = baseUsername + "@email.com",
                password = basePassword))
                .compose(NetworkTransformer<SignUpResponse>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        loginToReceiveToken()

        var newTestSubscriber = TestObserver<String>()

        sabenzaAPI.switchRole(EmployerRoleRequest())
                .compose(NetworkTransformer())
                .map {
                    httpProvider.token = ""
                    it
                }
                .subscribe(newTestSubscriber)

        newTestSubscriber.awaitTerminalEvent()

        newTestSubscriber.assertComplete()

        testSuccessfulLogin();
    }

}

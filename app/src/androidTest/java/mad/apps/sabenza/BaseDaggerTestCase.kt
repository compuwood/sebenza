package mad.apps.sabenza

import android.support.test.InstrumentationRegistry
import io.reactivex.observers.TestObserver
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.api.SabenzaAPI
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.data.rpc.calls.login.LoginRequest
import mad.apps.sabenza.data.rpc.calls.login.LoginResponse
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.data.rpc.calls.role.EmployeeRoleRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpResponse
import mad.apps.sabenza.dependancy.OkHttpClientProvider
import mad.apps.sabenza.di.APIModule
import mad.apps.sabenza.di.AppModule
import mad.apps.sabenza.di.ServiceModule
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.state.action.LoginRequestAction
import org.junit.Before
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val BASE_USERNAME = "androidTestUser11"
const val BASE_PASSWORD = "androidTestPass"

open abstract class BaseDaggerTestCase {

    @Inject lateinit var httpProvider: OkHttpClientProvider

    @Inject lateinit var sabenzaAPI : SabenzaAPI

    val component: TestComponent

    init {
        val application = InstrumentationRegistry.getTargetContext().applicationContext as SabenzaApplication

        component = DaggerTestComponent.builder()
                .appModule(AppModule(application))
                .aPIModule(APIModule())
                .serviceModule(ServiceModule())
                .build()
    }

    var baseUsername = BASE_USERNAME
    var basePassword = BASE_PASSWORD

    @Before
    abstract fun setupDependencies();

    open fun successfullyLogin(): Me {
        return sabenzaAPI.login(LoginRequest(
                email = baseUsername + "@email.com",
                password = basePassword))
                .map {
                    httpProvider.token = it.token!!
                    it
                }
                .flatMap { sabenzaAPI.switchRole(EmployeeRoleRequest()) }
                .map {
                    httpProvider.token = it
                    it
                }
                .flatMap { sabenzaAPI.fetchMe() }
                .compose(NetworkTransformer<Me>())
                .delay(750, TimeUnit.MILLISECONDS)
                .blockingGet()
    }

    open fun successfullyRandomSignUp(): Me {
        val testSubscriber = TestObserver<SignUpResponse>()

        sabenzaAPI.signup(SignUpRequest(
                email = baseUsername + TestStringHelper.nextPrintableString(4) + "@email.com",
                password = basePassword
        ))
                .compose(NetworkTransformer<SignUpResponse>())
                .map {
                    httpProvider.token = it.token!!
                    it
                }
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        assert(testSubscriber.values().first().me?.username == baseUsername)

        testSubscriber.assertComplete()

        return testSubscriber.values().first().me!!
    }

    open fun addRandomEmployeeToUser(me: Me, employeeAPI: EmployeeAPI) : Employee {
        val testSubscriber = TestObserver<Employee>()

        employeeAPI.addEmployee("New", "Employee" + TestStringHelper.nextPrintableString(3), me.email!!, "0821234567")
                .compose(NetworkTransformer<Employee>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        assert(testSubscriber.values().first().email == me.email)

        testSubscriber.assertComplete()

        return testSubscriber.values().first()
    }

    fun buildLoginAction() : LoginRequestAction {
        return LoginRequestAction(username = baseUsername + "@email.com", password = basePassword)
    }


}
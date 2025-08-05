package mad.apps.sabenza.api.employee

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerEmployerTestCase
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EmployerToEmployeeAPITest : BaseDaggerEmployerTestCase() {

    @Inject lateinit var employeeAPI : EmployeeAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun loginAsEmployerThenEmployeeThenFetchDetails() {
        successfullyLogin()
        val me = successfullyLoginAsEmployee()

        val testSubscriber = TestObserver<Employee>()

        employeeAPI.fetchEmployeeForCurrentUser()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().first().email == me.email)
    }


    @Test
    fun loginAsEmployerThenTryFetchEmployeeDetails() {
        val me = successfullyLogin()

        val testSubscriber = TestObserver<Employee>()

        employeeAPI.fetchEmployeeForCurrentUser()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNotComplete()
    }

    @Test
    fun loginAsEmployeeThenTryFetchEmployeeDetails() {
        val testSubscriber = TestObserver<Employee>()

        val me = successfullyLoginAsEmployee()

        employeeAPI.fetchEmployeeForCurrentUser()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().first().email == me.email)
    }
}
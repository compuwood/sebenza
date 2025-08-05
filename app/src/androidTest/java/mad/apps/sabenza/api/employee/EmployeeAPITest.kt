package mad.apps.sabenza.api.employee

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.api.EqualsQueryString
import mad.apps.sabenza.data.model.employee.EmployeeAddress
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EmployeeAPITest : BaseDaggerTestCase() {

    @Inject lateinit var employeeAPI : EmployeeAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testAddEmployee() {
        val testSubscriber = TestObserver<Employee>()

        val me = successfullyRandomSignUp()

        employeeAPI.addEmployee(
                firstName = "New",
                lastName = "User",
                phoneNumber = "0820808081",
                email = me.email!!)
                .compose(NetworkTransformer<Employee>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertComplete()
        assert(testSubscriber.values().first().firstName == "New")
        assert(testSubscriber.values().first().email == me.email!!)
    }

    @Test
    fun testAddExisitngEmployee() {
        val testSubscriber = TestObserver<Employee>()

        val me = successfullyLogin()

        employeeAPI.addEmployee(
                firstName = "New",
                lastName = "User",
                phoneNumber = "0820808081",
                email = me.email!!)
                .compose(NetworkTransformer<Employee>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertNotComplete()
        Assert.assertTrue(testSubscriber.errorCount() > 0)
    }


    @Test
    fun getEmployeeForUser() {
        val testSubscriber = TestObserver<Employee>()

        val me = successfullyLogin()

        employeeAPI.fetchEmployeeForCurrentUser()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().first().email == me.email)
    }

    @Test
    fun getAddressesForEmployee() {
        val testSubscriber = TestObserver<List<EmployeeAddress>>()

        val me = successfullyLogin()

        employeeAPI.fetchAddressesForCurrentEmployee()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

    @Test
    fun updateExistingEmployee() {
        var testSubscriber = TestObserver<Employee>()

        successfullyLogin()

        employeeAPI.fetchEmployeeForCurrentUser()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        val employee = testSubscriber.values().first()
        val newLastName = TestStringHelper.nextPrintableString(8)

        testSubscriber = TestObserver()

        employeeAPI.updateEmployee(
                firstName = employee.firstName!!,
                lastName =  newLastName,
                phoneNumber = "082121212",
                email = employee.email!!)
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().lastName != employee.lastName)
        Assert.assertTrue(testSubscriber.values().first().lastName.equals(newLastName))


    }

    @Test
    fun addAddressForEmployee() {

    }

    @Test
    fun testAddSkillToEmployee() {

    }


}
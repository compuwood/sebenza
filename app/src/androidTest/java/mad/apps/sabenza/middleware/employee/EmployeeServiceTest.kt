package mad.apps.sabenza.middleware.employee

import android.support.test.runner.AndroidJUnit4
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.StateTestCaseBuilder
import mad.apps.sabenza.framework.TestStateListener
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.middleware.BaseMiddlewareTestCase
import mad.apps.sabenza.state.action.AddOrUpdateEmployeeAction
import mad.apps.sabenza.state.models.EmployeeModel
import mad.apps.sabenza.state.util.EmployeeStateUtil
import mad.apps.sabenza.state.util.MeStateUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import zendesk.suas.Store
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EmployeeServiceTest : BaseMiddlewareTestCase() {

    @Inject
    lateinit var store: Store

    @Before
    override fun setupDependencies() {
        component.inject(this)
        filterStates(listOf(EmployeeModel::class.java))
    }

    @Test
    fun testEmployeeUpdates() {
        val testStateListener = TestStateListener(
                store.state.getState(EmployeeModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "New Employee Not Found",
                        testCase = { newState -> newState.hasEmployee && (newState.employee != null) }
                )
        )

        store.addListener(EmployeeModel::class.java, testStateListener)

        store.dispatch(buildLoginAction())

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun testAddNewEmployeeData() {
        randomLoggedInState(store)

        Assert.assertTrue(!store.state.getState(EmployeeModel::class.java)!!.hasEmployee)

        val newEmployee: Employee = Employee(
                firstName = "Random Employee",
                lastName = TestStringHelper.nextPrintableString(8),
                email = MeStateUtil.getMe(store)!!.email,
                phoneNumber = "082322323")

        val testStateListener = TestStateListener(
                store.state.getState(EmployeeModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Employee Was Not Added",
                        testCase = {
                            newState -> newState.hasEmployee
                                && newState.employee != null
                                && newState.employee!!.firstName == newEmployee.firstName})
        )

        store.addListener(EmployeeModel::class.java, testStateListener)

        store.dispatch(AddOrUpdateEmployeeAction(newEmployee))

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)

    }

    @Test
    fun testUpdateEmployeeData() {
        loggedInState(store)

        Assert.assertTrue(store.state.getState(EmployeeModel::class.java)!!.hasEmployee)

        val currentEmployee = EmployeeStateUtil.getEmployee(store)!!
        val newEmployee: Employee = Employee(
                firstName = currentEmployee.firstName,
                lastName = TestStringHelper.nextPrintableString(8),
                email = MeStateUtil.getMe(store)!!.email,
                phoneNumber = "082322323")

        val testStateListener = TestStateListener(
                store.state.getState(EmployeeModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Employee Was Not Updated",
                        testCase = {
                            newState -> newState.hasEmployee
                                && newState.employee != null
                                && newState.employee!!.firstName == newEmployee.firstName})
        )

        store.addListener(EmployeeModel::class.java, testStateListener)

        store.dispatch(AddOrUpdateEmployeeAction(newEmployee))

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)

        Assert.assertTrue(EmployeeStateUtil.getEmployee(store)!!.lastName == newEmployee.lastName)

    }

}
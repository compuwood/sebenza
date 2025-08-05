package mad.apps.sabenza.middleware.address

import android.support.test.runner.AndroidJUnit4
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.framework.StateTestCaseBuilder
import mad.apps.sabenza.framework.TestStateListener
import mad.apps.sabenza.middleware.BaseMiddlewareTestCase
import mad.apps.sabenza.state.action.AddAddressAndLinkAction
import mad.apps.sabenza.state.models.AddressModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import zendesk.suas.Store
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
class AddressServiceTest : BaseMiddlewareTestCase() {

    @Inject
    lateinit var store: Store

    @Before
    override fun setupDependencies() {
        component.inject(this)
        filterStates(listOf(AddressModel::class.java))
    }

    @Test
    fun getAddressesAtLogin() {
        var testStateListener = TestStateListener(
                store.state.getState(AddressModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Address Has Not Been Added or Linked",
                        testCase = {
                            newState ->
                            newState.employeeAddress.isNotEmpty()
                                    && newState.availableBackendAddress.isNotEmpty()
                                    && newState.selectedEmployeeAddress != null
                        }
                ))

        store.addListener(AddressModel::class.java, testStateListener)

        store.dispatch(buildLoginAction())

        var result = testStateListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun addAndLinkAnAddress() {
        loggedInState(store)

        var testStateListener = TestStateListener(
                store.state.getState(AddressModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Address Has Not Been Added or Linked",
                        testCase = {
                            newState ->
                            newState.employeeAddress.isNotEmpty()
                                    && newState.availableBackendAddress.isNotEmpty()
                                    && newState.selectedEmployeeAddress != null
                        }
                ))

        store.addListener(AddressModel::class.java, testStateListener)

        store.dispatch(AddAddressAndLinkAction(
                Address(
                        line1 = "A Place",
                        line2 = "A Road",
                        countryId = "1",
                        cityTown = "Durban",
                        postcode = "4120")
        ))

        var result = testStateListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)

    }


}
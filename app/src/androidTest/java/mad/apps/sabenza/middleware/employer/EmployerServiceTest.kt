package mad.apps.sabenza.middleware.employer

import android.support.test.runner.AndroidJUnit4
import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.framework.StateTestCaseBuilder
import mad.apps.sabenza.framework.TestStateListener
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.middleware.BaseMiddlewareTestCase
import mad.apps.sabenza.state.action.AddOrUpdateEmployerAction
import mad.apps.sabenza.state.models.EmployerModel
import mad.apps.sabenza.state.util.MeStateUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import zendesk.suas.Store
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EmployerServiceTest : BaseMiddlewareTestCase() {

    @Inject
    lateinit var store: Store

    @Before
    override fun setupDependencies() {
        component.inject(this)
        filterStates(listOf(EmployerModel::class.java))
    }

    @Test
    fun testFetchEmployerData() {
        val testStateListener = TestStateListener(
                store.state.getState(EmployerModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "No Employer Data Retrieved",
                        testCase = { newState -> newState.hasEmployer && newState.employer!!.firstName != null }
                )
        )

        store.addListener(EmployerModel::class.java, testStateListener)
        store.dispatch(buildEmployerLoginAction())

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun testAddNewEmployerData() {
        randomLoggedInEmployerState(store)

        Assert.assertTrue(!store.state.getState(EmployerModel::class.java)!!.hasEmployer)

        val newEmployer: Employer = Employer(
                firstName = "New Employer",
                lastName = TestStringHelper.nextPrintableString(8),
                pictureId = "5",
                email = MeStateUtil.getMe(store)!!.email,
                aboutMe = "I'm an employer", aboutCompany = "And this is my company",
                phoneNumber = "08243343432")

        val testStateListener = TestStateListener(
                store.state.getState(EmployerModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "There is no Employer data retrieved",
                        testCase = { newState ->
                            newState.hasEmployer
                                    && newState.employer!!.firstName != null
                                    && newState.employer!!.firstName == newEmployer.firstName
                        }
                ))

        store.addListener(EmployerModel::class.java, testStateListener)
        store.dispatch(AddOrUpdateEmployerAction(newEmployer))

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun testUpdateEmployerData() {
        loggedInEmployerState(store)

        Assert.assertTrue(store.state.getState(EmployerModel::class.java)!!.hasEmployer)

        val updatedEmployer = store.state.getState(EmployerModel::class.java)!!.employer!!.copy(
                lastName = TestStringHelper.nextPrintableString(8)
        )

        val testStateListener = TestStateListener(
                store.state.getState(EmployerModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "There is no Employer data retrieved",
                        testCase = { newState ->
                            newState.hasEmployer
                                    && newState.employer!!.firstName != null
                                    && newState.employer!!.lastName == updatedEmployer.lastName
                        }
                ))

        store.addListener(EmployerModel::class.java, testStateListener)
        store.dispatch(AddOrUpdateEmployerAction(updatedEmployer))

        val result = testStateListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)
    }



}
package mad.apps.sabenza.middleware.payment

import android.support.test.runner.AndroidJUnit4
import mad.apps.sabenza.data.model.payment.BankAccount
import mad.apps.sabenza.data.model.payment.CreditCard
import mad.apps.sabenza.framework.StateTestCaseBuilder
import mad.apps.sabenza.framework.TestStateListener
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.middleware.BaseMiddlewareTestCase
import mad.apps.sabenza.state.action.AddAndLinkBankAccountAction
import mad.apps.sabenza.state.action.AddCreditCardAction
import mad.apps.sabenza.state.action.LoginAsRoleRequestAction
import mad.apps.sabenza.state.models.PaymentModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import zendesk.suas.Store
import javax.inject.Inject
import org.junit.Assert
import java.util.*
import kotlin.collections.RandomAccess

@RunWith(AndroidJUnit4::class)
class PaymentServiceTest : BaseMiddlewareTestCase() {

    @Inject
    lateinit var store : Store

    @Before
    override fun setupDependencies() {
        component.inject(this)
        filterState(PaymentModel::class.java)
    }

    @Test
    fun fetchCards() {
        var stateListener = TestStateListener(
                store.state.getState(PaymentModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "No Credit Cards Received",
                        testCase = {newState -> newState.linkedCards.isNotEmpty() }
                )
        )

        store.addListener(PaymentModel::class.java, stateListener)
        store.dispatch(buildEmployerLoginAction())

        val result = stateListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun addAndLinkCard() {
        loggedInEmployerState(store)

        val newCreditCard = CreditCard(
                partOfNumber = TestStringHelper.nextPrintableNumber(6),
                nameOnCard = "John " + TestStringHelper.nextPrintableString(5),
                expiryMonth = Random().nextInt(11) + 1,
                expiryYear = Random().nextInt(10) + 2018
        )


        val stateTestListener = TestStateListener(
                store.state.getState(PaymentModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Credit Card Not Linked",
                        testCase = {newState -> newState.linkedCards.isNotEmpty()
                                && newState.linkedCards.last().creditCard.partOfNumber == newCreditCard.partOfNumber
                                && (newState.defaultCard != null)
                                && newState.defaultCard!!.creditCard.partOfNumber == newCreditCard.partOfNumber
                                && newState.defaultCard!!.employerCreditCard != null
                                && newState.defaultCard!!.employerCreditCard.credit_card_id == newState.defaultCard!!.creditCard.id
                        }
                )
        )

        store.addListener(PaymentModel::class.java, stateTestListener)
        store.dispatch(AddCreditCardAction(newCreditCard))

        val result = stateTestListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)
    }


    @Test
    fun addAndLinkBankAccount() {
        loggedInState(store)

        val newBankAccount = BankAccount(
                bankName = "First National Moneybags",
                accountName = "Cheque",
                branchNumber = TestStringHelper.nextPrintableNumber(6),
                accountNumber = TestStringHelper.nextPrintableNumber(6))

        val stateTestListener = TestStateListener(
                store.state.getState(PaymentModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "Bank Account Not Linked",
                        testCase = { newState -> newState.hasBankAccounts() && newState.defaultAccount!!.bankAccount.accountNumber == newBankAccount.accountNumber  }
                )
        )

        store.addListener(PaymentModel::class.java, stateTestListener)
        store.dispatch(AddAndLinkBankAccountAction(newBankAccount))

        val result = stateTestListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun listLinkedBankAccounts() {
        val stateTestListener = TestStateListener(
                store.state.getState(PaymentModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "No Linked Bank Accounts Available",
                        testCase = {newState -> newState.hasBankAccounts() }
                )
        )

        store.addListener(PaymentModel::class.java, stateTestListener)
        store.dispatch(buildLoginAction())

        val result = stateTestListener.awaitCompletionAndResult(10000)
        Assert.assertTrue(result.error, result.isSuccess)
    }
}
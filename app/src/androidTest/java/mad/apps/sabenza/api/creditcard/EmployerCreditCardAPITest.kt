package mad.apps.sabenza.api.creditcard

import io.reactivex.observers.TestObserver
import io.reactivex.rxkotlin.zipWith
import junit.framework.Assert
import mad.apps.sabenza.BaseDaggerEmployerTestCase
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.api.PaymentAPI
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.model.payment.CreditCard
import mad.apps.sabenza.data.model.payment.EmployerCreditCard
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class EmployerCreditCardAPITest : BaseDaggerEmployerTestCase() {

    @Inject lateinit var paymentAPI : PaymentAPI

    @Inject lateinit var employersAPI : EmployerAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testAddCreditCard() {
        val testSubscriber = TestObserver<Any>()

        val me = successfullyLogin()

        Assert.assertTrue(me.role == Role.EMPLOYER)

        paymentAPI.addCreditCard(
                CreditCard(partOfNumber = "510141", nameOnCard = "John Doe", expiryMonth = 12, expiryYear = 2020))
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

    @Test
    fun testAddExpiredCreditCard() {
        val testSubscriber = TestObserver<Any>()

        val me = successfullyLogin()

        Assert.assertTrue(me.role == Role.EMPLOYER)

        paymentAPI.addCreditCard(
                CreditCard(partOfNumber = "510141", nameOnCard = "John Doe", expiryMonth = 12, expiryYear = 2015))
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNotComplete()

        Assert.assertTrue(testSubscriber.errorCount() == 1)
//        var endpointError = RPCEndpointError(testSubscriber.errors().first() as HttpException)
//        Assert.assertTrue(endpointError.errorResponse!!.message!!.contains("credit_cards_expiry_year_check"))
    }

    @Test
    fun testLinkCreditCardToEmployer() {
        val testSubscriber = TestObserver<Any>()

        val me = successfullyRandomSignUp()
        addRandomEmployerToUser(me, employersAPI)

        paymentAPI.addCreditCard(
                CreditCard(partOfNumber = "510141", nameOnCard = "John Doe", expiryMonth = 12, expiryYear = 2025))
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        paymentAPI.listAllCards()
                .zipWith(employersAPI.fetchEmployer())
                .flatMap {
                    paymentAPI.linkEmployerCreditCard(
                            creditCardId = it.first.first().id!!,
                            employerId = it.second!!.id!!,
                            cardType = "Business",
                            isDefault = true)
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

    @Test
    fun testListAllCreditCards() {
        val testSubscriber = TestObserver<List<CreditCard>>()

        successfullyLogin()

        paymentAPI.listAllCards()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().size > 0)
    }

    @Test
    fun testListLinkedEmployerCards() {
        val testSubscriber = TestObserver<List<EmployerCreditCard>>()

        successfullyLogin()

        paymentAPI.listAllEmployerCards()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().size > 0)
    }
}
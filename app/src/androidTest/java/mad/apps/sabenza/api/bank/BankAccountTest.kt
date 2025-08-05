package mad.apps.sabenza.api.bank

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import io.reactivex.rxkotlin.zipWith
import junit.framework.Assert
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.api.PaymentAPI
import mad.apps.sabenza.data.model.payment.BankAccount
import mad.apps.sabenza.data.model.payment.EmployeeBankAccount
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class BankAccountTest : BaseDaggerTestCase() {

    @Inject
    lateinit var paymentAPI : PaymentAPI

    @Inject
    lateinit var employeeAPI : EmployeeAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun getBankAccountsAsEmployee() {
        val observer = TestObserver<List<BankAccount>>()

        successfullyLogin()

        paymentAPI.getBankAccounts()
                .compose(NetworkTransformer())
                .subscribe(observer)

        observer.awaitTerminalEvent()
        observer.assertComplete()

        Assert.assertTrue(observer.values().first().size > 0)
    }


    @Test
    fun getBankAccountsAsNewEmployee() {
        val observer = TestObserver<List<BankAccount>>()

        successfullyRandomSignUp()

        paymentAPI.getBankAccounts()
                .compose(NetworkTransformer())
                .subscribe(observer)

        observer.awaitTerminalEvent()
        observer.assertComplete()

        Assert.assertTrue(observer.values().first().size == 0)
    }

    @Test
    fun addNewBankAccount() {
        val observer = TestObserver<BankAccount>()

        successfullyLogin()

        val newBank = BankAccount(
                bankName = "First National Moneybags",
                accountName = "Cheque",
                branchNumber = "43413",
                accountNumber = 525252.toString())

        paymentAPI.addBankAccount(newBank)
                .compose(NetworkTransformer())
                .subscribe(observer)

        observer.awaitTerminalEvent()
        observer.assertComplete()

        Assert.assertTrue(observer.values().first().bankName == newBank.bankName)
    }

    @Test
    fun addAndLinkBankAccount() {
        val observer = TestObserver<EmployeeBankAccount>()

        successfullyLogin()

        var bank : BankAccount? = null
        val newBank = BankAccount(
                bankName = "First National Moneybags",
                accountName = "Cheque",
                branchNumber = TestStringHelper.nextPrintableNumber(6),
                accountNumber = TestStringHelper.nextPrintableNumber(6))

        paymentAPI.addBankAccount(newBank)
                .flatMap { paymentAPI.getBankAccounts() }.zipWith( employeeAPI.fetchEmployeeForCurrentUser() )
                .flatMap {
                    bank = it.first.find { it.branchNumber == newBank.branchNumber }
                    val employee = it.second
                    paymentAPI.linkBankAccount(EmployeeBankAccount(employeeId = employee!!.id!!, bankAccountId = bank!!.id!!))
                }
                .compose(NetworkTransformer())
                .subscribe(observer)

        observer.awaitTerminalEvent()
        observer.assertComplete()

        Assert.assertTrue(observer.values().first().bankAccountId == bank!!.id)
    }

    @Test
    fun getLinkedBankAccounts() {
        val observer = TestObserver<List<EmployeeBankAccount>>()

        successfullyLogin()

        paymentAPI.listLinkedBankAccounts()
                .compose(NetworkTransformer())
                .subscribe(observer)

        observer.awaitTerminalEvent()
        observer.assertComplete()

        Assert.assertTrue(observer.values().first().isNotEmpty())
    }
}
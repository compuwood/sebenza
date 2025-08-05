package mad.apps.sabenza.api.employer

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerEmployerTestCase
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import javax.inject.Inject

@RunWith((AndroidJUnit4::class))
class EmployerTest : BaseDaggerEmployerTestCase() {

    @Inject lateinit var employerAPI: EmployerAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testEmployerDoesntExist() {
        val testSubscriber = TestObserver<Employer>()

        val me = successfullyLogin()

        employerAPI.fetchEmployer()
                .compose(NetworkTransformer<Employer>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertTerminated()

        assert(testSubscriber.errorCount() > 0)
        assert(testSubscriber.values().size == 0)
    }

    @Test
    fun addEmployerToNewAccount() {
        val testSubscriber = TestObserver<Employer>()

        val me = successfullyRandomSignUp()

        employerAPI.addEmployer(Employer(
                firstName = "John",
                lastName = "Doe",
                email = me.email,
                aboutCompany = "A brand new company, dealing in " + TestStringHelper.nextPrintableString(4),
                aboutMe = "A bright, young capitalist slave-master",
                phoneNumber = "082121212",
                pictureId = "5"))
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertComplete()

        testSubscriber.assertValueCount(1)
        Assert.assertTrue(testSubscriber.values().first().email == me.email)
    }

    @Test
    fun addExistingEmployerToExistingAccount() {
        val testSubscriber = TestObserver<Employer>()

        val me = successfullyLogin()

        employerAPI.addEmployer(Employer(
                firstName = "John",
                lastName = "Doe",
                email = me.email,
                aboutCompany = "A brand new company, dealing in " + TestStringHelper.nextPrintableString(4),
                aboutMe = "A bright, young capitalist slave-master",
                phoneNumber = "082121212",
                pictureId = "5"))
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertNotComplete()
        Assert.assertTrue(testSubscriber.errorCount() == 1)
        Assert.assertTrue(testSubscriber.errors().first() is HttpException)
    }

    @Test
    fun updateExistingEmployer() {
        var testSubscriber = TestObserver<Employer>()

        successfullyLogin()

        employerAPI.fetchEmployer()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        val employer = testSubscriber.values().first()
        val newLastName = TestStringHelper.nextPrintableString(8)

        testSubscriber = TestObserver()

        employerAPI.updateEmployer(employer.copy(lastName = newLastName))
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().lastName != employer.lastName)
        Assert.assertTrue(testSubscriber.values().first().lastName.equals(newLastName))
    }

}

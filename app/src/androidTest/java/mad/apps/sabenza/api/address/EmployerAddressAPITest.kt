package mad.apps.sabenza.api.address

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerEmployerTestCase
import mad.apps.sabenza.data.api.AddressAPI
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EmployerAddressAPITest : BaseDaggerEmployerTestCase() {

    @Inject lateinit var addressAPI : AddressAPI

    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testGetAddresses() {
        successfullyLogin()

        val testSubscriber = TestObserver<List<Address>>()

        addressAPI.listAddresses()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())

    }

    @Test
    fun addAddress() {
        successfullyLogin()

        val testSubscriber = TestObserver<Address>()

        addressAPI
                .addAddress(Address(
                        line1 = "6 Swapo Road",
                        line2 = "Dentist's Den",
                        cityTown = "Durban",
                        postcode = "4051",
                        countryId = "1"
                ))
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

}
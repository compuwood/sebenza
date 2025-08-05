package mad.apps.sabenza.api.handshake

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import junit.framework.Assert
import mad.apps.sabenza.BaseDaggerEmployerTestCase
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.data.api.HandshakeAPI
import mad.apps.sabenza.data.model.handshake.Handshake
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class HandshakeTest : BaseDaggerTestCase() {

    @Inject
    lateinit var handshakeAPI : HandshakeAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testHandshake() {
        successfullyLogin()

        val testSubscriber = TestObserver<List<Handshake>>()

        handshakeAPI.getHandshakes()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())

    }
}
package mad.apps.sabenza.api.messaging

import android.support.test.runner.AndroidJUnit4
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.rxkotlin.zipWith
import mad.apps.sabenza.BaseDaggerEmployerTestCase
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.api.JobsAPI
import mad.apps.sabenza.data.api.MessagingAPI
import mad.apps.sabenza.data.api.toEqualQueryString
import mad.apps.sabenza.data.model.messaging.MessageBody
import mad.apps.sabenza.data.model.messaging.MessageHeader
import mad.apps.sabenza.data.model.messaging.SebenzaMessage
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EmployeeMessagingTest : BaseDaggerEmployerTestCase() {

    @Inject lateinit var employerAPI: EmployerAPI
    @Inject lateinit var messagingAPI: MessagingAPI
    @Inject lateinit var jobsAPI: JobsAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testGetAllMessagesAsEmployee() {
        successfullyLoginAsEmployee()

        val testSubscriber = TestObserver<List<SebenzaMessage>>()

        messagingAPI.getMessages()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    @Test
    fun testAddNewMessageHeaderAsEmployer() {
        successfullyLogin()

        val testSubscriber = TestObserver<MessageHeader>()

        employerAPI.fetchEmployer().zipWith(jobsAPI.getEmployeeJobs())
                .flatMap {
                    val job = it.second.first()
                    val employer = it.first
                    messagingAPI.addNewMessageHeader(MessageHeader(relatedJobId = job.jobId, relatedEmployeeId = job.employeeId, relatedEmployerId = employer.id!!.toString()))
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().size == 1)
    }

    @Test
    fun testAddNewMessageHeaderAndBodyAsEmployer() {
        successfullyLogin()

        val testSubscriber = TestObserver<MessageBody>()

        employerAPI.fetchEmployer().zipWith(jobsAPI.getEmployeeJobs())
                .flatMap {
                    val job = it.second.first()
                    val employer = it.first
                    messagingAPI.addNewMessageHeader(MessageHeader(relatedJobId = job.jobId, relatedEmployeeId = job.employeeId, relatedEmployerId = employer.id!!.toString()))
                }
                .flatMap { messagingAPI.addMessageBodyToHeader(it.id!!, "Hello there, Dear Slave") }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().size == 1)
    }

    @Test
    fun getAllMessageBodyForAMessageHeader() {
        successfullyLoginAsEmployee()

        val testSubscriber = TestObserver<List<MessageBody>>()

        messagingAPI.fetchMessageHeadersToUser()
                .flatMap { messagingAPI.fetchAllMessagesForHeader(it.last().id!!.toEqualQueryString()) }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    @Test
    fun fetchAllMessageHeadersSentToEmployee() {
        val me = successfullyLoginAsEmployee()

        val testSubscriber = TestObserver<List<MessageHeader>>()

        messagingAPI.fetchMessageHeadersToUser()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    @Test
    fun fetchAllMessageHeadersSentByEmployer() {
        val me = successfullyLogin()
        val testSubscriber = TestObserver<List<MessageHeader>>()

        messagingAPI.fetchMessageHeadersFromUser()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    @Test
    fun testGetMessageHeaderForJobAsEmployee() {
        successfullyLoginAsEmployee()

        val testSubscriber = TestObserver<List<SebenzaMessage>>()

        messagingAPI.getMessages()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().size >= 1)
    }

    @Test
    fun addMessageToThreadAsEmployee() {
        successfullyLoginAsEmployee()

        val testSubscriber = TestObserver<MessageBody>()
        val message = "Replying to you, dear master " + TestStringHelper.nextPrintableNumber(4)

        messagingAPI.fetchMessageHeadersToUser().toObservable()
                .flatMapIterable { x -> x }
                .flatMap { header ->
                    messagingAPI.fetchAllMessagesForHeader(header.id!!.toEqualQueryString())
                            .map { Pair(header, it) }
                            .toObservable()
                }
                .toList()
                .flatMap { pairs ->
                    val pair = pairs.filter { it.second.isNotEmpty() }
                            .first()
                    if (pair != null) {
                        messagingAPI.addNewMessageHeader(MessageHeader(
                                relatedJobId = pair.first.relatedJobId,
                                relatedEmployerId = pair.first.relatedEmployerId,
                                relatedEmployeeId = pair.first.relatedEmployeeId))
                    } else {
                        Single.error<MessageHeader>(Throwable("Something went wrong"))
                    }
                }
                .flatMap { messagingAPI.addMessageBodyToHeader(it.id!!, message) }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().body == message)

    }

}
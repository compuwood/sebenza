package mad.apps.sabenza.ui.employer.inbox

import io.reactivex.Completable
import mad.apps.sabenza.data.model.messaging.MessagePreviewModel
import mad.apps.sabenza.framework.rx.CompletableNetworkTransformer
import mad.apps.sabenza.framework.ui.BasePresenter
import zendesk.suas.Store
import java.util.Calendar
import java.util.concurrent.TimeUnit

class InboxStubPresenter(store: Store) : BasePresenter<InboxViewInterface>(store), InboxPresenterInterface {

    val messageList = mutableListOf<MessagePreviewModel>() // for testing only

    init { // for testing only
        val date = Calendar.getInstance()
        date.set(2016,0,24)

        val message1 = MessagePreviewModel(messageId = "101", relatedJobId = "501", relatedJobName = "Holiday Home", sendDate = date, senderName = "Ray Neal", messageBody = "Hi there! I'd like to apply for the job")
        val message2 = MessagePreviewModel(messageId = "102", relatedJobId = "502", relatedJobName = "Au Pair Needed", sendDate = date, senderName = "Carrie Mann", messageBody = "Amazing Au Pair here to blow you away")
        val message3 = MessagePreviewModel(messageId = "103", relatedJobId = "503", relatedJobName = "House Sitter", sendDate = date, senderName = "Linda Ross", messageBody = "Happy to house sit now and forever")
        val message4 = MessagePreviewModel(messageId = "104", relatedJobId = "504", relatedJobName = "Hippy Hopper", sendDate = date, senderName = "Gavin De Sousa", messageBody = "Trippy hippy here being trippy")
        val message5 = MessagePreviewModel(messageId = "105", relatedJobId = "505", relatedJobName = "Holiday Home", sendDate = date, senderName = "Ray Neal", messageBody = "Hi there! I'd like to apply for the job")
        val message6 = MessagePreviewModel(messageId = "106", relatedJobId = "506", relatedJobName = "Au Pair Needed", sendDate = date, senderName = "Carrie Mann", messageBody = "Amazing Au Pair here to blow you away")
        val message7 = MessagePreviewModel(messageId = "107", relatedJobId = "507", relatedJobName = "House Sitter", sendDate = date, senderName = "Linda Ross", messageBody = "Happy to house sit now and forever")
        val message8 = MessagePreviewModel(messageId = "108", relatedJobId = "508", relatedJobName = "Hippy Hopper", sendDate = date, senderName = "Gavin De Sousa", messageBody = "Trippy hippy here being trippy")
        val message9 = MessagePreviewModel(messageId = "109", relatedJobId = "509", relatedJobName = "Holiday Home", sendDate = date, senderName = "Ray Neal", messageBody = "Hi there! I'd like to apply for the job")
        val message10 = MessagePreviewModel(messageId = "110", relatedJobId = "510", relatedJobName = "Au Pair Needed", sendDate = date, senderName = "Carrie Mann", messageBody = "Amazing Au Pair here to blow you away")
        val message11 = MessagePreviewModel(messageId = "111", relatedJobId = "511", relatedJobName = "House Sitter", sendDate = date, senderName = "Linda Ross", messageBody = "Happy to house sit now and forever")
        val message12 = MessagePreviewModel(messageId = "112", relatedJobId = "512", relatedJobName = "Hippy Hopper", sendDate = date, senderName = "Gavin De Sousa", messageBody = "Trippy hippy here being trippy")

        messageList.addAll(listOf(message1, message2, message3, message4, message5, message6, message7, message8, message9, message10, message11, message12))
    }

    override fun loaded() {
        super.loaded()
        getMessages()
    }

    override fun openMessage(messageId: String) {
    }

    override fun deleteMessage(index: Int, messageId: String) {
        view.busy()
        Completable.complete()
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(CompletableNetworkTransformer())
                .subscribe {
                    messageList.removeAt(index) // for testing only
                    view.successfullyDeletedMessage()
                }
    }


    override fun getMessages() {
        view.busy()
        Completable.complete()
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(CompletableNetworkTransformer())
                .subscribe {
                    view.updateMessages(messageList)
                    view.idle()
                }
    }

}
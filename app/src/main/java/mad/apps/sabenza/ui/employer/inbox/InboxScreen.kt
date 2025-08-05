package mad.apps.sabenza.ui.employer.inbox

import mad.apps.sabenza.data.model.messaging.MessagePreviewModel
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import mad.apps.sabenza.ui.util.SuccessErrorViewInterface
import zendesk.suas.Store

interface InboxViewInterface : ViewInterface, BusyViewInterface, SuccessErrorViewInterface {
    fun successfullyDeletedMessage()
    fun updateMessages(list: MutableList<MessagePreviewModel>)
}

interface InboxPresenterInterface : PresenterInterface<InboxViewInterface> {
    fun getMessages()
    fun deleteMessage(index: Int, messageId : String)
    fun openMessage(messageId : String)
}

object InboxScreen : BaseScreen<InboxViewInterface, InboxPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return InboxViewController()
    }

    override fun providePresenter(store: Store): InboxPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return InboxStubPresenter(store)
//        } else {
//            return InboxPresenter(store)
//        }
    }
}
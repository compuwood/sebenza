package mad.apps.sabenza.ui.employer.inbox

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.messaging.MessagePreviewModel
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.widget.ConfirmModal
import mad.apps.sabenza.ui.widget.ErrorModal
import mad.apps.sabenza.ui.widget.LoadingScreen
import mad.apps.sabenza.ui.widget.navigationfooter.NavigationFooter
import javax.inject.Inject

class InboxViewController : BaseViewController(), InboxViewInterface {
    override fun layout(): Int = R.layout.employer_inbox

    @Inject lateinit var presenter: InboxPresenterInterface

    val messagesRecycler by bindView<RecyclerView>(R.id.messages_recycler)

    val navigationFooter by bindView<NavigationFooter>(R.id.employer_navigation_footer)
    val errorModal by bindView<ErrorModal>(R.id.error_modal)
    val loadingScreen by bindView<LoadingScreen>(R.id.loading_screen)
    val confirmModal by bindView<ConfirmModal>(R.id.confirm_modal)

    val messagesRecyclerAdapter = MessagesRecyclerAdapter(mutableListOf())

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)
        navigationFooter.selectButtonHighlighted(NavigationFooter.FOOTER_ENUM.INBOX)

        messagesRecyclerAdapter.setItemClickListener(object : MessagesRecyclerAdapter.ItemClickListener {
            override fun clickItem(index: Int, messageId: String) {
                presenter.openMessage(messageId)
            }
        })
        messagesRecyclerAdapter.setDeleteClickListener(object : MessagesRecyclerAdapter.DeleteClickListener {
            override fun clickItem(index: Int, messageId: String) {
                confirmModal.confirmButton.setOnClickListener {
                    confirmModal.hide()
                    presenter.deleteMessage(index, messageId)
                }
                confirmModal.show()
            }
        })

        messagesRecycler.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
        messagesRecycler.adapter = messagesRecyclerAdapter
    }

    override fun updateMessages(list : MutableList<MessagePreviewModel>) {
        messagesRecyclerAdapter.update(list)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.takeView(this)
    }


    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.dropView()
    }

    override fun busy() {
        loadingScreen.show()
    }

    override fun idle() {
        loadingScreen.hide()
    }

    override fun successfullyDeletedMessage() {
        presenter.getMessages()
    }

    override fun success() {
    }

    override fun error(error: String) {
        idle()
        errorModal.show(error)
    }
}
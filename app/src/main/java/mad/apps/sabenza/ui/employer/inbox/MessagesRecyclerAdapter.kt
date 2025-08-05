package mad.apps.sabenza.ui.employer.inbox

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.messaging.MessagePreviewModel
import mad.apps.sabenza.framework.ui.bindView

class MessagesRecyclerAdapter(private val dataSet: MutableList<MessagePreviewModel>) : RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.messages_line_item_layout, parent, false) as LinearLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupLineItem(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profilePicture by bindView<CircleImageView>(R.id.profile_preview_image)
        val jobNameTextView by bindView<TextView>(R.id.job_name_textview)
        val jobDateTextView by bindView<TextView>(R.id.job_date_textview)
        val messageTextView by bindView<TextView>(R.id.message_preview_textview)
        val deleteButton by bindView<ImageView>(R.id.delete_item_button)

        var messageId = ""

        init {
            itemView.setOnClickListener { itemClickListener.clickItem(adapterPosition, messageId) }
            deleteButton.setOnClickListener { deleteClickListener.clickItem(adapterPosition,messageId) }
        }

        fun setupLineItem(message: MessagePreviewModel) {
            jobNameTextView.text = message.relatedJobName
            messageTextView.text = message.senderName + " - " + message.messageBody.replace("\n", " ")
            jobDateTextView.text = message.getSendDateAsString()
        }
    }

    fun update(messageList: MutableList<MessagePreviewModel>) {
        dataSet.clear()
        dataSet.addAll(messageList)
        notifyDataSetChanged()
    }

    //==================On Click Listener================================//
    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun clickItem(index: Int, messageId: String)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    //==================Delete Click Listener================================//
    private lateinit var deleteClickListener: DeleteClickListener

    interface DeleteClickListener {
        fun clickItem(index: Int, messageId: String)
    }

    fun setDeleteClickListener(deleteClickListener: DeleteClickListener) {
        this.deleteClickListener = deleteClickListener
    }

}

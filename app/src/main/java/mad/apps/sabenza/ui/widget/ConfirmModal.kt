package mad.apps.sabenza.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.framework.ui.bindView

class ConfirmModal(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    val messageTextView by bindView<TextView>(R.id.confirm_message_textview)
    val cancelButton by bindView<FrameLayout>(R.id.cancel_button)
    val confirmButton by bindView<FrameLayout>(R.id.confirm_button)

    init {
        LayoutInflater.from(context).inflate(R.layout.confirm_modal, this, true)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ConfirmModal, 0, 0)
        val message = attributes.getString(R.styleable.ConfirmModal_confirmMessage)
        if (!message.isNullOrEmpty()) {
            messageTextView.text = message
        }
        cancelButton.setOnClickListener { hide() }
    }

    fun setMessage(message: String) {
        messageTextView.text = message
    }

    fun show() {
        var anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 250
        this.startAnimation(anim)
        this.visibility = View.VISIBLE

        this.setOnClickListener {
            anim = AlphaAnimation(1.0f, 0.0f)
            anim.duration = 250
            this.startAnimation(anim)
            this.visibility = View.GONE
        }
    }

    fun hide() {
        confirmButton.setOnClickListener {  }
        var anim = AlphaAnimation(1.0f, 0.0f)
        anim.duration = 250
        this.startAnimation(anim)
        this.visibility = View.GONE
    }
}
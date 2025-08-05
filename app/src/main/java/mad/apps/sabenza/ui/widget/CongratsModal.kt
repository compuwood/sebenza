package mad.apps.sabenza.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.framework.ui.bindView

class CongratsModal(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    val messageTextView by bindView<TextView>(R.id.write_up_textview)

    init {
        LayoutInflater.from(context).inflate(R.layout.congrats_modal, this, true)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CongratsModal, 0, 0)
        val message = attributes.getString(R.styleable.CongratsModal_message)
        if (!message.isNullOrEmpty()){
            messageTextView.text = attributes.getString(R.styleable.ErrorModal_errorText)
        }
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
}
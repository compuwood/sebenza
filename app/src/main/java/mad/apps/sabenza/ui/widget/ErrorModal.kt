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

class ErrorModal(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    val errorText by bindView<TextView>(R.id.error_modal_error_text)

    init {
        LayoutInflater.from(context).inflate(R.layout.error_modal, this, true)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ErrorModal, 0, 0)
        val errorMsg = attributes.getString(R.styleable.ErrorModal_errorText)
        if (!errorMsg.isNullOrEmpty()){
            errorText.text = attributes.getString(R.styleable.ErrorModal_errorText)
        }
    }

    fun show(error: String = "") {
        if (error.isNotEmpty()) {
            errorText.text = error
        }
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

    fun setError(error: String) {
        errorText.text = error
    }
}
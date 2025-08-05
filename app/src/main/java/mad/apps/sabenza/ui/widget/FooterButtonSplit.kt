package mad.apps.sabenza.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.framework.ui.bindView

class FooterButtonSplit(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    val footerTextLeft by bindView<TextView>(R.id.left_button_textview)
    val footerTextRight by bindView<TextView>(R.id.right_button_textview)

    init {
        LayoutInflater.from(context).inflate(R.layout.footer_button_split, this, true)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FooterButtonSplit, 0, 0)
        footerTextLeft.text = attributes.getString(R.styleable.FooterButtonSplit_LeftText)
        footerTextRight.text = attributes.getString(R.styleable.FooterButtonSplit_RightText)
    }

    fun setLeftButtonText (text: String) {
        footerTextLeft.text = text
    }

    fun setRightButtonText (text: String) {
        footerTextRight.text = text
    }
}

package mad.apps.sabenza.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.framework.ui.bindView

class FooterButtonViewBase(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    val footerText by bindView<TextView>(R.id.employer_footer_button_text)

    init {
        LayoutInflater.from(context).inflate(R.layout.footer_button_base, this, true)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FooterButtonViewBase, 0, 0)

        footerText.text = attributes.getString(R.styleable.FooterButtonViewBase_buttonText)

    }
}

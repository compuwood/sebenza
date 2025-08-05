package mad.apps.sabenza.ui.widget.headerviewbase

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.hideKeyboard
import mad.apps.sabenza.ui.widget.headerviewbase.presenter.HeaderViewBasePresenterInterface
import javax.inject.Inject

class HeaderViewBase(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    @Inject lateinit var presenter : HeaderViewBasePresenterInterface

    val headerTitle by bindView<TextView>(R.id.employer_header_title)
    val headerSubtitle by bindView<TextView>(R.id.employer_header_subtitle)
    val employerHeaderIcon by bindView<ImageView>(R.id.employer_header_icon)
    val backButton by bindView<ImageView>(R.id.back_button)

    init {
        DaggerService.getDaggerComponent(context).inject(this)
        LayoutInflater.from(context).inflate(R.layout.header_base, this, true)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.HeaderViewBase, 0, 0)
        headerTitle.text = attributes.getString(R.styleable.HeaderViewBase_title)
        headerSubtitle.text = attributes.getString(R.styleable.HeaderViewBase_subtitle)

        val imageRef = attributes.getResourceId(R.styleable.HeaderViewBase_icon, 0)
        val showBack = attributes.getBoolean(R.styleable.HeaderViewBase_showBack, true)
        val liftIcon = attributes.getBoolean(R.styleable.HeaderViewBase_liftIcon, false)

        employerHeaderIcon.setImageResource(imageRef)

        if (liftIcon) { employerHeaderIcon.setPadding(0,0,0, 45) }

        if (showBack) {
            backButton.setOnClickListener {
                hideKeyboard(this)
                presenter.goBack()
            }
        } else {
            backButton.visibility = View.GONE
        }
    }

    fun setTitle(title: String) {
        headerTitle.text = title
    }

    fun setSubTitle(subtitle: String) {
        headerSubtitle.text = subtitle
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.takeView(this)
    }

    override fun onDetachedFromWindow() {
        presenter.dropView()
        super.onDetachedFromWindow()
    }
}

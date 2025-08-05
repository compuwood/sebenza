package mad.apps.sabenza.ui.widget.navigationfooter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.widget.tablayout.presenter.NavigationFooterPresenterInterface
import javax.inject.Inject

class NavigationFooter(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    @Inject lateinit var presenter : NavigationFooterPresenterInterface

    val profileImg by bindView<ImageView>(R.id.bottombar_profile_image)
    val calendarImg by bindView<ImageView>(R.id.bottombar_calendar_image)
    val jobsImg by bindView<ImageView>(R.id.bottombar_jobs_image)
    val inboxImg by bindView<ImageView>(R.id.bottombar_inbox_image)

    val moreImg by bindView<ImageView>(R.id.bottombar_more_image)
    val profileText by bindView<TextView>(R.id.bottombar_profile_textview)
    val calendarText by bindView<TextView>(R.id.bottombar_calendar_textview)
    val jobsText by bindView<TextView>(R.id.bottombar_jobs_textview)
    val inboxText by bindView<TextView>(R.id.bottombar_inbox_textview)

    val moreText by bindView<TextView>(R.id.bottombar_more_textview)
    val profileButton by bindView<FrameLayout>(R.id.bottombar_profile_button)
    val calendarButton by bindView<FrameLayout>(R.id.bottombar_calendar_button)
    val jobsButton by bindView<FrameLayout>(R.id.bottombar_jobs_button)
    val inboxButton by bindView<FrameLayout>(R.id.bottombar_inbox_button)
    val moreButton by bindView<FrameLayout>(R.id.bottombar_more_button)

    var isEmployer : Boolean = false


    init {
        DaggerService.getDaggerComponent(context).inject(this)
        LayoutInflater.from(context).inflate(R.layout.navigation_footer, this, true)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.NavigationFooter, 0, 0)
        isEmployer = attributes.getBoolean(R.styleable.NavigationFooter_isEmployerFooter,false)

        if (isEmployer) {
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.nice_blue))
        } else {
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.cerulean))
        }

        profileButton.setOnClickListener { changeScreen(FOOTER_ENUM.PROFILE) }
        calendarButton.setOnClickListener { changeScreen(FOOTER_ENUM.CALENDAR) }
        jobsButton.setOnClickListener { changeScreen(FOOTER_ENUM.JOBS) }
        inboxButton.setOnClickListener { changeScreen(FOOTER_ENUM.INBOX) }
        moreButton.setOnClickListener { changeScreen(FOOTER_ENUM.MORE) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.takeView(this)
    }

    override fun onDetachedFromWindow() {
        presenter.dropView()
        super.onDetachedFromWindow()
    }

    fun selectButtonHighlighted(mode: FOOTER_ENUM) {

        resetAlpha()
        when (mode) {
            FOOTER_ENUM.PROFILE -> {
                profileImg.alpha = 1f
                profileText.alpha = 1f
            }
            FOOTER_ENUM.CALENDAR -> {
                calendarImg.alpha = 1f
                calendarText.alpha = 1f
            }
            FOOTER_ENUM.JOBS -> {
                jobsImg.alpha = 1f
                jobsText.alpha = 1f
            }
            FOOTER_ENUM.INBOX -> {
                inboxImg.alpha = 1f
                inboxText.alpha = 1f
            }
            FOOTER_ENUM.MORE -> {
                moreImg.alpha = 1f
                moreText.alpha = 1f
            }
        }
    }

    fun changeScreen(mode: FOOTER_ENUM) {
        selectButtonHighlighted(mode)
        if (isEmployer) {
            when (mode) {
                FOOTER_ENUM.PROFILE -> {
                    presenter.navigateToEmployerProfile()
                }
                FOOTER_ENUM.CALENDAR -> {
                    presenter.navigateToEmployerCalendar()
                }
                FOOTER_ENUM.JOBS -> {
                    presenter.navigateToEmployerJobs()
                }
                FOOTER_ENUM.INBOX -> {
                    presenter.navigateToEmployerInbox()
                }
                FOOTER_ENUM.MORE -> {
                    presenter.navigateToEmployerMore()
                }
            }
        }else {
            when (mode) {
                FOOTER_ENUM.PROFILE -> {
                    presenter.navigateToEmployeeProfile()
                }
                FOOTER_ENUM.CALENDAR -> {
                    presenter.navigateToEmployeeCalendar()
                }
                FOOTER_ENUM.JOBS -> {
                    presenter.navigateToEmployeeJobs()
                }
                FOOTER_ENUM.INBOX -> {
                    presenter.navigateToEmployeeInbox()
                }
                FOOTER_ENUM.MORE -> {
                    presenter.navigateToEmployeeMore()
                }
            }
        }
    }

    private fun resetAlpha() {
        profileImg.alpha = 0.5f
        profileText.alpha = 0.5f
        calendarImg.alpha = 0.5f
        calendarText.alpha = 0.5f
        jobsImg.alpha = 0.5f
        jobsText.alpha = 0.5f
        inboxImg.alpha = 0.5f
        inboxText.alpha = 0.5f
        moreImg.alpha = 0.5f
        moreText.alpha = 0.5f
    }


    enum class FOOTER_ENUM {
        PROFILE, CALENDAR, JOBS, INBOX, MORE
    }
}

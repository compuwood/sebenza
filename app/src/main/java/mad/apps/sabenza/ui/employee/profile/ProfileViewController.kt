package mad.apps.sabenza.ui.employee.profile

import android.view.View
import android.widget.*
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import javax.inject.Inject
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.noveogroup.android.log.LoggerManager
import io.techery.properratingbar.ProperRatingBar
import mad.apps.sabenza.data.model.picture.SebenzaPicture
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.ui.widget.image.AddSebenzaImageDialogBuilder
import mad.apps.sabenza.ui.util.BusyViewInterface
import mad.apps.sabenza.ui.widget.SkillBubble
import mad.apps.sabenza.ui.widget.navigationfooter.NavigationFooter
import java.text.SimpleDateFormat
import java.util.*


class ProfileViewController : BaseViewController(), ProfileViewInterface, BusyViewInterface {
    @Inject lateinit var presenter: ProfilePresenterInterface

    override fun layout(): Int = R.layout.employee_profile

    val navigationFooter by bindView<NavigationFooter>(R.id.employee_profile_navigation_footer)

    val editModePencil by bindView<ImageView>(R.id.employee_profile_editmode_button)
    val addressPencil by bindView<ImageView>(R.id.employee_profile_address_pencil)
    val paymentPencil by bindView<ImageView>(R.id.employee_profile_payment_pencil)

    val averageReviewRatingBar by bindView<ProperRatingBar>(R.id.average_review_ratingbar)
    val totalReviewsTextView by bindView<TextView>(R.id.employee_profile_total_reviews_textview)
    val readAllReviewsTextView by bindView<TextView>(R.id.employee_profile_reviews_preview_read_all_reviews_textview)
    val phoneNumber by bindView<TextView>(R.id.employee_profile_phone_textview)
    val email by bindView<TextView>(R.id.employee_profile_email_textview)
    val fullNameTextView by bindView<TextView>(R.id.employee_profile_full_name_textview)
    val addressTextView by bindView<TextView>(R.id.employee_profile_address_textview)
    val paymentTextView by bindView<TextView>(R.id.employee_profile_payment_info_textview)
    val reviewWriteUpTextView by bindView<TextView>(R.id.employee_profile_reviews_preview_review_description)
    val reviewerTextView by bindView<TextView>(R.id.employee_profile_reviews_preview_reviewer_textview)
    val reviewWorkDoneTextView by bindView<TextView>(R.id.employee_profile_reviews_preview_work_done_textview)
    val reviewRatingBar by bindView<ProperRatingBar>(R.id.employee_profile_top_review_ratingbar)

    val fullNameEditText by bindView<EditText>(R.id.employee_profile_full_name_edittext)
    val phoneEditText by bindView<EditText>(R.id.employee_profile_phone_edittext)
    val emailEditText by bindView<EditText>(R.id.employee_profile_email_edittext)

    val fullNameSwitcher by bindView<ViewSwitcher>(R.id.employee_profile_full_name_switcher)
    val phoneSwitcher by bindView<ViewSwitcher>(R.id.employee_profile_phone_switcher)
    val emailSwitcher by bindView<ViewSwitcher>(R.id.employee_profile_email_switcher)

    val skillsFlexBox by bindView<FlexboxLayout>(R.id.skills_flexbox)
    val profilePicture by bindView<ImageView>(R.id.employee_profile_picture)
    val profilePictureEditButton by bindView<ImageView>(R.id.employee_profile_picture_edit_button)
    val editPictureOverlay by bindView<FrameLayout>(R.id.employee_profile_picture_edit_overlay)

    var editMode: Boolean = false

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        navigationFooter.selectButtonHighlighted(NavigationFooter.FOOTER_ENUM.PROFILE)
        updateProfilePicture()
        initializeTextViews()
        initializeRatingBars()
        inflateSkillBubbles()
        setOnClickListeners()
    }

    private fun updateProfilePicture() {
        Glide.with(view?.context)
                .load(presenter.getProfilePictureUrl())
                .into(profilePicture)
    }

    override fun busy() {
    }

    override fun idle() {
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.takeView(this)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.dropView()
    }

    fun initializeTextViews() {
        totalReviewsTextView.text = "View " + presenter.getProfileTotalReviews().toString() + " reviews"

        fullNameTextView.text = presenter.getProfileFullName()
        fullNameEditText.setText(presenter.getProfileFullName())
        phoneNumber.text = presenter.getProfileTelephone()
        phoneEditText.setText(presenter.getProfileTelephone())
        email.text = presenter.getProfileEmail()
        emailEditText.setText(presenter.getProfileEmail())

        addressTextView.text = presenter.getProfileDefaultAddress()
        paymentTextView.text = presenter.getProfilePaymentInfo()

        reviewWriteUpTextView.text = presenter.getTopReviewWriteUp()
        val reviewer = presenter.getTopReviewerName() + " on " + buildStringFromDate(presenter.getTopReviewDate())
        reviewerTextView.text = reviewer
        reviewWorkDoneTextView.text = presenter.getTopReviewWorkDone()
    }

    fun initializeRatingBars() {
        averageReviewRatingBar.rating = presenter.getAverageRating().toInt()
        reviewRatingBar.rating = presenter.getTopReviewRating()
    }

    fun inflateSkillBubbles() {
        for ((first, second) in presenter.getSkills()) {
            skillsFlexBox.addView(SkillBubble(getContext(), first, second))
        }
    }

    fun setOnClickListeners() {
        totalReviewsTextView.setOnClickListener { presenter.goToReviews() }
        readAllReviewsTextView.setOnClickListener { presenter.goToReviews() }
        editModePencil.setOnClickListener { switchEditMode() }
        addressPencil.setOnClickListener { editAddress() }
        paymentPencil.setOnClickListener { editPaymentInfo() }

        profilePictureEditButton.setOnClickListener{
            AddSebenzaImageDialogBuilder.build(it.context)
                    .subscribe(object : EnhancedSingleObserver<SebenzaPicture>() {
                        override fun onSuccess(t: SebenzaPicture) {
                            presenter.updateProfilePicture(t)
                        }

                        override fun onError(e: Throwable) {
                            LoggerManager.getLogger().e(e)
                        }
                    })
        }
    }

    override fun switchEditMode() {
        editMode = !editMode
        if (editMode) {
            editModePencil.setImageResource(R.drawable.edit_circle_button_tick)
            addressPencil.visibility = View.VISIBLE
            paymentPencil.visibility = View.VISIBLE

            fullNameSwitcher.showNext()
            phoneSwitcher.showNext()
            emailSwitcher.showNext()

            editPictureOverlay.visibility = View.VISIBLE

        } else {

            if (fullNameEditText.text.toString() != presenter.getProfileFullName()) { presenter.updateFullName(fullNameEditText.text.toString()) }
            if (phoneEditText.text.toString() != presenter.getProfileTelephone()) { presenter.updateTelNum(phoneEditText.text.toString()) }
            if (emailEditText.text.toString() != presenter.getProfileEmail()) { presenter.updateEmail(emailEditText.text.toString()) }

            editModePencil.setImageResource(R.drawable.edit_circle_button)
            addressPencil.visibility = View.GONE
            paymentPencil.visibility = View.GONE
            editPictureOverlay.visibility = View.GONE

            fullNameSwitcher.showPrevious()
            phoneSwitcher.showPrevious()
            emailSwitcher.showPrevious()
        }
    }

    override fun update() {
        initializeTextViews()
        updateProfilePicture()
        skillsFlexBox.removeAllViews()
        inflateSkillBubbles()
        initializeRatingBars()
    }

    override fun editAddress() {
    }

    override fun editPaymentInfo() {
    }

    private fun buildStringFromDate(calendar: Calendar) : String {
        val sdf = SimpleDateFormat("MMMM DD yyyy", Locale.US)
        return sdf.format(calendar.time)
    }

}
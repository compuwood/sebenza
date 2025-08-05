package mad.apps.sabenza.ui.employer.profile

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import io.techery.properratingbar.ProperRatingBar
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.widget.navigationfooter.NavigationFooter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ProfileViewController : BaseViewController(), ProfileViewInterface {

    override fun layout(): Int = R.layout.employer_profile

    @Inject lateinit var presenter: ProfilePresenterInterface

    val navigationFooter by bindView<NavigationFooter>(R.id.employer_profile_navigation_footer)

    val editModePencil by bindView<ImageView>(R.id.employer_profile_editmode_button)
    val addressPencil by bindView<ImageView>(R.id.employer_profile_address_pencil)
    val paymentPencil by bindView<ImageView>(R.id.employer_profile_payment_pencil)

    val averageReviewRatingBar by bindView<ProperRatingBar>(R.id.average_review_ratingbar)
    val totalReviewsTextView by bindView<TextView>(R.id.employer_profile_total_reviews_textview)
    val readAllReviewsTextView by bindView<TextView>(R.id.employer_profile_reviews_preview_read_all_reviews_textview)
    val phoneNumber by bindView<TextView>(R.id.employer_profile_phone_textview)
    val email by bindView<TextView>(R.id.employer_profile_email_textview)
    val fullNameTextView by bindView<TextView>(R.id.employer_profile_full_name_textview)
    val companyTextView by bindView<TextView>(R.id.employer_profile_company_details_textview)
    val addressTextView by bindView<TextView>(R.id.employer_profile_address_textview)
    val paymentTextView by bindView<TextView>(R.id.employer_profile_payment_info_textview)

    val reviewWriteUpTextView by bindView<TextView>(R.id.employer_profile_reviews_preview_review_description)
    val reviewerTextView by bindView<TextView>(R.id.employer_profile_reviews_preview_reviewer_textview)
    val reviewWorkDoneTextView by bindView<TextView>(R.id.employer_profile_reviews_preview_work_done_textview)
    val reviewRatingBar by bindView<ProperRatingBar>(R.id.employer_profile_reviews_preview_ratingbar)

    val fullNameEditText by bindView<EditText>(R.id.employer_profile_full_name_edittext)
    val phoneEditText by bindView<EditText>(R.id.employer_profile_phone_edittext)
    val emailEditText by bindView<EditText>(R.id.employer_profile_email_edittext)
    val companyDetailsEditText by bindView<EditText>(R.id.employer_profile_company_details_edittext)

    val fullNameSwitcher by bindView<ViewSwitcher>(R.id.employer_profile_full_name_switcher)
    val phoneSwitcher by bindView<ViewSwitcher>(R.id.employer_profile_phone_switcher)
    val emailSwitcher by bindView<ViewSwitcher>(R.id.employer_profile_email_switcher)
    val companyDetailsSwitcher by bindView<ViewSwitcher>(R.id.employer_profile_company_details_switcher)

    var editMode: Boolean = false

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        navigationFooter.selectButtonHighlighted(NavigationFooter.FOOTER_ENUM.PROFILE)
        initializeTextViews()
        initializeRatingBars()
        setOnClickListeners()
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
        companyTextView.text = presenter.getProfileCompanyDetails()
        companyDetailsEditText.setText(presenter.getProfileCompanyDetails())

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

    fun setOnClickListeners() {
        totalReviewsTextView.setOnClickListener { presenter.goToReviews() }
        readAllReviewsTextView.setOnClickListener { presenter.goToReviews() }
        editModePencil.setOnClickListener { switchEditMode() }
        addressPencil.setOnClickListener { editAddress() }
        paymentPencil.setOnClickListener { editPaymentInfo() }
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
            companyDetailsSwitcher.showNext()

        } else {

            if (fullNameEditText.text.toString() != presenter.getProfileFullName()) { presenter.updateFullName(fullNameEditText.text.toString()) }
            if (phoneEditText.text.toString() != presenter.getProfileTelephone()) { presenter.updateTelNum(phoneEditText.text.toString()) }
            if (emailEditText.text.toString() != presenter.getProfileEmail()) { presenter.updateEmail(emailEditText.text.toString()) }
            if (companyDetailsEditText.text.toString() != presenter.getProfileCompanyDetails()) { presenter.updateCompanyDetails(companyDetailsEditText.text.toString()) }

            editModePencil.setImageResource(R.drawable.edit_circle_button)
            addressPencil.visibility = View.GONE
            paymentPencil.visibility = View.GONE

            fullNameSwitcher.showPrevious()
            phoneSwitcher.showPrevious()
            emailSwitcher.showPrevious()
            companyDetailsSwitcher.showPrevious()
        }
    }

    override fun update() {
        initializeTextViews()
        initializeRatingBars()
    }

    override fun editAddress() {
    }

    override fun editPaymentInfo() {
    }

    override fun busy() {
    }

    override fun idle() {
    }

    private fun buildStringFromDate(calendar: Calendar) : String {
        val sdf = SimpleDateFormat("MMMM DD yyyy", Locale.US)
        return sdf.format(calendar.time)
    }


}
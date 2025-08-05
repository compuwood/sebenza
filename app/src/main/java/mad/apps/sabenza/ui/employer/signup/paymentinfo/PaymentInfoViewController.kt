package mad.apps.sabenza.ui.employer.signup.paymentinfo

import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.MissingFeature
import javax.inject.Inject

class PaymentInfoViewController : BaseViewController(), PaymentInfoViewInterface {

    override fun layout(): Int = R.layout.employer_payment_info

    @Inject lateinit var presenter : PaymentInfoPresenterInterface

    val footerContainer by bindView<FrameLayout>(R.id.signup_employer_complete_profile_button)

    val nameOnCardEditText by bindView<EditText>(R.id.employer_name_on_card_edittext)
    val cardNumberEditText by bindView<EditText>(R.id.employer_card_number_edittext)
    val expiryDateEditText by bindView<EditText>(R.id.employer_expiry_date_edittext)
    val cvvEditText by bindView<EditText>(R.id.employer_cvv_edittext)
    val couponCodeEditText by bindView<EditText>(R.id.employer_coupon_code_edittext)


    override fun initView(view: View) {
        super.initView(view)
        DaggerService.getDaggerComponent(view.context).inject(this)

        footerContainer.setOnClickListener {
            if (validate()) {
                presenter.savePaymentInfo(
                        name = nameOnCardEditText.text.toString(),
                        cardNumber = cardNumberEditText.text.toString(),
                        expiry = expiryDateEditText.text.toString(),
                        cvv = cvvEditText.text.toString(),
                        couponCode = couponCodeEditText.text.toString()
                )
                if (BuildConfig.IS_UI_BUILD) {
                    presenter.gotoNextScreen()
                }
            }
        }


    }


    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.takeView(this)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.dropView()
    }

    //We disable back on this screen
    override fun handleBack(): Boolean {
        return true
    }

    fun validate(): Boolean {

//        checkIfEmpty(nameOnCardEditText,"Name on Card is a required field")

        if (nameOnCardEditText.error.isNullOrEmpty() && cardNumberEditText.error.isNullOrEmpty() && expiryDateEditText.error.isNullOrEmpty() && cvvEditText.error.isNullOrEmpty() && couponCodeEditText.error.isNullOrEmpty()){
            return true
        }
        return false
    }

    override fun busy() {
        MissingFeature.busy(getContext())
    }

    override fun idle() {
        MissingFeature.idle(getContext())
    }

    override fun success() {
        MissingFeature.show(getContext(), "CARD ADDED SUCCESSFULLY")
        presenter.completeProfile()
    }

    override fun error(error: String) {
        MissingFeature.error(getContext(), error)
    }
}
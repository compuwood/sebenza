package mad.apps.sabenza.ui.employee.signup.paymentinfo

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

    override fun layout(): Int = R.layout.employee_payment_info

    @Inject lateinit var presenter: PaymentInfoPresenterInterface

    val footerContainer by bindView<FrameLayout>(R.id.signup_employee_complete_profile_button)

    val accountHolderEditText by bindView<EditText>(R.id.employee_account_holder_edittext)
    val bankEditText by bindView<EditText>(R.id.employee_bank_edittext)
    val accountNumberEditText by bindView<EditText>(R.id.employee_account_number_edittext)
    val otherDetailsEditText by bindView<EditText>(R.id.employee_other_details_edittext)


    override fun initView(view: View) {
        super.initView(view)
        DaggerService.getDaggerComponent(view.context).inject(this)

        footerContainer.setOnClickListener {
            if (validate()) {
                presenter.savePaymentInfo(
                        name = accountHolderEditText.text.toString(),
                        bank = bankEditText.text.toString(),
                        accountNum = accountNumberEditText.text.toString(),
                        details = otherDetailsEditText.text.toString()
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

    fun validate(): Boolean {

//        checkIfEmpty(accountHolderEditText, "Account Holder's name is a required field")

        if (accountHolderEditText.error.isNullOrEmpty() && bankEditText.error.isNullOrEmpty() && accountNumberEditText.error.isNullOrEmpty() && otherDetailsEditText.error.isNullOrEmpty()) {
            return true
        }
        return false
    }

    override fun busy() {
        MissingFeature.busy(context = getContext())
    }

    override fun success() {
        if (!BuildConfig.IS_UI_BUILD) {
            presenter.gotoNextScreen()
        }
    }

    override fun idle() {
        MissingFeature.idle(context = getContext())
    }

    override fun error(error: String) {
        MissingFeature.error(context = getContext(), error = "Couldn't link Error")
    }
}
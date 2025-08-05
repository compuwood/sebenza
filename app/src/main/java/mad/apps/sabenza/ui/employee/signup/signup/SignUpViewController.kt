package mad.apps.sabenza.ui.employee.signup.signup

import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.MissingFeature
import mad.apps.sabenza.ui.util.checkIfEmpty
import mad.apps.sabenza.ui.util.isValidEmail
import javax.inject.Inject

class SignUpViewController : BaseViewController(), SignUpViewInterface {

    override fun layout(): Int = R.layout.employee_signup

    @Inject lateinit var presenter : SignUpPresenterInterface

    val footerContainer by bindView<FrameLayout>(R.id.signup_employee_create_profile_button)
    val employeeSignupDisclaimer by bindView<TextView>(R.id.employee_signup_disclaimer)

    val emailEditText by bindView<EditText>(R.id.employee_email_edittext)
    val passwordEditText by bindView<EditText>(R.id.employee_password_edittext)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        val termsAndConditionsUrl = "https://www.google.com/"
        val termsAndConditionsLink = "<a href='$termsAndConditionsUrl'>Terms & Conditions</a>"
        val disclaimerHtmlText = "By signing up you agree to Sebenza's <br> $termsAndConditionsLink"
        employeeSignupDisclaimer.linksClickable = true
        employeeSignupDisclaimer.movementMethod = LinkMovementMethod.getInstance()
        employeeSignupDisclaimer.text = (Html.fromHtml(disclaimerHtmlText))

        footerContainer.setOnClickListener{
            if (validate()) {
                presenter.saveEmailAndPassword(email = emailEditText.text.toString(), password = passwordEditText.text.toString())
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

        if (!checkIfEmpty(emailEditText,"Email Address is Required")) {
            if (!isValidEmail(emailEditText.text.toString())) {
                emailEditText.error = "Email address is invalid"
            }
        }

        if (!checkIfEmpty(passwordEditText,"Password is required")) {
            if (passwordEditText.text.toString().length < 6) {
                passwordEditText.error = "Password must be at least 6 characters"
//            } else {
//                if (!checkIfEmpty(passwordConfirmEditText, "Please confirm your password")) {
//                    if (!passwordConfirmEditText.text.toString().contentEquals(passwordEditText.text.toString())) {
//                        passwordConfirmEditText.error = "Passwords do not match"
//                    }
//                }
            }
        }

        if (emailEditText.error.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()){
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

    override fun successfulSignUp() {
        presenter.goToCreateProfileWithEmail()
    }

    override fun failedSignUp(error: String) {
        MissingFeature.error(getContext(), error)
    }


}
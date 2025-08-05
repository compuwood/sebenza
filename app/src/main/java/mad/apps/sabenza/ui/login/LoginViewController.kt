package mad.apps.sabenza.ui.login

import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.checkIfEmpty
import mad.apps.sabenza.ui.util.isValidEmail
import mad.apps.sabenza.ui.widget.ErrorModal
import mad.apps.sabenza.ui.widget.LoadingScreen
import javax.inject.Inject


class LoginViewController : BaseViewController(), LoginViewInterface {
    override fun layout(): Int = R.layout.login

    @Inject lateinit var presenter : LoginPresenterInterface

    val loginButton by bindView<FrameLayout>(R.id.login_employer_login_button)
    val forgotPasswordButton by bindView<TextView>(R.id.login_employer_forgot_password_text)
    val emailEditText by bindView<EditText>(R.id.employer_login_email_edittext)
    val passwordEditText by bindView<EditText>(R.id.employer_login_password_edittext)

    val forgotPasswordModal by bindView<RelativeLayout>(R.id.login_employer_forgot_password_modal)
    val forgotPasswordEmail by bindView<EditText>(R.id.employer_forgot_password_email_edittext)
    val resetPasswordButton by bindView<FrameLayout>(R.id.employer_reset_password_button)

    val errorModal by bindView<ErrorModal>(R.id.login_employer_error_modal)
    val loadingScreen by bindView<LoadingScreen>(R.id.login_employer_loading_screen)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        SetupUiTesting()
        SetupForgotPassword()

        loginButton.setOnClickListener{
            if (validate()) {
                presenter.attemptLogin(email = emailEditText.text.toString(), password = passwordEditText.text.toString())
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

    fun SetupUiTesting() {
//        if (BuildConfig.IS_UI_BUILD) {
//            emailEditText.setText("wrong@email.com")
//            passwordEditText.setText("wrongpassword")
//        } else if (true) {
            emailEditText.setText("androidTestUser11@email.com")
            passwordEditText.setText("androidTestPass")
//        }
    }

    fun SetupForgotPassword() {
        //Forgot Password Modal
        forgotPasswordButton.setOnClickListener {
            val anim = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 250
            forgotPasswordModal.startAnimation(anim)
            forgotPasswordModal.visibility = View.VISIBLE
        }

        forgotPasswordModal.setOnClickListener {
            val anim = AlphaAnimation(1.0f, 0.0f)
            anim.duration = 250
            forgotPasswordModal.startAnimation(anim)
            forgotPasswordModal.visibility = View.GONE
        }

        resetPasswordButton.setOnClickListener {
            val email = forgotPasswordEmail.text.toString()
            if (isValidEmail(email)) {
                presenter.resetPassword(email)
            } else {
                forgotPasswordEmail.error = "Email address is invalid"
            }
        }
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
            }
        }

        if (emailEditText.error.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()){
            return true
        }
        return false
    }

    override fun busy() {
        loadingScreen.show()
    }

    override fun idle() {
        loadingScreen.hide()
    }

    override fun showLoginSuccess() {
//        Toast.makeText(getContext(), "SHOW LOGIN SUCCESS", Toast.LENGTH_SHORT).show()
//        view?.postDelayed({
//        }, 2000)
    }

    override fun showLoginError() {
        errorModal.show("The email / password that you’ve entered is incorrect. If you’ve forgotten these details, please email us on info@sebenza.com.")
    }

}
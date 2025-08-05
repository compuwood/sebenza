package mad.apps.sabenza.ui.employer.signup.createprofile

import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.checkIfEmpty
import mad.apps.sabenza.ui.util.isPhoneNumber
import javax.inject.Inject

class CreateProfileViewController : BaseViewController(), CreateProfileViewInterface {

    override fun layout(): Int = R.layout.employer_create_profile

    @Inject lateinit var presenter : CreateProfilePresenterInterface

    val footerContainer by bindView<FrameLayout>(R.id.signup_employer_next_button)
    val profilePictureButton by bindView<LinearLayout>(R.id.signup_employer_upload_profile_picture_button)

    val firstNameEditText by bindView<EditText>(R.id.employer_firstname_edittext)
    val lastNameEditText by bindView<EditText>(R.id.employer_lastname_edittext)
    val mobileEditText by bindView<EditText>(R.id.employer_phone_edittext)
    val companyEditText by bindView<EditText>(R.id.employer_company_edittext)
    val companyDescriptionEditText by bindView<EditText>(R.id.employer_company_description_edittext)


    override fun initView(view: View) {
        super.initView(view)
        DaggerService.getDaggerComponent(view.context).inject(this)

        footerContainer.setOnClickListener {
            if (validate()) {
                presenter.saveDetails(
                        firstName = firstNameEditText.text.toString(),
                        lastName = lastNameEditText.text.toString(),
                        number = mobileEditText.text.toString(),
                        company = companyEditText.text.toString(),
                        companyDescription = companyDescriptionEditText.text.toString()
                )
                presenter.gotoNextScreen()
            }
        }
        profilePictureButton.setOnClickListener {
            presenter.uploadProfilePicture()
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

        if (!checkIfEmpty(firstNameEditText, "First Name is required")) {
            if (firstNameEditText.text.toString().length < 3) {
                firstNameEditText.error = "Name must be at least 3 characters"
            }
        }

        if (!checkIfEmpty(lastNameEditText, "Last Name is required")) {
            if (lastNameEditText.text.toString().length < 3) {
                lastNameEditText.error = "Name must be at least 3 characters"
            }
        }

        if (!checkIfEmpty(mobileEditText, "Mobile Number is required")) {
            if (!isPhoneNumber(mobileEditText.text.toString())) {
                mobileEditText.error = "Not a valid phone number"
            }
        }

        if (firstNameEditText.error.isNullOrEmpty() && lastNameEditText.error.isNullOrEmpty() && mobileEditText.error.isNullOrEmpty() && companyEditText.error.isNullOrEmpty() && companyDescriptionEditText.error.isNullOrEmpty()){
            return true
        }
        return false
    }

    override fun busy() {
        Toast.makeText(getContext(), "SHOW A BUSY STATE", Toast.LENGTH_SHORT).show()
    }

    override fun idle() {
        Toast.makeText(getContext(), "SHOW AN IDLE STATE", Toast.LENGTH_SHORT).show()
    }
}
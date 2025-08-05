package mad.apps.sabenza.ui.employee.signup.createprofile

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.MissingFeature
import mad.apps.sabenza.ui.util.checkIfEmpty
import mad.apps.sabenza.ui.util.isPhoneNumber
import javax.inject.Inject

class CreateProfileViewController : BaseViewController(), CreateProfileViewInterface {

    override fun layout(): Int = R.layout.employee_create_profile

    @Inject lateinit var presenter : CreateProfilePresenterInterface

    val footerContainer by bindView<FrameLayout>(R.id.signup_employee_next_button)
    val profilePictureButton by bindView<LinearLayout>(R.id.signup_employee_upload_profile_picture_button)
    val scanUploadIdDocButton by bindView<LinearLayout>(R.id.signup_employee_scan_upload_docs_button)
    val driversLicenceCheckbox by bindView<CheckBox>(R.id.signup_employee_drivers_licence_checkbox)

    val firstNameEditText by bindView<EditText>(R.id.employee_firstname_edittext)
    val lastNameEditText by bindView<EditText>(R.id.employee_lastname_edittext)
    val mobileEditText by bindView<EditText>(R.id.employee_phone_edittext)


    override fun initView(view: View) {
        super.initView(view)
        DaggerService.getDaggerComponent(view.context).inject(this)

        footerContainer.setOnClickListener {
            if (validate()) {
                presenter.saveDetails(
                        firstName = firstNameEditText.text.toString(),
                        lastName = lastNameEditText.text.toString(),
                        number = mobileEditText.text.toString(),
                        driversLicence = driversLicenceCheckbox.isChecked
                )
                presenter.gotoNextScreen()
            }
        }
        profilePictureButton.setOnClickListener {
            presenter.updateProfilePicture("")
        }
        scanUploadIdDocButton.setOnClickListener {
            presenter.scanUploadIdDocs()
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

        if (firstNameEditText.error.isNullOrEmpty() && lastNameEditText.error.isNullOrEmpty() && mobileEditText.error.isNullOrEmpty()){
            return true
        }
        return false
    }

    override fun busy() {
        MissingFeature.busy(getContext())
    }

    override fun success() {
        presenter.gotoNextScreen()
    }

    override fun idle() {
        MissingFeature.idle(getContext())
    }

    override fun error(error: String) {
        MissingFeature.error(getContext(), error)
    }
}
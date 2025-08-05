package mad.apps.sabenza.ui.employee.signup.address

import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import javax.inject.Inject

class YourAddressViewController : BaseViewController(), YourAddressViewInterface {

    override fun layout(): Int = R.layout.employee_your_address

    @Inject lateinit var presenter : YourAddressPresenterInterface

    val footerContainer by bindView<FrameLayout>(R.id.signup_employee_address_next_button)

    val addressLine1EditText by bindView<EditText>(R.id.employee_addressline1_edittext)
    val cityEditText by bindView<EditText>(R.id.employee_city_edittext)
    val stateEditText by bindView<EditText>(R.id.employee_state_edittext)
    val zipCodeEditText by bindView<EditText>(R.id.employee_zipcode_edittext)

    override fun initView(view: View) {
        super.initView(view)
        DaggerService.getDaggerComponent(view.context).inject(this)

        footerContainer.setOnClickListener{
            presenter.saveAddressDetails(
                    line1 = addressLine1EditText.text.toString(),
                    city = cityEditText.text.toString(),
                    state = stateEditText.text.toString(),
                    zipCode = zipCodeEditText.text.toString()
            )
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

    override fun addressUpdatedSuccessfully() {
        presenter.gotoNextScreen()
    }
}
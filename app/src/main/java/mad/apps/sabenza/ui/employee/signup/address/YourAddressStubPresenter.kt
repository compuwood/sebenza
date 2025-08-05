package mad.apps.sabenza.ui.employee.signup.address

import mad.apps.sabenza.framework.ui.BasePresenter
import zendesk.suas.Store

class YourAddressStubPresenter(store : Store) : BasePresenter<YourAddressViewInterface>(store), YourAddressPresenterInterface {
    override fun saveAddressDetails(line1: String, city: String, state: String, zipCode: String) {
    }

    override fun gotoNextScreen() {
//        router().pushController(RouterTransaction.with(EmployeePaymentInfoScreen.provideViewController())
//                .pushChangeHandler(FadeChangeHandler())
//                .popChangeHandler(FadeChangeHandler()))
    }
}
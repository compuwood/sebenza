package mad.apps.sabenza.ui.employer.signup.address

import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.signup.paymentinfo.PaymentInfoScreen
import zendesk.suas.Store

class YourAddressStubPresenter(store : Store) : BasePresenter<YourAddressViewInterface>(store), YourAddressPresenterInterface {
    override fun saveAddressDetails(line1: String, city: String, state: String, zipCode: String) {
        gotoNextScreen()
    }

    override fun gotoNextScreen() {
        router().pushController(RouterTransaction.with(PaymentInfoScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }
}
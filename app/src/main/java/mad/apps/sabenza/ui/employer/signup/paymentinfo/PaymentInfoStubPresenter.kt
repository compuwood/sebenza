package mad.apps.sabenza.ui.employer.signup.paymentinfo

import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import zendesk.suas.Store


class PaymentInfoStubPresenter(store : Store) : BasePresenter<PaymentInfoViewInterface>(store), PaymentInfoPresenterInterface {
    override fun savePaymentInfo(name: String, cardNumber: String, expiry: String, cvv: String, couponCode: String) {
    }

    override fun completeProfile() {
    }

    override fun gotoNextScreen() {
        router().pushController(RouterTransaction.with(ViewProjectsScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }

}
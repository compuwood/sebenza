package mad.apps.sabenza.ui.employer.signup.paymentinfo

import mad.apps.sabenza.data.model.payment.CreditCard
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.AddCreditCardAction
import mad.apps.sabenza.state.models.PaymentModel
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import zendesk.suas.Store
import zendesk.suas.Subscription

class PaymentInfoPresenter(store: Store) : BasePresenter<PaymentInfoViewInterface>(store), PaymentInfoPresenterInterface {

    var subscription : Subscription? = null

    override fun loaded() {
        subscription = store.addListener(PaymentModel::class.java, {
            if (it.defaultCard != null) {
                view.idle()
                view.success()
            } else {
                view.error("No Card Added")
            }
        })
        subscription?.addListener()
    }

    override fun dropView() {
        subscription?.removeListener()
    }

    override fun savePaymentInfo(name: String, cardNumber: String, expiry: String, cvv: String, couponCode: String) {
        view.busy()
        store.dispatch(AddCreditCardAction(creditCard = CreditCard(nameOnCard = name, partOfNumber = cardNumber.substring(0,6), expiryYear = 2020, expiryMonth = 5)))
    }

    override fun completeProfile() {
        routeToNewTop(ViewProjectsScreen.provideViewController())
    }

    override fun gotoNextScreen() {

    }
}
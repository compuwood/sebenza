package mad.apps.sabenza.ui.employee.signup.paymentinfo

import mad.apps.sabenza.data.model.payment.BankAccount
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.AddAndLinkBankAccountAction
import mad.apps.sabenza.state.models.PaymentModel
import mad.apps.sabenza.ui.employee.jobsfeed.JobsFeedScreen
import mad.apps.sabenza.ui.util.MissingFeature
import zendesk.suas.Listener
import zendesk.suas.Store

class PaymentInfoPresenter(store: Store) : BasePresenter<PaymentInfoViewInterface>(store), PaymentInfoPresenterInterface {

    var paymentListener : Listener<PaymentModel>? = null

    override fun savePaymentInfo(name: String, bank: String, accountNum: String, details: String) {
        MissingFeature.show(context = view.getContext(), feature = "We need to add support for branch numbers? and account numbers that are longer than 6 lines")

        val bankAccount = BankAccount(bankName = bank, accountName = name, branchNumber = "0", accountNumber = accountNum)

        paymentListener = Listener {
            if (it.hasBankAccounts()) {
                view.success()
            } else {
                view.error("No bank accounts added")
            }
        }

        store.addListener(PaymentModel::class.java, paymentListener!!)
        store.dispatch(AddAndLinkBankAccountAction(bankAccount))
    }

    override fun dropView() {
        super.dropView()
        if (paymentListener != null) {
            store.removeListener(paymentListener!!)
        }
    }

    override fun gotoNextScreen() {
        routeToNewTop(JobsFeedScreen.provideViewController())
    }
}
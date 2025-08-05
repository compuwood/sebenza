package mad.apps.sabenza.ui.employee.signup.paymentinfo

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employee.jobsfeed.JobsFeedScreen
import zendesk.suas.Store

class PaymentInfoStubPresenter(store : Store) : BasePresenter<PaymentInfoViewInterface>(store), PaymentInfoPresenterInterface {
    override fun savePaymentInfo(name: String, bank: String, accountNum: String, details: String) {

    }

    override fun gotoNextScreen() {
        routeToNewTop(JobsFeedScreen.provideViewController())
    }

}
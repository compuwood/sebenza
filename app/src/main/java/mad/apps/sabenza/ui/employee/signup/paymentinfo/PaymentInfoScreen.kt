package mad.apps.sabenza.ui.employee.signup.paymentinfo

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import mad.apps.sabenza.ui.util.SuccessErrorViewInterface
import zendesk.suas.Store

interface PaymentInfoViewInterface : ViewInterface, BusyViewInterface, SuccessErrorViewInterface

interface PaymentInfoPresenterInterface : PresenterInterface<PaymentInfoViewInterface> {
    fun savePaymentInfo(name: String, bank: String, accountNum: String, details: String)
    fun gotoNextScreen()
}

object PaymentInfoScreen : BaseScreen<PaymentInfoViewInterface, PaymentInfoPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return PaymentInfoViewController()
    }

    override fun providePresenter(store: Store): PaymentInfoPresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            PaymentInfoStubPresenter(store)
        } else {
            PaymentInfoPresenter(store)
        }
    }
}
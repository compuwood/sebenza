package mad.apps.sabenza.ui.employer.signup.paymentinfo

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
    fun savePaymentInfo(name: String, cardNumber: String, expiry: String, cvv: String, couponCode: String)
    fun completeProfile()
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
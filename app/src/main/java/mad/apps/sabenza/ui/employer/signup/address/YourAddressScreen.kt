package mad.apps.sabenza.ui.employer.signup.address

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface YourAddressViewInterface : ViewInterface, BusyViewInterface {
    fun addressUpdatedSuccessfully()
    fun errorUpdatingAddress(error: String)
}

interface YourAddressPresenterInterface : PresenterInterface<YourAddressViewInterface> {
    fun gotoNextScreen()
    fun saveAddressDetails(line1: String, city: String, state: String, zipCode: String)
}

object YourAddressScreen : BaseScreen<YourAddressViewInterface, YourAddressPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return YourAddressViewController()
    }

    override fun providePresenter(store: Store): YourAddressPresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            YourAddressStubPresenter(store)
        } else {
            YourAddressPresenter(store)
        }
    }
}
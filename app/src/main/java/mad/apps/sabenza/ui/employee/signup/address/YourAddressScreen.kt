package mad.apps.sabenza.ui.employee.signup.address

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import zendesk.suas.Store

interface YourAddressViewInterface : ViewInterface {
    fun addressUpdatedSuccessfully()
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
        return YourAddressStubPresenter(store)
    }
}
package mad.apps.sabenza.ui.blankscreen

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import zendesk.suas.Store

interface BlankScreenEmployeeViewInterface : ViewInterface
interface BlankScreenEmployeePresenterInterface : PresenterInterface<BlankScreenEmployeeViewInterface>

object BlankScreenEmployee : BaseScreen<BlankScreenEmployeeViewInterface, BlankScreenEmployeePresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return BlankViewControllerEmployee()
    }

    override fun providePresenter(store: Store): BlankScreenEmployeePresenterInterface {
        return BlankPresenterEmployee(store)
    }
}
package mad.apps.sabenza.ui.blankscreen

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import zendesk.suas.Store


interface BlankScreenEmployerViewInterface : ViewInterface
interface BlankScreenEmployerPresenterInterface : PresenterInterface<BlankScreenEmployerViewInterface>

object BlankScreenEmployer : BaseScreen<BlankScreenEmployerViewInterface, BlankScreenEmployerPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return BlankViewControllerEmployer()
    }

    override fun providePresenter(store: Store): BlankScreenEmployerPresenterInterface {
        return BlankPresenterEmployer(store)
    }
}
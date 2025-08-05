package mad.apps.sabenza.ui.widget.headerviewbase.presenter

import mad.apps.sabenza.framework.ui.ViewPresenter
import mad.apps.sabenza.ui.widget.headerviewbase.HeaderViewBase
import zendesk.suas.Store

class HeaderViewBaseStubPresenter(store: Store) : ViewPresenter<HeaderViewBase>(store), HeaderViewBasePresenterInterface {
    override fun goBack() {
        routePop()
    }
}
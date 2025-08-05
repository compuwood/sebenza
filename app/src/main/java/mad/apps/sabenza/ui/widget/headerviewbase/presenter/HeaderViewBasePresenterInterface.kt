package mad.apps.sabenza.ui.widget.headerviewbase.presenter

import mad.apps.sabenza.framework.ui.ViewPresenterInterface
import mad.apps.sabenza.ui.widget.headerviewbase.HeaderViewBase
import zendesk.suas.Store

interface HeaderViewBasePresenterInterface : ViewPresenterInterface<HeaderViewBase> {

    fun goBack()

    object Provider {
        fun providePresenter(store : Store) : HeaderViewBasePresenterInterface {
//            if (BuildConfig.IS_UI_BUILD) {
                return HeaderViewBaseStubPresenter(store)
//            } else {
//                return HeaderViewBasePresenter(store)
//            }
        }
    }
}
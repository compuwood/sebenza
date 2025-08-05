package mad.apps.sabenza.framework.ui

import zendesk.suas.Store

interface BaseScreen<VIEW : ViewInterface, PRESENTER : PresenterInterface<VIEW>> : SimpleScreen {
    fun providePresenter(store: Store): PRESENTER
}


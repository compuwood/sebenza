package mad.apps.sabenza.framework.ui

import zendesk.suas.Action

interface PresenterInterface<T : ViewInterface> {
    fun takeView(baseViewController: T)
    fun dropView()
    fun fireAction(action: Action<*>)
}
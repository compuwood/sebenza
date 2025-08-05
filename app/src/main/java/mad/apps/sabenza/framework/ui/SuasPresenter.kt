package mad.apps.sabenza.framework.ui

import zendesk.suas.Action
import zendesk.suas.Store

open class SuasPresenter(val store: Store) {

    open fun fireAction(action: Action<*>) {
        if (!filterAction(action)) {
            processAction(action)
        }
    }

    protected open fun processAction(action: Action<*>) {
        store.dispatch(action)
    }

    open protected fun filterAction(action: Action<*>): Boolean {
        return false
    }

}
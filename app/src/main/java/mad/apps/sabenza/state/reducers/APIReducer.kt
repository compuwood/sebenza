package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.framework.redux.Reducer
import mad.apps.sabenza.state.action.MiddlewareErrorAction
import mad.apps.sabenza.state.models.ErrorModel
import zendesk.suas.Action

class APIReducer : Reducer<ErrorModel>() {
    override fun getInitialState(): ErrorModel = ErrorModel()

    override fun reduce(state: ErrorModel, action: Action<*>): ErrorModel? {
        return when (action) {
            is MiddlewareErrorAction -> state.copy(
                    apiErrors = state.apiErrors.plus(action.throwable),
                    mostRecentError = action.throwable)
            else -> state
        }
    }
}
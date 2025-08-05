package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.framework.redux.Reducer
import mad.apps.sabenza.state.action.EmployerUpdatedAction
import mad.apps.sabenza.state.models.EmployerModel
import zendesk.suas.Action

class EmployerReducer : Reducer<EmployerModel>() {

    override fun reduce(state: EmployerModel, action: Action<*>): EmployerModel? {
        return when (action) {
            is EmployerUpdatedAction -> {
                return state.copy(employer = action.employer, hasEmployer = true)
            }

            else -> state
        }
    }

    override fun getInitialState(): EmployerModel {
        return EmployerModel()
    }
}
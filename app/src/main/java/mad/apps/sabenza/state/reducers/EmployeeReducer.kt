package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.framework.redux.Reducer
import mad.apps.sabenza.state.action.EmployeeSkillsUpdatedAction
import mad.apps.sabenza.state.action.EmployeeUpdatedAction
import mad.apps.sabenza.state.models.EmployeeModel
import zendesk.suas.Action

class EmployeeReducer : Reducer<EmployeeModel>() {
    override fun getInitialState(): EmployeeModel {
        return EmployeeModel()
    }

    override fun reduce(state: EmployeeModel, action: Action<*>): EmployeeModel? {
        return when (action) {
            is EmployeeUpdatedAction -> {
                state.copy(employee = action.employee, hasEmployee = true)
            }

            is EmployeeSkillsUpdatedAction -> {
                state.copy(skills = action.skills)
            }

            else -> state
        }
    }
}
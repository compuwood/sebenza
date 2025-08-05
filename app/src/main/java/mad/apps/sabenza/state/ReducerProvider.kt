package mad.apps.sabenza.state

import mad.apps.sabenza.state.reducers.*
import zendesk.suas.Reducer

class ReducerProvider {

    fun reducers() : List<Reducer<out Any>> {
        return listOf(
                SettingsReducer(),
                LoginReducer(),
                SkillsReducer(),
                EmployeeReducer(),
                EmployerReducer(),
                PaymentReducer(),
                ProjectReducer(),
                EmployeeJobsReducer(),
                APIReducer(),
                AddressReducer())
    }

}
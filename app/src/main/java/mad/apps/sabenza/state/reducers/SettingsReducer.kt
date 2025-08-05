package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.state.action.RefreshAction
import mad.apps.sabenza.state.models.SettingModel
import zendesk.suas.Action
import zendesk.suas.Reducer

class SettingsReducer : Reducer<SettingModel>() {
    override fun reduce(state: SettingModel, action: Action<*>): SettingModel? {
        return when (action) {
            is RefreshAction -> state.copy(initialized = true)

//            is FormViewController.NameAction ->
//                state.copy(username = (action).getData<String>()!!)
//
//            is FormViewController.AgeAction ->
//            try {
//                state.copy(age = (action).getData<String>()!!.toInt())
//            } catch (e: Exception) {
//                state.copy(age = 0)
//            }

            else -> state
        }
    }

    override fun getInitialState(): SettingModel {
        return SettingModel()
    }
}
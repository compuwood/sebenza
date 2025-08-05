package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.framework.redux.Reducer
import mad.apps.sabenza.state.action.NewSkillsAvailableAction
import mad.apps.sabenza.state.models.SkillsModel
import zendesk.suas.Action


class SkillsReducer : Reducer<SkillsModel>() {
    override fun reduce(state: SkillsModel, action: Action<*>): SkillsModel? {
        return when (action) {
            is NewSkillsAvailableAction -> {
                return state.copy(availableSkills = action.skills)
            }

            else -> state
        }
    }

    override fun getInitialState(): SkillsModel {
        return SkillsModel()
    }
}
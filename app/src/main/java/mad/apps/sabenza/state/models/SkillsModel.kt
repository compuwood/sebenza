package mad.apps.sabenza.state.models

import mad.apps.sabenza.data.rpc.calls.employee.Skill
import mad.apps.sabenza.framework.redux.RxModel

data class SkillsModel(
        val availableSkills : List<Skill> = ArrayList()
) : RxModel()
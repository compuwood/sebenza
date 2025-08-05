package mad.apps.sabenza.state.models

import mad.apps.sabenza.data.model.jobs.EmployerProject
import mad.apps.sabenza.framework.redux.RxModel

data class ProjectModel(
        val projects : List<EmployerProject> = emptyList()
) : RxModel()
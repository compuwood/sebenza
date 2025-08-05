package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.framework.redux.Reducer
import mad.apps.sabenza.state.action.NewJobAddedAction
import mad.apps.sabenza.state.action.NewProjectAddedAction
import mad.apps.sabenza.state.action.NewProjectsAvailableAction
import mad.apps.sabenza.state.action.RefreshProjectsAndJobsAction
import mad.apps.sabenza.state.models.ProjectModel
import zendesk.suas.Action

class ProjectReducer : Reducer<ProjectModel>() {

    override fun reduce(state: ProjectModel, action: Action<*>): ProjectModel? {
        return when(action) {
            is RefreshProjectsAndJobsAction -> state

            is NewProjectsAvailableAction -> {
                state.copy(projects = action.projects)
            }

            is NewProjectAddedAction -> {
                state.copy(projects = state.projects.plus(action.project))
            }

            else -> state
        }
    }

    override fun getInitialState(): ProjectModel {
        return ProjectModel()
    }
}
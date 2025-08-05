package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.data.model.jobs.EmployeeJobPair
import mad.apps.sabenza.framework.redux.Reducer
import mad.apps.sabenza.state.action.*
import mad.apps.sabenza.state.models.EmployeeJobsModel
import zendesk.suas.Action


class EmployeeJobsReducer : Reducer<EmployeeJobsModel>() {
    override fun reduce(state: EmployeeJobsModel, action: Action<*>): EmployeeJobsModel? {
        return when(action) {
            is NewEmployeeJobsAvailableAction -> {
               state.copy(jobs = action.jobs)
            }

            is LinkedJobsUpdated -> {
                state.copy(linkedJobs = action.jobEmployees)
            }

            is JobSuccessfullyAppliedForAction -> {
                state.copy(linkedJobs = action.jobEmployees)
            }

            is JobStatusChangedAction -> {
                state.copy(linkedJobs = action.jobEmployees)
            }

            else -> state
        }
    }

    override fun getInitialState(): EmployeeJobsModel {
        return EmployeeJobsModel()
    }
}
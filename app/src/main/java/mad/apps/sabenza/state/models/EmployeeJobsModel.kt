package mad.apps.sabenza.state.models

import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.JobEmployee
import mad.apps.sabenza.framework.redux.RxModel

data class EmployeeJobsModel(
        val jobs : List<Job> = emptyList(),
        val linkedJobs : List<JobEmployee> = emptyList()
): RxModel()
package mad.apps.sabenza.state.action

import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.JobEmployee
import mad.apps.sabenza.framework.redux.Action

class RefreshEmployeeJobsAction : Action<Any>("refresh_employee_jobs", Any())

class NewEmployeeJobsAvailableAction(val jobs: List<Job>) : Action<Any>("update", jobs)

class ApplyForJobAction(val jobToApplyFor: Job) : Action<Job>("appy_for_job", jobToApplyFor)

class WithdrawFromJobAction(val jobToWithdrawFrom: Job) : Action<Job>("withdraw_from_job", jobToWithdrawFrom)

class JobSuccessfullyAppliedForAction(val jobEmployees: List<JobEmployee>) : Action<List<JobEmployee>>("job_successfully_applied_for", jobEmployees)

class JobStatusChangedAction(val jobEmployees: List<JobEmployee>) : Action<List<JobEmployee>>("job_status_changed_action", jobEmployees)

class LinkedJobsUpdated(val jobEmployees: List<JobEmployee>) : Action<List<JobEmployee>>("linked_jobs_updated", jobEmployees)

class RefreshLinkedJobsAction() : Action<Any>("refresh_linked_jobs", Any())
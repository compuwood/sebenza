package mad.apps.sabenza.state.action

import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.data.model.jobs.EmployerProject
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.Project
import mad.apps.sabenza.data.model.jobs.ProjectStateEnum
import mad.apps.sabenza.framework.redux.Action

class RefreshProjectsAndJobsAction : Action<Any>("refresh_projects_and_jobs", Any())

class NewProjectsAvailableAction(val projects : List<EmployerProject>) : Action<List<EmployerProject>>("new_projects_available", projects)

class CreateNewProjectAction(val project: Project) : Action<Project>("add_new_project", project)

class NewProjectAddedAction(val project: EmployerProject) : Action<EmployerProject>("new_project_added", project)

class ChangeProjectStateAction(val project: Project, val status: ProjectStateEnum) : Action<Pair<Project, ProjectStateEnum>>("change_project_status", Pair(project, status))

class ProjectStatusChangedAction(val project: Project) : Action<Project>("project_status_changed", project)

class AddJobToProjectAction(val job : Job) : Action<Job>("add_new_job_to_project", job)

class NewJobAddedAction(val job: Job) : Action<Job>("new_job_added", job)

class RemoveJobFromProjectAction(val job: Job, val project: Project) : Action<Pair<Job, Project>>("remove_job_from_project", Pair(job, project))
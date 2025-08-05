package mad.apps.sabenza.util

import com.noveogroup.android.log.LoggerManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert
import mad.apps.sabenza.TestComponent
import mad.apps.sabenza.data.model.jobs.EmployerProject
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.Project
import mad.apps.sabenza.data.model.jobs.ProjectStateEnum
import mad.apps.sabenza.framework.SequencedTestStateListener
import mad.apps.sabenza.framework.StateTestCaseBuilder
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.middleware.BaseMiddlewareTestCase
import mad.apps.sabenza.state.action.AddJobToProjectAction
import mad.apps.sabenza.state.action.ChangeProjectStateAction
import mad.apps.sabenza.state.action.CreateNewProjectAction
import mad.apps.sabenza.state.models.PaymentModel
import mad.apps.sabenza.state.models.ProjectModel
import mad.apps.sabenza.state.models.SkillsModel
import zendesk.suas.Store
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MiddlewareTestProjectHelper : BaseMiddlewareTestCase() {

    @Inject
    lateinit var store: Store

    override fun setupDependencies() {
    }

    init {
        component.inject(this)
    }

    fun addAndPostNewProjectAndJob() : EmployerProject {
        loggedInEmployerState(store)

        val newProject = Project(
                creditCardId = store.state.getState(PaymentModel::class.java)!!.linkedCards.first().creditCard.id,
                description = "Test Project: " + TestStringHelper.nextPrintableString(5)
        )

        val newJob = Job(
                projectId = null,
                creditCardId = store.state.getState(PaymentModel::class.java)!!.linkedCards.first().creditCard.id,
                skillId = store.state.getState(SkillsModel::class.java)!!.availableSkills.first().id,
                description = "Test Job: " + TestStringHelper.nextPrintableString(5),
                quantity = "1",
                duration = "2 hours",
                startDate = "2018-08-04 8:00:00",
                endDate = "2018-08-05 8:00:00")

        val postedState = ProjectStateEnum.POSTED

        val testStateListener = SequencedTestStateListener(
                store.state.getState(ProjectModel::class.java),

                //Test new project has been added
                StateTestCaseBuilder.buildTestCase(
                        error = "No Projects Available",

                        testCase = { newState -> newState.projects.map { it.project.description }.contains(newProject.description) }
                ),

                //Test new job has been added to project, and that project and job state are "DRAFT"
                StateTestCaseBuilder.buildTestCase(
                        error = "No Job Added, Or Project and Job State are Wrong",

                        testCase = { newState ->
                            val project = newState.projects.find { it.project.description == newProject.description }
                            val job = project?.linkedJobs?.find { it.description!!.equals(newJob.description) }

                            (job != null) && (project != null)
                                    && project.project.projectState == ProjectStateEnum.DRAFT
                                    && job.state == Job.JobStateEnum.DRAFT
                        }
                ),

                //Ensure that State changed to posted state
                StateTestCaseBuilder.buildTestCase(
                        error = "State did not change to POSTED",
                        testCase = { newState ->
                            val project = newState.projects.find { it.project.description == newProject.description }!!

                            project.project.projectState == postedState }
                ))

        store.addListener(ProjectModel::class.java, testStateListener)

        testStateListener.stateStream()
                .delay(750, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.first) {
                        0 -> {
                            val project = it.second.projects.find { it.project.description.equals(newProject.description) }!!
                            val job = newJob.copy(projectId = project.project.id)
                            store.dispatch(AddJobToProjectAction(job))
                        }

                        1 -> {
                            store.dispatch(ChangeProjectStateAction(it.second.projects.find { it.project.description == newProject.description }!!.project, postedState))
                        }
                    }
                }, { LoggerManager.getLogger().e(it) })

        store.dispatch(CreateNewProjectAction(newProject))

        val result = testStateListener.awaitCompletionAndResult(500000)
        Assert.assertTrue(result.error, result.isSuccess)

        val project = store.state.getState(ProjectModel::class.java)!!.projects.find { it.project.description == newProject.description }!!
        Assert.assertTrue(project.linkedJobs.isNotEmpty())

        return project
    }

}
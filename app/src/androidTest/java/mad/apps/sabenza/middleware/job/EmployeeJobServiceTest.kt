package mad.apps.sabenza.middleware.job

import android.support.test.runner.AndroidJUnit4
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.EmployeeJobStateEnum
import mad.apps.sabenza.framework.SequencedTestStateListener
import mad.apps.sabenza.framework.StateTestCaseBuilder
import mad.apps.sabenza.framework.TestStateListener
import mad.apps.sabenza.middleware.BaseMiddlewareTestCase
import mad.apps.sabenza.state.action.ApplyForJobAction
import mad.apps.sabenza.state.action.RefreshEmployeeJobsAction
import mad.apps.sabenza.state.models.EmployeeJobsModel
import mad.apps.sabenza.util.MiddlewareTestProjectHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import zendesk.suas.Store
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EmployeeJobServiceTest : BaseMiddlewareTestCase() {

    @Inject
    lateinit var store : Store

    @Before
    override fun setupDependencies() {
        component.inject(this)
        filterState(EmployeeJobsModel::class.java)
    }

    @Test
    fun testGetJobs() {
        loggedInState(store)

        val testStateListener = TestStateListener(
                store.state.getState(EmployeeJobsModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "No Jobs Available",

                        testCase = { newState -> newState.jobs.isNotEmpty() }
                )
        )

        store.addListener(EmployeeJobsModel::class.java,
                testStateListener)

        store.dispatch(RefreshEmployeeJobsAction())

        val result = testStateListener.awaitCompletionAndResult(500000)

        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun testApplyForJob() {
        val project = MiddlewareTestProjectHelper().addAndPostNewProjectAndJob()
        val job : Job = project.linkedJobs.first()

        loggedInState(store)

        val testStateListener = SequencedTestStateListener(
                store.state.getState(EmployeeJobsModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "No Jobs Available",

                        testCase = { newState -> newState.jobs.map { it.id }.contains(job.id)}
                ),

                StateTestCaseBuilder.buildTestCase(
                        error = "Job Was Not Applied For",

                        testCase = { newState ->
                            val addedJob = newState.linkedJobs.find { it.jobId == job.id }!!

                            newState.linkedJobs.map { it.jobId }.contains(job.id)
                                    && addedJob.isMyApplication!!
                                    && addedJob.employeeJobState == EmployeeJobStateEnum.APPLIED
                        }
                )
        )

        store.addListener(EmployeeJobsModel::class.java, testStateListener)

        testStateListener.stateStream()
                .delay(750, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it.first) {
                        0 -> {
                            val jobToApplyFor = store.state.getState(EmployeeJobsModel::class.java)!!.jobs.find { it.id == job.id }!!
                            store.dispatch(ApplyForJobAction(jobToApplyFor))
                        }
                    }
                }

        store.dispatch(RefreshEmployeeJobsAction())

        val result = testStateListener.awaitCompletionAndResult()
        Assert.assertTrue(result.error, result.isSuccess)

    }

}
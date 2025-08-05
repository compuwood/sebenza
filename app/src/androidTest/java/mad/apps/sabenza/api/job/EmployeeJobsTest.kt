package mad.apps.sabenza.api.job

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.api.util.EmployeeUtil
import mad.apps.sabenza.api.util.JobUtil
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.api.EqualsQueryString
import mad.apps.sabenza.data.api.JobsAPI
import mad.apps.sabenza.data.api.SkillsAPI
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.JobEmployee
import mad.apps.sabenza.data.rpc.calls.employee.Skill
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EmployeeJobsTest : BaseDaggerTestCase() {

    @Inject lateinit var jobsAPI : JobsAPI
    @Inject lateinit var employeeAPI : EmployeeAPI
    @Inject lateinit var skillsAPI : SkillsAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testListAllJobs() {
        val testSubscriber = TestObserver<List<Job>>()

        val me = successfullyLogin()

        jobsAPI.listJobs()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

    @Test
    fun failAddingJobAsAEmployee() {
        val testSubscriber = TestObserver<Any>()

        val job = Job()

        jobsAPI.addJob(job)
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNotComplete()
    }

    @Test
    fun listAllJobsForSkill() {
        val testSubscriber = TestObserver<List<Job>>()
        val pair = JobUtil(false, component).createRandomJob()

        successfullyLogin()
        val employee = EmployeeUtil(component).getCurrentEmployee()

        skillsAPI.addSkillToEmployee(employeeId = employee.id.toString(), skillId = pair.second.id.toString(), yearsExperience = "3")
                .flatMap { jobsAPI.listJobsForEmployeeSkill(EqualsQueryString(pair.second.id.toString())) }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    @Test
    fun testApplyForJobAsEmployee() {
        val me = successfullyRandomSignUp()
        val employee = addRandomEmployeeToUser(me, employeeAPI)

        val pair : Pair<Job, Skill> = JobUtil(component).createRandomJob()

        var testSubscriber = TestObserver<JobEmployee>()

        employeeAPI.fetchEmployeeForCurrentUser()
                .flatMap { skillsAPI.addSkillToEmployee(
                        employeeId = it.id.toString()!!,
                        skillId = pair.second.id.toString(),
                        yearsExperience = "5")
                }
                .flatMap {
                    jobsAPI.applyForJobAsEmployee(
                            JobEmployee(
                                    jobId = pair.first.id!!,
                                    skillId = it.skillId!!,
                                    employeeId = it.employeeId!!.toString())
                    )
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

    @Test
    fun getJobsForEmployee() {
        val me = successfullyRandomSignUp()
        val employee = addRandomEmployeeToUser(me, employeeAPI)

        val pair : Pair<Job, Skill> = JobUtil(component).createRandomJob()

        val job = employeeAPI.fetchEmployeeForCurrentUser()
                .flatMap { skillsAPI.addSkillToEmployee(
                        employeeId = it.id.toString()!!,
                        skillId = pair.second.id.toString(),
                        yearsExperience = "5")
                }
                .flatMap {
                    jobsAPI.applyForJobAsEmployee(
                            JobEmployee(
                                    jobId = pair.first.id!!,
                                    skillId = it.skillId!!,
                                    employeeId = it.employeeId!!.toString())
                    )
                }
                .compose(NetworkTransformer())
                .blockingGet()

        var testSubscriber = TestObserver<List<JobEmployee>>()

        jobsAPI.getEmployeeJobs()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        testSubscriber.values().first().size == 1
        testSubscriber.values().first().map { it.jobId }.contains(job.jobId)
    }
}
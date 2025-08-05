package mad.apps.sabenza.api.job

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import io.reactivex.rxkotlin.zipWith
import mad.apps.sabenza.BaseDaggerEmployerTestCase
import mad.apps.sabenza.api.util.JobUtil
import mad.apps.sabenza.data.api.*
import mad.apps.sabenza.data.model.jobs.*
import mad.apps.sabenza.data.rpc.calls.employee.Skill
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class EmployerJobsTest : BaseDaggerEmployerTestCase() {

    @Inject lateinit var jobsAPI: JobsAPI
    @Inject lateinit var addressAPI: AddressAPI
    @Inject lateinit var paymentAPI: PaymentAPI
    @Inject lateinit var skillsAPI: SkillsAPI
    @Inject lateinit var employerAPI : EmployerAPI
    @Inject lateinit var employeeAPI : EmployeeAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testAddNewJob() {

    }

    @Test
    fun listAllJobs() {
        successfullyLogin()

        val testSubscriber = TestObserver<List<Job>>()

        jobsAPI.listJobs()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    @Test
    fun listAllProjectsForEmployer() {
        val me = successfullyLogin()

        val testSubscriber = TestObserver<List<Project>>()

        employerAPI.fetchEmployer()
                .flatMap {
                    jobsAPI.listProjectsForEmployer(it.id!!.toEqualQueryString())
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().isNotEmpty())
    }

    @Test
    fun listAllJobsAppliedFor() {
        val testSubscriber = TestObserver<List<JobEmployee>>()

        val me = successfullyLogin()

        jobsAPI.getEmployeeJobs()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    @Test
    fun addJobAllowEmployeeToApplyAndChangeStatusToFilled() {
        val pair : Pair<Job, Skill> = JobUtil(false, component).createRandomJob()

        successfullyLoginAsEmployee()

        var testSubscriber = TestObserver<JobEmployee>()
        var jobId = ""

        employeeAPI.fetchEmployeeForCurrentUser()
                .flatMap { skillsAPI.addSkillToEmployee(
                        employeeId = it.id.toString()!!,
                        skillId = pair.second.id.toString(),
                        yearsExperience = "5")
                }
                .flatMap {
                    jobId = pair.first.id!!
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

        successfullyLogin()

        val secondSubscriber = TestObserver<JobEmployee>()

        jobsAPI.getEmployeeJobs()
                .map { it.find { it.jobId == jobId }!! }
                .flatMap {
                    jobsAPI.changeStatusOfEmployeeJob(
                            it.jobId.toEqualQueryString(),
                            EmployeeJobStateEnum.CONFIRMED, "Job Confirmed")
                }
                .compose(NetworkTransformer())
                .subscribe(secondSubscriber)

        secondSubscriber.awaitTerminalEvent()
        secondSubscriber.assertComplete()

    }

    @Test
    fun testFetchAllProjects() {
        successfullyLogin()

        val testSubscriber = TestObserver<List<Project>>()

        jobsAPI.listProjects()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    @Test
    fun testAddProject() {
        successfullyLogin()

        val testSubscriber = TestObserver<Any>()

        paymentAPI.listAllEmployerCards()
                .zipWith(addressAPI.listAddresses())
                .flatMap {
                    jobsAPI.addProject(
                            Project(
                                    creditCardId = it.first.first().credit_card_id,
                                    description = "ExistingProject"))
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }


    @Test
    fun testAddProjectWithAddress() {
        successfullyLogin()

        val testSubscriber = TestObserver<Any>()

        paymentAPI.listAllEmployerCards()
                .zipWith(addressAPI.listAddresses())
                .flatMap {
                    jobsAPI.addProject(
                            Project(
                                    primaryAddressId = it.second.first().id,
                                    creditCardId = it.first.first().credit_card_id,
                                    description = "ExistingProject"))
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

    @Test
    fun addJobToProject() {
        successfullyLogin()

        val testSubscriber = TestObserver<Job>()

        jobsAPI.listProjects()
                .zipWith(skillsAPI.fetchAllSkills())
                .flatMap {
                    jobsAPI.addJob(Job(
                            projectId = it.first.first().id,
                            creditCardId = it.first.first().creditCardId,
                            skillId = it.second.first().id.toString(),
                            description = "A Job Making Things",
                            quantity = "2",
                            duration = "2 hours",
                            startDate = "2018-08-04 8:00:00",
                            endDate = "2018-08-05 8:00:00"))
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

    @Test
    fun getAllJobsForProject() {
        successfullyLogin()

        val testSubscriber = TestObserver<List<Job>>()

        jobsAPI.listProjects()
                .flatMap {
                    jobsAPI.listJobsForProject(it.first().id!!.toEqualQueryString())
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().isNotEmpty())
    }

    @Test
    fun getAllJobsForProjectEfficiently() {
        successfullyLogin()

        val testSubscriber = TestObserver<List<Job>>()

        jobsAPI.listProjects()
                .flatMap {
                    jobsAPI.listJobsForProjects(it.map { it.id }.toEqualQueryList())
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().isNotEmpty())
    }

    @Test
    fun postProject() {
        successfullyLogin()

        val testSubscriber = TestObserver<Project>()

        jobsAPI.listProjects()
                .flatMap { jobsAPI.changeProjectState(it.first().id!!.toEqualQueryString(), ProjectStateEnum.POSTED) }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

    }
}
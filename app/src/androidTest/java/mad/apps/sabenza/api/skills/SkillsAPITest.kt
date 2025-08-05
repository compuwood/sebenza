package mad.apps.sabenza.api.skills

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import io.reactivex.rxkotlin.zipWith
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.api.SkillsAPI
import mad.apps.sabenza.data.model.employee.EmployeeSkill
import mad.apps.sabenza.data.rpc.calls.employee.Skill
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SkillsAPITest : BaseDaggerTestCase() {

    @Inject lateinit var skillsAPI : SkillsAPI

    @Inject lateinit var employeeAPI : EmployeeAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun getAllSkills() {
        val testSubscriber = TestObserver<List<Skill>>()

        val me = successfullyLogin()

        skillsAPI.fetchAllSkills()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
        Assert.assertTrue(testSubscriber.values().first().size > 0)
    }

    @Test
    fun testAddNewSkill() {
        val testSubscriber = TestObserver<Skill>()

        val me = successfullyLogin()

        val description = "New Skill " + TestStringHelper.nextPrintableString(4)

        skillsAPI.addSkill(description)
                .compose(NetworkTransformer<Skill>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

    @Test
    fun testAddExistingSkill() {
        val testSubscriber = TestObserver<Skill>()

        val me = successfullyLogin()

        val description = "Woodworking"

        skillsAPI.addSkill(description)
                .compose(NetworkTransformer<Skill>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNotComplete()
        Assert.assertTrue(testSubscriber.errorCount() == 1)
    }

    @Test
    fun testSuccessfullyAddSkillToEmployee() {
        val me = successfullyRandomSignUp()
        val employee = addRandomEmployeeToUser(me, employeeAPI)

        val testSubscriber = TestObserver<EmployeeSkill>()

        skillsAPI.fetchAllSkills()
                .zipWith(employeeAPI.fetchEmployeeForCurrentUser())
                .flatMap { skillsAPI.addSkillToEmployee(
                        it.second.id.toString(),
                        it.first.last().id.toString(),
                        5.toString()) }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertComplete()
    }

    @Test
    fun testAddExistingSkillToEmployee() {
        val me = successfullyLogin()

        val testSubscriber = TestObserver<EmployeeSkill>()

        skillsAPI.fetchAllSkills()
                .zipWith(employeeAPI.fetchEmployeeForCurrentUser())
                .flatMap { skillsAPI.addSkillToEmployee(
                        it.second.id.toString(),
                        it.first.last().id.toString(),
                        5.toString()) }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertNotComplete()
        Assert.assertTrue(testSubscriber.errorCount() == 1)
    }

    @Test
    fun getAllSkillsForAnEmployee() {
        val testSubscriber = TestObserver<List<EmployeeSkill>>()

        val me = successfullyLogin()

        skillsAPI.fetchSkillsForEmployee()
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        Assert.assertTrue(testSubscriber.values().first().isNotEmpty())
    }

    @Test
    fun addAndDeleteASkillFromAnEmployee() {
        val addSkillSubscriber = TestObserver<EmployeeSkill>()

        val me = successfullyRandomSignUp()
        val employee = addRandomEmployeeToUser(me, employeeAPI)

        skillsAPI.fetchAllSkills()
                .zipWith(employeeAPI.fetchEmployeeForCurrentUser())
                .flatMap { skillsAPI.addSkillToEmployee(
                        it.second.id.toString(),
                        it.first.last().id.toString(),
                        5.toString()) }
                .compose(NetworkTransformer())
                .subscribe(addSkillSubscriber)

        addSkillSubscriber.awaitTerminalEvent()
        addSkillSubscriber.assertComplete()
        val skill = addSkillSubscriber.values().first()

        val checkSkillHasBeenAddedSubscriber = TestObserver<List<EmployeeSkill>>()

        skillsAPI.fetchSkillsForEmployee()
                .compose(NetworkTransformer())
                .subscribe(checkSkillHasBeenAddedSubscriber)

        checkSkillHasBeenAddedSubscriber.awaitTerminalEvent()
        checkSkillHasBeenAddedSubscriber.assertComplete()
        Assert.assertTrue(checkSkillHasBeenAddedSubscriber.values().first().size == 1)

        val deleteSkillSubscriber = TestObserver<Any>()

        skillsAPI.deleteSkillFromAnEmployee(skillId = "eq."+ skill.skillId.toString())
                .compose(NetworkTransformer())
                .subscribe(deleteSkillSubscriber)


        deleteSkillSubscriber.awaitTerminalEvent()
        deleteSkillSubscriber.assertComplete()


        val checkSkillHasBeenDeletedSubscriber = TestObserver<List<EmployeeSkill>>()

        skillsAPI.fetchSkillsForEmployee()
                .compose(NetworkTransformer())
                .subscribe(checkSkillHasBeenDeletedSubscriber)

        checkSkillHasBeenDeletedSubscriber.awaitTerminalEvent()
        checkSkillHasBeenDeletedSubscriber.assertComplete()

        Assert.assertTrue(!checkSkillHasBeenDeletedSubscriber.values().first().map { it.skillId.toString() }.contains(skill.skillId))
    }


}
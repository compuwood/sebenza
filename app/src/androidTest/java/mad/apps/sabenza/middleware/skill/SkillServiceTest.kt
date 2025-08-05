package mad.apps.sabenza.middleware.skill

import android.support.test.runner.AndroidJUnit4
import mad.apps.sabenza.framework.StateTestCaseBuilder
import mad.apps.sabenza.framework.TestStateListener
import mad.apps.sabenza.middleware.BaseMiddlewareTestCase
import mad.apps.sabenza.state.action.LinkSkillToEmployeeAction
import mad.apps.sabenza.state.action.UnlinkSkillToEmployeeAction
import mad.apps.sabenza.state.models.EmployeeModel
import mad.apps.sabenza.state.models.SkillsModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import zendesk.suas.Store
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SkillServiceTest : BaseMiddlewareTestCase() {

    @Inject lateinit var store: Store

    @Before
    override fun setupDependencies() {
        component.inject(this)
        filterState(SkillsModel::class.java)
    }

    @Test
    fun testUpdateAvailableSkills() {
        val testStateListener = TestStateListener(
                store.state.getState(SkillsModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "FailedToGetUpdatedSkills",
                        testCase = { newState -> newState.availableSkills.isNotEmpty() }
                )
        )

        store.addListener(SkillsModel::class.java, testStateListener)

        store.dispatch(buildLoginAction())

        val result = testStateListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)
    }

    @Test
    fun linkAndUnlinkEmployeeToSkill() {
        loggedInState(store)
        val skill = store.state.getState(SkillsModel::class.java)!!.availableSkills.last()
        val linkSkillListener = TestStateListener(
                store.state.getState(EmployeeModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "New Skill Has Not Been Added To List",

                        testCase = { newState, previousState, initialState ->
                            newState.skills.size == previousState.skills.size + 1
                        }
                )
        )

        store.addListener(EmployeeModel::class.java, linkSkillListener)

        store.dispatch(LinkSkillToEmployeeAction(skill, 5))

        var result = linkSkillListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)

        store.removeListener(linkSkillListener)

        val unlinkSkillListener = TestStateListener(
                store.state.getState(EmployeeModel::class.java),

                StateTestCaseBuilder.buildTestCase(
                        error = "New Skill Has Not Been Unlinked To List",

                        testCase = { newState, previousState, initialState ->
                            newState.skills.size == previousState.skills.size - 1
                        }
                ))

        store.addListener(EmployeeModel::class.java, unlinkSkillListener)

        store.dispatch(UnlinkSkillToEmployeeAction(skill = skill))

        result = unlinkSkillListener.awaitCompletionAndResult(5000)
        Assert.assertTrue(result.error, result.isSuccess)

    }
}
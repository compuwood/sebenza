package mad.apps.sabenza.ui.employee.signup.addskills

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface AddSkillsViewInterface : ViewInterface, BusyViewInterface

interface AddSkillsPresenterInterface : PresenterInterface<AddSkillsViewInterface> {
    fun gotoNextScreen()
    fun saveSkills(skillsList: List<String>)
    fun getCuratedSkillsList() : List<String>
}

object AddSkillsScreen : BaseScreen<AddSkillsViewInterface, AddSkillsPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return AddSkillsViewController()
    }

    override fun providePresenter(store: Store): AddSkillsPresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            AddSkillsStubPresenter(store)
        } else {
            AddSkillsPresenter(store)
        }
    }
}
package mad.apps.sabenza.ui.employer.addproject.configureskill

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface ConfigureSkillViewInterface : ViewInterface, BusyViewInterface {

}

interface ConfigureSkillPresenterInterface : PresenterInterface<ConfigureSkillViewInterface> {
    fun getSelectedSkill(): String
    fun saveSkillConfiguration(description: String, budget: Int, yearsExp: Int)
    fun gotoJobLocationScreen(job: Boolean = false)
    fun updateWithJobViewModel(addJobViewModel: AddJobViewModel)
}

object ConfigureSkillScreen : BaseScreen<ConfigureSkillViewInterface, ConfigureSkillPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return ConfigureSkillViewController()
    }

    override fun providePresenter(store: Store): ConfigureSkillPresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            ConfigureSkillStubPresenter(store)
        } else {
            ConfigureSkillPresenter(store)
        }
    }
}
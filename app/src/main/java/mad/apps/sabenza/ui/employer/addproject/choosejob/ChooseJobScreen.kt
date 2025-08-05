package mad.apps.sabenza.ui.employer.addproject.choosejob

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface ChooseJobViewInterface : ViewInterface, BusyViewInterface {

}

interface ChooseJobPresenterInterface : PresenterInterface<ChooseJobViewInterface> {
    fun gotoConfigureSkillScreen(job: Boolean = false)
    fun saveSelectedSkill(skill: String)
    fun getCuratedSkillsList(): List<String>
}

object ChooseJobScreen : BaseScreen<ChooseJobViewInterface, ChooseJobPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return ChooseJobViewController()
    }

    override fun providePresenter(store: Store): ChooseJobPresenterInterface {
        if (BuildConfig.IS_UI_BUILD) {
            return ChooseJobStubPresenter(store)
        } else {
            return ChooseJobPresenter(store)
        }
    }
}
package mad.apps.sabenza.ui.employer.addjob.addjobconfigureskill

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.employer.addproject.configureskill.ConfigureSkillPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.configureskill.ConfigureSkillStubPresenter
import mad.apps.sabenza.ui.employer.addproject.configureskill.ConfigureSkillViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface AddJobConfigureSkillViewInterface : ViewInterface, BusyViewInterface {

}

object AddJobConfigureSkillScreen : BaseScreen<ConfigureSkillViewInterface, ConfigureSkillPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return AddJobConfigureSkillViewController()
    }

    override fun providePresenter(store: Store): ConfigureSkillPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return ConfigureSkillStubPresenter(store)
//        } else {
//            return ChooseJobPresenter(store)
//        }
    }
}
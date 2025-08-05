package mad.apps.sabenza.ui.landing

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.inbox.InboxScreen
import mad.apps.sabenza.ui.roleselect.RoleSelectScreen
import zendesk.suas.Store

class LandingStubPresenter(store : Store) : BasePresenter<LandingViewInterface>(store), LandingPresenterInterface {

    override fun goToRoleSelectScreen() {
        if (BuildConfig.IS_UI_BUILD) {
            routeTo(InboxScreen.provideViewController())
        } else {
            routeTo(RoleSelectScreen.provideViewController())
        }
    }
}
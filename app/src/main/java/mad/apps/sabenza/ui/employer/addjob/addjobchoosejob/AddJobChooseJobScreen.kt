package mad.apps.sabenza.ui.employer.addjob.addjobchoosejob

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobStubPresenter
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface AddJobChooseJobViewInterface : ViewInterface, BusyViewInterface {

}

object AddJobChooseJobScreen : BaseScreen<ChooseJobViewInterface, ChooseJobPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return AddJobChooseJobViewController()
    }

    override fun providePresenter(store: Store): ChooseJobPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return ChooseJobStubPresenter(store)
//        } else {
//            return ChooseJobPresenter(store)
//        }
    }
}
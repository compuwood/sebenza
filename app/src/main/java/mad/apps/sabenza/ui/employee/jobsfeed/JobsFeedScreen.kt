package mad.apps.sabenza.ui.employee.jobsfeed

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface JobsFeedViewInterface : ViewInterface, BusyViewInterface {

}

interface JobsFeedPresenterInterface : PresenterInterface<JobsFeedViewInterface> {
}

object JobsFeedScreen : BaseScreen<JobsFeedViewInterface, JobsFeedPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return JobsFeedViewController()
    }

    override fun providePresenter(store: Store): JobsFeedPresenterInterface {
        if (BuildConfig.IS_UI_BUILD) {
            return JobsFeedStubPresenter(store)
        } else {
            return JobsFeedPresenter(store)
        }
    }
}
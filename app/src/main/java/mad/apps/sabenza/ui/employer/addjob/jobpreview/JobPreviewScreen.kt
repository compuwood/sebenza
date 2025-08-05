package mad.apps.sabenza.ui.employer.addjob.jobpreview

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.util.BusyViewInterface
import mad.apps.sabenza.ui.util.SuccessErrorViewInterface
import zendesk.suas.Store

interface JobPreviewViewInterface : ViewInterface, BusyViewInterface, SuccessErrorViewInterface {
}

interface JobPreviewPresenterInterface : PresenterInterface<JobPreviewViewInterface> {
    fun getJobObject() : JobPreviewModel
    fun postJob()
    fun goToJobsFeed()
    fun updateWithJobViewModel(fetchViewArgument: AddJobViewModel)
}

object JobPreviewScreen : BaseScreen<JobPreviewViewInterface, JobPreviewPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return JobPreviewViewController()
    }

    override fun providePresenter(store: Store): JobPreviewPresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            JobPreviewStubPresenter(store)
        } else {
            JobPreviewPresenter(store)
        }
    }
}
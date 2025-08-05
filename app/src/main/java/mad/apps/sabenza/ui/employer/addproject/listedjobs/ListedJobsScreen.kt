package mad.apps.sabenza.ui.employer.addproject.listedjobs

import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface ListedJobsViewInterface : ViewInterface, BusyViewInterface {

}

interface ListedJobsPresenterInterface : PresenterInterface<ListedJobsViewInterface> {
    fun getProjectJobsList(): List<JobPreviewModel>
    fun editJob(jobId: Int)
    fun addJob()
    fun goToProjectPreview()
}

object ListedJobsScreen : BaseScreen<ListedJobsViewInterface, ListedJobsPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return ListedJobsViewController()
    }

    override fun providePresenter(store: Store): ListedJobsPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return ListedJobsStubPresenter(store)
//        } else {
//            return ListedJobsPresenter(store)
//        }
    }
}
package mad.apps.sabenza.ui.employee.joblisting

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import mad.apps.sabenza.ui.util.SuccessErrorViewInterface
import zendesk.suas.Store

interface JobListingViewInterface : ViewInterface, BusyViewInterface, SuccessErrorViewInterface {
}

interface JobListingPresenterInterface : PresenterInterface<JobListingViewInterface> {
    fun getJobObject(): JobPreviewModel
    fun getHasAppliedForJob(): Boolean
    fun getHasBeenAcceptedForJob(): Boolean
    fun getQrCodeUrl(): String

    fun messageEmployer()
    fun applyForJob()
    fun withdrawApplication()
    fun updateJobId(jobId: String)
}

object JobListingScreen : BaseScreen<JobListingViewInterface, JobListingPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return JobListingViewController()
    }

    override fun providePresenter(store: Store): JobListingPresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            JobListingStubPresenter(store)
        } else {
            JobListingPresenter(store)
        }
    }
}
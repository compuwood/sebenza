package mad.apps.sabenza.ui.employer.joblisting

import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.viewapplicants.ViewApplicantsScreen
import zendesk.suas.Store

class JobListingPresenter(store: Store) : BasePresenter<JobListingViewInterface>(store), JobListingPresenterInterface {

    lateinit var currentJobId: String

    override fun setJobId(jobId: String) {
        this.currentJobId = jobId;
    }

    override fun loaded() {

    }

    override fun getJobObject(): JobPreviewModel {
        return JobPreviewModel.buildFromEmployeeJobId(currentJobId, store).blockingGet()
    }

    override fun goToViewApplicants() {
        routeTo(ViewApplicantsScreen.provideViewController())
    }

}
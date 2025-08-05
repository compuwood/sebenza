package mad.apps.sabenza.ui.employer.addproject.projectpreview

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface ProjectPreviewViewInterface : ViewInterface, BusyViewInterface {
}

interface ProjectPreviewPresenterInterface : PresenterInterface<ProjectPreviewViewInterface> {
    fun getProjectName() : String
    fun getProjectDescription() : String
    fun getProjectAddress(): Address

    fun getProjectJobsList(): List<JobPreviewModel>
    fun editProject()
    fun postProject()
}

object ProjectPreviewScreen : BaseScreen<ProjectPreviewViewInterface, ProjectPreviewPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return ProjectPreviewViewController()
    }

    override fun providePresenter(store: Store): ProjectPreviewPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return ProjectPreviewStubPresenter(store)
//        } else {
//            return ProjectPreviewPresenter(store)
//        }
    }
}
package mad.apps.sabenza.ui.employer.addproject.projectdetails

import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface ProjectDetailsViewInterface : ViewInterface, BusyViewInterface {

}

interface ProjectDetailsPresenterInterface : PresenterInterface<ProjectDetailsViewInterface> {
    fun saveNameAndDescription(name: String, description: String)
    fun goToAddJobScreen()
    fun goToChooseJobScreen()
}

object ProjectDetailsScreen : BaseScreen<ProjectDetailsViewInterface, ProjectDetailsPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return ProjectDetailsViewController()
    }

    override fun providePresenter(store: Store): ProjectDetailsPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return ProjectDetailsStubPresenter(store)
//        } else {
//            return ProjectDetailsPresenter(store)
//        }
    }
}
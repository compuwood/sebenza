package mad.apps.sabenza.ui.employer.addproject.projectdetails

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addproject.addjobs.AddJobsScreen
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobScreen
import zendesk.suas.Store

class ProjectDetailsStubPresenter(store : Store) : BasePresenter<ProjectDetailsViewInterface>(store), ProjectDetailsPresenterInterface {
    override fun saveNameAndDescription(name: String, description: String) {

    }

    override fun goToAddJobScreen() {
        routeTo(AddJobsScreen.provideViewController())
    }

    override fun goToChooseJobScreen() {
        routeTo(ChooseJobScreen.provideViewController())
    }



}
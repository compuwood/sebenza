package mad.apps.sabenza.ui.employer.StartWork.startwork

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addjob.addjobchoosejob.AddJobChooseJobScreen
import mad.apps.sabenza.ui.employer.addproject.projectdetails.ProjectDetailsScreen
import mad.apps.sabenza.ui.employer.addproject.startwork.StartWorkPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.startwork.StartWorkViewInterface
import zendesk.suas.Store

class StartWorkStubPresenter(store : Store) : BasePresenter<StartWorkViewInterface>(store), StartWorkPresenterInterface {
    override fun startProject() {
        routeTo(ProjectDetailsScreen.provideViewController())
    }

    override fun startJob() {
        routeTo(AddJobChooseJobScreen.provideViewController())
    }
}
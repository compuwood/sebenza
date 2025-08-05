package mad.apps.sabenza.ui.employer.viewprojects

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addproject.startwork.StartWorkScreen
import zendesk.suas.Store

class ViewProjectsStubPresenter(store : Store) : BasePresenter<ViewProjectsViewInterface>(store), ViewProjectsPresenterInterface {
    override fun addProject() {
        routeTo(StartWorkScreen.provideViewController())
    }

}
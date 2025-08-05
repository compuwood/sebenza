package mad.apps.sabenza.ui.employer.addproject.addjobs

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobScreen
import zendesk.suas.Store


class AddJobsStubPresenter(store : Store) : BasePresenter<AddJobsViewInterface>(store), AddJobsPresenterInterface {
    override fun startAddJob() {
        routeTo(ChooseJobScreen.provideViewController())
    }


}
package mad.apps.sabenza.ui.employer.addproject.configureskill

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.employer.addjob.addjoblocation.AddJobLocationScreen
import mad.apps.sabenza.ui.employer.addproject.joblocation.JobLocationScreen
import zendesk.suas.Store

class ConfigureSkillStubPresenter(store : Store) : BasePresenter<ConfigureSkillViewInterface>(store), ConfigureSkillPresenterInterface {
    override fun updateWithJobViewModel(addJobViewModel: AddJobViewModel) {

    }

    override fun getSelectedSkill(): String {
        return "Painter"
    }

    override fun saveSkillConfiguration(Description: String, budget: Int, yearsExp: Int) {
    }

    override fun gotoJobLocationScreen(job: Boolean) {
        if (job) {
            routeTo(AddJobLocationScreen.provideViewController())
        } else {
            routeTo(JobLocationScreen.provideViewController())
        }
    }


}
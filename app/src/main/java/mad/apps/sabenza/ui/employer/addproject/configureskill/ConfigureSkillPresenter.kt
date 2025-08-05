package mad.apps.sabenza.ui.employer.addproject.configureskill

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.employer.addjob.addjoblocation.AddJobLocationScreen
import mad.apps.sabenza.ui.employer.addproject.joblocation.JobLocationScreen
import zendesk.suas.Store

class ConfigureSkillPresenter(store : Store) : BasePresenter<ConfigureSkillViewInterface>(store), ConfigureSkillPresenterInterface {

    lateinit var addJobViewModel : AddJobViewModel

    override fun updateWithJobViewModel(addJobViewModel: AddJobViewModel) {
        this.addJobViewModel = addJobViewModel
    }

    override fun getSelectedSkill(): String {
        return addJobViewModel.skill?.description ?: "N/A"
    }

    override fun saveSkillConfiguration(description: String, budget: Int, yearsExp: Int) {
        addJobViewModel.description = description
        addJobViewModel.budget = budget
        addJobViewModel.yearsExp = yearsExp
    }

    override fun gotoJobLocationScreen(job: Boolean) {
        if (job) {
            routeTo(AddJobLocationScreen.provideViewController().withViewArgs(Pair(AddJobViewModel.argName(), addJobViewModel)))
        } else {
            routeTo(JobLocationScreen.provideViewController())
        }
    }


}
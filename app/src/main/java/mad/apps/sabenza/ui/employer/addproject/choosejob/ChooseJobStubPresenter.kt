package mad.apps.sabenza.ui.employer.addproject.choosejob

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addjob.addjobconfigureskill.AddJobConfigureSkillScreen
import mad.apps.sabenza.ui.employer.addproject.configureskill.ConfigureSkillScreen
import zendesk.suas.Store

class ChooseJobStubPresenter(store : Store) : BasePresenter<ChooseJobViewInterface>(store), ChooseJobPresenterInterface {

    override fun gotoConfigureSkillScreen(job: Boolean) {
        if (job) {
            routeTo(AddJobConfigureSkillScreen.provideViewController())
        } else {
            routeTo(ConfigureSkillScreen.provideViewController())
        }
    }

    override fun saveSelectedSkill(skill: String) {

    }

    override fun getCuratedSkillsList(): List<String> {
        return listOf("Painter", "Panel Beater", "Pattern Maker", "Parkinsons Doctor", "Party Planner", "Park Builder", "Assassin", "Cupcake Tester", "Babysitter", "Dog Walker", "German Lullaby Singer for Adults")
    }


}
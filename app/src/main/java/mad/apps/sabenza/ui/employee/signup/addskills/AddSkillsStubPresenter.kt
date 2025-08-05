package mad.apps.sabenza.ui.employee.signup.addskills

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employee.signup.paymentinfo.PaymentInfoScreen
import zendesk.suas.Store


class AddSkillsStubPresenter(store : Store) : BasePresenter<AddSkillsViewInterface>(store), AddSkillsPresenterInterface {
    override fun gotoNextScreen() {
        routeToNewTop(PaymentInfoScreen.provideViewController())
    }

    override fun saveSkills(skillsList: List<String>) {
    }

    override fun getCuratedSkillsList(): List<String> {
        return listOf("Painter", "Panel Beater", "Pattern Maker", "Parkinsons Doctor", "Party Planner", "Park Builder", "Assassin", "Cupcake Tester", "Babysitter", "Dog Walker", "German Lullaby Singer for Adults")
    }

}
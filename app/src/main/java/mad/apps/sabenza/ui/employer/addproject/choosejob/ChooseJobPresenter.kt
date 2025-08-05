package mad.apps.sabenza.ui.employer.addproject.choosejob

import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.models.SkillsModel
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.employer.addjob.addjobconfigureskill.AddJobConfigureSkillScreen
import mad.apps.sabenza.ui.employer.addproject.configureskill.ConfigureSkillScreen
import mad.apps.sabenza.ui.util.MissingFeature
import zendesk.suas.Store

class ChooseJobPresenter(store: Store) : BasePresenter<ChooseJobViewInterface>(store), ChooseJobPresenterInterface {

    private lateinit var addJobViewModel : AddJobViewModel

    private var skillName : String = ""

    override fun loaded() {
        super.loaded()
        addJobViewModel = AddJobViewModel()
    }

    override fun gotoConfigureSkillScreen(job: Boolean) {
        if (job) {
            routeTo(AddJobConfigureSkillScreen.provideViewController().withViewArgs(Pair(AddJobViewModel.argName() , addJobViewModel)))
        } else {
            routeTo(ConfigureSkillScreen.provideViewController())
        }
    }

    override fun saveSelectedSkill(skill: String) {
        skillName = skill
        val skill = store.state.getState(SkillsModel::class.java)?.availableSkills?.find { it.description == skill }
        if (skill != null) {
            addJobViewModel.skill = skill
        } else {
            MissingFeature.error(view.getContext(), "What happens if we dont find the skill. Is that possible?")
        }
    }

    override fun getCuratedSkillsList(): List<String> {
        return store.state.getState(SkillsModel::class.java)?.availableSkills?.map { it.description!! } ?: listOf()
    }
}
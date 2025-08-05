package mad.apps.sabenza.ui.employer.addjob.addjobconfigureskill

import android.view.View
import mad.apps.sabenza.ui.employer.addproject.configureskill.ConfigureSkillViewController

class AddJobConfigureSkillViewController : AddJobConfigureSkillViewInterface, ConfigureSkillViewController() {

    override fun initView(view: View) {
        isJob = true
        super.initView(view)
    }
}
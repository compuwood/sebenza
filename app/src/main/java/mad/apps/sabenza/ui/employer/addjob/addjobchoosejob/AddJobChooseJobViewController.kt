package mad.apps.sabenza.ui.employer.addjob.addjobchoosejob

import android.view.View
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobViewController

class AddJobChooseJobViewController : AddJobChooseJobViewInterface, ChooseJobViewController() {

    override fun initView(view: View) {
        isJob = true
        super.initView(view)
    }
}
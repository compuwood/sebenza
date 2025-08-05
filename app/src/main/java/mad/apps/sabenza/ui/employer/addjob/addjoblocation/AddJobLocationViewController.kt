package mad.apps.sabenza.ui.employer.addjob.addjoblocation

import android.view.View
import mad.apps.sabenza.ui.employer.addproject.joblocation.JobLocationViewController

class AddJobLocationViewController : AddJobLocationViewInterface, JobLocationViewController() {

    override fun initView(view: View) {
        isJob = true
        super.initView(view)
    }
}
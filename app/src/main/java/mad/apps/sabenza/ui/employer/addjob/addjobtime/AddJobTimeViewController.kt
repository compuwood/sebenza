package mad.apps.sabenza.ui.employer.addjob.addjobtime

import android.view.View
import mad.apps.sabenza.ui.employer.addproject.jobtime.JobTimeViewController

class AddJobTimeViewController : AddJobTimeViewInterface, JobTimeViewController() {

    override fun initView(view: View) {
        isJob = true
        super.initView(view)
    }
}
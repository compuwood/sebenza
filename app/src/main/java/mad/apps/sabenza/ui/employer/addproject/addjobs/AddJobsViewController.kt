package mad.apps.sabenza.ui.employer.addproject.addjobs

import android.view.View
import android.widget.ImageView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import javax.inject.Inject

class AddJobsViewController : BaseViewController(), AddJobsViewInterface {

    override fun layout(): Int = R.layout.employer_add_jobs_layout
    @Inject lateinit var presenter: AddJobsPresenterInterface

    val addJobButton by bindView<ImageView>(R.id.add_job_button)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)
        addJobButton.setOnClickListener { presenter.startAddJob() }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.takeView(this)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.dropView()
    }

    override fun busy() {
    }

    override fun idle() {
    }


}
package mad.apps.sabenza.ui.employer.addproject.startwork

import android.view.View
import android.widget.FrameLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import javax.inject.Inject

class StartWorkViewController : BaseViewController(), StartWorkViewInterface {

    override fun layout(): Int = R.layout.employer_add_project

    @Inject lateinit var presenter: StartWorkPresenterInterface

    val startProjectButton by bindView<FrameLayout>(R.id.start_project_button)
    val startJobButton by bindView<FrameLayout>(R.id.start_job_button)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)
        startProjectButton.setOnClickListener { presenter.startProject() }
        startJobButton.setOnClickListener { presenter.startJob() }
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
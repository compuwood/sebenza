package mad.apps.sabenza.ui.employer.addproject.jobconfirm

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.widget.ErrorModal
import mad.apps.sabenza.ui.widget.LoadingScreen
import mad.apps.sabenza.ui.widget.headerviewbase.HeaderViewBase
import javax.inject.Inject

class JobConfirmViewController : BaseViewController(), JobConfirmViewInterface {

    override fun layout(): Int = R.layout.employer_job_confirm

    @Inject lateinit var presenter: JobConfirmPresenterInterface

    val header by bindView<HeaderViewBase>(R.id.employer_header)
    val jobNameTextView by bindView<TextView>(R.id.job_name_textview)
    val jobDescriptionTextView by bindView<TextView>(R.id.job_description_textview)
    val yearsExpTextView by bindView<TextView>(R.id.years_exp_textview)
    val budgetTextView by bindView<TextView>(R.id.budget_textview)
    val fromDateTextView by bindView<TextView>(R.id.from_date_textview)
    val toDateTextView by bindView<TextView>(R.id.to_date_textview)
    val confirmButton by bindView<FrameLayout>(R.id.confirm_button)
    val errorModal by bindView<ErrorModal>(R.id.error_modal)
    val loadingScreen by bindView<LoadingScreen>(R.id.loading_screen)


    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)
        val selectedJob = presenter.getSelectedJob()

        header.setTitle(selectedJob.title)
        jobNameTextView.text = selectedJob.title
        jobDescriptionTextView.text = selectedJob.description
        yearsExpTextView.text = selectedJob.getExperienceYears()
        budgetTextView.text = "$" + selectedJob.getBudget()
        fromDateTextView.text = selectedJob.getStartDateAsString()
        toDateTextView.text = selectedJob.getEndDateAsString()

        confirmButton.setOnClickListener {
            presenter.confirmJob()
        }
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
        loadingScreen.show()
    }

    override fun idle() {
        loadingScreen.hide()
    }

    override fun success() {
        presenter.gotoListedJobScreen()
        idle()
    }

    override fun error(error: String) {
        idle()
        errorModal.show(error)
    }

}
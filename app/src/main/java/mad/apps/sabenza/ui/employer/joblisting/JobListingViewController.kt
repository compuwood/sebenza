package mad.apps.sabenza.ui.employer.joblisting

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.widget.ErrorModal
import mad.apps.sabenza.ui.widget.LoadingScreen
import javax.inject.Inject

class JobListingViewController : BaseViewController(), JobListingViewInterface {

    override fun layout(): Int = R.layout.employer_job_listing

    @Inject lateinit var presenter: JobListingPresenterInterface

    val jobNameTextView by bindView<TextView>(R.id.job_name_textview)
    val jobDescriptionTextView by bindView<TextView>(R.id.job_description_textview)
    val jobAddressTextView by bindView<TextView>(R.id.job_address_textview)
    val fromDateTextView by bindView<TextView>(R.id.from_date_textview)
    val toDateTextView by bindView<TextView>(R.id.to_date_textview)
    val expRequiredTextView by bindView<TextView>(R.id.experience_textview)
    val budgetTextView by bindView<TextView>(R.id.budget_textview)
    val viewApplicantsButton by bindView<FrameLayout>(R.id.view_applicants_button)
    val errorModal by bindView<ErrorModal>(R.id.error_modal)
    val loadingScreen by bindView<LoadingScreen>(R.id.loading_screen)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        presenter.setJobId(fetchViewArgument<String>("jobid"))

        val job = presenter.getJobObject()

        jobNameTextView.text = job.title
        jobDescriptionTextView.text = job.description
        jobAddressTextView.text = job.getAddressAsString()
        fromDateTextView.text = job.getStartDateAsString()
        toDateTextView.text = job.getEndDateAsString()
        expRequiredTextView.text = job.experience.toString()
        budgetTextView.text = "$" + job.budget.toString()
        viewApplicantsButton.setOnClickListener { presenter.goToViewApplicants() }
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
        idle()
        presenter.goToViewApplicants()
    }

    override fun error(error: String) {
        idle()
        errorModal.show(error)
    }
}
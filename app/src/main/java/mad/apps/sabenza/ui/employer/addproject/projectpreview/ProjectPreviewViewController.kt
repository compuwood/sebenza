package mad.apps.sabenza.ui.employer.addproject.projectpreview

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.employer.addproject.listedjobs.JobsListRecyclerAdapter
import javax.inject.Inject

class ProjectPreviewViewController : BaseViewController(), ProjectPreviewViewInterface {

    override fun layout(): Int = R.layout.employer_project_preview

    @Inject lateinit var presenter: ProjectPreviewPresenterInterface

    val jobListRecycler by bindView<RecyclerView>(R.id.job_list_recycler)
    val projectNameTextView by bindView<TextView>(R.id.project_name_textview)
    val projectDescriptionTextView by bindView<TextView>(R.id.project_description_textview)
    val projectAddressTextView by bindView<TextView>(R.id.project_address_textview)
    val postButton by bindView<FrameLayout>(R.id.post_button)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)
        setupRecycler()

        val projectAddress = presenter.getProjectAddress()
        val addressString = projectAddress.line1 + ", " + projectAddress.line2 + ", " + projectAddress.cityTown + ", " + projectAddress.postcode
        projectNameTextView.text = presenter.getProjectName()
        projectDescriptionTextView.text = presenter.getProjectDescription()
        projectAddressTextView.text = addressString

        postButton.setOnClickListener { presenter.postProject() }
    }

    private fun setupRecycler() {
        val jobListRecyclerAdapter = JobsListRecyclerAdapter(presenter.getProjectJobsList(), editable = false)
        jobListRecycler.layoutManager = LinearLayoutManager(view!!.context, LinearLayout.VERTICAL, false)
        jobListRecycler.adapter = jobListRecyclerAdapter
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
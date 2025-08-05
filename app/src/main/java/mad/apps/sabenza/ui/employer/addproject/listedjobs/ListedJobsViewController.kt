package mad.apps.sabenza.ui.employer.addproject.listedjobs

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import javax.inject.Inject

class ListedJobsViewController : BaseViewController(), ListedJobsViewInterface {

    override fun layout(): Int = R.layout.employer_listed_jobs

    @Inject lateinit var presenter: ListedJobsPresenterInterface

    val jobListRecycler by bindView<RecyclerView>(R.id.job_list_recycler)
    val addJobButton by bindView<ImageView>(R.id.add_job_button)
    val nextButton by bindView<FrameLayout>(R.id.next_button)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)
        addJobButton.setOnClickListener { presenter.addJob() }
        nextButton.setOnClickListener { presenter.goToProjectPreview() }
        setupRecycler()
    }

    private fun setupRecycler() {
        val jobListRecyclerAdapter = JobsListRecyclerAdapter(presenter.getProjectJobsList())
        jobListRecyclerAdapter.setItemClickListener(object : JobsListRecyclerAdapter.ItemClickListener {
            override fun clickItem(index: Int, jobId: Int) {
                presenter.editJob(jobId)
            }
        })
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
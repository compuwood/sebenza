package mad.apps.sabenza.ui.widget.tablayout

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.jobs.ProjectPreviewModel
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.ui.employee.signup.addskills.ProjectsJobsRecyclerViewAdapter
import mad.apps.sabenza.ui.util.TabName
import mad.apps.sabenza.ui.widget.tablayout.presenter.ProjectsJobsPresenterInterface
import javax.inject.Inject

class ProjectsJobsContainerView(context: Context?, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    @Inject lateinit var presenter: ProjectsJobsPresenterInterface

    val isEmployer : Boolean

    init {
        DaggerService.getDaggerComponent(context).inject(this)
        this.layoutManager = LinearLayoutManager(context, VERTICAL, false)

        val attributes = context!!.obtainStyledAttributes(attrs, R.styleable.ProjectsJobsContainerView, 0, 0)
        isEmployer = attributes.getBoolean(R.styleable.ProjectsJobsContainerView_isEmployerViewing,false)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.takeView(this)
    }

    override fun onDetachedFromWindow() {
        presenter.dropView()
        super.onDetachedFromWindow()
    }

    private lateinit var tabType: TabName

    fun setTabType(tabType: TabName) {
        this.tabType = tabType

        val unsortedList: List<ProjectPreviewModel> = presenter.getProjects()
        val sortedList: MutableList<ProjectPreviewModel> = mutableListOf()

        when (tabType) {
            TabName.FILLED -> unsortedList.forEach {
                if (it.isFilled) {
                    sortedList.add(it)
                }
            }
            TabName.UNFILLED -> unsortedList.forEach {
                if (!it.isFilled) {
                    sortedList.add(it)
                }
            }
            TabName.FORYOU -> unsortedList.forEach {
                if (it.isForYou) {
                    sortedList.add(it)
                }
            }
            TabName.APPLIED -> unsortedList.forEach {
                if (it.isAppliedFor) {
                    sortedList.add(it)
                }
            }
            TabName.CONFIRMED -> unsortedList.forEach {
                if (it.isConfirmed) {
                    sortedList.add(it)
                }
            }
        }

        val projectJobsRecyclerViewAdapter = ProjectsJobsRecyclerViewAdapter(sortedList)
        projectJobsRecyclerViewAdapter.setItemClickListener(object : ProjectsJobsRecyclerViewAdapter.ItemClickListener {
            override fun clickItem(index: Int, jobId: String) {
                presenter.gotoSelectedJob(jobId, isEmployer = isEmployer)
            }
        })
        this.adapter = projectJobsRecyclerViewAdapter
    }

    fun projectsUpdated(unsortedList: List<ProjectPreviewModel>) {
        val sortedList: MutableList<ProjectPreviewModel> = mutableListOf()

        when (tabType) {
            TabName.FILLED -> unsortedList.forEach {
                if (it.isFilled) {
                    sortedList.add(it)
                }
            }
            TabName.UNFILLED -> unsortedList.forEach {
                if (!it.isFilled) {
                    sortedList.add(it)
                }
            }
            TabName.FORYOU -> unsortedList.forEach {
                if (it.isForYou) {
                    sortedList.add(it)
                }
            }
            TabName.APPLIED -> unsortedList.forEach {
                if (it.isAppliedFor) {
                    sortedList.add(it)
                }
            }
            TabName.CONFIRMED -> unsortedList.forEach {
                if (it.isConfirmed) {
                    sortedList.add(it)
                }
            }
        }
        (this.adapter as ProjectsJobsRecyclerViewAdapter).update(sortedList)
    }

}
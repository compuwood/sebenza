package mad.apps.sabenza.ui.employee.signup.addskills

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.jobs.ProjectPreviewModel
import mad.apps.sabenza.framework.ui.bindView

class ProjectsJobsRecyclerViewAdapter(private val dataSet: MutableList<ProjectPreviewModel>) : RecyclerView.Adapter<ProjectsJobsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsJobsRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.projects_jobs_line_item_layout, parent, false) as RelativeLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.jobId = dataSet[position].jobId
        holder.setTitle(dataSet[position].title, dataSet[position].isProject)
        holder.setDescription(dataSet[position].description)
        holder.setDate(dataSet[position].getStartDate(),dataSet[position].getEndDate())
        holder.setBudget(dataSet[position].getBudget())

        if (position == 0){
            holder.setupFirstItem()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val topDivider by bindView<FrameLayout>(R.id.top_divider)
        val projectTitle by bindView<TextView>(R.id.project_name_textview)
        val projectDescription by bindView<TextView>(R.id.project_description_textview)
        val projectDateTextView by bindView<TextView>(R.id.project_date_range_textview)
        val projectBudgetTextView by bindView<TextView>(R.id.project_budget_textview)

        var jobId = ""

        init {
            itemView.setOnClickListener { itemClickListener.clickItem(adapterPosition,jobId) }
        }

        fun setId(id : String) {
            jobId = id
        }

        fun setTitle(title: String, isProject: Boolean) {
            if (isProject) {
                projectTitle.text = title
            } else
                projectTitle.text = "Job - " + title
        }

        fun setDescription(description: String) {
            projectDescription.text = description
        }

        fun setDate(startDate: String, endDate: String){
            val dateRange = startDate + " - " + endDate
            projectDateTextView.text = dateRange
        }

        fun setBudget(budget: String){
            projectBudgetTextView.text = budget
        }


        fun setupFirstItem() {
            topDivider.visibility = View.GONE
        }
    }


    fun update(unsortedList: List<ProjectPreviewModel>) {
        dataSet.clear()
        dataSet.addAll(unsortedList)
        notifyDataSetChanged()
    }

    //==================On Click Listener================================//
    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun clickItem(index: Int, jobId: String)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}


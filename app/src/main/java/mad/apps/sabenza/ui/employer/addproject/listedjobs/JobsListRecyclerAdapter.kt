package mad.apps.sabenza.ui.employer.addproject.listedjobs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.ui.bindView

class JobsListRecyclerAdapter(private val dataSet: List<JobPreviewModel>, val editable: Boolean = true) : RecyclerView.Adapter<JobsListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsListRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.job_line_item_layout, parent, false) as LinearLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setJob(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val jobNameTextView by bindView<TextView>(R.id.job_name_textview)
        val jobDescriptionTextView by bindView<TextView>(R.id.job_description_textview)
        val yearsExpTextView by bindView<TextView>(R.id.years_exp_textview)
        val budgetTextView by bindView<TextView>(R.id.budget_textview)
        val fromDateTextView by bindView<TextView>(R.id.from_date_textview)
        val toDateTextView by bindView<TextView>(R.id.to_date_textview)
        val editButton by bindView<ImageView>(R.id.edit_button)

        init {
            if (editable) {
                editButton.setOnClickListener { itemClickListener.clickItem(adapterPosition, dataSet[adapterPosition].jobId) }
            } else {
                editButton.visibility = View.GONE
            }
        }

        fun setJob(jobPreviewModel: JobPreviewModel) {
            jobNameTextView.text = jobPreviewModel.title
            jobDescriptionTextView.text = jobPreviewModel.description
            yearsExpTextView.text = jobPreviewModel.getExperienceYears()
            budgetTextView.text = "$" + jobPreviewModel.getBudget()
            fromDateTextView.text = jobPreviewModel.getStartDateAsString()
            toDateTextView.text = jobPreviewModel.getEndDateAsString()
        }


    }

    //==================On Click Listener================================//
    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun clickItem(index: Int, jobId: Int)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}

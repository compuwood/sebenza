package mad.apps.sabenza.ui.employer.viewapplicants

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import io.techery.properratingbar.ProperRatingBar
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.employee.ApplicantPreviewModel
import mad.apps.sabenza.framework.ui.bindView

class ApplicantListRecyclerAdapter(private val dataSet: List<ApplicantPreviewModel>) : RecyclerView.Adapter<ApplicantListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantListRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.applicant_line_item_layout, parent, false) as LinearLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupLineItem(dataSet[position])

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameTextView by bindView<TextView>(R.id.applicant_name_textview)
        val ratingBar by bindView<ProperRatingBar>(R.id.average_review_ratingbar)
        val topSkillsTextView by bindView<TextView>(R.id.top_skills_textview)
        val profilePicture by bindView<CircleImageView>(R.id.profile_preview_image)
        var employeeId = ""

        init {
            itemView.setOnClickListener { itemClickListener.clickItem(adapterPosition, employeeId) }
        }

        fun setupLineItem(applicant: ApplicantPreviewModel) {
            employeeId = applicant.employeeId
            nameTextView.text = applicant.firstName + " " + applicant.lastName
            ratingBar.rating = applicant.ratingAvg

            var topSkillsString = ""
            if (applicant.topSkill1.isNotEmpty()) {
                topSkillsString += applicant.topSkill1
                if (applicant.topSkill2.isNotEmpty()) {
                    topSkillsString += ( " • " +  applicant.topSkill2)
                    if (applicant.topSkill3.isNotEmpty()) {
                        topSkillsString += ( " • " +  applicant.topSkill3)
                    }
                }
            }

            topSkillsTextView.text = topSkillsString
        }

    }

    //==================On Click Listener================================//
    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun clickItem(index: Int, employeeId: String)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}

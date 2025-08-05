package mad.apps.sabenza.ui.employee.signup.addskills

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import mad.apps.sabenza.R
import mad.apps.sabenza.framework.ui.bindView

class AdapterAddSkillsRecycler(private val dataSet: List<String>) : RecyclerView.Adapter<AdapterAddSkillsRecycler.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterAddSkillsRecycler.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.added_skills_line_item_layout, parent, false) as RelativeLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setSkill(dataSet[position])
        if (position == 0){
            holder.setupFirstItem()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val skillsEditText by bindView<TextView>(R.id.skill_name_textview)
        val topDivider by bindView<FrameLayout>(R.id.top_divider)
        val deleteButton by bindView<ImageView>(R.id.delete_item_button)
        val plusButton by bindView<ImageView>(R.id.increase_years_button)
        val minusButton by bindView<ImageView>(R.id.decrease_years_button)
        val yearsTextView by bindView<TextView>(R.id.years_exp_textview)

        var yearsExp = 1

        private lateinit var skill: String

        init {

            yearsTextView.text = yearsExp.toString()

            deleteButton.setOnClickListener {
                itemDeletedListener.delete(adapterPosition)
            }
            plusButton.setOnClickListener {
                yearsExp++
                yearsTextView.text = yearsExp.toString()
            }
            minusButton.setOnClickListener {
                if (yearsExp > 1) {
                    yearsExp--
                    yearsTextView.text = yearsExp.toString()
                }
            }
        }

        fun setSkill(skill: String) {
            this.skill = skill
            skillsEditText.text = skill
        }

        fun setupFirstItem() {
            topDivider.visibility = View.GONE
        }
    }


    //==================Item Delete Listener================================//
    private lateinit var itemDeletedListener: DeletedItemListener

    interface DeletedItemListener {
        fun delete(index: Int)
    }

    fun setDeletedItemListener(deletedItemListener: DeletedItemListener) {
        this.itemDeletedListener = deletedItemListener
    }

}


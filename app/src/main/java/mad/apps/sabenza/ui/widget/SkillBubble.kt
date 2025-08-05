package mad.apps.sabenza.ui.widget

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.framework.ui.bindView

class SkillBubble(context: Context, val skill: String, val experience: Int) : LinearLayout(context) {

    val skillText by bindView<TextView>(R.id.bubble_skill_textview)
    val expText by bindView<TextView>(R.id.bubble_exp_textview)


    init {
        LayoutInflater.from(context).inflate(R.layout.skill_bubble_layout, this, true)
//        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FooterButtonViewBase, 0, 0)

        skillText.text = skill
        expText.text = experience.toString()
    }
}

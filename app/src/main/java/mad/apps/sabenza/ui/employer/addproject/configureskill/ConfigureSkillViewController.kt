package mad.apps.sabenza.ui.employer.addproject.configureskill

import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.util.DecimalDigitsInputFilter
import mad.apps.sabenza.ui.util.checkIfEmpty
import javax.inject.Inject



open class ConfigureSkillViewController : BaseViewController(), ConfigureSkillViewInterface {

    override fun layout(): Int = R.layout.employer_configure_skill

    @Inject lateinit var presenter: ConfigureSkillPresenterInterface

    val skillNameTextView by bindView<TextView>(R.id.skill_name_textview)
    val skillsDescriptionEditText by bindView<EditText>(R.id.skill_description_edittext)
    val budgetEditText by bindView<EditText>(R.id.budget_edittext)

    val nextButton by bindView<FrameLayout>(R.id.next_button)

    val plusButton by bindView<ImageView>(R.id.increase_years_button)
    val minusButton by bindView<ImageView>(R.id.decrease_years_button)
    val yearsTextView by bindView<TextView>(R.id.years_exp_textview)

    var yearsExp = 1

    var isJob = false

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        presenter.updateWithJobViewModel(fetchViewArgument<AddJobViewModel>(AddJobViewModel.argName()))

        //set digit limits of edit text
        budgetEditText.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2))

        //hide hint text on first tap of the edit text
        skillsDescriptionEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                skillsDescriptionEditText.hint = ""
            else
                skillsDescriptionEditText.hint = "Add a brief description of this job to help potential employees better understand your needs."
        }

        skillNameTextView.text = presenter.getSelectedSkill()
        yearsTextView.text = yearsExp.toString()

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

        nextButton.setOnClickListener {
            if (validate()) {
                presenter.saveSkillConfiguration(skillsDescriptionEditText.text.toString(), budgetEditText.text.toString().toInt(), yearsExp)
                presenter.gotoJobLocationScreen(isJob)
            }
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

    fun validate(): Boolean {

        checkIfEmpty(budgetEditText,"Please indicate the budget for this job")

        if (budgetEditText.error.isNullOrEmpty()){
            return true
        }
        return false
    }

    override fun busy() {
    }

    override fun idle() {
    }


}
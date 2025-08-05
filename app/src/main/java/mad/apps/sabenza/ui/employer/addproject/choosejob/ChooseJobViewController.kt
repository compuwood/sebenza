package mad.apps.sabenza.ui.employer.addproject.choosejob

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.hideKeyboard
import mad.apps.sabenza.ui.widget.AutocompleteHandlesBack
import javax.inject.Inject


open class ChooseJobViewController : BaseViewController(), ChooseJobViewInterface {

    override fun layout(): Int = R.layout.employer_choose_job

    @Inject lateinit var presenter: ChooseJobPresenterInterface

    val skillSelectEditText by bindView<AutocompleteHandlesBack>(R.id.skill_select_edittext)
    var isJob = false

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        val curatedList = presenter.getCuratedSkillsList().toMutableList()

        skillSelectEditText.threshold = 1
        skillSelectEditText.setAdapter(ArrayAdapter<String>(view.context, R.layout.autocomplete_line_item, curatedList))
        skillSelectEditText.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as String
            presenter.saveSelectedSkill(selected)
            hideKeyboard(activity!!)
            presenter.gotoConfigureSkillScreen(isJob)
            skillSelectEditText.setText("")
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

    override fun busy() {
    }

    override fun idle() {
    }
}
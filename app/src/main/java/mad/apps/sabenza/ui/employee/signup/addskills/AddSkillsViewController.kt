package mad.apps.sabenza.ui.employee.signup.addskills

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.MissingFeature
import mad.apps.sabenza.ui.util.hideKeyboard
import mad.apps.sabenza.ui.widget.AutocompleteHandlesBack
import javax.inject.Inject


class AddSkillsViewController : BaseViewController(), AddSkillsViewInterface {

    override fun layout(): Int = R.layout.employee_add_skills

    @Inject lateinit var presenter : AddSkillsPresenterInterface

    val skillsEditText by bindView<AutocompleteHandlesBack>(R.id.employee_add_skills_edittext)
    val noSkillsText by bindView<TextView>(R.id.no_skills_textview)
    val skillsListRecycler by bindView<RecyclerView>(R.id.skills_list_recycler)
    val header by bindView<TextView>(R.id.first_item_header)

    val nextButton by bindView<FrameLayout>(R.id.signup_employee_next_button)

    val selectedList: MutableList<String> = mutableListOf()

    override fun initView(view: View) {
        super.initView(view)
        DaggerService.getDaggerComponent(view.context).inject(this)

        val addSkillsRecyclerAdapter = AdapterAddSkillsRecycler(selectedList)
        val curatedList = presenter.getCuratedSkillsList().toMutableList()

        //Autocomplete setup
        skillsEditText.threshold = 1
        updateCuratedList(view, curatedList)
        skillsEditText.onItemClickListener = OnItemClickListener { parent, skillEditTextView, position, _ ->
            val selected = parent.getItemAtPosition(position) as String
            addJobToList(selected)
            curatedList.remove(selected)
            updateCuratedList(skillEditTextView, curatedList)
            hideKeyboard(activity!!)
            skillsEditText.setText("")
        }


        //Skills list recyclerview setup
        addSkillsRecyclerAdapter.setDeletedItemListener(object : AdapterAddSkillsRecycler.DeletedItemListener {
            override fun delete(index: Int) {
                curatedList.add(selectedList[index])
                updateCuratedList(view, curatedList)
                selectedList.removeAt(index)
                refreshAdapter()
            }
        })
        skillsListRecycler.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
        skillsListRecycler.adapter = addSkillsRecyclerAdapter


        //UI listeners
        nextButton.setOnClickListener {
            presenter.saveSkills(selectedList)
            presenter.gotoNextScreen()
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

    fun updateCuratedList(view: View, curatedList: MutableList<String>) {
        skillsEditText.setAdapter(ArrayAdapter<String>(view.context, R.layout.autocomplete_line_item, curatedList))
    }

    fun addJobToList(job :String) {
        selectedList.add(job)
        refreshAdapter()
    }

    fun refreshAdapter() {
        skillsListRecycler.adapter.notifyDataSetChanged()

        if (!selectedList.isEmpty()){
            noSkillsText.visibility = View.GONE
            header.visibility = View.VISIBLE
            skillsEditText.hint = "Add another skill"
        } else {
            noSkillsText.visibility = View.VISIBLE
            header.visibility = View.GONE
            skillsEditText.hint = "Add Skills"
        }
    }

    override fun busy() {
        MissingFeature.busy(getContext())
    }

    override fun idle() {
        MissingFeature.idle(getContext())
    }
}
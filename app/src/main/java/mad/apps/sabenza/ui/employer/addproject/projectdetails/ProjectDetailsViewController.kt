package mad.apps.sabenza.ui.employer.addproject.projectdetails

import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.checkIfEmpty
import mad.apps.sabenza.ui.widget.headerviewbase.HeaderViewBase
import javax.inject.Inject

open class ProjectDetailsViewController : BaseViewController(), ProjectDetailsViewInterface {

    override fun layout(): Int = R.layout.employer_project_details

    @Inject open lateinit var presenter: ProjectDetailsPresenterInterface

    val header by bindView<HeaderViewBase>(R.id.employer_header)
    val projectNameEditText by bindView<EditText>(R.id.project_name_edittext)
    val projectDescriptionEditText by bindView<EditText>(R.id.project_description_edittext)
    val nextButton by bindView<FrameLayout>(R.id.next_button)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        nextButton.setOnClickListener {
            if (validate()) {
                presenter.saveNameAndDescription(projectNameEditText.text.toString(), projectDescriptionEditText.text.toString())
                    presenter.goToAddJobScreen()
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

        checkIfEmpty(projectNameEditText,"Project Name can not be blank")

        if (projectNameEditText.error.isNullOrEmpty() && projectDescriptionEditText.error.isNullOrEmpty()){
            return true
        }
        return false
    }



    override fun busy() {
    }

    override fun idle() {
    }


}
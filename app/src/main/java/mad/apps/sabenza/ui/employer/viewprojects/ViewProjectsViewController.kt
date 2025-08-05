package mad.apps.sabenza.ui.employer.viewprojects

import android.view.View
import android.widget.ImageView
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.widget.navigationfooter.NavigationFooter
import javax.inject.Inject

class ViewProjectsViewController : BaseViewController(), ViewProjectsViewInterface {

    override fun layout(): Int = R.layout.employer_view_projects

    @Inject lateinit var presenter: ViewProjectsPresenterInterface

    val navigationFooter by bindView<NavigationFooter>(R.id.employer_navigation_footer)
    val addProjectButton by bindView<ImageView>(R.id.employer_view_projects_add_button)


    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        addProjectButton.setOnClickListener { presenter.addProject() }
        navigationFooter.selectButtonHighlighted(NavigationFooter.FOOTER_ENUM.JOBS)
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
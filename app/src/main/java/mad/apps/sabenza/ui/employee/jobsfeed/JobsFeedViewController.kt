package mad.apps.sabenza.ui.employee.jobsfeed

import android.view.View
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.widget.navigationfooter.NavigationFooter
import javax.inject.Inject

class JobsFeedViewController : BaseViewController(), JobsFeedViewInterface {

    override fun layout(): Int = R.layout.employee_jobs_feed

    @Inject lateinit var presenter: JobsFeedPresenterInterface

    val navigationFooter by bindView<NavigationFooter>(R.id.employee_navigation_footer)


    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        setOnClickListeners()
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


    fun setOnClickListeners() {

    }

    override fun busy() {
    }

    override fun idle() {
    }


}
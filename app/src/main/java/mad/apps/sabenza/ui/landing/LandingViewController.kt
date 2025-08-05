package mad.apps.sabenza.ui.landing

import android.view.View
import android.widget.FrameLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import javax.inject.Inject

class LandingViewController : BaseViewController(), LandingViewInterface {

    override fun layout(): Int = R.layout.landing

    @Inject lateinit var presenter : LandingPresenterInterface

    val getStartedButton by bindView<FrameLayout>(R.id.get_started_button)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        getStartedButton.setOnClickListener{
            presenter.goToRoleSelectScreen()
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

}
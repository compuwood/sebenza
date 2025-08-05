package mad.apps.sabenza.ui.roleselect

import android.view.View
import android.widget.FrameLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import javax.inject.Inject

class RoleSelectViewController : BaseViewController(), RoleSelectViewInterface {

    override fun layout(): Int = R.layout.role_select

    @Inject lateinit var presenter : RoleSelectPresenterInterface

    val gotoEmployerButton by bindView<FrameLayout>(R.id.goto_employer_button)
    val gotoEmployeeButton by bindView<FrameLayout>(R.id.goto_employee_button)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        gotoEmployerButton.setOnClickListener{
            presenter.goToEmployerScreen()
        }

        gotoEmployeeButton.setOnClickListener{
            presenter.goToEmployeeScreen()
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
package mad.apps.sabenza.ui.blankscreen

import mad.apps.sabenza.R
import mad.apps.sabenza.framework.ui.BaseViewController

class BlankViewControllerEmployee : BaseViewController(), BlankScreenEmployeeViewInterface {
    override fun layout(): Int = R.layout.blankscreen_employee
}
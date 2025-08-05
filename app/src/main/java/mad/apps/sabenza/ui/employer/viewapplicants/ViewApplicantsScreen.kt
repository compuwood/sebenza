package mad.apps.sabenza.ui.employer.viewapplicants

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.data.model.employee.ApplicantPreviewModel
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import mad.apps.sabenza.ui.util.SuccessErrorViewInterface
import zendesk.suas.Store

interface ViewApplicantsViewInterface : ViewInterface, BusyViewInterface, SuccessErrorViewInterface {
}

interface ViewApplicantsPresenterInterface : PresenterInterface<ViewApplicantsViewInterface> {
    fun getApplicants() : List<ApplicantPreviewModel>
    fun gotoSelectedApplicantProfile(employeeId : String)
}

object ViewApplicantsScreen : BaseScreen<ViewApplicantsViewInterface, ViewApplicantsPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return ViewApplicantsViewController()
    }

    override fun providePresenter(store: Store): ViewApplicantsPresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            ViewApplicantsStubPresenter(store)
        } else {
            ViewApplicantsPresenter(store)
        }
    }
}
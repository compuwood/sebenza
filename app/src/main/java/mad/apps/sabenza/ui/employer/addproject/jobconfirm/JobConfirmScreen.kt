package mad.apps.sabenza.ui.employer.addproject.jobconfirm

import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import mad.apps.sabenza.ui.util.SuccessErrorViewInterface
import zendesk.suas.Store

interface JobConfirmViewInterface : ViewInterface, BusyViewInterface, SuccessErrorViewInterface {

}

interface JobConfirmPresenterInterface : PresenterInterface<JobConfirmViewInterface> {
    fun getSelectedJob(): JobPreviewModel
    fun confirmJob()
    fun gotoListedJobScreen()
}

object JobConfirmScreen : BaseScreen<JobConfirmViewInterface, JobConfirmPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return JobConfirmViewController()
    }

    override fun providePresenter(store: Store): JobConfirmPresenterInterface {
//        if (BuildConfig.IS_UI_BUILD) {
        return JobConfirmStubPresenter(store)
//        } else {
//            return JobConfirmPresenter(store)
//        }
    }
}
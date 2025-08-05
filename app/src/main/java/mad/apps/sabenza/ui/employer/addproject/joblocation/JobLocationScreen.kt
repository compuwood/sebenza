package mad.apps.sabenza.ui.employer.addproject.joblocation

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store

interface JobLocationViewInterface : ViewInterface, BusyViewInterface {

}

interface JobLocationPresenterInterface : PresenterInterface<JobLocationViewInterface> {
    fun getEmployerAddressList() : List<Address>
    fun getEmployerDefaultAddressId() : String
    fun saveAddress(address: Address)
    fun gotoDateTimeScreen(job: Boolean = false)
    fun addAddress()
    fun updateWithAddJobModel(addJobViewModel: AddJobViewModel)
}

object JobLocationScreen : BaseScreen<JobLocationViewInterface, JobLocationPresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return JobLocationViewController()
    }

    override fun providePresenter(store: Store): JobLocationPresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            JobLocationStubPresenter(store)
        } else {
            JobLocationPresenter(store)
        }
    }
}
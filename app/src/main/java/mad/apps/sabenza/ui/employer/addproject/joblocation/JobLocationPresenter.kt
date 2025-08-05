package mad.apps.sabenza.ui.employer.addproject.joblocation

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.models.AddressModel
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.employer.addjob.addjobtime.AddJobTimeScreen
import mad.apps.sabenza.ui.employer.addproject.jobtime.JobTimeScreen
import zendesk.suas.Store

class JobLocationPresenter(store : Store) : BasePresenter<JobLocationViewInterface>(store), JobLocationPresenterInterface {

    lateinit var addJobViewModel : AddJobViewModel

    override fun addAddress() {
        //route to add address screen
    }

    override fun updateWithAddJobModel(addJobViewModel: AddJobViewModel) {
        this.addJobViewModel = addJobViewModel
    }

    override fun getEmployerAddressList(): List<Address> {
        return store.state.getState(AddressModel::class.java)?.employerAddress ?: listOf()
    }

    override fun getEmployerDefaultAddressId(): String {
        if (addJobViewModel.address?.id != null) {
            return addJobViewModel.address!!.id!!
        }

        val defaultAddress = store.state.getState(AddressModel::class.java)?.selectedEmployerAddress
        if (defaultAddress?.id != null) {
            return defaultAddress.id
        }

        val lastAddress = store.state.getState(AddressModel::class.java)?.employerAddress?.last()
        return lastAddress?.id ?: ""
    }

    override fun saveAddress(address: Address) {
        addJobViewModel.address = address
    }

    override fun gotoDateTimeScreen(job: Boolean) {
        if (job) {
            routeTo(AddJobTimeScreen.provideViewController().withViewArgs(Pair(AddJobViewModel.argName(), addJobViewModel)))
        } else {
            routeTo(JobTimeScreen.provideViewController())
        }
    }

}
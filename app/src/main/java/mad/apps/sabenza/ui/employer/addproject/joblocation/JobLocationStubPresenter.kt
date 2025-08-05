package mad.apps.sabenza.ui.employer.addproject.joblocation

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.employer.addjob.addjobtime.AddJobTimeScreen
import mad.apps.sabenza.ui.employer.addproject.jobtime.JobTimeScreen
import zendesk.suas.Store

class JobLocationStubPresenter(store : Store) : BasePresenter<JobLocationViewInterface>(store), JobLocationPresenterInterface {
    override fun updateWithAddJobModel(addJobViewModel: AddJobViewModel) {
    }

    override fun addAddress() {
        //route to add address screen
    }

    override fun getEmployerAddressList(): List<Address> {
        val address1 = Address("6834 Hollywood Blvd", "Los Angeles", "California","90028-6102", "", "", "1")
        val address2 = Address("683 Vine Street", "Los Angeles", "California","90210-6789", "", "", "2")
        val address3 = Address("223 Highland Green", "Los Angeles", "California","90210-6789", "", "", "3")
        val address4 = Address("6834 Hollywood Blvd", "Los Angeles", "California","90028-6102", "", "", "4")
        val address5 = Address("683 Vine Street", "Los Angeles", "California","90210-6789", "", "", "5")
        val address6 = Address("223 Highland Green", "Los Angeles", "California","90210-6789", "", "", "6")
        return listOf(address1,address2,address3,address4,address5,address6)
    }

    override fun getEmployerDefaultAddressId(): String {
        return "1"
    }

    override fun saveAddress(address: Address) {

    }

    override fun gotoDateTimeScreen(job: Boolean) {
        if (job) {
            routeTo(AddJobTimeScreen.provideViewController())
        } else {
            routeTo(JobTimeScreen.provideViewController())
        }
    }



}
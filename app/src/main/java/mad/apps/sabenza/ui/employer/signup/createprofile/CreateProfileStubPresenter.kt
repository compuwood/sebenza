package mad.apps.sabenza.ui.employer.signup.createprofile

import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employer.signup.address.YourAddressScreen
import zendesk.suas.Store

class CreateProfileStubPresenter(store : Store) : BasePresenter<CreateProfileViewInterface>(store), CreateProfilePresenterInterface {
    override fun saveDetails(firstName: String, lastName: String, number: String, company: String, companyDescription: String) {
    }

    override fun uploadProfilePicture() {
        router().pushController(RouterTransaction.with(YourAddressScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }

    override fun gotoNextScreen() {
        router().pushController(RouterTransaction.with(YourAddressScreen.provideViewController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
    }

}
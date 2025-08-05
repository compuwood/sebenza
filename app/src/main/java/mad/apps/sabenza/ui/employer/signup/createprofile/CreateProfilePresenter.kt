package mad.apps.sabenza.ui.employer.signup.createprofile

import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.framework.rx.state.StateObserver
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.AddOrUpdateEmployerAction
import mad.apps.sabenza.state.models.EmployerModel
import mad.apps.sabenza.ui.employer.signup.address.YourAddressScreen
import zendesk.suas.Store

class CreateProfilePresenter(store: Store) : BasePresenter<CreateProfileViewInterface>(store), CreateProfilePresenterInterface {

    override fun loaded() {
        RxStateBinder.bindSingleState(store, EmployerModel::class.java)
                .compose(NetworkTransformer())
                .subscribe(StateObserver.singleObserver {
                    view.idle()
                    gotoNextScreen()
                })
    }

    override fun uploadProfilePicture() {
//        router().pushController(RouterTransaction.with(EmployerYourAddressScreen.provideViewController())
//                .pushChangeHandler(FadeChangeHandler())
//                .popChangeHandler(FadeChangeHandler()))
    }

    override fun gotoNextScreen() {
        routeToNewTop(YourAddressScreen.provideViewController())
    }

    override fun saveDetails(firstName: String, lastName: String, number: String, company: String, companyDescription: String) {
        view.busy()
        store.dispatch(AddOrUpdateEmployerAction(
                Employer(firstName = firstName, lastName = lastName, phoneNumber = number, aboutCompany = companyDescription, aboutMe = company)
        ))
    }
}
package mad.apps.sabenza.ui.employer.signup.address

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.framework.rx.UIObservableTransformer
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.framework.rx.state.RxStateObserver
import mad.apps.sabenza.framework.rx.state.StateObserver
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.AddAddressAndLinkAction
import mad.apps.sabenza.state.models.AddressModel
import mad.apps.sabenza.ui.employer.signup.paymentinfo.PaymentInfoScreen
import zendesk.suas.Store

class YourAddressPresenter(store: Store) : BasePresenter<YourAddressViewInterface>(store), YourAddressPresenterInterface {

    var stateObserver: RxStateObserver<AddressModel>? = null

    override fun loaded() {
        stateObserver = StateObserver.buildObserver {
            view.idle()
            if (it.selectedEmployerAddress != null) {
                view.addressUpdatedSuccessfully()
            } else {
                view.errorUpdatingAddress("Address could not be added")
            }
        }

        RxStateBinder.bindState(store, AddressModel::class.java)
                .compose(UIObservableTransformer())
                .subscribe(stateObserver!!)
    }

    override fun dropView() {
        stateObserver?.dispose()
    }

    override fun gotoNextScreen() {
        routeToNewTop(PaymentInfoScreen.provideViewController())
    }

    override fun saveAddressDetails(line1: String, city: String, state: String, zipCode: String) {
        view.busy()
        store.dispatch(
                AddAddressAndLinkAction(Address(
                        line1 = line1,
                        line2 = state,
                        cityTown = city,
                        postcode = zipCode,
                        countryId = "1"))
        )
    }

}
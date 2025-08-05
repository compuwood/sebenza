package mad.apps.sabenza.ui.employee.signup.createprofile

import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.state.RxSingleStateObserver
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.framework.rx.state.StateObserver
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.AddOrUpdateEmployeeAction
import mad.apps.sabenza.state.models.EmployeeModel
import mad.apps.sabenza.state.util.MeStateUtil
import mad.apps.sabenza.ui.employee.signup.addskills.AddSkillsScreen
import mad.apps.sabenza.ui.util.MissingFeature
import zendesk.suas.Store

class CreateProfilePresenter(store: Store) : BasePresenter<CreateProfileViewInterface>(store), CreateProfilePresenterInterface {
    override fun updateProfilePicture(pictureUrl: String) {
        MissingFeature.show(view.getContext(), "Upload Profile Pic")
    }

    var observer : RxSingleStateObserver<EmployeeModel>? = null


    override fun scanUploadIdDocs() {
        MissingFeature.show(view.getContext(), "Scan Docs")
    }

    override fun gotoNextScreen() {
        routeToNewTop(AddSkillsScreen.provideViewController())
    }

    override fun saveDetails(firstName: String, lastName: String, number: String, driversLicence: Boolean) {
        observer = StateObserver.singleObserver {
            view.idle()
            if (it.hasEmployee) {
                view.success()
            } else {
                view.error("Could not add employee details")
            }
        }

        view.busy()
        RxStateBinder.dispatchAndBindForResult(
                AddOrUpdateEmployeeAction(
                        Employee(firstName = firstName, lastName = lastName, phoneNumber = number, email = MeStateUtil.getMe(store)?.email)
                ), store, EmployeeModel::class.java)
                .compose(NetworkTransformer())
                .subscribe(observer!!)
    }
}
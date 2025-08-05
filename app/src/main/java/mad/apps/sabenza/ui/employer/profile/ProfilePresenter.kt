package mad.apps.sabenza.ui.employer.profile

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.AddOrUpdateEmployerAction
import mad.apps.sabenza.state.models.AddressModel
import mad.apps.sabenza.state.models.EmployerModel
import mad.apps.sabenza.ui.util.MissingFeature
import zendesk.suas.Store
import java.util.*

class ProfilePresenter(store: Store) : BasePresenter<ProfileViewInterface>(store), ProfilePresenterInterface {
    override fun getAverageRating(): Float {
        return 4.3f
        MissingFeature.log("Average Rating in Employer Profile Presenter")
    }

    override fun getTopReviewRating(): Int {
        return 4
        MissingFeature.log("Top Review Rating in Employer Profile Presenter")
    }

    override fun getTopReviewTitle(): String {
        return "What a boss!"
        MissingFeature.log("Top Review Title in Employer Profile Presenter")
    }

    override fun getTopReviewerName(): String {
        return "Charlie Theron"
        MissingFeature.log("Top Reviewer Name in Employer Profile Presenter")
    }

    override fun getTopReviewDate(): Calendar {
        val date = Calendar.getInstance()
        date.set(2016,0,24)
        return date
        MissingFeature.log("Top Review Date in Employer Profile Presenter")
    }

    override fun getTopReviewWorkDone(): String {
        return "Nanny"
        MissingFeature.log("Top Review Skill Name in Employer Profile Presenter")
    }

    override fun getTopReviewWriteUp(): String {
        return "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."
        MissingFeature.log("Top Review Writeup in Employer Profile Presenter")
    }

    override fun goToReviews() {
    }

    init {

    }

    private fun currentEmployer(): Employer? {
        return store.state.getState(EmployerModel::class.java)?.employer
    }

    private fun addresses(): List<Address>? {
        return store.state.getState(AddressModel::class.java)?.employerAddress
    }

    private fun primaryAddress(): Address? {
        return store.state.getState(AddressModel::class.java)?.selectedEmployerAddress
    }

    override fun getProfileTelephone(): String {
        return if (currentEmployer() != null) currentEmployer()!!.phoneNumber!! else ""
    }

    override fun getProfileEmail(): String {
        return if (currentEmployer() != null) currentEmployer()!!.email!! else ""
    }

    override fun getProfileTotalReviews(): Int {
        return if (currentEmployer() != null) currentEmployer()!!.ratingCount!! else 0
    }

    override fun getProfileCompanyDetails(): String {
        return if (currentEmployer() != null) currentEmployer()!!.aboutCompany!! else ""
    }

    override fun getProfileDefaultAddress(): String {
        MissingFeature.log("Full Address Summary in Employer Profile Presenter")
        return if (primaryAddress() != null) primaryAddress()!!.line1!! else ""
    }

    override fun getProfilePaymentInfo(): String {
        MissingFeature.log("Payment Info in Employer Profile Presenter")
        return "Payment Info"
    }

    override fun getProfileFullName(): String {
        return if (currentEmployer() != null) {
            currentEmployer()!!.firstName!! + currentEmployer()!!.lastName!!
        } else {
            ""
        }
    }

    override fun updateFullName(fullName: String) {

    }

    override fun updateTelNum(telNum: String) {
        val updateEmployer = currentEmployer()?.copy(telNum)
        updateEmployer(updateEmployer)
    }

    override fun updateEmail(email: String) {
        val updatedEmployer = currentEmployer()?.copy(email = email)
        updateEmployer(updatedEmployer)
    }

    override fun updateCompanyDetails(details: String) {
        val updatedEmployer = currentEmployer()?.copy(aboutCompany = details)
        updateEmployer(updatedEmployer)
    }

    private fun updateEmployer(employer: Employer?) {
        if (employer != null) {
            view.busy()
            RxStateBinder.dispatchAndBindForResult(AddOrUpdateEmployerAction(employer), store, EmployerModel::class.java)
                    .compose(NetworkTransformer())
                    .subscribe(object : EnhancedSingleObserver<EmployerModel>() {
                        override fun onSuccess(t: EmployerModel) {
                            view.idle()
                            view.update()
                        }

                        override fun onError(e: Throwable) {
                            MissingFeature.error(view.getContext(), "Error updating Employee Phone Number")
                        }
                    })
        } else {
            MissingFeature.error(view.getContext(), "Error updating Employee Phone Number")
        }
    }


}
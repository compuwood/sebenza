package mad.apps.sabenza.ui.employer.profile

import mad.apps.sabenza.framework.ui.BasePresenter
import zendesk.suas.Store
import java.util.*

class ProfileStubPresenter(store : Store) : BasePresenter<ProfileViewInterface>(store), ProfilePresenterInterface {

    override fun getProfileFullName(): String {
        return "Mad Max"
    }

    override fun getAverageRating(): Float {
        return 4.3f
    }

    override fun getProfileTotalReviews(): Int {
        return 126
    }

    override fun getProfileTelephone(): String {
        return "012 345 6789"
    }

    override fun getProfileEmail(): String {
        return "mad.max@warboyz.com"
    }

    override fun getProfileCompanyDetails(): String {
        return "War Boyz - We’re determined demolishers and desserters looking for oil riggers and rioters."
    }

    override fun getProfileDefaultAddress(): String {
        return "6834 Hollywood Blvd\nLos Angeles, California\n90028-6102"
    }

    override fun getProfilePaymentInfo(): String {
        return "M.Max\n9002861027861\n06 / 2020\n007"
    }

    override fun getTopReviewRating(): Int {
        return 4
    }

    override fun getTopReviewTitle(): String {
        return "What a boss!"
    }

    override fun getTopReviewerName(): String {
        return "Charlie Theron"
    }

    override fun getTopReviewDate(): Calendar {
        val date = Calendar.getInstance()
        date.set(2016,0,24)
        return date
    }

    override fun getTopReviewWorkDone(): String {
        return "Nanny"
    }

    override fun getTopReviewWriteUp(): String {
        return "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."
    }

    override fun updateFullName(fullName: String) {
    }

    override fun updateTelNum(telNum: String) {
    }

    override fun updateEmail(email: String) {
    }

    override fun updateCompanyDetails(details: String) {
    }

    override fun goToReviews() {
    }



}
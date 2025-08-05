package mad.apps.sabenza.ui.employee.profile

import android.net.Uri
import mad.apps.sabenza.data.model.picture.SebenzaPicture
import mad.apps.sabenza.framework.ui.BasePresenter
import zendesk.suas.Store
import java.util.*


class ProfileStubPresenter(store : Store) : BasePresenter<ProfileViewInterface>(store), ProfilePresenterInterface {
    override fun getProfilePictureUrl(): String {
        return ""
    }

    override fun updateProfilePicture(picture: SebenzaPicture) {
    }

    override fun getProfileFullName(): String {
        return "Charlie Theron"
    }

    override fun getAverageRating() : Float {
        return 3.2f
    }

    override fun getProfileTotalReviews(): Int {
        return 321
    }

    override fun getProfileTelephone(): String {
        return "012 345 6789"
    }

    override fun getProfileEmail(): String {
        return "imperator@furiosa.com"
    }

    override fun getSkills(): List<Pair<String, Int>> {
        val skill1 = Pair("Au Pair", 6)
        val skill2 = Pair("Nanny", 3)
        val skill3 = Pair("Dog Walker", 2)
        val skill4 = Pair("All Round Hotty", 17)

        return listOf(skill1 ,skill2 ,skill3 ,skill4)
    }

    override fun getProfileDefaultAddress(): String {
        return "783 Hollywood Blvd\nLos Angeles, California\n90028-6102"
    }

    override fun getProfilePaymentInfo(): String {
        return "C. Theron\n9002861027861\n06 / 2020\n007"
    }

    override fun getTopReviewRating(): Int {
        return 4
    }

    override fun getTopReviewTitle(): String {
        return "Great nanny!"
    }

    override fun getTopReviewerName(): String {
        return "Mad Max"
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

    override fun goToReviews() {
    }






}
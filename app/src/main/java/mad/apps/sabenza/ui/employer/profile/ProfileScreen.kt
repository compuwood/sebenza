package mad.apps.sabenza.ui.employer.profile

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import zendesk.suas.Store
import java.util.*


interface ProfileViewInterface : ViewInterface, BusyViewInterface {
    fun switchEditMode()
    fun editAddress()
    fun editPaymentInfo()
    fun update()
}

interface ProfilePresenterInterface : PresenterInterface<ProfileViewInterface> {

    fun getProfileFullName(): String
    fun getAverageRating(): Float
    fun getProfileTotalReviews() : Int
    fun getProfileTelephone() : String
    fun getProfileEmail() : String

    fun getTopReviewRating(): Int
    fun getTopReviewTitle(): String
    fun getTopReviewerName(): String
    fun getTopReviewDate(): Calendar
    fun getTopReviewWorkDone(): String
    fun getTopReviewWriteUp() : String

    fun getProfileCompanyDetails(): String
    fun getProfileDefaultAddress(): String
    fun getProfilePaymentInfo(): String

    fun updateFullName(fullName: String)
    fun updateTelNum(telNum: String)
    fun updateEmail(email: String)
    fun updateCompanyDetails(details: String)

    fun goToReviews()
}

object ProfileScreen : BaseScreen<ProfileViewInterface, ProfilePresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return ProfileViewController()
    }

    override fun providePresenter(store: Store): ProfilePresenterInterface {
        if (BuildConfig.IS_UI_BUILD) {
            return ProfileStubPresenter(store)
        } else {
            return ProfilePresenter(store)
        }
    }
}
package mad.apps.sabenza.ui.employee.signup.createprofile

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.BaseScreen
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.PresenterInterface
import mad.apps.sabenza.framework.ui.ViewInterface
import mad.apps.sabenza.ui.util.BusyViewInterface
import mad.apps.sabenza.ui.util.SuccessErrorViewInterface
import zendesk.suas.Store

interface CreateProfileViewInterface : ViewInterface, BusyViewInterface, SuccessErrorViewInterface {


}

interface CreateProfilePresenterInterface : PresenterInterface<CreateProfileViewInterface> {
    fun updateProfilePicture(pictureUrl : String)
    fun scanUploadIdDocs()
    fun gotoNextScreen()
    fun saveDetails(firstName: String, lastName: String, number: String, driversLicence: Boolean)
}

object CreateProfileScreen : BaseScreen<CreateProfileViewInterface, CreateProfilePresenterInterface> {
    override fun provideViewController(): BaseViewController {
        return CreateProfileViewController()
    }

    override fun providePresenter(store: Store): CreateProfilePresenterInterface {
        return if (BuildConfig.IS_UI_BUILD) {
            CreateProfileStubPresenter(store)
        } else {
            CreateProfilePresenter(store)
        }
    }
}
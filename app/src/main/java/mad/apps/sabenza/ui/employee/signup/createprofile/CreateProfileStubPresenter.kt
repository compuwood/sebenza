package mad.apps.sabenza.ui.employee.signup.createprofile
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.ui.employee.signup.addskills.AddSkillsScreen
import zendesk.suas.Store


class CreateProfileStubPresenter(store : Store) : BasePresenter<CreateProfileViewInterface>(store), CreateProfilePresenterInterface {
    override fun updateProfilePicture(pictureUrl: String) {
    }

    override fun saveDetails(firstName: String, lastName: String, number: String, driversLicence: Boolean) {
    }


    override fun scanUploadIdDocs() {
    }

    override fun gotoNextScreen() {
        routeToNewTop(AddSkillsScreen.provideViewController())
    }
}
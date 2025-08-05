package mad.apps.sabenza.ui.employee.profile

import android.net.Uri
import com.noveogroup.android.log.LoggerManager
import mad.apps.sabenza.data.model.employee.EmployeeSkill
import mad.apps.sabenza.data.model.picture.SebenzaPicture
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.data.rpc.calls.employee.Skill
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.AddOrUpdateEmployeeAction
import mad.apps.sabenza.state.models.AddressModel
import mad.apps.sabenza.state.models.EmployeeModel
import mad.apps.sabenza.state.models.SkillsModel
import mad.apps.sabenza.ui.util.MissingFeature
import zendesk.suas.Store
import java.util.*

class ProfilePresenter(store: Store) : BasePresenter<ProfileViewInterface>(store), ProfilePresenterInterface {
    init {

    }

    private fun currentAddressModel(): AddressModel? {
        return store.state.getState(AddressModel::class.java)
    }

    private fun currentEmployee(): Employee? {
        return store.state.getState(EmployeeModel::class.java)?.employee
    }

    private fun currentEmployeeSkills(): List<EmployeeSkill>? {
        return store.state.getState(EmployeeModel::class.java)?.skills
    }

    private fun currentAvailableSkills(): List<Skill>? {
        return store.state.getState(SkillsModel::class.java)?.availableSkills
    }

    override fun getProfilePictureUrl(): String {
        val pictureId = currentEmployee()?.pictureId
        if ((pictureId != null) && pictureId.isNotEmpty()) {
            return pictureId;
        }
        return ""
    }


    override fun getProfileFullName(): String {
        return (currentEmployee()?.firstName ?: "") + " " + (currentEmployee()?.lastName ?: "")
    }

    override fun getAverageRating(): Float {
        return currentEmployee()?.ratingAvg?.toFloat() ?: 0f
    }

    override fun getProfileTotalReviews(): Int {
        return currentEmployee()?.ratingSum ?: 0
    }

    override fun getProfileTelephone(): String {
        return currentEmployee()?.phoneNumber ?: "No Phone."
    }

    override fun getProfileEmail(): String {
        return currentEmployee()?.email ?: "No Email."
    }

    override fun getSkills(): List<Pair<String, Int>> {
        val skillsList = currentAvailableSkills()
        return currentEmployeeSkills()?.map { employeeSkill ->
            val skill = skillsList?.find { it.id == employeeSkill.skillId }
            val experience = (employeeSkill.yearsExperience ?: "0").toInt()
            Pair(skill?.description ?: "", experience)
        } ?: listOf()
    }

    override fun getProfileDefaultAddress(): String {
        val address = if (currentAddressModel()?.selectedEmployeeAddress == null) {
            currentAddressModel()?.selectedEmployeeAddress?.address
        } else {
            currentAddressModel()?.employeeAddress?.first()?.address
        }

        return if (address != null) {
            "${address.line1}\n${address.cityTown}\n${address.postcode}"
        } else {
            ""
        }
    }

    override fun getProfilePaymentInfo(): String {
        return MissingFeature.missingValue("Employee Payment Info", "")
    }

    override fun getTopReviewRating(): Int {
        return MissingFeature.missingValue("Top Review in Employee Profile", 0)
    }

    override fun getTopReviewTitle(): String {
        return MissingFeature.missingValue("Top Review Title in Employee Profile", "")
    }

    override fun getTopReviewerName(): String {
        return MissingFeature.missingValue("Top Review Name in Employee", "")
    }

    override fun getTopReviewDate(): Calendar {
        val date = Calendar.getInstance()
        date.set(2016, 0, 24)
        return MissingFeature.missingValue("Top Review Data in Employee", date)
    }

    override fun getTopReviewWorkDone(): String {
        return MissingFeature.missingValue("Top Review Work Done in Employee", "" )
    }

    override fun getTopReviewWriteUp(): String {
        return MissingFeature.missingValue("Top Review in Employee", "")
    }

    override fun updateFullName(fullName: String) {

    }

    override fun updateProfilePicture(picture: SebenzaPicture) {
        val updatedEmployee = currentEmployee()?.copy(pictureId = picture.id)
        updateEmployee(updatedEmployee)
    }

    override fun updateTelNum(telNum: String) {
        val updatedEmployee = currentEmployee()?.copy(phoneNumber = telNum)
        updateEmployee(updatedEmployee)
    }

    override fun updateEmail(email: String) {
        val updatedEmployee = currentEmployee()?.copy(email = email)
        updateEmployee(updatedEmployee)
    }

    private fun updateEmployee(updatedEmployee: Employee?) {
        if (updatedEmployee != null) {
            view.busy()
            RxStateBinder.dispatchAndBindForResult(AddOrUpdateEmployeeAction(updatedEmployee), store, EmployeeModel::class.java)
                    .compose(NetworkTransformer())
                    .subscribe(object : EnhancedSingleObserver<EmployeeModel>() {
                        override fun onError(e: Throwable) {
                            MissingFeature.error(view.getContext(), "Error updating Employee Phone Number")
                        }

                        override fun onSuccess(t: EmployeeModel) {
                            view.idle()
                            view.update()
                        }
                    })
        } else {
            MissingFeature.error(view.getContext(), "Error updating Employee Phone Number")
        }
    }

    override fun goToReviews() {
    }
}
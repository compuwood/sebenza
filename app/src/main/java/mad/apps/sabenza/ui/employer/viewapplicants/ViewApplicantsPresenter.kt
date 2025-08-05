package mad.apps.sabenza.ui.employer.viewapplicants

import mad.apps.sabenza.data.model.employee.ApplicantPreviewModel
import mad.apps.sabenza.framework.ui.BasePresenter
import zendesk.suas.Store

class ViewApplicantsPresenter(store: Store) : BasePresenter<ViewApplicantsViewInterface>(store), ViewApplicantsPresenterInterface {
    override fun getApplicants() : List<ApplicantPreviewModel> {
        val applicant1 = ApplicantPreviewModel(employeeId = "101", firstName = "Ray", lastName = "Neal", ratingAvg = 3, topSkill1 = "Au Pair", topSkill2 = "Nanny", topSkill3 = "Dog Walker")
        val applicant2 = ApplicantPreviewModel(employeeId = "102", firstName = "Carrie", lastName = "Mann", ratingAvg = 5, topSkill1 = "Nanny", topSkill2 = "Babysitter", topSkill3 = "Child Minder")
        val applicant3 = ApplicantPreviewModel(employeeId = "103", firstName = "Linda", lastName = "Ross", ratingAvg = 4, topSkill1 = "Care Giver", topSkill2 = "Au Pair", topSkill3 = "Nurse")
        val applicant4 = ApplicantPreviewModel(employeeId = "104", firstName = "Gavin", lastName = "Desousa", ratingAvg = 2, topSkill1 = "Child of Jesus", topSkill2 = "Lover of Light")
        return listOf(applicant1,applicant2,applicant3,applicant4)
    }

    override fun gotoSelectedApplicantProfile(employeeId: String) {
//        routeTo()
    }

}
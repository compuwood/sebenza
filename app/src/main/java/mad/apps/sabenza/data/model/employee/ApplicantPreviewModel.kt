package mad.apps.sabenza.data.model.employee

class ApplicantPreviewModel(
        val employeeId: String,
        val firstName: String,
        val lastName: String,
        val ratingAvg: Int = 0,
        val profileImg: String = "",
        val topSkill1: String = "",
        val topSkill2: String = "",
        val topSkill3: String = ""
)
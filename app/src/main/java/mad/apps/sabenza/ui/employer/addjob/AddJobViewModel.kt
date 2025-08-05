package mad.apps.sabenza.ui.employer.addjob

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.Project
import mad.apps.sabenza.data.model.payment.EmployerCreditCard
import mad.apps.sabenza.data.rpc.calls.employee.Skill
import mad.apps.sabenza.data.util.getISOTimeFormattedString
import java.util.*

class AddJobViewModel {

    var skill : Skill? = null
    var description: String? = null
    var budget: Int? = null
    var address: Address? = null
    var yearsExp : Int? = null
    var startDate: Calendar? = null
    var endDate: Calendar? = null

    companion object {
        fun argName() = "AddJobViewModel"
    }

    fun buildProject(employerId : Int, creditCard: EmployerCreditCard): Project {
        return Project(
                description = description,
                creditCardId = creditCard.credit_card_id)
    }

    fun buildJob(projectId : Int, creditCard: EmployerCreditCard): Job {
        return Job(
                projectId = projectId,
                skillId = skill?.id,
                description = description,
                quantity = 1.toString(),
                duration = calculateDuration(),
                startDate = startDate?.getISOTimeFormattedString(),
                endDate = endDate?.getISOTimeFormattedString(),
                creditCardId = creditCard.credit_card_id
        )
    }

    private fun calculateDuration(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = endDate!!.timeInMillis - startDate!!.timeInMillis
        return calendar.get(Calendar.HOUR).toString()

    }

}
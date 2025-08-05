package mad.apps.sabenza.data.model.jobs

import mad.apps.sabenza.data.util.CurrencyUtil
import mad.apps.sabenza.data.util.DateUtil
import java.text.SimpleDateFormat
import java.util.*

data class EmployerProject(
        val project : Project,
        val linkedJobs : List<Job>) {

    fun calculateStartDate(): Calendar {
        return DateUtil.sortListOfDateStrings(linkedJobs.map { it.startDate }.filterNotNull()).first()
    }

    fun calculateEndDate(): Calendar {
        return DateUtil.sortListOfDateStrings(linkedJobs.map { it.endDate }.filterNotNull()).last()
    }

    fun isFilled(): Boolean {
        linkedJobs.forEach {
            if (!(it.filled!! == "1")) {
                return false
            }
        }
        return true
    }

    fun calculateBudget(): Int {
        var totalRate : Int = 0
        linkedJobs.map{it.rate?.replace(CurrencyUtil.getCurrencySymbol(), "") }.forEach { totalRate += it!!.toDouble().toInt() }
        return totalRate
    }

    fun hasJobs() : Boolean {
        return linkedJobs.isNotEmpty()
    }
}
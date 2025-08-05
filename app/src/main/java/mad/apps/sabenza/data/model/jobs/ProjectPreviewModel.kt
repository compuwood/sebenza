package mad.apps.sabenza.data.model.jobs

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar


class ProjectPreviewModel(
        val jobId: String,
        val title: String,
        val description: String,
        val startDate: Calendar,
        val endDate: Calendar,
        val budget: Int = 0,
        val isForYou: Boolean = false,
        val isAppliedFor: Boolean = false,
        val isConfirmed: Boolean = false,
        val isProject: Boolean = false,
        val isFilled: Boolean = false) {

    val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    fun getStartDate() : String {
        return format.format(startDate.time)
    }

    fun getEndDate() : String {
        return format.format(endDate.time)
    }

    fun getBudget() : String {
        return budget.toString()
    }

}
package mad.apps.sabenza.data.model.jobs

import io.reactivex.Single
import java.util.*
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.state.models.AddressModel
import mad.apps.sabenza.state.models.EmployeeJobsModel
import mad.apps.sabenza.state.models.ProjectModel
import mad.apps.sabenza.ui.util.MissingFeature
import zendesk.suas.Store

class JobPreviewModel(
        val jobId: Int,
        val title: String,
        val description: String,
        val experience: Int,
        val budget: Int,
        val startDate: Calendar,
        val endDate: Calendar,
        val startTimeHour: Int,
        val endTimeHour: Int,
        val startTimeMinute: Int,
        val endTimeMinute: Int,
        val address: Address?
) {
    fun getExperienceYears() : String {
        return experience.toString()
    }

    fun getBudget() : String {
        return budget.toString()
    }

    fun getStartDateAsString() : String{
        return buildStringFromDate(startDate) + " at " +  buildStringFromTime(startTimeHour, startTimeMinute)
    }

    fun getEndDateAsString() : String{
        return buildStringFromDate(endDate) + " at " + buildStringFromTime(endTimeHour, endTimeMinute)
    }

    fun getAddressAsString() : String {
        return address?.line1 + ", " + address?.line2 + ", " + address?.cityTown + ", " + address?.postcode
    }

    private fun buildStringFromTime(hour: Int, minute: Int) : String {
        var suffix = "AM"
        val minuteFormat = "%1$02d" // two digits

        var hourAsAmPm = hour
        if (hourAsAmPm > 12) {
            hourAsAmPm -= 12
            suffix = "PM"
        }
        val minuteFormatted = String.format(minuteFormat, minute)
        return "$hourAsAmPm:$minuteFormatted $suffix"
    }

    private fun buildStringFromDate(calendar: Calendar) : String {
        return "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR)
    }

    companion object {
        fun buildFromEmployeeJobId(jobId: String, store: Store) : Single<JobPreviewModel> {
            val jobsModel : EmployeeJobsModel = store.state.getState(EmployeeJobsModel::class.java)!!
            val addressModel: AddressModel = store.state.getState(AddressModel::class.java)!!
            val projectModel : ProjectModel = store.state.getState(ProjectModel::class.java)!!

            return Single.create {
                val job = jobsModel.jobs.find { it.id == jobId } ?: fetchJobFromProject(jobId, projectModel)
                val address = addressModel.availableBackendAddress.find { it.id == job?.addressId.toString() }

                it.onSuccess(JobPreviewModel(
                        jobId = job!!.id!!.toInt(),
                        title = job!!.description!!,
                        description = job!!.description!!,
                        experience = MissingFeature.missingValue("Need Experience for JobPreviewModel", 0),
                        budget = MissingFeature.missingValue("Need Budget for JobPreviewModel", 0),
                        startDate = job.calculateStartDate(),
                        endDate = job.calculateEndDate(),
                        startTimeHour = job.calculateStartDate().get(Calendar.HOUR),
                        endTimeHour = job.calculateEndDate().get(Calendar.HOUR),
                        startTimeMinute = job.calculateStartDate().get(Calendar.MINUTE),
                        endTimeMinute = job.calculateEndDate().get(Calendar.MINUTE),
                        address = MissingFeature.missingValue("We dont have addresses for jobs", null)
                ))
            }
        }

        private fun fetchJobFromProject(jobId: String, projectModel: ProjectModel): Job? {
            var jobList : MutableList<Job> = mutableListOf()
            projectModel.projects.forEach { project -> jobList.addAll(project.linkedJobs) }
            return jobList.find { it.id == jobId }
        }
    }
}
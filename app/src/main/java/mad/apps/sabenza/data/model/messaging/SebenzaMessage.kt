package mad.apps.sabenza.data.model.messaging

import com.google.gson.annotations.SerializedName

data class SebenzaMessage(
        @SerializedName("is_read") val isRead: Boolean,// false
        @SerializedName("id") val id: String,// d8d1e833-6f59-45e7-9d29-3a705bd49958
        @SerializedName("parent_id") val parentId: String,
        @SerializedName("send_date") val sendDate: String,// 2017-10-09T15:45:49.901416+00:00
        @SerializedName("related_employer_id") val relatedEmployerId: Int,// 33
        @SerializedName("related_job_id") val relatedJobId: Int,// 87
        @SerializedName("related_employee_id") val relatedEmployeeId: Int,// 152
        @SerializedName("description") val description: String,// New Job
        @SerializedName("employee_first_name") val employeeFirstName: String,// New
        @SerializedName("employee_last_name") val employeeLastName: String,// OASHBQOR
        @SerializedName("employer_first_name") val employerFirstName: String,// John
        @SerializedName("employer_last_name") val employerLastName: String,// GKMUNNDP
        @SerializedName("body") val body: String,// Replying to you, dear master 6511
        @SerializedName("is_from_me") val isFromMe: Boolean,// true
        @SerializedName("is_to_me") val isToMe: Boolean// false
)
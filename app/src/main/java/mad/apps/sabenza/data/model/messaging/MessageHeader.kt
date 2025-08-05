package mad.apps.sabenza.data.model.messaging

import com.google.gson.annotations.SerializedName
import java.util.*

data class MessageHeader(
        @SerializedName("related_job_id")
        val relatedJobId: String,
        @SerializedName("related_employer_id")
        val relatedEmployerId: String,
        @SerializedName("related_employee_id")
        val relatedEmployeeId: String
) {
    @SerializedName("id")
    val id: String? = null

    @SerializedName("parent_id")
    val parentId: UUID? = null

    @SerializedName("send_date")
    val sendDate: String? = null

    @SerializedName("is_read")
    val isRead: Boolean? = null

    @SerializedName("is_deleted")
    val isDeleted: Boolean? = null

    @SerializedName("is_from_me")
    val isFromMe: Boolean? = null

    @SerializedName("is_to_me")
    val isToMe: Boolean? = null
}
package mad.apps.sabenza.data.model.jobs

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class JobEmployee(
        @SerializedName("job_id")
        val jobId: String,
        @SerializedName("employee_id")
        val employeeId: String,
        @SerializedName("skill_id")
        val skillId: String
) {

    @SerializedName("employer_id")
    val employerId: String? = null

    @SerializedName("state")
    val employeeJobState: EmployeeJobStateEnum? = null

    @SerializedName("changed_date")
    val changedDate: String? = null

    @SerializedName("start_date")
    val startDate: String? = null

    @SerializedName("end_date")
    val endDate: String? = null

    @SerializedName("state_change_reason")
    val stateChangeReason: String? = null

    @SerializedName("is_my_application")
    val isMyApplication: Boolean? = null

    @SerializedName("is_my_job")
    val isMyJob: Boolean? = null
}

/**
 * Gets or Sets state
 */
@JsonAdapter(EmployeeJobStateEnum.Adapter::class)
enum class EmployeeJobStateEnum private constructor(val value: String) {
    APPLIED("Applied"),

    WITHDRAWN("Withdrawn"),

    DECLINED("Declined"),

    CONFIRMED("Confirmed"),

    CANCELLED("Cancelled"),

    REVOKED("Revoked"),

    COMPLETED("Completed");

    override fun toString(): String {
        return value.toString()
    }

    class Adapter : TypeAdapter<EmployeeJobStateEnum>() {
        @Throws(IOException::class)
        override fun write(jsonWriter: JsonWriter, enumeration: EmployeeJobStateEnum) {
            jsonWriter.value(enumeration.value)
        }

        @Throws(IOException::class)
        override fun read(jsonReader: JsonReader): EmployeeJobStateEnum? {
            val value = jsonReader.nextString()
            return EmployeeJobStateEnum.fromValue(value.toString())!!
        }
    }

    companion object {

        fun fromValue(text: String): EmployeeJobStateEnum? {
            for (b in EmployeeJobStateEnum.values()) {
                if (b.value.toString() == text) {
                    return b
                }
            }
            return null
        }
    }
}
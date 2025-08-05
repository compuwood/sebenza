package mad.apps.sabenza.data.model.jobs

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class Project(
        @SerializedName("primary_address_id")
        val primaryAddressId: String? = null,

        @SerializedName("description")
        val description: String? = null,

        @SerializedName("credit_card_id")
        val creditCardId: Int? = 1,

        @SerializedName("employer_id")
        val employerId: Int? = null

) {
    @SerializedName("id")
    val id: Int? = null

    @SerializedName("state")
    val projectState: ProjectStateEnum? = null
}

@JsonAdapter(ProjectStateEnum.Adapter::class)
enum class ProjectStateEnum private constructor(val value: String) {
    DRAFT("Draft"),

    POSTED("Posted"),

    IN_PROGRESS("In-progress"),

    DELETED("Deleted"),

    COMPLETED("Completed");

    override fun toString(): String {
        return value.toString()
    }

    class Adapter : TypeAdapter<ProjectStateEnum>() {
        @Throws(IOException::class)
        override fun write(jsonWriter: JsonWriter, enumeration: ProjectStateEnum) {
            jsonWriter.value(enumeration.value)
        }

        @Throws(IOException::class)
        override fun read(jsonReader: JsonReader): ProjectStateEnum {
            val value = jsonReader.nextString()
            return ProjectStateEnum.fromValue(value.toString())!!
        }
    }

    companion object {

        fun fromValue(text: String): ProjectStateEnum? {
            for (b in ProjectStateEnum.values()) {
                if (b.value.toString() == text) {
                    return b
                }
            }
            return null
        }
    }
}
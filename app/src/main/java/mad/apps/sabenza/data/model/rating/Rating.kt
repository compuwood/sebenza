package mad.apps.sabenza.data.model.rating

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.LocalDate
import java.io.IOException

data class Rating(
        @SerializedName("rating")
        val rating: String,

        @SerializedName("review")
        val review: String,

        @SerializedName("is_never_use_again")
        val isNeverUseAgain: Boolean? = null,

        @SerializedName("employee_id")
        val employeeId: Int? = null,

        @SerializedName("employer_id")
        val employerId: Int? = null

) {
    @SerializedName("rating_date")
    val ratingDate: LocalDate? = null

    @SerializedName("acting_as")
    val actingAs: ActingAsEnum? = null

    @SerializedName("is_deleted")
    val isDeleted: Boolean? = null

    @SerializedName("is_rating_by_me")
    val isRatingByMe: Boolean? = null

    @SerializedName("is_rating_for_me")
    val isRatingForMe: Boolean? = null

}


@JsonAdapter(ActingAsEnum.Adapter::class)
enum class ActingAsEnum private constructor(val value: String) {
    EMPLOYER("employer"),

    EMPLOYEE("employee"),

    ADMINUSER("adminuser");

    override fun toString(): String {
        return value.toString()
    }

    class Adapter : TypeAdapter<ActingAsEnum>() {
        @Throws(IOException::class)
        override fun write(jsonWriter: JsonWriter, enumeration: ActingAsEnum) {
            jsonWriter.value(enumeration.value)
        }

        @Throws(IOException::class)
        override fun read(jsonReader: JsonReader): ActingAsEnum? {
            val value = jsonReader.nextString()
            return ActingAsEnum.fromValue(value.toString())
        }
    }

    companion object {

        fun fromValue(text: String): ActingAsEnum? {
            for (b in ActingAsEnum.values()) {
                if (b.value.toString() == text) {
                    return b
                }
            }
            return null
        }
    }
}
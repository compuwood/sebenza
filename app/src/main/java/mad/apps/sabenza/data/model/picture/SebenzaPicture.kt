package mad.apps.sabenza.data.model.picture

import com.google.gson.annotations.SerializedName

data class SebenzaPicture (
        @SerializedName("caption") val caption : String? = null,
        @SerializedName("uri") val uri : String? = null
) {
    @SerializedName("id")
    val id : String? = null
}
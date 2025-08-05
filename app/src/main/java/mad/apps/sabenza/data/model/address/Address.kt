package mad.apps.sabenza.data.model.address

import com.google.gson.annotations.SerializedName

class Address(
        @SerializedName("line_1")
        val line1: String? = null,

        @SerializedName("line_2")
        val line2: String? = null,

        @SerializedName("city_town")
        val cityTown: String? = null,

        @SerializedName("postcode")
        val postcode: String? = null,

        @SerializedName("country_id")
        val countryId: String? = null,

        @SerializedName("location")
        val location: String? = null,

        @SerializedName("id")
        val id: String? = null){

    @SerializedName("mine")
    val mine: Boolean? = null
}


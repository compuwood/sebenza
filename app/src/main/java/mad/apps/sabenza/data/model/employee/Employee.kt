package mad.apps.sabenza.data.rpc.calls.employee

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Employee(
        @SerializedName("first_name")
        @Expose
        val firstName: String? = null,

        @SerializedName("last_name")
        @Expose
        val lastName: String? = null,

        @SerializedName("email")
        @Expose
        val email: String? = null,

        @SerializedName("phone_number")
        @Expose
        val phoneNumber: String? = null,

        @SerializedName("picture_id")
        @Expose
        val pictureId: String? = null
) {
    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("state")
    @Expose
    val state: String? = null

    @SerializedName("identification_document_id")
    @Expose
    val identificationDocumentId: Any? = null

    @SerializedName("drivers_license")
    @Expose
    val driversLicense: String? = null

    @SerializedName("contact_me_at")
    @Expose
    val contactMeAt: String? = null

    @SerializedName("contact_me_km_range")
    @Expose
    val contactMeKmRange: Any? = null

    @SerializedName("location_last")
    @Expose
    val locationLast: Any? = null

    @SerializedName("location_last_date")
    @Expose
    val locationLastDate: Any? = null

    @SerializedName("rating_avg")
    @Expose
    val ratingAvg: Int? = null

    @SerializedName("rating_sum")
    @Expose
    val ratingSum: Int? = null

    @SerializedName("rating_count")
    @Expose
    val ratingCount: Int? = null

    @SerializedName("mine")
    @Expose
    val mine: Boolean? = null

}
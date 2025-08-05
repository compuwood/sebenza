package mad.apps.sabenza.data.model.payment

import com.google.gson.annotations.SerializedName


data class CreditCard(
        @SerializedName("part_of_number")
        val partOfNumber: String, // 510141
        @SerializedName("name_on_card")
        val nameOnCard: String, // John Doe
        @SerializedName("expiry_month")
        val expiryMonth: Int, // 12
        @SerializedName("expiry_year")
        val expiryYear: Int// 2020
) {
        @SerializedName("id")
        val id: Int? = null // 39
}
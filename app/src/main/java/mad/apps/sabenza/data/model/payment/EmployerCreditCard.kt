package mad.apps.sabenza.data.model.payment

import com.google.gson.annotations.SerializedName


data class EmployerCreditCard(
        @SerializedName("employer_id")
        val employer_id: Int, // 33
        @SerializedName("credit_card_id")
        val credit_card_id: Int, // 32
        @SerializedName("card_type")
        val card_type: String, // Business
        @SerializedName("is_default")
        val is_default: Boolean// true
)
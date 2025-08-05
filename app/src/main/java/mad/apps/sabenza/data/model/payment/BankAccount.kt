package mad.apps.sabenza.data.model.payment

import com.google.gson.annotations.SerializedName

data class BankAccount(
        @SerializedName("bank_name")
        val bankName: String,

        @SerializedName("branch_number")
        val branchNumber: String,

        @SerializedName("account_name")
        val accountName: String,

        @SerializedName("account_number")
        val accountNumber: String
) {
    @SerializedName("id")
    val id: String? = null

    @SerializedName("is_deleted")
    val isDeleted: Boolean? = null
}



package mad.apps.sabenza.data.model.payment

import com.google.gson.annotations.SerializedName

data class EmployeeBankAccount(
        @SerializedName("employee_id")
        val employeeId: String,
        @SerializedName("bank_account_id")
        val bankAccountId: String
) {
    @SerializedName("is_default")
    val isDefault: Boolean? = false
}

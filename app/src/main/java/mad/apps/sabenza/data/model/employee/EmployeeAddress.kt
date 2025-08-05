package mad.apps.sabenza.data.model.employee

import com.google.gson.annotations.SerializedName

data class EmployeeAddress(
        @SerializedName("employee_id") val employeeId: String,
        @SerializedName("address_id") val  addressId : String,
        @SerializedName("is_default") val isDefault : Boolean)
package mad.apps.sabenza.data.rpc.calls.signup

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
        @SerializedName("email") val email : String,
        @SerializedName("password") val password : String,
        @SerializedName("signup_coupon_code") val couponCode : String = "random"
)
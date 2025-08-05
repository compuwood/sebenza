package mad.apps.sabenza.data.rpc.calls

import com.google.gson.annotations.SerializedName

class ErrorResponse {

    @SerializedName("hint")
    var hint : String? = ""

    @SerializedName("details")
    var details : String? = ""

    @SerializedName("code")
    var code : String? = ""

    @SerializedName("message")
    var message : String? = ""

}
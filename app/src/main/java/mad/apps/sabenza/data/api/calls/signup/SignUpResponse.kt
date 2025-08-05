package mad.apps.sabenza.data.rpc.calls.signup

import com.google.gson.annotations.SerializedName
import mad.apps.sabenza.data.rpc.calls.me.Me

class SignUpResponse {

    @SerializedName("me")
    var me: Me? = null
    @SerializedName("token")
    var token: String? = null

}

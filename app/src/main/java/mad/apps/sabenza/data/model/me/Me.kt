package mad.apps.sabenza.data.rpc.calls.me

import com.google.gson.annotations.SerializedName
import mad.apps.sabenza.data.model.me.Role

class Me {

    @SerializedName("email")
    var email: String? = null
    @SerializedName("id")
    var id: Long? = null
    @SerializedName("role")
    var role: Role = Role.NONE
    @SerializedName("username")
    var username: String? = null

}

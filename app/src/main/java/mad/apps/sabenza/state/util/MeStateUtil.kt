package mad.apps.sabenza.state.util

import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.state.models.LoginModel
import zendesk.suas.GetState

object MeStateUtil {

    fun getMe(state : GetState): Me? {
        return state.state.getState(LoginModel::class.java)!!.me
    }
}
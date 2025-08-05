package mad.apps.sabenza.state.util

import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.state.models.LoginModel
import zendesk.suas.GetState

object RoleStateUtil {

    fun hasRole(state: GetState) : Boolean {
        val loginModel = state.state.getState(LoginModel::class.java)

        return (loginModel != null) && (loginModel.me != null)
    }

    fun roleChecker(state: GetState) : Role {
        val loginModel = state.state.getState(LoginModel::class.java)

        if (hasRole(state)) {
            return loginModel!!.me!!.role
        }
        return Role.NONE
    }

    fun getRole(state: GetState): Role {
        val loginModel = state.state.getState(LoginModel::class.java)

        return loginModel!!.me!!.role
    }
}
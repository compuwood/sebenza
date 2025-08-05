package mad.apps.sabenza.state.action

import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.framework.redux.Action

enum class LoginType {
    LOGIN, SIGNUP;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }}

data class LoginRequestAction(val username : String, val password: String) : Action<Pair<String, String>>("request_login", Pair(username, password))

data class LoginAsRoleRequestAction(val username : String, val password: String, val role: Role) : Action<Triple<String, String, Role>>("request_login_with_role", Triple(username, password, role))

data class SignUpRequestAction(val username: String, val password: String, val couponCode : String = "") : Action<Pair<String, String>>("request_signup", Pair(username, password))

data class SignUpAsRoleRequestAction(val username : String, val password: String, val couponCode: String = "", val role: Role) : Action<List<Any>>("request_signup_with_role", listOf(username, password, role, couponCode))

data class LoginOrSignUpSuccessAction(val me: Me, val type : LoginType) : Action<Pair<LoginType, Me>>(type.toString() + "_success", Pair(type, me))

data class LoginOrSignUpFailureAction(val failureMessage : String, val type: LoginType) : Action<Pair<LoginType, String>>(type.toString() + "_failed", Pair(type, failureMessage))

class AttemptAutoLoginAction() : Action<Any>("attempt_auto_login", Any())

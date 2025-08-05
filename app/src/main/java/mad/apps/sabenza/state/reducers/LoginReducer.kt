package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.state.action.*
import mad.apps.sabenza.state.models.LoginModel
import zendesk.suas.Action
import zendesk.suas.Reducer

class LoginReducer : Reducer<LoginModel>() {
    override fun reduce(state: LoginModel, action: Action<*>): LoginModel? {
        return when (action) {
            is LoginRequestAction -> {
                if (state.inProgress) {
                    state
                } else {
                    state.copy(
                            username = action.username,
                            password = action.password,
                            error = "",
                            hasError = false,
                            isSuccess = false,
                            me = null,
                            inProgress = true)
                }
            }

            is LoginAsRoleRequestAction ->{
                if (state.inProgress) {
                    state
                } else {
                    state.copy(
                            username = action.username,
                            password = action.password,
                            error = "",
                            hasError = false,
                            isSuccess = false,
                            me = null,
                            inProgress = true)
                }
            }

            is SignUpRequestAction -> {
                if (state.inProgress) {
                    state
                } else {
                    state.copy(
                            username = action.username,
                            password = action.password,
                            error = "",
                            hasError = false,
                            isSuccess = false,
                            me = null,
                            inProgress = true)
                }
            }

            is SignUpAsRoleRequestAction -> {
                if (state.inProgress) {
                    state
                } else {
                    state.copy(
                            username = action.username,
                            password = action.password,
                            error = "",
                            hasError = false,
                            isSuccess = false,
                            me = null,
                            inProgress = true)
                }
            }

            is LoginOrSignUpSuccessAction ->
                state.copy(
                        inProgress = false,
                        isLoggedIn = true,
                        hasError = false,
                        isSuccess = true,
                        me = action.me,
                        error = "")

            is LoginOrSignUpFailureAction ->
                state.copy(
                        inProgress = false,
                        isLoggedIn = false,
                        hasError = true,
                        isSuccess = false,
                        error = action.failureMessage,
                        me = null,
                        unsuccessfulAttempts = state.unsuccessfulAttempts + 1)

            else -> state
        }
    }

    override fun getInitialState(): LoginModel {
        return LoginModel()
    }
}
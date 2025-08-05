package mad.apps.sabenza.middleware.login

import io.reactivex.Single
import mad.apps.sabenza.data.api.SabenzaAPI
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.rpc.calls.login.LoginRequest
import mad.apps.sabenza.data.rpc.calls.login.LoginResponse
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.data.rpc.calls.role.EmployeeRoleRequest
import mad.apps.sabenza.data.rpc.calls.role.EmployerRoleRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpResponse
import mad.apps.sabenza.dependancy.OkHttpClientProvider
import mad.apps.sabenza.dependancy.PreferencesProvider
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.NullOrEmptyErrorMapper
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.service.MiddlewareService
import mad.apps.sabenza.middleware.rx.MiddlewareSingleObserver
import mad.apps.sabenza.state.action.*
import retrofit2.HttpException
import zendesk.suas.Action
import zendesk.suas.Continuation
import zendesk.suas.Dispatcher
import zendesk.suas.GetState

interface LoginService : MiddlewareService

class LoginServiceImpl(
        val clientProvider: OkHttpClientProvider,
        val sabenzaAPI: SabenzaAPI,
        val preferencesProvider: PreferencesProvider) : LoginService {


    private var currentMe: Me? = null
    private var currentEmail: String = ""
    private var currentPassword: String = ""
    private var loginPending = false

    override fun onAction(action: Action<*>, state: GetState, dispatcher: Dispatcher, continuation: Continuation) {
        when (action) {
            is LoginRequestAction -> {
                if (!loginPending && !action.username.isNullOrEmpty() && !action.password.isNullOrEmpty()) {
                    performLogin(dispatcher, action.username, action.password)
                    continuation.next(action)
                }
            }

            is LoginAsRoleRequestAction -> {
                if (!loginPending && !action.username.isNullOrEmpty() && !action.password.isNullOrEmpty()) {
                    performLogin(dispatcher, action.username, action.password, action.role)
                    continuation.next(action)
                }
            }

            is SignUpRequestAction -> {
                if (!loginPending) {
                    performSignUp(dispatcher, action.username, action.password, action.couponCode)
                    continuation.next(action)
                }
            }

            is SignUpAsRoleRequestAction -> {
                if (!loginPending) {
                    performSignUpWithRole(dispatcher, action.username, action.password, action.couponCode, action.role)
                    continuation.next(action)
                }
            }

            is AttemptAutoLoginAction -> {
                if (!loginPending) {
                    checkForActiveToken(dispatcher)
                    continuation.next(action)
                }
            }

            else -> continuation.next(action)
        }

    }

    private fun checkForActiveToken(dispatcher: Dispatcher) {
        sabenzaAPI.fetchMe().compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<Me>(dispatcher) {
                    override fun onSuccess(me: Me) {
                        dispatcher.dispatch(LoginOrSignUpSuccessAction(me, LoginType.LOGIN))
                    }

                    override fun onError(e: Throwable) {
                        clientProvider.resetToken()
                        if (e is HttpException) {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message(), LoginType.LOGIN))
                        } else {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message!!, LoginType.LOGIN))
                        }
                    }
                })
    }

    private fun performSignUpWithRole(dispatcher: Dispatcher, username: String, password: String, couponCode: String, role: Role) {
        val roleRequest = if (role == Role.EMPLOYER) { EmployerRoleRequest() } else { EmployeeRoleRequest() }

        attemptSignUp(email = username, password = password, couponCode = couponCode)
                .flatMap { sabenzaAPI.switchRole(roleRequest) }
                .map {
                    clientProvider.token = it
                    it
                }
                .flatMap { sabenzaAPI.fetchMe() }
                .map {
                    currentMe = it
                    it
                }
                .compose(NetworkTransformer<Me>())
                .subscribe(object : EnhancedSingleObserver<Me>() {
                    override fun onSuccess(t: Me) {
                        dispatcher.dispatch(LoginOrSignUpSuccessAction(t, LoginType.SIGNUP))
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message(), LoginType.SIGNUP))
                        } else {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message!!, LoginType.SIGNUP))
                        }
                    }
                })
    }

    private fun performLogin(dispatcher: Dispatcher, username: String, password: String, role: Role) {
        val roleRequest = if (role == Role.EMPLOYER) { EmployerRoleRequest() } else { EmployeeRoleRequest() }

        attemptLogin(email = username, password = password)
                .flatMap {
                    sabenzaAPI.switchRole(roleRequest)
                }
                .map {
                    clientProvider.token = it
                    it
                }
                .flatMap {
                    sabenzaAPI.fetchMe()
                }
                .map {
                    currentMe = it
                    it
                }
                .compose(NetworkTransformer<Me>())
                .subscribe(object : EnhancedSingleObserver<Me>() {
                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message(), LoginType.LOGIN))
                        } else {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message!!, LoginType.LOGIN))
                        }
                    }

                    override fun onSuccess(t: Me) {
                        dispatcher.dispatch(LoginOrSignUpSuccessAction(t, LoginType.LOGIN))
                    }

                })
    }

    private fun performLogin(dispatcher: Dispatcher, username: String, password: String) {
        clientProvider.resetToken()
        attemptLogin(email = username, password = password)
                .compose(NetworkTransformer<Me>())
                .subscribe(object : EnhancedSingleObserver<Me>() {
                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message(), LoginType.LOGIN))
                        } else {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message!!, LoginType.LOGIN))
                        }
                    }

                    override fun onSuccess(t: Me) {
                        dispatcher.dispatch(LoginOrSignUpSuccessAction(t, LoginType.LOGIN))
                    }

                })
    }

    fun attemptSignUp(password: String, email: String, couponCode: String): Single<Me> {
        clientProvider.resetToken()
        return sabenzaAPI.signup(SignUpRequest(password = password, email = email, couponCode = couponCode))
                .map(NullOrEmptyErrorMapper<SignUpResponse>())
                .map {
                    if (it.me != null && it.token != null) {
                        currentMe = it.me
                        clientProvider.token = it.token!!
                        saveLoginDetails(email, password)
                    } else {
                        throw Exception("Invalid Signup Response")
                    }

                    it
                }.map {
            it.me
        }
    }

    fun attemptLogin(email: String, password: String): Single<Me> {
        clientProvider.resetToken()
        return sabenzaAPI.login(LoginRequest(email = email, password = password))
                .map(NullOrEmptyErrorMapper<LoginResponse>())
                .map {
                    if (it.me != null && it.token != null) {
                        currentMe = it.me
                        clientProvider.token = it.token!!
                        saveLoginDetails(email, password)
                    } else {
                        throw Exception("Invalid Login Response")
                    }

                    it
                }.map {
            it.me
        }
    }

    private fun saveLoginDetails(email: String, password: String) {
        currentEmail = email
        currentPassword = password
        preferencesProvider.storeString(SETTING_KEY_EMAIL, email)
        preferencesProvider.storeString(SETTING_KEY_PASSWORD, password)
    }

    fun attemptAutoLogin(): Single<Me> {
        if (hasLoginDetails()) {
            return sabenzaAPI.login(LoginRequest(
                    email = preferencesProvider.getString(SETTING_KEY_EMAIL),
                    password = preferencesProvider.getString(SETTING_KEY_PASSWORD)))
                    .map {
                        if (it.me != null && it.token != null) {
                            currentMe = it.me
                            clientProvider.token = it.token!!
                        } else {
                            throw Exception("Invalid Login Response")
                        }

                        it
                    }.map {
                it.me
            }
        }
        return Single.error(Exception("No saved login details, can't attempt auto login."))
    }

    fun hasLoginDetails(): Boolean {
        return ((preferencesProvider.hasString(SETTING_KEY_EMAIL) && preferencesProvider.hasString(SETTING_KEY_PASSWORD)) || (currentEmail.isNotEmpty() && currentPassword.isNotEmpty()))
    }

    private fun performSignUp(dispatcher: Dispatcher, username: String, password: String, couponCode: String) {
        clientProvider.resetToken()
        attemptSignUp(email = username, password = password, couponCode = couponCode)
                .compose(NetworkTransformer<Me>())
                .subscribe(object : EnhancedSingleObserver<Me>() {
                    override fun onSuccess(t: Me) {
                        dispatcher.dispatch(LoginOrSignUpSuccessAction(t, LoginType.SIGNUP))
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message(), LoginType.SIGNUP))
                        } else {
                            dispatcher.dispatch(LoginOrSignUpFailureAction(e.message!!, LoginType.SIGNUP))
                        }
                    }
                })
    }
}

const val SETTING_KEY_EMAIL = "SETTING_KEY_EMAIL"
const val SETTING_KEY_PASSWORD = "SETTING_KEY_PASSWORD"
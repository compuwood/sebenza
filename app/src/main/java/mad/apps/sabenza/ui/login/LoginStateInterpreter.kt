package mad.apps.sabenza.ui.login

import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleSource
import io.reactivex.subjects.SingleSubject
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.state.action.LoginRequestAction
import mad.apps.sabenza.state.models.*
import mad.apps.sabenza.ui.employee.jobsfeed.JobsFeedScreen
import mad.apps.sabenza.ui.employee.signup.addskills.AddSkillsScreen
import mad.apps.sabenza.ui.employer.signup.address.YourAddressScreen
import mad.apps.sabenza.ui.employer.signup.createprofile.CreateProfileScreen
import mad.apps.sabenza.ui.employer.signup.paymentinfo.PaymentInfoScreen
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import zendesk.suas.State
import zendesk.suas.Store
import zendesk.suas.Subscription
import java.util.concurrent.TimeUnit

class LoginStateInterpreter(val store: Store) {

    val MAX_TIME_MS = 5000
    val viewControllerSubject: SingleSubject<BaseViewController> = SingleSubject.create()

    val subscription: Subscription

    var hasLogin = false
    var hasRole = false
    var role = Role.NONE

    var hasEmployeeDetails = false
    var hasEmployeeAddress = false
    var hasEmployeeSkills = false
    var hasEmployeePaymentDetails = false

    var hasEmployerDetails = false
    var hasEmployerAddress = false
    var hasEmployerPaymentDetails = false

    init {
        subscription = store.addListener { updateLoginState(it) }
    }

    var previousErrorModel: ErrorModel? = null

    private fun updateLoginState(state: State) {
        val apiModel = state.getState(ErrorModel::class.java)
        updateAPIErrors(apiModel)

        val loginModel = state.getState(LoginModel::class.java)
        val employeeModel = state.getState(EmployeeModel::class.java)
        val employerModel = state.getState(EmployerModel::class.java)
        val paymentModel = state.getState(PaymentModel::class.java)
        val addressModel = state.getState(AddressModel::class.java)

        loginModel?.let { it -> processLoginModel(it) }

        if (!hasRole) {
            attemptToCompleteLogin(state, viewControllerSubject, { timerExpired() })
            return
        }

        if (role == Role.EMPLOYER) {
            employerModel?.let { it -> processEmployerModel(it) }
            addressModel?.let { it -> processAddressModel(it) }
            paymentModel?.let { it -> processPaymentModel(it) }
        } else if (role == Role.EMPLOYEE) {
            employeeModel?.let { it -> processEmployeeModel(it) }
            addressModel?.let { it -> processAddressModel(it) }
            paymentModel?.let { _ -> processPaymentModel(paymentModel) }
        }

        attemptToCompleteLogin(state, viewControllerSubject, { timerExpired() })
    }

    private fun attemptToCompleteLogin(state: State, singleObserver: SingleObserver<in BaseViewController>, timeExpired: () -> Boolean): Any {
        if (role == Role.EMPLOYER) {

            if (hasRole && hasLogin
                    && hasEmployerAddress
                    && hasEmployerDetails
                    && hasEmployerPaymentDetails) {
                return singleObserver.onSuccess(ViewProjectsScreen.provideViewController())

            }

            if (timeExpired()) {
                if (!hasLogin || !hasRole) {
                    val error = state.getState(LoginModel::class.java)?.error
                    return singleObserver.onError(LoginFailedError(error!!))
                }

                if (!hasEmployerDetails) {
                    return singleObserver.onSuccess(CreateProfileScreen.provideViewController())
                }

                if (!hasEmployerAddress) {
                    return singleObserver.onSuccess(YourAddressScreen.provideViewController())
                }

                if (!hasEmployerPaymentDetails) {
                    return singleObserver.onSuccess(PaymentInfoScreen.provideViewController())
                }

                return singleObserver.onSuccess(ViewProjectsScreen.provideViewController())
            }
        } else if (role == Role.EMPLOYEE) {

            if (hasRole && hasLogin
                    && hasEmployeeDetails
                    && hasEmployeeSkills
                    && hasEmployeePaymentDetails) {
                return singleObserver.onSuccess(JobsFeedScreen.provideViewController())
            }

            if (timeExpired()) {
                if (!hasLogin || !hasRole) {
                    val error = state.getState(LoginModel::class.java)?.error
                    return singleObserver.onError(LoginFailedError(error!!))
                }

                if (!hasEmployeeDetails) {
                    return singleObserver.onSuccess(CreateProfileScreen.provideViewController())
                }

                if (!hasEmployeeSkills) {
                    return singleObserver.onSuccess(AddSkillsScreen.provideViewController())
                }

                if (!hasEmployeePaymentDetails) {
                    return singleObserver.onSuccess(PaymentInfoScreen.provideViewController())
                }

                return singleObserver.onSuccess(JobsFeedScreen.provideViewController())
            }
        }

        return if (timeExpired() && (!hasLogin || !hasRole)) {
            val error = state.getState(LoginModel::class.java)?.error
            singleObserver.onError(LoginFailedError(error!!))
        } else if (timeExpired()) {
            singleObserver.onError(LoginFailedError("A Timeout Occurred, Please Try Again"))
        } else {
            false
        }
    }

    private fun timerExpired() = System.currentTimeMillis() - timeStart > MAX_TIME_MS

    private fun processPaymentModel(paymentModel: PaymentModel) {
        if (role == Role.EMPLOYER) {
            hasEmployerPaymentDetails = paymentModel.hasCards()
        } else if (role == Role.EMPLOYEE) {
            hasEmployeePaymentDetails = paymentModel.hasBankAccounts()
        }
    }


    private fun processEmployeeModel(employeeModel: EmployeeModel) {
        hasEmployeeDetails = employeeModel.hasEmployee
        hasEmployeeSkills = employeeModel.skills.isNotEmpty()
    }

    private fun processAddressModel(addressModel: AddressModel) {
        if (role == Role.EMPLOYER) {
            hasEmployerAddress = addressModel.employerAddress.isNotEmpty()
        } else if (role == Role.EMPLOYEE) {
            hasEmployeeAddress = addressModel.employeeAddress.isNotEmpty()
        }
    }

    private fun processEmployerModel(employerModel: EmployerModel) {
        hasEmployerDetails = employerModel.hasEmployer
    }

    private fun processLoginModel(loginModel: LoginModel) {
        hasRole = (loginModel.me?.role != null)
        role = if (loginModel.me?.role != null) {
            loginModel.me.role
        } else {
            Role.NONE
        }
        hasLogin = loginModel.isSuccess
    }

    private fun updateAPIErrors(errorModel: ErrorModel?) {
        this.previousErrorModel = errorModel
    }

    var timeStart = 0L

    fun dispatchLoginAndAwaitNextScreenOrFailure(email: String, password: String): Single<BaseViewController> {
        timeStart = System.currentTimeMillis()
        store.dispatch(LoginRequestAction(username = email, password = password))
        subscription.addListener()
        return viewControllerSubject
                .timeout(MAX_TIME_MS + 100L, TimeUnit.MILLISECONDS, SingleSource {
                    val result = attemptToCompleteLogin(store.state, it, { true })
                    if (result is Boolean && result == false) {
                        it.onError(LoginFailedError("A Timeout Occurred, Please Try Again"))
                    }
                })
                .doOnError { subscription.removeListener() }
                .doOnSuccess { subscription.removeListener() }
    }

    fun dispose() {
        subscription.removeListener()
    }


}

class LoginFailedError(error: String) : Throwable(error)

package mad.apps.sabenza.api.util

import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BASE_PASSWORD
import mad.apps.sabenza.BASE_USERNAME
import mad.apps.sabenza.data.api.SabenzaAPI
import mad.apps.sabenza.data.rpc.calls.login.LoginRequest
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.data.rpc.calls.role.EmployeeRoleRequest
import mad.apps.sabenza.data.rpc.calls.role.EmployerRoleRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpResponse
import mad.apps.sabenza.dependancy.OkHttpClientProvider
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.framework.rx.NetworkTransformer
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class BaseTestUtil {

    @Inject lateinit var httpProvider : OkHttpClientProvider
    @Inject lateinit var sabenzaAPI : SabenzaAPI

    fun successfullyLoginAsEmployer() {
        val me = succesfullyLogin(BASE_USERNAME + "employer", BASE_PASSWORD)

        val testSubscriber = TestObserver<String>()

        sabenzaAPI.switchRole(EmployerRoleRequest())
                .compose(NetworkTransformer())
                .map {
                    if (!it.isNullOrEmpty()) {
                        httpProvider.token = it
                    }
                    it
                }
                .blockingGet()
    }

    fun successfullyLogin() {
        val me = succesfullyLogin(BASE_USERNAME, BASE_PASSWORD)

        val testSubscriber = TestObserver<String>()

        sabenzaAPI.switchRole(EmployeeRoleRequest())
                .compose(NetworkTransformer())
                .map {
                    if (!it.isNullOrEmpty()) {
                        httpProvider.token = it
                    }
                    it
                }
                .blockingGet()
    }


    fun successfullyRandomSignUpAsEmployer() {
        val me = randomSignUp(BASE_USERNAME, BASE_PASSWORD)

        val testSubscriber = TestObserver<String>()

        sabenzaAPI.switchRole(EmployerRoleRequest())
                .compose(NetworkTransformer())
                .map {
                    httpProvider.token = it
                    it
                }
                .blockingGet()

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()
    }

    private fun randomSignUp(username : String, password : String) {
        sabenzaAPI.signup(SignUpRequest(
                email = username + TestStringHelper.nextPrintableString(4) + "@email.com",
                password = password))
                .compose(NetworkTransformer<SignUpResponse>())
                .map {
                    httpProvider.token = it.token!!
                    it
                }
                .blockingGet()
    }

    private fun succesfullyLogin(username : String, password : String) {
        sabenzaAPI.login(LoginRequest(
                email = username + "@email.com",
                password = password))
                .map {
                    httpProvider.token = it.token!!
                    it
                }
                .flatMap { sabenzaAPI.switchRole(EmployeeRoleRequest()) }
                .map {
                    httpProvider.token = it
                    it
                }
                .flatMap { sabenzaAPI.fetchMe() }
                .compose(NetworkTransformer<Me>())
                .delay(750, TimeUnit.MILLISECONDS)
                .blockingGet()
    }

}
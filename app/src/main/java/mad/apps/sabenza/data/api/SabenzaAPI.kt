package mad.apps.sabenza.data.api

import io.reactivex.Single
import mad.apps.sabenza.data.rpc.calls.login.LoginRequest
import mad.apps.sabenza.data.rpc.calls.login.LoginResponse
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.data.rpc.calls.role.NewRoleRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpRequest
import mad.apps.sabenza.data.rpc.calls.signup.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SabenzaAPI {

    @POST("rpc/me")
    fun fetchMe(): Single<Me>

    @POST("rpc/signup")
    fun signup(@Body request: SignUpRequest): Single<SignUpResponse>

    @POST("rpc/login")
    fun login(@Body request: LoginRequest): Single<LoginResponse>

    @POST("rpc/switchrole")
    fun switchRole(@Body request: NewRoleRequest): Single<String>

    @POST("rpc/refresh_token")
    fun refreshTokenPost(): Single<String>

}
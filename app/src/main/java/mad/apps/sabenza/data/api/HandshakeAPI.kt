package mad.apps.sabenza.data.api

import io.reactivex.Completable
import io.reactivex.Single
import mad.apps.sabenza.data.model.handshake.Handshake
import retrofit2.http.*

interface HandshakeAPI {

    @DELETE("handshakes")
    fun handshakesDelete(@Query("id") id: EqualsQueryString): Completable

    @GET("handshakes")
    @Headers("Accept: application/json")
    fun getHandshakes(): Single<List<Handshake>>


    @PATCH("handshakes")
    @FormUrlEncoded
    fun handshakesPatch(@Body handshake: Handshake): Single<Handshake>

    @POST("handshakes")
    fun handshakesPost(@Body handshake: Handshake): Single<Handshake>

}
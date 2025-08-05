package mad.apps.sabenza.data.api

import io.reactivex.Single
import mad.apps.sabenza.data.model.address.Address
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AddressAPI {

    @GET("addresses?mine=eq.true")
    @Headers("Accept: application/json")
    fun listAddresses() : Single<List<Address>>

    @POST("addresses")
    fun addAddress(@Body address: Address) : Single<Address>

}
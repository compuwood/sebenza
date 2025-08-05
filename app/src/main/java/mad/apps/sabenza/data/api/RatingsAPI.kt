package mad.apps.sabenza.data.api

import io.reactivex.Single
import mad.apps.sabenza.data.model.rating.Rating
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RatingsAPI {

    @GET("ratings")
    @Headers("Accept: application/json")
    fun getRatings() : Single<Rating>

    @POST("ratings?select=rating,review")
    fun postRatingForEmployee(@Body rating : Rating) : Single<Rating>


}
package mad.apps.sabenza.data.api

import io.reactivex.Single
import mad.apps.sabenza.data.model.picture.SebenzaPicture
import retrofit2.http.*

interface PicturesAPI {

    @POST("pictures")
    @FormUrlEncoded
    fun createPicture(@Field("caption") caption: String, @Field("uri") uri : String) : Single<SebenzaPicture>

    @GET("pictures?select=id")
    fun getPictureForId(@Query("id") pictureId: EqualsQueryString) : Single<SebenzaPicture>
}
package mad.apps.sabenza.data.api

import io.reactivex.Single
import mad.apps.sabenza.data.model.messaging.SebenzaMessage
import mad.apps.sabenza.data.model.messaging.MessageBody
import mad.apps.sabenza.data.model.messaging.MessageHeader
import retrofit2.http.*

interface MessagingAPI {

    @POST("message_headers?select=id,parent_id,send_date,related_employer_id,related_job_id,related_employee_id,is_read")
    fun addNewMessageHeader(@Body messageHeader: MessageHeader): Single<MessageHeader>

    @GET("message_headers?select=id,parent_id,send_date,related_employer_id,related_job_id,related_employee_id,is_read?is_to_me=eq.1")
    @Headers("Accept: application/json")
    fun fetchMessageHeadersToUser(): Single<List<MessageHeader>>

    @GET("message_headers?select=id,parent_id,send_date,related_employer_id,related_job_id,related_employee_id,is_read?is_from_me=eq.1")
    @Headers("Accept: application/json")
    fun fetchMessageHeadersFromUser(): Single<List<MessageHeader>>

    @POST("message_bodies?select=header_id,body")
    @FormUrlEncoded
    fun addMessageBodyToHeader(@Field("header_id") headerId: String, @Field("body") body: String): Single<MessageBody>

    @GET("messages")
    @Headers("Accept: application/json")
    fun getMessages(): Single<List<SebenzaMessage>>

    @GET("message_bodies?select=header_id,body,is_from_me,is_to_me")
    @Headers("Accept: application/json")
    fun fetchAllMessagesForHeader(@Query("header_id") headerId: EqualsQueryString): Single<List<MessageBody>>

}
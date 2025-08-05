package mad.apps.sabenza.data.api

import io.reactivex.Single
import mad.apps.sabenza.data.model.employee.EmployeeAddress
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import retrofit2.http.*

interface EmployeeAPI {
    @POST("employees")
    @FormUrlEncoded
    fun addEmployee(
            @Field("first_name") firstName: String,
            @Field("last_name") lastName: String,
            @Field("email") email: String,
            @Field("phone_number") phoneNumber: String,
            @Field("picture_id") pictureId : String = ""): Single<Employee>



    @GET("employees")
    fun fetchEmployeeForCurrentUser(): Single<Employee>

    @PATCH("employees")
    @FormUrlEncoded
    fun updateEmployee(
            @Field("first_name") firstName: String,
            @Field("last_name") lastName: String,
            @Field("email") email: String,
            @Field("phone_number") phoneNumber: String,
            @Field("picture_id") pictureId : String = ""): Single<Employee>

    @GET("employee_addresses")
    @Headers("Accept: application/json")
    fun fetchAddressesForCurrentEmployee() : Single<List<EmployeeAddress>>

    @POST("employee_addresses")
    fun addAddressToEmployee(@Body employeeAddress: EmployeeAddress) : Single<Any>



}
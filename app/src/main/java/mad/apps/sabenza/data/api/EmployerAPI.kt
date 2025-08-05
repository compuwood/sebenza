package mad.apps.sabenza.data.api

import io.reactivex.Single
import mad.apps.sabenza.data.model.employer.Employer
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface EmployerAPI {
    @POST("employers")
    fun addEmployer(@Body employer : Employer) : Single<Employer>

    @GET("employers?select=id,first_name,last_name,email,about_me,phone_number,about_company,picture_id,rating_count,rating_avg,rating_sum")
    fun fetchEmployer(): Single<Employer>

    @PATCH("employers")
    fun updateEmployer(@Body employer: Employer) : Single<Employer>

}
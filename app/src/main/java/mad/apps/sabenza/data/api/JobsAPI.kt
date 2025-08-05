package mad.apps.sabenza.data.api

import io.reactivex.Completable
import io.reactivex.Single
import mad.apps.sabenza.data.model.jobs.*
import retrofit2.http.*

interface JobsAPI {

    @GET("jobs?select=id,skill_id,quantity,start_date,end_date,duration,description,project_id,rate,address_id,employer_id")
    @Headers("Accept: application/json")
    fun listJobs(): Single<List<Job>>

    @GET("jobs?select=id,skill_id,quantity,start_date,end_date,duration,description,project_id,rate,address_id,employer_id")
    @Headers("Accept: application/json")
    fun listJobsForEmployeeSkill(@Query("skill_id") skillID : EqualsQueryString): Single<List<Job>>

    @GET("jobs")
    @Headers("Accept: application/json")
    fun listJobsForProject(@Query("project_id") projectId : EqualsQueryString) : Single<List<Job>>

    @GET("jobs")
    @Headers("Accept: application/json")
    fun listJobsForProjects(@Query("project_id") projectId : EqualsQueryList) : Single<List<Job>>

    @POST("jobs")
    fun addJob(@Body job : Job) : Single<Job>

    @GET("projects")
    @Headers("Accept: application/json")
    fun listProjects() : Single<List<Project>>

    @GET("projects")
    @Headers("Accept: application/json")
    fun listProjectsForEmployer(@Query("employer_id") employerId: EqualsQueryString) : Single<List<Project>>

    @POST("projects")
    fun addProject(@Body project : Project) : Single<Project>

    @PATCH("projects")
    @FormUrlEncoded
    fun changeProjectState(@Query("id") projectId : EqualsQueryString, @Field("state") state : ProjectStateEnum) : Single<Project>

    @POST("job_employees?select=job_id")
    fun applyForJobAsEmployee(@Body jobEmployee: JobEmployee) : Single<JobEmployee>

    @GET("job_employees")
    @Headers("Accept: application/json")
    fun getEmployeeJobs(): Single<List<JobEmployee>>

    @PATCH("job_employees")
    @FormUrlEncoded
    fun changeStatusOfEmployeeJob(
            @Query("job_id") jobId: EqualsQueryString,
            @Field("state") state: EmployeeJobStateEnum,
            @Field("state_change_reason") reason : String) : Single<JobEmployee>

}

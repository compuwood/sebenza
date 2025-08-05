package mad.apps.sabenza.data.api

import io.reactivex.Single
import mad.apps.sabenza.data.model.employee.EmployeeSkill
import mad.apps.sabenza.data.rpc.calls.employee.Skill
import retrofit2.http.*

interface SkillsAPI {

    @POST("skills?select=id,description")
    @FormUrlEncoded
    fun addSkill(
            @Field("description") descripton: String
    ): Single<Skill>

    @GET("skills?select=id,description")
    @Headers("Accept: application/json")
    fun fetchAllSkills(): Single<List<Skill>>

    //Add a skill to an employee
    @POST("employee_skills")
    @FormUrlEncoded
    fun addSkillToEmployee(
            @Field("employee_id") employeeId: String,
            @Field("skill_id") skillId: String,
            @Field("years_experience") yearsExperience: String

    ): Single<EmployeeSkill>

    //Get All skills for an Employee
    @GET("employee_skills")
    @Headers("Accept: application/json")
    fun fetchSkillsForEmployee(): Single<List<EmployeeSkill>>

    //Remove Skill From an Employee
    @DELETE("employee_skills")
    fun deleteSkillFromAnEmployee(@Query("skill_id") skillId: String): Single<Any>

}
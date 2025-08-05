package mad.apps.sabenza.data.rpc.calls.role

import com.google.gson.annotations.SerializedName

abstract class NewRoleRequest(
        @SerializedName("newrole")
        var newrole : String
)

class EmployeeRoleRequest : NewRoleRequest("employee")

class EmployerRoleRequest : NewRoleRequest("employer")
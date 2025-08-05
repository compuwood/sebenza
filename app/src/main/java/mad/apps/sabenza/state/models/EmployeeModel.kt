package mad.apps.sabenza.state.models

import mad.apps.sabenza.data.model.employee.EmployeeSkill
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.redux.RxModel

data class EmployeeModel(
        val employee: Employee? = null,
        val hasEmployee : Boolean = false,
        val skills : List<EmployeeSkill> = emptyList()) : RxModel()
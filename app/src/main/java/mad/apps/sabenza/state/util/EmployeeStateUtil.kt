package mad.apps.sabenza.state.util

import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.state.models.EmployeeModel
import zendesk.suas.GetState

object EmployeeStateUtil {

    fun hasEmployee(state : GetState) : Boolean {
        val employeeModel = state.state.getState(EmployeeModel::class.java)

        if (employeeModel != null) {
            return employeeModel.hasEmployee
        }

        return false
    }

    fun getEmployee(state: GetState) : Employee? {
        val employeeModel = state.state.getState(EmployeeModel::class.java)
        return employeeModel?.employee
    }
}
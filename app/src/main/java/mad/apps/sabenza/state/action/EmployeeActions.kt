package mad.apps.sabenza.state.action

import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.redux.Action

class EmployeeUpdatedAction(val employee: Employee) : Action<Employee>("employee_updated", employee)

class NoEmployeeLinkedAction() : Action<Any>("no_employee_linked", Any())

class AddOrUpdateEmployeeAction(val employee: Employee) : Action<Employee>("add_or_update_employee", employee)
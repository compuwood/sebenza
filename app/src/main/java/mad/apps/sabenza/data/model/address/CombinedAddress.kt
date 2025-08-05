package mad.apps.sabenza.data.model.address

import mad.apps.sabenza.data.model.employee.EmployeeAddress

data class CombinedAddress(
        val employeeAddress: EmployeeAddress,
        val address: Address)
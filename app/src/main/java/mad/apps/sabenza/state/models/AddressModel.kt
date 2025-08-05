package mad.apps.sabenza.state.models

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.address.CombinedAddress
import mad.apps.sabenza.data.model.employee.EmployeeAddress
import mad.apps.sabenza.framework.redux.RxModel

data class AddressModel (
        val employeeAddress: List<CombinedAddress> = emptyList(),
        val selectedEmployeeAddress: CombinedAddress? = null,
        val availableBackendAddress : List<Address> = emptyList(),
        val employerAddress : List<Address> = emptyList(),
        val selectedEmployerAddress : Address? = null) : RxModel()
package mad.apps.sabenza.state.action

import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.employee.EmployeeAddress
import mad.apps.sabenza.framework.redux.Action

class AddAddressAndLinkAction(val address : Address) : Action<Address>("add_and_link_address", address)

class AddressSuccessfullyLinkedAction : Action<Any>("address_successfully_linked", Any())

class AddressesUpdatedAction(val addresses : List<Address>, val employeeAddress: List<EmployeeAddress>) : Action<Pair<List<Address>, List<EmployeeAddress>>> ("addresses_updated", Pair(addresses, employeeAddress))

class EmployerAddressesUpdatedAction(val addresses: List<Address>) : Action<List<Address>>("employer_address_updated", addresses)
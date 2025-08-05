package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.data.model.address.CombinedAddress
import mad.apps.sabenza.framework.redux.Reducer
import mad.apps.sabenza.state.action.AddressesUpdatedAction
import mad.apps.sabenza.state.action.EmployerAddressesUpdatedAction
import mad.apps.sabenza.state.models.AddressModel
import zendesk.suas.Action

class AddressReducer : Reducer<AddressModel>() {
    override fun reduce(state: AddressModel, action: Action<*>): AddressModel? {
        return when (action) {
            is AddressesUpdatedAction -> {
                val combinedList = buildCombinedList(action)
                state.copy(
                        availableBackendAddress = action.addresses,
                        employeeAddress = combinedList,
                        selectedEmployeeAddress = selectDefault(combinedList),
                        employerAddress = emptyList(),
                        selectedEmployerAddress = null
                )
            }

            is EmployerAddressesUpdatedAction -> {
                state.copy(
                        availableBackendAddress = action.addresses,
                        employerAddress = action.addresses,
                        employeeAddress = emptyList(),
                        selectedEmployeeAddress = null,
                        selectedEmployerAddress =
                        if (action.addresses.isNotEmpty()) {
                            action.addresses.last()
                        } else {
                            null
                        }
                )
            }

            else -> state
        }
    }

    fun buildCombinedList(action: AddressesUpdatedAction): List<CombinedAddress> {
        val addressList: MutableList<CombinedAddress> = ArrayList()

        for (employeeAddress in action.employeeAddress) {
            for (address in action.addresses) {
                if (employeeAddress.addressId == address.id) {
                    addressList.add(CombinedAddress(employeeAddress, address))
                    break
                }
            }
        }

        return addressList.toList()
    }

    private fun selectDefault(combinedList: List<CombinedAddress>): CombinedAddress? {
        val defaultList = combinedList.filter {
            it.employeeAddress.isDefault
        }

        if (defaultList.isNotEmpty()) {
            return defaultList.first()
        } else if (combinedList.isNotEmpty()) {
            return combinedList.first()
        } else {
            return null
        }
    }

    override fun getInitialState(): AddressModel {
        return AddressModel()
    }
}
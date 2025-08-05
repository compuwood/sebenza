package mad.apps.sabenza.middleware.address

import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import mad.apps.sabenza.data.api.AddressAPI
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.employee.EmployeeAddress
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.service.MiddlewareService
import mad.apps.sabenza.middleware.MiddlewareError
import mad.apps.sabenza.middleware.rx.MiddlewareSingleObserver
import mad.apps.sabenza.state.action.*
import mad.apps.sabenza.state.models.LoginModel
import mad.apps.sabenza.state.util.EmployeeStateUtil
import mad.apps.sabenza.state.util.RoleStateUtil
import zendesk.suas.Action
import zendesk.suas.Continuation
import zendesk.suas.Dispatcher
import zendesk.suas.GetState

interface AddressService : MiddlewareService

class AddressServiceImpl(val addressAPI: AddressAPI, val employeeAPI: EmployeeAPI, val employerAPI: EmployerAPI) : AddressService {

    override fun onAction(action: Action<*>, state: GetState, dispatcher: Dispatcher, continuation: Continuation) {
        when (action) {
            is AddAddressAndLinkAction -> {
                if (RoleStateUtil.hasRole(state)) {
                    attemptToAddAndLinkAddress(dispatcher, action, state, RoleStateUtil.getRole(state))
                    continuation.next(action)
                } else {
                    continuation.next(NoEmployeeLinkedAction())
                }
            }

            is AddressSuccessfullyLinkedAction -> {
                refreshAddresses(dispatcher, state.state.getState(LoginModel::class.java)!!.me!!)
                continuation.next(action)
            }

            is LoginOrSignUpSuccessAction -> {
                refreshAddresses(dispatcher, action.me)
                continuation.next(action)
            }

            else -> continuation.next(action)
        }
    }

    private fun attemptToAddAndLinkAddress(dispatcher: Dispatcher, action: AddAddressAndLinkAction, state: GetState, role: Role) {
        if (role == Role.EMPLOYEE) {
            addressAPI.addAddress(action.address)
                    .flatMap {
                        val currentRole = RoleStateUtil.roleChecker(state)

                        if (currentRole == Role.EMPLOYEE) {
                            return@flatMap employeeAPI.addAddressToEmployee(
                                    EmployeeAddress(
                                            employeeId = EmployeeStateUtil.getEmployee(state)!!.id!!,
                                            addressId = it.id!!,
                                            isDefault = false
                                    )
                            )
                        } else if (currentRole == Role.EMPLOYER) {
                            return@flatMap Single.just(1)
                        } else {
                            return@flatMap Single.just(1)
                        }
                    }
                    .flatMap {
                        refreshEmployeeAddress()
                    }
                    .compose(NetworkTransformer())
                    .subscribe(object : MiddlewareSingleObserver<Pair<List<Address>, List<EmployeeAddress>>>(dispatcher) {
                        override fun handleError(e: Throwable): Boolean {
                            return false
                        }

                        override fun onSuccess(t: Pair<List<Address>, List<EmployeeAddress>>) {
                            dispatcher.dispatch(AddressesUpdatedAction(t.first, t.second))
                        }
                    })
        } else if (role == Role.EMPLOYER) {
            addressAPI.addAddress(action.address)
                    .compose(NetworkTransformer())
                    .subscribe(object : MiddlewareSingleObserver<Address>(dispatcher) {
                        override fun handleError(e: Throwable): Boolean {
                            return false
                        }

                        override fun onSuccess(t: Address) {
                            dispatcher.dispatch(EmployerAddressesUpdatedAction(listOf(t)))
                        }
                    })
        }
    }

    private fun refreshEmployeeAddress(): Single<Pair<List<Address>, List<EmployeeAddress>>> {
        return addressAPI.listAddresses().zipWith(employeeAPI.fetchAddressesForCurrentEmployee())
    }

    private fun refreshEmployerAddress(): Single<List<Address>> {
        return addressAPI.listAddresses()
    }

    private fun refreshAddresses(dispatcher: Dispatcher, me: Me) {
        if (me.role == Role.EMPLOYEE) {
            refreshEmployeeAddress()
                    .compose(NetworkTransformer())
                    .subscribe(object : MiddlewareSingleObserver<Pair<List<Address>, List<EmployeeAddress>>>(dispatcher, MiddlewareError.FAILED_TO_REFRESH_ADDRESSES) {
                        override fun onSuccess(t: Pair<List<Address>, List<EmployeeAddress>>) {
                            dispatcher.dispatch(AddressesUpdatedAction(t.first, t.second))
                        }
                    })
        } else if (me.role == Role.EMPLOYER) {
            refreshEmployerAddress()
                    .compose(NetworkTransformer())
                    .subscribe(object : MiddlewareSingleObserver<List<Address>>(dispatcher, MiddlewareError.FAILED_TO_REFRESH_ADDRESSES) {
                        override fun onSuccess(t: List<Address>) {
                            dispatcher.dispatch(EmployerAddressesUpdatedAction(t))
                        }
                    })
        }
    }
}

class NoRoleException : Throwable()

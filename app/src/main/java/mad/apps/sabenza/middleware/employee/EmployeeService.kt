package mad.apps.sabenza.middleware.employee

import io.reactivex.Single
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.service.MiddlewareService
import mad.apps.sabenza.middleware.MiddlewareError
import mad.apps.sabenza.middleware.rx.MiddlewareSingleObserver
import mad.apps.sabenza.state.action.MiddlewareErrorAction
import mad.apps.sabenza.state.action.AddOrUpdateEmployeeAction
import mad.apps.sabenza.state.action.EmployeeUpdatedAction
import mad.apps.sabenza.state.action.LoginOrSignUpSuccessAction
import mad.apps.sabenza.state.util.EmployeeStateUtil
import zendesk.suas.Action
import zendesk.suas.Continuation
import zendesk.suas.Dispatcher
import zendesk.suas.GetState

interface EmployeeService : MiddlewareService

class EmployeeServiceImpl(val employeeAPI: EmployeeAPI) : EmployeeService {
    override fun onAction(action: Action<*>, state: GetState, dispatcher: Dispatcher, continuation: Continuation) {
        when (action) {
            is LoginOrSignUpSuccessAction -> {
                if (action.me.role == Role.EMPLOYEE) {
                    refreshEmployee(dispatcher)
                }

                continuation.next(action)
            }

            is AddOrUpdateEmployeeAction -> {
                addOrUpdateEmployee(dispatcher, action, state)

                continuation.next(action)
            }


            else -> continuation.next(action)
        }
    }

    private fun addOrUpdateEmployee(dispatcher: Dispatcher, action: AddOrUpdateEmployeeAction, state: GetState) {
        val hasEmployee = EmployeeStateUtil.hasEmployee(state)
        val employee = action.employee

        val single: Single<Employee>
        if (!hasEmployee) {
            single = employeeAPI.addEmployee(
                    firstName = employee.firstName!!,
                    lastName = employee.lastName!!,
                    email = employee.email!!,
                    phoneNumber = employee.phoneNumber!!)

        } else {
            single = employeeAPI.updateEmployee(
                    firstName = employee.firstName!!,
                    lastName = employee.lastName!!,
                    email = employee.email!!,
                    phoneNumber = employee.phoneNumber!!)
        }

        single
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<Employee>(dispatcher, MiddlewareError.FAILED_TO_ADD_EMPLOYEE) {
                    override fun onSuccess(t: Employee) {
                        dispatcher.dispatch(EmployeeUpdatedAction(t))
                    }
                })
    }

    private fun refreshEmployee(dispatcher: Dispatcher) {
        employeeAPI.fetchEmployeeForCurrentUser()
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<Employee>() {
                    override fun onError(e: Throwable) {
                        dispatcher.dispatch(MiddlewareErrorAction(e, MiddlewareError.FAILED_TO_REFRESH_EMPLOYEE))
                    }

                    override fun onSuccess(t: Employee) {
                        dispatcher.dispatch(EmployeeUpdatedAction(t))
                    }
                })
    }

}
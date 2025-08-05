package mad.apps.sabenza.api.util

import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.TestComponent
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.rx.NetworkTransformer
import javax.inject.Inject

class EmployeeUtil(val component : TestComponent) {

    @Inject lateinit var employeeAPI : EmployeeAPI

    init {
        component.inject(this)
    }

    fun getCurrentEmployee() : Employee {
        return employeeAPI
                .fetchEmployeeForCurrentUser()
                .compose(NetworkTransformer())
                .blockingGet()
    }


}
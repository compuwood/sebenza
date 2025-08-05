package mad.apps.sabenza.state

import android.content.Context
import mad.apps.sabenza.middleware.address.AddressService
import mad.apps.sabenza.middleware.employee.EmployeeService
import mad.apps.sabenza.middleware.employer.EmployerService
import mad.apps.sabenza.middleware.jobs.employee.EmployeeJobsService
import mad.apps.sabenza.middleware.jobs.employer.ProjectService
import mad.apps.sabenza.middleware.logging.EnhancedLoggingMiddlewareBuilder
import mad.apps.sabenza.middleware.logging.StateLoggingTransformer
import mad.apps.sabenza.middleware.login.LoginService
import mad.apps.sabenza.middleware.payment.PaymentService
import mad.apps.sabenza.middleware.skill.SkillService
import zendesk.suas.AsyncMiddleware
import zendesk.suas.Filters
import zendesk.suas.MonitorMiddleware
import zendesk.suas.Suas

class StoreProvider(context: Context,
                    loggingTransformer: StateLoggingTransformer,
                    loginService: LoginService,
                    skillsService : SkillService,
                    employeeService: EmployeeService,
                    employerService: EmployerService,
                    addressService: AddressService,
                    paymentService: PaymentService,
                    projectService : ProjectService,
                    employeeJobsService: EmployeeJobsService) {

    val store by lazy {
        val loggerMiddleware = EnhancedLoggingMiddlewareBuilder.buildEnhancedLoggerMiddleware(loggingTransformer)

        val monitorMiddleware = MonitorMiddleware.Builder(context)
                .withEnableAdb(true)
                .withEnableBonjour(true)
                .build()

        val store = Suas.createStore(ReducerProvider().reducers())
                .withMiddleware(listOf(
                        AsyncMiddleware(),
                        loginService,
                        employeeService,
                        employerService,
                        skillsService,
                        addressService,
                        loggerMiddleware,
                        paymentService,
                        projectService,
                        employeeJobsService,
                        monitorMiddleware))
                .withDefaultFilter(Filters.EQUALS)
                .build()

        store
    }

}
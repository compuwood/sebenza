package mad.apps.sabenza.di

import dagger.Module
import dagger.Provides
import mad.apps.sabenza.data.api.*
import mad.apps.sabenza.dependancy.OkHttpClientProvider
import mad.apps.sabenza.dependancy.PreferencesProvider
import mad.apps.sabenza.middleware.address.AddressService
import mad.apps.sabenza.middleware.address.AddressServiceImpl
import mad.apps.sabenza.middleware.employee.EmployeeService
import mad.apps.sabenza.middleware.employee.EmployeeServiceImpl
import mad.apps.sabenza.middleware.employer.EmployerService
import mad.apps.sabenza.middleware.employer.EmployerServiceImpl
import mad.apps.sabenza.middleware.jobs.employee.EmployeeJobsService
import mad.apps.sabenza.middleware.jobs.employee.EmployeeJobsServiceImpl
import mad.apps.sabenza.middleware.jobs.employer.ProjectService
import mad.apps.sabenza.middleware.jobs.employer.ProjectServiceImpl
import mad.apps.sabenza.middleware.login.LoginService
import mad.apps.sabenza.middleware.login.LoginServiceImpl
import mad.apps.sabenza.middleware.payment.PaymentService
import mad.apps.sabenza.middleware.payment.PaymentServiceImpl
import mad.apps.sabenza.middleware.skill.SkillService
import mad.apps.sabenza.middleware.skill.SkillServiceImpl
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun providesLoginService(clientProvider: OkHttpClientProvider, sabenzaAPI: SabenzaAPI, preferencesProvider: PreferencesProvider): LoginService {
        return LoginServiceImpl(clientProvider = clientProvider, sabenzaAPI = sabenzaAPI, preferencesProvider = preferencesProvider)
    }

    @Provides
    @Singleton
    fun providesSkillService(sabenzaAPI: SabenzaAPI, skillsAPI: SkillsAPI): SkillService {
        return SkillServiceImpl(sabenzaAPI, skillsAPI)
    }

    @Provides
    @Singleton
    fun providesEmployeeService(employeeAPI: EmployeeAPI): EmployeeService {
        return EmployeeServiceImpl(employeeAPI)
    }

    @Provides
    @Singleton
    fun providesAddressService(employeeAPI: EmployeeAPI, employerAPI: EmployerAPI, addressAPI: AddressAPI): AddressService {
        return AddressServiceImpl(addressAPI = addressAPI, employeeAPI = employeeAPI, employerAPI = employerAPI)
    }

    @Provides
    @Singleton
    fun providesEmployerService(employerAPI: EmployerAPI): EmployerService {
        return EmployerServiceImpl(employerAPI = employerAPI)
    }

    @Provides
    @Singleton
    fun providesPaymentMiddleware(paymentAPI: PaymentAPI, employerAPI: EmployerAPI, employeeAPI: EmployeeAPI): PaymentService {
        return PaymentServiceImpl(paymentAPI = paymentAPI, employerAPI = employerAPI, employeeAPI = employeeAPI)
    }

    @Provides
    @Singleton
    fun providesProjectMiddleware(employerAPI: EmployerAPI, jobsAPI: JobsAPI): ProjectService {
        return ProjectServiceImpl(jobsAPI, employerAPI)
    }

    @Provides
    @Singleton
    fun providesEmployeeJobMiddleware(jobsAPI: JobsAPI): EmployeeJobsService {
        return EmployeeJobsServiceImpl(jobsAPI)
    }

}
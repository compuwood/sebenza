package mad.apps.sabenza

import dagger.Component
import mad.apps.sabenza.api.address.EmployerAddressAPITest
import mad.apps.sabenza.api.bank.BankAccountTest
import mad.apps.sabenza.api.creditcard.EmployerCreditCardAPITest
import mad.apps.sabenza.api.employee.EmployeeAPITest
import mad.apps.sabenza.api.employee.EmployerToEmployeeAPITest
import mad.apps.sabenza.api.employer.EmployerTest
import mad.apps.sabenza.api.handshake.HandshakeTest
import mad.apps.sabenza.api.job.EmployeeJobsTest
import mad.apps.sabenza.api.job.EmployerJobsTest
import mad.apps.sabenza.api.util.JobUtil
import mad.apps.sabenza.di.AppModule
import mad.apps.sabenza.api.login.LoginTest
import mad.apps.sabenza.api.messaging.EmployeeMessagingTest
import mad.apps.sabenza.api.ratings.RatingsEmployeeAPITest
import mad.apps.sabenza.api.ratings.RatingsEmployerAPITest
import mad.apps.sabenza.api.skills.SkillsAPITest
import mad.apps.sabenza.api.util.EmployeeUtil
import mad.apps.sabenza.di.APIModule
import mad.apps.sabenza.di.ServiceModule
import mad.apps.sabenza.middleware.address.AddressServiceTest
import mad.apps.sabenza.middleware.employee.EmployeeServiceTest
import mad.apps.sabenza.middleware.employer.EmployerServiceTest
import mad.apps.sabenza.middleware.job.EmployeeJobServiceTest
import mad.apps.sabenza.middleware.login.LoginServiceTest
import mad.apps.sabenza.middleware.payment.PaymentServiceTest
import mad.apps.sabenza.middleware.project.ProjectServiceTest
import mad.apps.sabenza.middleware.skill.SkillServiceTest
import mad.apps.sabenza.util.MiddlewareTestProjectHelper
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ServiceModule::class, APIModule::class))
interface TestComponent {
    fun inject(instrumentTest: LoginTest)
    fun inject(instrumentTest: LoginServiceTest)
    fun inject(employerJobs: EmployerTest)
    fun inject(employeeAPITest: EmployeeAPITest)
    fun inject(skillsAPITest: SkillsAPITest)
    fun inject(skillServiceTest: SkillServiceTest)
    fun inject(employeeServiceTest: EmployeeServiceTest)
    fun inject(employeeJobsTest: EmployeeJobsTest)
    fun inject(employerJobsTest: EmployerJobsTest)
    fun inject(employerAddressAPITest: EmployerAddressAPITest)
    fun inject(employerCreditCardAPITest: EmployerCreditCardAPITest)
    fun inject(employeeMessagingTest: EmployeeMessagingTest)
    fun inject(jobUtil: JobUtil)
    fun inject(employeeUtil: EmployeeUtil)
    fun inject(addressServiceTest: AddressServiceTest)
    fun inject(employerServiceTest: EmployerServiceTest)
    fun inject(paymentServiceTest: PaymentServiceTest)
    fun inject(projectServiceTest: ProjectServiceTest)
    fun inject(employeeJobServiceTest: EmployeeJobServiceTest)
    fun inject(handshakeTest: HandshakeTest)
    fun inject(middlewareTestProjectHelper: MiddlewareTestProjectHelper)
    fun inject(employerToEmployeeAPITest: EmployerToEmployeeAPITest)
    fun inject(bankAccountTest: BankAccountTest)
    fun inject(ratingsEmployeeAPITest: RatingsEmployeeAPITest)
    fun inject(ratingsEmployerAPITest: RatingsEmployerAPITest)
}
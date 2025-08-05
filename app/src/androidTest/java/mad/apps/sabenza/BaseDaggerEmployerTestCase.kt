package mad.apps.sabenza

import io.reactivex.observers.TestObserver
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.api.SabenzaAPI
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.data.rpc.calls.role.EmployerRoleRequest
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Before
import javax.inject.Inject
import junit.framework.Assert
import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.framework.TestStringHelper

open abstract class BaseDaggerEmployerTestCase : BaseDaggerTestCase() {

    init {
        baseUsername = baseUsername + "employer"
    }

    override fun successfullyLogin(): Me {
        val me = super.successfullyLogin()

        val testSubscriber = TestObserver<String>()

        sabenzaAPI.switchRole(EmployerRoleRequest())
                .compose(NetworkTransformer())
                .map {
                    if (!it.isNullOrEmpty()) {
                        httpProvider.token = it
                    }
                    it
                }
                .blockingGet()

        return me
    }

    override fun successfullyRandomSignUp(): Me {
        val me = super.successfullyRandomSignUp()

        val testSubscriber = TestObserver<String>()

        sabenzaAPI.switchRole(EmployerRoleRequest())
                .compose(NetworkTransformer())
                .map {
                    httpProvider.token = it
                    it
                }
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        return me
    }

    fun successfullyLoginAsEmployee(): Me {
        baseUsername = baseUsername.replace("employer", "")
        val me = super.successfullyLogin()
        baseUsername = baseUsername + "employer"
        return me
    }


    fun addRandomEmployerToUser(me: Me, employerAPI: EmployerAPI): Employer {
        val testSubscriber = TestObserver<Employer>()

        employerAPI.addEmployer(Employer(firstName = "New", lastName =  "Employee" + TestStringHelper.nextPrintableString(3),email =  me.email!!, phoneNumber = "0821234567"))
                .compose(NetworkTransformer<Employer>())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()

        assert(testSubscriber.values().first().email == me.email)

        testSubscriber.assertComplete()

        return testSubscriber.values().first()
    }

}
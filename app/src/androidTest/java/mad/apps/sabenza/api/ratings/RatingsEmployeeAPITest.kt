package mad.apps.sabenza.api.ratings

import android.support.test.runner.AndroidJUnit4
import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerEmployerTestCase
import mad.apps.sabenza.BaseDaggerTestCase
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.api.RatingsAPI
import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.data.model.rating.Rating
import mad.apps.sabenza.framework.rx.NetworkTransformer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class RatingsEmployeeAPITest : BaseDaggerEmployerTestCase() {

    @Inject
    lateinit var ratingsAPI: RatingsAPI

    @Inject
    lateinit var employeeAPI: EmployeeAPI

    @Inject
    lateinit var employerAPI: EmployerAPI

    @Before
    override fun setupDependencies() {
        component.inject(this)
    }

    @Test
    fun testGetRatingForEmployee() {
        val testObserver = TestObserver<Rating>()

        successfullyLogin()

        ratingsAPI.getRatings()
                .compose(NetworkTransformer())
                .subscribe(testObserver)

        testObserver.awaitTerminalEvent()
        testObserver.assertComplete()
    }

    @Test
    fun testRateEmployer() {
        val me = successfullyLogin()
        val employer : Employer = employerAPI.fetchEmployer()
                .compose(NetworkTransformer<Employer>())
                .blockingGet()

        successfullyLoginAsEmployee()

        val employee = employeeAPI.fetchEmployeeForCurrentUser()
                .compose(NetworkTransformer())
                .blockingGet()

        val rating = Rating(rating = "4", review = "Above Average", employeeId = employee.id!!.toInt(), employerId = employer.id)
        val testObserver = TestObserver<Rating>()

        ratingsAPI.postRatingForEmployee(rating)
                .compose(NetworkTransformer())
                .subscribe(testObserver)

        testObserver.awaitTerminalEvent()
        testObserver.assertComplete()
    }

}


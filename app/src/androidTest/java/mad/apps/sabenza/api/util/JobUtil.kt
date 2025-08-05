package mad.apps.sabenza.api.util

import io.reactivex.observers.TestObserver
import mad.apps.sabenza.BaseDaggerEmployerTestCase
import mad.apps.sabenza.TestComponent
import mad.apps.sabenza.data.api.*
import mad.apps.sabenza.data.model.employer.Employer
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.Project
import mad.apps.sabenza.data.model.jobs.ProjectStateEnum
import mad.apps.sabenza.data.model.payment.CreditCard
import mad.apps.sabenza.data.model.payment.EmployerCreditCard
import mad.apps.sabenza.data.rpc.calls.employee.Skill
import mad.apps.sabenza.data.rpc.calls.me.Me
import mad.apps.sabenza.framework.TestStringHelper
import mad.apps.sabenza.framework.rx.NetworkTransformer
import javax.inject.Inject

class JobUtil(component : TestComponent) : BaseTestUtil() {

    @Inject lateinit var jobsAPI: JobsAPI
    @Inject lateinit var paymentAPI: PaymentAPI
    @Inject lateinit var skillsAPI: SkillsAPI
    @Inject lateinit var employerAPI: EmployerAPI

    var randomEmployer: Boolean = true

    constructor(randomEmployer: Boolean, component: TestComponent) : this(component){
        this.randomEmployer = randomEmployer
    }

    init {
        component.inject(this)
    }

    fun createRandomJob(): Pair<Job, Skill> {
        if (!randomEmployer) {
            successfullyLoginAsEmployer()
        } else {
            successfullyRandomSignUpAsEmployer()
        }

        var creditCardId: Int = 0
        var projectId = 0
        var skillId = ""

        var employer: Employer? = null
        var skill: Skill? = null
        var job: Job? = null
        var employerCard: EmployerCreditCard? = null

        var testSubscriber: TestObserver<Any> = TestObserver()

        if (randomEmployer) {
            employerAPI.addEmployer(Employer(
                    firstName = "Random", lastName = "Stranger",
                    email = "random" + TestStringHelper.nextPrintableString(4) + "@email.com",
                    aboutMe = "Some blurb", aboutCompany = "moreBlurb",
                    phoneNumber = "numbers", pictureId = "5"))
                    .map {
                        employer = it
                        it
                    }
                    .compose(NetworkTransformer())
                    .subscribe(testSubscriber)
        } else {
            employerAPI.fetchEmployer()
                    .map {
                        employer = it
                        it
                    }
                    .compose(NetworkTransformer())
                    .subscribe(testSubscriber)
        }

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        testSubscriber = TestObserver()

        skillsAPI.addSkill("New Skill" + TestStringHelper.nextPrintableString(4))
                .map {
                    skill = it
                    skillId = it.id.toString()
                    it
                }
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        testSubscriber = TestObserver()

        if (randomEmployer) {
            paymentAPI.addCreditCard(CreditCard(
                    partOfNumber = "423232",
                    nameOnCard = "John Doe",
                    expiryYear = 2025, expiryMonth = 4))
                    .map {
                        creditCardId = it.id!!
                        it
                    }
                    .flatMap {
                        paymentAPI.linkEmployerCreditCard(
                                employerId = employer!!.id!!,
                                creditCardId = it.id!!,
                                cardType = "Business",
                                isDefault = true)
                    }
                    .map {
                        employerCard = it
                        it
                    }
                    .compose(NetworkTransformer())
                    .subscribe(testSubscriber)
        } else {
            paymentAPI.listAllEmployerCards()
                    .map {
                        employerCard = it.first()
                        creditCardId = it.first().credit_card_id
                        it
                    }
                    .compose(NetworkTransformer())
                    .subscribe(testSubscriber)
        }

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        testSubscriber = TestObserver()

        jobsAPI.addProject(Project(
                description = "Random Job " + TestStringHelper.nextPrintableString(4),
                creditCardId = employerCard?.credit_card_id))
                .compose(NetworkTransformer())
                .map {
                    projectId = it.id!!
                    it
                }
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        testSubscriber = TestObserver()

        val starttime = TestDateUtil.generateFutureDate()
        val endtime = TestDateUtil.addDayToDate(starttime)

        jobsAPI.addJob(Job(
                projectId = projectId,
                skillId = skillId,
                description = "New Job",
                creditCardId = creditCardId,
                quantity = "2",
                duration = "2 hours",
                startDate = starttime.getISOTimeFormattedString(),
                endDate = endtime.getISOTimeFormattedString()))
                .map {
                    job = it
                    it
                }
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        testSubscriber = TestObserver()

        jobsAPI.changeProjectState(
                projectId = projectId.toEqualQueryString(),
                state = ProjectStateEnum.POSTED)
                .compose(NetworkTransformer())
                .subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertComplete()

        return Pair(job!!, skill!!)
    }

}
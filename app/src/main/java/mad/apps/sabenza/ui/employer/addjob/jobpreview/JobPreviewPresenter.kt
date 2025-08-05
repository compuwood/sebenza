package mad.apps.sabenza.ui.employer.addjob.jobpreview

import io.reactivex.Completable
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.data.model.jobs.Job
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.data.model.jobs.Project
import mad.apps.sabenza.data.model.payment.CreditCard
import mad.apps.sabenza.framework.rx.CompletableNetworkTransformer
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.AddJobToProjectAction
import mad.apps.sabenza.state.action.CreateNewProjectAction
import mad.apps.sabenza.state.action.NewProjectAddedAction
import mad.apps.sabenza.state.models.EmployerModel
import mad.apps.sabenza.state.models.PaymentModel
import mad.apps.sabenza.state.models.ProjectModel
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import zendesk.suas.Store
import java.util.*
import java.util.concurrent.TimeUnit

class JobPreviewPresenter(store: Store) : BasePresenter<JobPreviewViewInterface>(store), JobPreviewPresenterInterface {

    lateinit var addJobViewModel : AddJobViewModel

    override fun updateWithJobViewModel(model: AddJobViewModel) {
        addJobViewModel = model
    }

    override fun getJobObject(): JobPreviewModel {
        val job = JobPreviewModel(
                jobId = 1,
                title = addJobViewModel.skill?.description!!,
                description = addJobViewModel.description!!,
                experience = addJobViewModel.yearsExp!!,
                budget = addJobViewModel.budget!!,
                startDate = addJobViewModel.startDate!!,
                endDate = addJobViewModel.endDate!!,
                startTimeHour = addJobViewModel.startDate?.get(Calendar.HOUR)!!,
                startTimeMinute = addJobViewModel.startDate?.get(Calendar.MINUTE)!!,
                endTimeHour = addJobViewModel.endDate?.get(Calendar.HOUR)!!,
                endTimeMinute = addJobViewModel.endDate?.get(Calendar.MINUTE)!!,
                address = addJobViewModel.address!!)

        return job
    }

    override fun postJob() {
        view.busy()
        createNewJobApplication()
    }

    private fun createNewJobApplication() {
        val creditCard = store.state.getState(PaymentModel::class.java)?.defaultCard!!.employerCreditCard

        val project: Project = addJobViewModel.buildProject(
                store.state.getState(EmployerModel::class.java)?.employer?.id!!,
                creditCard)

        RxStateBinder.dispatchAndBindForResult(CreateNewProjectAction(project), store, ProjectModel::class.java)
                .compose(NetworkTransformer())
                .flatMap { projectModel ->
                    val project = projectModel.projects.findLast { it.project.description == addJobViewModel.description }
                    val job = addJobViewModel.buildJob(projectId = project!!.project.id!!, creditCard = creditCard)
                    RxStateBinder.dispatchAndBindForResult(AddJobToProjectAction(job), store, ProjectModel::class.java)
                }
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<ProjectModel>() {
                    override fun onSuccess(projectModel: ProjectModel) {
                        view.idle()
                        view.success()
                    }

                    override fun onError(e: Throwable) {
                        view.error(e.message!!)
                    }
                })
    }

    override fun goToJobsFeed() {
        routeToNewTop(ViewProjectsScreen.provideViewController())
    }

}
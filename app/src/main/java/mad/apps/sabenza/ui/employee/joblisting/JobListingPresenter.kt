package mad.apps.sabenza.ui.employee.joblisting

import mad.apps.sabenza.data.model.jobs.EmployeeJobStateEnum
import mad.apps.sabenza.data.model.jobs.JobPreviewModel
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.framework.ui.BasePresenter
import mad.apps.sabenza.state.action.ApplyForJobAction
import mad.apps.sabenza.state.action.WithdrawFromJobAction
import mad.apps.sabenza.state.models.EmployeeJobsModel
import zendesk.suas.Store

class JobListingPresenter(store: Store) : BasePresenter<JobListingViewInterface>(store), JobListingPresenterInterface {

    lateinit var currentJobId : String

    override fun updateJobId(jobId: String) {
        currentJobId = jobId
    }

    override fun getJobObject(): JobPreviewModel {
        return JobPreviewModel.buildFromEmployeeJobId(currentJobId, store).blockingGet()
    }

    override fun getHasBeenAcceptedForJob(): Boolean {
        return false
    }

    override fun getQrCodeUrl(): String {
        return "https://www.google.com/"
    }

    override fun getHasAppliedForJob(): Boolean {
        val employeeJobModel = store.state.getState(EmployeeJobsModel::class.java)
        val employeeJob = employeeJobModel!!.linkedJobs.find { it.jobId == currentJobId }
        return (employeeJob?.employeeJobState == EmployeeJobStateEnum.APPLIED || employeeJob?.employeeJobState == EmployeeJobStateEnum.CONFIRMED )
    }

    override fun messageEmployer() {

    }

    override fun applyForJob() {
        view.busy()
        val job = store.state.getState(EmployeeJobsModel::class.java)!!.jobs.find { it.id == currentJobId }
        RxStateBinder.dispatchAndBindForResult(
                action = ApplyForJobAction(job!!),
                store = store,
                clazz = EmployeeJobsModel::class.java)
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<EmployeeJobsModel>() {
                    override fun onSuccess(t: EmployeeJobsModel) {
                        view.success()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        view.error(e.message ?: "There was an error applying for that job.")
                    }
                })
    }

    override fun withdrawApplication() {
        view.busy()
        val job = store.state.getState(EmployeeJobsModel::class.java)!!.jobs.find { it.id == currentJobId }

        RxStateBinder.dispatchAndBindForResult(
                action = WithdrawFromJobAction(job!!),
                store = store,
                clazz = EmployeeJobsModel::class.java)
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<EmployeeJobsModel>() {
                    override fun onSuccess(t: EmployeeJobsModel) {
                        view.success()
                    }
                })
    }


}
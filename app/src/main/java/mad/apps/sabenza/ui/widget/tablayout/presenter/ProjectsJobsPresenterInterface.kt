package mad.apps.sabenza.ui.widget.tablayout.presenter

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.data.model.jobs.ProjectPreviewModel
import mad.apps.sabenza.framework.ui.ViewPresenterInterface
import mad.apps.sabenza.ui.widget.tablayout.ProjectsJobsContainerView
import zendesk.suas.Store


interface ProjectsJobsPresenterInterface : ViewPresenterInterface<ProjectsJobsContainerView> {
    fun getProjects() : List<ProjectPreviewModel>
    fun gotoSelectedJob(jobId : String, isEmployer: Boolean)

    object Provider {
        fun providePresenter(store : Store) : ProjectsJobsPresenterInterface {
            return if (BuildConfig.IS_UI_BUILD) {
                ProjectsJobsStubPresenter(store)
            } else {
                ProjectsJobsPresenter(store)
            }
        }
    }
}


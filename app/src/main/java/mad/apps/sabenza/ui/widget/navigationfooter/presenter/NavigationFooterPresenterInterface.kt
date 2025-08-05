package mad.apps.sabenza.ui.widget.tablayout.presenter

import mad.apps.sabenza.BuildConfig
import mad.apps.sabenza.framework.ui.ViewPresenterInterface
import mad.apps.sabenza.ui.widget.navigationfooter.NavigationFooter
import zendesk.suas.Store


interface NavigationFooterPresenterInterface : ViewPresenterInterface<NavigationFooter> {

    fun navigateToEmployerProfile()
    fun navigateToEmployerCalendar()
    fun navigateToEmployerJobs()
    fun navigateToEmployerInbox()
    fun navigateToEmployerMore()

    fun navigateToEmployeeProfile()
    fun navigateToEmployeeCalendar()
    fun navigateToEmployeeJobs()
    fun navigateToEmployeeInbox()
    fun navigateToEmployeeMore()

    object Provider {
        fun providePresenter(store : Store) : NavigationFooterPresenterInterface {
            return if (BuildConfig.IS_UI_BUILD) {
                NavigationFooterStubPresenter(store)
            } else {
                NavigationFooterStubPresenter(store)
            }
        }
    }
}


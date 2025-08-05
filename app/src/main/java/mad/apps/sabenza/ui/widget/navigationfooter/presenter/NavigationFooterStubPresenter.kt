package mad.apps.sabenza.ui.widget.tablayout.presenter

import mad.apps.sabenza.framework.ui.ViewPresenter
import mad.apps.sabenza.ui.blankscreen.BlankScreenEmployee
import mad.apps.sabenza.ui.blankscreen.BlankScreenEmployer
import mad.apps.sabenza.ui.employee.jobsfeed.JobsFeedScreen
import mad.apps.sabenza.ui.employer.inbox.InboxScreen as EmployerInboxScreen
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import mad.apps.sabenza.ui.widget.navigationfooter.NavigationFooter
import zendesk.suas.Store
import mad.apps.sabenza.ui.employee.profile.ProfileScreen as EmployeeProfileScreen
import mad.apps.sabenza.ui.employer.profile.ProfileScreen as EmployerProfileScreen


class NavigationFooterStubPresenter(store: Store) : ViewPresenter<NavigationFooter>(store), NavigationFooterPresenterInterface {
    override fun navigateToEmployerProfile() {
        routeToReplaceCurrent(EmployerProfileScreen.provideViewController())
    }

    override fun navigateToEmployerCalendar() {
        routeToReplaceCurrent(BlankScreenEmployer.provideViewController()) //** Needs Implementation **//
    }

    override fun navigateToEmployerJobs() {
        routeToReplaceCurrent(ViewProjectsScreen.provideViewController())
    }

    override fun navigateToEmployerInbox() {
        routeToReplaceCurrent(EmployerInboxScreen.provideViewController()) //** Needs Implementation **//
    }

    override fun navigateToEmployerMore() {
        routeToReplaceCurrent(BlankScreenEmployer.provideViewController()) //** Needs Implementation **//
    }

    override fun navigateToEmployeeProfile() {
        routeToReplaceCurrent(EmployeeProfileScreen.provideViewController()) //** Needs Implementation **//
    }

    override fun navigateToEmployeeCalendar() {
        routeToReplaceCurrent(BlankScreenEmployee.provideViewController()) //** Needs Implementation **//
    }

    override fun navigateToEmployeeJobs() {
        routeToReplaceCurrent(JobsFeedScreen.provideViewController())
    }

    override fun navigateToEmployeeInbox() {
        routeToReplaceCurrent(BlankScreenEmployee.provideViewController()) //** Needs Implementation **//
    }

    override fun navigateToEmployeeMore() {
        routeToReplaceCurrent(BlankScreenEmployee.provideViewController()) //** Needs Implementation **//
    }

}
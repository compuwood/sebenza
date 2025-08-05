package mad.apps.sabenza.di


import dagger.Module
import dagger.Provides
import mad.apps.sabenza.ui.employee.jobsfeed.JobsFeedPresenterInterface
import mad.apps.sabenza.ui.employee.jobsfeed.JobsFeedScreen
import mad.apps.sabenza.ui.employee.signup.address.YourAddressPresenterInterface
import mad.apps.sabenza.ui.employee.signup.address.YourAddressScreen
import mad.apps.sabenza.ui.employee.signup.addskills.AddSkillsPresenterInterface
import mad.apps.sabenza.ui.employee.signup.addskills.AddSkillsScreen
import mad.apps.sabenza.ui.employee.signup.createprofile.CreateProfilePresenterInterface
import mad.apps.sabenza.ui.employee.signup.createprofile.CreateProfileScreen
import mad.apps.sabenza.ui.employee.signup.paymentinfo.PaymentInfoPresenterInterface
import mad.apps.sabenza.ui.employee.signup.paymentinfo.PaymentInfoScreen
import mad.apps.sabenza.ui.employee.signup.signup.SignUpPresenterInterface
import mad.apps.sabenza.ui.employee.signup.signup.SignUpScreen
import mad.apps.sabenza.ui.employer.addjob.jobpreview.JobPreviewPresenterInterface
import mad.apps.sabenza.ui.employer.addjob.jobpreview.JobPreviewScreen
import mad.apps.sabenza.ui.employer.addproject.addjobs.AddJobsPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.addjobs.AddJobsScreen
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobScreen
import mad.apps.sabenza.ui.employer.addproject.configureskill.ConfigureSkillPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.configureskill.ConfigureSkillScreen
import mad.apps.sabenza.ui.employer.addproject.jobconfirm.JobConfirmPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.jobconfirm.JobConfirmScreen
import mad.apps.sabenza.ui.employer.addproject.joblocation.JobLocationPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.joblocation.JobLocationScreen
import mad.apps.sabenza.ui.employer.addproject.jobtime.JobTimePresenterInterface
import mad.apps.sabenza.ui.employer.addproject.jobtime.JobTimeScreen
import mad.apps.sabenza.ui.employer.addproject.listedjobs.ListedJobsPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.listedjobs.ListedJobsScreen
import mad.apps.sabenza.ui.employer.addproject.projectdetails.ProjectDetailsPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.projectdetails.ProjectDetailsScreen
import mad.apps.sabenza.ui.employer.addproject.projectpreview.ProjectPreviewPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.projectpreview.ProjectPreviewScreen
import mad.apps.sabenza.ui.employer.addproject.startwork.StartWorkPresenterInterface
import mad.apps.sabenza.ui.employer.addproject.startwork.StartWorkScreen
import mad.apps.sabenza.ui.employer.inbox.InboxPresenterInterface as EmployerInboxPresenterInterface
import mad.apps.sabenza.ui.employer.inbox.InboxScreen as EmployerInboxScreen
import mad.apps.sabenza.ui.employer.viewapplicants.ViewApplicantsPresenterInterface
import mad.apps.sabenza.ui.employer.viewapplicants.ViewApplicantsScreen
import mad.apps.sabenza.ui.employer.joblisting.JobListingScreen as EmployerJobListingScreen
import mad.apps.sabenza.ui.employer.joblisting.JobListingPresenterInterface as EmployerJobListingPresenterInterface
import mad.apps.sabenza.ui.employee.joblisting.JobListingScreen as EmployeeJobListingScreen
import mad.apps.sabenza.ui.employee.joblisting.JobListingPresenterInterface as EmployeeJobListingPresenterInterface
import mad.apps.sabenza.ui.employer.profile.ProfilePresenterInterface as EmployerProfilePresenterInterface
import mad.apps.sabenza.ui.employee.profile.ProfilePresenterInterface as EmployeeProfilePresenterInterface
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsPresenterInterface
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import mad.apps.sabenza.ui.landing.LandingPresenterInterface
import mad.apps.sabenza.ui.landing.LandingScreen
import mad.apps.sabenza.ui.login.LoginPresenterInterface
import mad.apps.sabenza.ui.login.LoginScreen
import mad.apps.sabenza.ui.roleselect.RoleSelectPresenterInterface
import mad.apps.sabenza.ui.roleselect.RoleSelectScreen
import mad.apps.sabenza.ui.widget.headerviewbase.presenter.HeaderViewBasePresenterInterface
import mad.apps.sabenza.ui.widget.tablayout.presenter.NavigationFooterPresenterInterface
import mad.apps.sabenza.ui.widget.tablayout.presenter.ProjectsJobsPresenterInterface
import zendesk.suas.Store
import mad.apps.sabenza.ui.employer.profile.ProfileScreen as EmployerProfileScreen
import mad.apps.sabenza.ui.employee.profile.ProfileScreen as EmployeeProfileScreen
import mad.apps.sabenza.ui.employer.signup.address.YourAddressPresenterInterface as EmployerYourAddressPresenterInterface
import mad.apps.sabenza.ui.employer.signup.address.YourAddressScreen as EmployerYourAddressScreen
import mad.apps.sabenza.ui.employer.signup.createprofile.CreateProfilePresenterInterface as EmployerCreateProfilePresenterInterface
import mad.apps.sabenza.ui.employer.signup.createprofile.CreateProfileScreen as EmployerCreateProfileScreen
import mad.apps.sabenza.ui.employer.signup.paymentinfo.PaymentInfoPresenterInterface as EmployerPaymentInfoPresenterInterface
import mad.apps.sabenza.ui.employer.signup.paymentinfo.PaymentInfoScreen as EmployerPaymentInfoScreen
import mad.apps.sabenza.ui.employer.signup.signup.SignUpPresenterInterface as EmployerSignUpPresenterInterface
import mad.apps.sabenza.ui.employer.signup.signup.SignUpScreen as EmployerSignUpScreen

@Module
class UIModule {

    @Provides
    fun providesLandingPresenter(store: Store): LandingPresenterInterface {
        return LandingScreen.providePresenter(store)
    }

    @Provides
    fun providesRoleSelectPresenter(store: Store): RoleSelectPresenterInterface {
        return RoleSelectScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployerSignupPresenter(store: Store): EmployerSignUpPresenterInterface {
        return EmployerSignUpScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployerCreateProfilePresenterInterface(store: Store): EmployerCreateProfilePresenterInterface {
        return EmployerCreateProfileScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployerYourAddressPresenterInterface(store: Store): EmployerYourAddressPresenterInterface {
        return EmployerYourAddressScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployerPaymentInfoPresenterInterface(store: Store): EmployerPaymentInfoPresenterInterface {
        return EmployerPaymentInfoScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployerProfilePresenterInterface(store: Store): EmployerProfilePresenterInterface {
        return EmployerProfileScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployerViewProjectPresenterInterface(store: Store): ViewProjectsPresenterInterface {
        return ViewProjectsScreen.providePresenter(store)
    }

    @Provides
    fun providesProjectsJobsPresenter(store: Store): ProjectsJobsPresenterInterface {
        return ProjectsJobsPresenterInterface.Provider.providePresenter(store)
    }

    @Provides
    fun providesEmployeeSignupPresenterInterface(store: Store): SignUpPresenterInterface {
        return SignUpScreen.providePresenter(store)
    }

    @Provides
    fun providesLoginPresenterInterface(store: Store): LoginPresenterInterface {
        return LoginScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployeeCreateProfilePresenterInterface(store: Store): CreateProfilePresenterInterface {
        return CreateProfileScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployeeYourAddressPresenterInterface(store: Store): YourAddressPresenterInterface {
        return YourAddressScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployeePaymentInfoPresenterInterface(store: Store): PaymentInfoPresenterInterface {
        return PaymentInfoScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployeeAddSkillsPresenterInterface(store: Store): AddSkillsPresenterInterface {
        return AddSkillsScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployeeJobsFeedPresenter(store: Store): JobsFeedPresenterInterface {
        return JobsFeedScreen.providePresenter(store)
    }

    @Provides
    fun providesNavigationFooterPresenter(store: Store): NavigationFooterPresenterInterface {
        return NavigationFooterPresenterInterface.Provider.providePresenter(store)
    }

    @Provides
    fun providesAddProjectPresenter(store: Store): StartWorkPresenterInterface {
        return StartWorkScreen.providePresenter(store)
    }

    @Provides
    fun providesProjectDetailsPresenter(store: Store): ProjectDetailsPresenterInterface {
        return ProjectDetailsScreen.providePresenter(store)
    }

    @Provides
    fun providesAddJobsPresenter(store: Store): AddJobsPresenterInterface {
        return AddJobsScreen.providePresenter(store)
    }

    @Provides
    fun providesHeaderViewBasePresenter(store: Store): HeaderViewBasePresenterInterface {
        return HeaderViewBasePresenterInterface.Provider.providePresenter(store)
    }

    @Provides
    fun providesChooseJobPresenter(store: Store): ChooseJobPresenterInterface {
        return ChooseJobScreen.providePresenter(store)
    }

    @Provides
    fun providesConfigureSkillPresenter(store: Store): ConfigureSkillPresenterInterface {
        return ConfigureSkillScreen.providePresenter(store)
    }

    @Provides
    fun providesJobLocationPresenter(store: Store): JobLocationPresenterInterface {
        return JobLocationScreen.providePresenter(store)
    }

    @Provides
    fun providesJobTimePresenter(store: Store): JobTimePresenterInterface {
        return JobTimeScreen.providePresenter(store)
    }

    @Provides
    fun providesJobConfirmPresenter(store: Store): JobConfirmPresenterInterface {
        return JobConfirmScreen.providePresenter(store)
    }

    @Provides
    fun providesListedJobsPresenter(store: Store): ListedJobsPresenterInterface {
        return ListedJobsScreen.providePresenter(store)
    }

    @Provides
    fun providesProjectPreviewPresenter(store: Store): ProjectPreviewPresenterInterface {
        return ProjectPreviewScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployeeProfilePresenter(store: Store): EmployeeProfilePresenterInterface {
        return EmployeeProfileScreen.providePresenter(store)
    }

    @Provides
    fun providesJobPreviewPresenter(store: Store): JobPreviewPresenterInterface {
        return JobPreviewScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployerJobListingPresenter(store: Store): EmployerJobListingPresenterInterface {
        return EmployerJobListingScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployeeJobListingPresenter(store: Store): EmployeeJobListingPresenterInterface {
        return EmployeeJobListingScreen.providePresenter(store)
    }

    @Provides
    fun providesViewApplicantsPresenter(store: Store): ViewApplicantsPresenterInterface {
        return ViewApplicantsScreen.providePresenter(store)
    }

    @Provides
    fun providesEmployerInboxPresenter(store: Store): EmployerInboxPresenterInterface {
        return EmployerInboxScreen.providePresenter(store)
    }
}
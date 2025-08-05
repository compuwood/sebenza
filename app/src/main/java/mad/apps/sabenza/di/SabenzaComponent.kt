package mad.apps.sabenza.di

import dagger.Component
import mad.apps.sabenza.SabenzaActivity
import mad.apps.sabenza.ui.employee.jobsfeed.JobsFeedViewController
import mad.apps.sabenza.ui.employee.signup.address.YourAddressViewController
import mad.apps.sabenza.ui.employee.signup.addskills.AddSkillsViewController
import mad.apps.sabenza.ui.employee.signup.createprofile.CreateProfileViewController
import mad.apps.sabenza.ui.employee.signup.paymentinfo.PaymentInfoViewController
import mad.apps.sabenza.ui.employee.signup.signup.SignUpViewController
import mad.apps.sabenza.ui.employer.addjob.jobpreview.JobPreviewViewController
import mad.apps.sabenza.ui.employer.addproject.addjobs.AddJobsViewController
import mad.apps.sabenza.ui.employer.addproject.choosejob.ChooseJobViewController
import mad.apps.sabenza.ui.employer.addproject.configureskill.ConfigureSkillViewController
import mad.apps.sabenza.ui.employer.addproject.jobconfirm.JobConfirmViewController
import mad.apps.sabenza.ui.employer.addproject.joblocation.JobLocationViewController
import mad.apps.sabenza.ui.employer.addproject.jobtime.JobTimeViewController
import mad.apps.sabenza.ui.employer.addproject.listedjobs.ListedJobsViewController
import mad.apps.sabenza.ui.employer.addproject.startwork.StartWorkViewController
import mad.apps.sabenza.ui.employer.addproject.projectdetails.ProjectDetailsViewController
import mad.apps.sabenza.ui.employer.addproject.projectpreview.ProjectPreviewViewController
import mad.apps.sabenza.ui.employer.inbox.InboxViewController as EmployerInboxViewController
import mad.apps.sabenza.ui.employer.viewapplicants.ViewApplicantsViewController
import mad.apps.sabenza.ui.employer.joblisting.JobListingViewController as EmployerJobListingViewController
import mad.apps.sabenza.ui.employee.joblisting.JobListingViewController as EmployeeJobListingViewController
import mad.apps.sabenza.ui.employer.profile.ProfileViewController as EmployerProfileViewController
import mad.apps.sabenza.ui.employee.profile.ProfileViewController as EmployeeProfileViewController
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsViewController
import mad.apps.sabenza.ui.widget.image.AddImageDialog
import mad.apps.sabenza.ui.widget.image.AddSebenzaImageDialog
import mad.apps.sabenza.ui.widget.tablayout.ProjectsJobsContainerView
import mad.apps.sabenza.ui.landing.LandingViewController
import mad.apps.sabenza.ui.login.LoginViewController
import mad.apps.sabenza.ui.roleselect.RoleSelectIntegrationViewController
import mad.apps.sabenza.ui.roleselect.RoleSelectViewController
import mad.apps.sabenza.ui.widget.headerviewbase.HeaderViewBase
import mad.apps.sabenza.ui.widget.navigationfooter.NavigationFooter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, APIModule::class, ServiceModule::class, UIModule::class))
interface SabenzaComponent {
    fun inject(sabenzaActivity: SabenzaActivity)
    fun inject(landingViewController: LandingViewController)
    fun inject(roleSelectViewController: RoleSelectViewController)
    fun inject(employerSignUpViewController: mad.apps.sabenza.ui.employer.signup.signup.SignUpViewController)
    fun inject(employerCreateProfileViewController: mad.apps.sabenza.ui.employer.signup.createprofile.CreateProfileViewController)
    fun inject(employerYourAddressViewController: mad.apps.sabenza.ui.employer.signup.address.YourAddressViewController)
    fun inject(employerPaymentInfoViewController: mad.apps.sabenza.ui.employer.signup.paymentinfo.PaymentInfoViewController)
    fun inject(employerLoginViewController: LoginViewController)
    fun inject(employeeSignUpViewController: SignUpViewController)
    fun inject(employeeCreateProfileViewController: CreateProfileViewController)
    fun inject(employeeYourAddressViewController: YourAddressViewController)
    fun inject(employeePaymentInfoViewController: PaymentInfoViewController)
    fun inject(employeeAddSkillsViewController: AddSkillsViewController)
    fun inject(employerProfileViewController: EmployerProfileViewController)
    fun inject(roleSelectIntegrationViewController: RoleSelectIntegrationViewController)
    fun inject(employerViewProjectViewController: ViewProjectsViewController)
    fun inject(employeeJobsFeedViewController: JobsFeedViewController)
    fun inject(projectsJobsContainerView: ProjectsJobsContainerView)
    fun inject(navigationFooter: NavigationFooter)
    fun inject(addProjectViewController: StartWorkViewController)
    fun inject(projectDetailsViewController: ProjectDetailsViewController)
    fun inject(addJobViewController: AddJobsViewController)
    fun inject(headerViewBase: HeaderViewBase)
    fun inject(chooseJobViewController: ChooseJobViewController)
    fun inject(configureSkillViewController: ConfigureSkillViewController)
    fun inject(jobLocationViewController: JobLocationViewController)
    fun inject(jobTimeViewController: JobTimeViewController)
    fun inject(jobConfirmViewController: JobConfirmViewController)
    fun inject(listedJobsViewController: ListedJobsViewController)
    fun inject(projectPreviewViewController: ProjectPreviewViewController)
    fun inject(employeeProfileViewController: EmployeeProfileViewController )
    fun inject(addImageDialog: AddImageDialog)
    fun inject(addSebenzaImageDialog: AddSebenzaImageDialog)
    fun inject(jobPreviewViewController: JobPreviewViewController)
    fun inject(employerJobListingViewController: EmployerJobListingViewController)
    fun inject(employeeJobListingViewController: EmployeeJobListingViewController)
    fun inject(viewApplicantsViewController: ViewApplicantsViewController)
    fun inject(employerInboxViewController: EmployerInboxViewController)
}
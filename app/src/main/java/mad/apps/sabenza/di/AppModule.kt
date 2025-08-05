package mad.apps.sabenza.di

import dagger.Module
import dagger.Provides
import mad.apps.sabenza.SabenzaApplication
import mad.apps.sabenza.data.api.PicturesAPI
import mad.apps.sabenza.dependancy.OkHttpClientProvider
import mad.apps.sabenza.dependancy.PreferencesProvider
import mad.apps.sabenza.middleware.address.AddressService
import mad.apps.sabenza.middleware.employee.EmployeeService
import mad.apps.sabenza.middleware.employer.EmployerService
import mad.apps.sabenza.middleware.jobs.employee.EmployeeJobsService
import mad.apps.sabenza.middleware.jobs.employer.ProjectService
import mad.apps.sabenza.middleware.logging.StateLoggingTransformer
import mad.apps.sabenza.middleware.login.LoginService
import mad.apps.sabenza.middleware.payment.PaymentService
import mad.apps.sabenza.middleware.skill.SkillService
import mad.apps.sabenza.service.image.*
import mad.apps.sabenza.service.permission.PermissionService
import mad.apps.sabenza.state.StoreProvider
import okhttp3.OkHttpClient
import zendesk.suas.Dispatcher
import zendesk.suas.Store
import javax.inject.Singleton

@Module
class AppModule(val application: SabenzaApplication) {

    @Provides
    @Singleton
    fun providesStore(loginService: LoginService,
                      skillService: SkillService,
                      employeeService: EmployeeService,
                      addressService: AddressService,
                      employerService: EmployerService,
                      paymentService: PaymentService,
                      projectService: ProjectService,
                      employeeJobsService: EmployeeJobsService,
                      transformer: StateLoggingTransformer): Store {
        return StoreProvider(
                context = application.applicationContext,
                loggingTransformer = transformer,
                loginService = loginService,
                skillsService = skillService,
                employeeService = employeeService,
                employerService = employerService,
                addressService = addressService,
                paymentService = paymentService,
                projectService = projectService,
                employeeJobsService = employeeJobsService)
                .store
    }

    @Provides
    @Singleton
    fun providesDispatcher(store: Store): Dispatcher {
        return store
    }

    @Provides
    @Singleton
    fun providesPreferencesProvider(): PreferencesProvider {
        return PreferencesProvider(application)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(clientProvider: OkHttpClientProvider): OkHttpClient {
        return clientProvider.client
    }

    @Provides
    @Singleton
    fun providesOkHttpClientService(preferencesProvider: PreferencesProvider): OkHttpClientProvider {
        return OkHttpClientProvider(preferencesProvider)
    }

    @Provides
    @Singleton
    fun providesLoggingTransformer() : StateLoggingTransformer {
        return StateLoggingTransformer()
    }

    @Provides
    @Singleton
    fun providesPermissionService() : PermissionService {
        return PermissionService()
    }

    @Provides
    @Singleton
    fun providesImageService(permissionService: PermissionService) : ImageServiceAPI {
        return ImageService(application.applicationContext, permissionService)
    }

    @Provides
    @Singleton
    fun providesFirebaseImageCapture(imageServiceAPI: ImageServiceAPI) : ImageCapturer {
        return imageServiceAPI;
    }

    @Provides
    fun providesSebenzaImageService(picturesAPI: PicturesAPI, imageCapturer: ImageCapturer) : SebenzaImageService {
        return SebenzaImageServiceImpl(picturesAPI, imageCapturer)
    }
}
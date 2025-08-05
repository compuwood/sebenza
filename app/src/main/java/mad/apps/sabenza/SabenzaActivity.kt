package mad.apps.sabenza

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.google.firebase.iid.FirebaseInstanceId
import com.noveogroup.android.log.LoggerManager
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.Single
import mad.apps.sabenza.data.api.SabenzaAPI
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.dependancy.OkHttpClientProvider
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.rx.state.RxStateBinder
import mad.apps.sabenza.service.image.ImageService
import mad.apps.sabenza.service.image.ImageServiceAPI
import mad.apps.sabenza.service.image.ImageUtil
import mad.apps.sabenza.service.permission.PermissionService
import mad.apps.sabenza.state.action.AttemptAutoLoginAction
import mad.apps.sabenza.state.action.RefreshAction
import mad.apps.sabenza.state.models.LoginModel
import mad.apps.sabenza.ui.employee.jobsfeed.JobsFeedScreen
import mad.apps.sabenza.ui.employer.viewprojects.ViewProjectsScreen
import mad.apps.sabenza.ui.landing.LandingScreen
import zendesk.suas.Store
import javax.inject.Inject

class SabenzaActivity : AppCompatActivity() {

    @Inject lateinit var sabenzaAPI: SabenzaAPI
    @Inject lateinit var store: Store
    @Inject lateinit var okHttpClientProvider: OkHttpClientProvider
    @Inject lateinit var permissionService : PermissionService
    @Inject lateinit var imageService : ImageServiceAPI

    lateinit var router: Router
    var initScreen = LandingScreen.provideViewController()

    private val logger = LoggerManager.getLogger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerService.getDaggerComponent(this).inject(this)
        permissionService.init(this)
        imageService.init(this)

        setContentView(R.layout.root)

        val container = findViewById<ViewGroup>(R.id.container)

        LoggerManager.getLogger().e("Firebase Messaging Token: " + FirebaseInstanceId.getInstance().token)

        router = Conductor.attachRouter(this, container, savedInstanceState)
        checkForAutoLoginAndNavigateToScreen()

    }

    private fun checkForAutoLoginAndNavigateToScreen() {
        if (okHttpClientProvider.hasToken()) {
            RxStateBinder.dispatchAndBindForResult(AttemptAutoLoginAction(), store, LoginModel::class.java)
                    .compose(NetworkTransformer())
                    .subscribe(object : EnhancedSingleObserver<LoginModel>() {
                        override fun onSuccess(loginModel: LoginModel) {
                            if (loginModel.me == null) {
                                gotoInitialScreen(initScreen)
                            } else {
                                when (loginModel.me.role) {
                                    Role.EMPLOYER -> {
                                        //Employer - ViewProject
                                        gotoInitialScreen(ViewProjectsScreen.provideViewController())
                                    }
                                    Role.EMPLOYEE -> {
                                        //Employee - JobFeed
                                        gotoInitialScreen(JobsFeedScreen.provideViewController())
                                    }
                                    else -> {
                                        gotoInitialScreen(initScreen)
                                    }
                                }
                            }
                        }

                        override fun onError(e: Throwable) {
                            gotoInitialScreen(initScreen)
                        }
                    })
        } else {
            gotoInitialScreen(initScreen)
        }
    }

    private fun gotoInitialScreen(screen: Controller) {
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(screen))
        }
    }

    override fun onResume() {
        super.onResume()
        store.dispatch(RefreshAction())
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionService.requestPermissionsResult(requestCode = requestCode, permissions = permissions, grantResults = grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == ImageUtil.REQUEST_CAMERA && resultCode == RESULT_OK) {
            imageService.imageResult(data)
            return
        }
        if (requestCode == ImageUtil.REQUEST_CAMERA && resultCode == RESULT_CANCELED) {
            imageService.imageCaptureCancelled()
            return
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            imageService.imageResult(result.uri)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
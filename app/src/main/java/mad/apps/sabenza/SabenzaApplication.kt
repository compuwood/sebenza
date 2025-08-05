package mad.apps.sabenza

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDexApplication
import mad.apps.sabenza.dependancy.TrackingProvider
import mad.apps.sabenza.di.*

class SabenzaApplication : MultiDexApplication() {

    var component : SabenzaComponent? = null

    override fun onCreate() {
        super.onCreate()
        component = DaggerSabenzaComponent.builder()
                .appModule(AppModule(this))
                .aPIModule(APIModule())
                .serviceModule(ServiceModule())
                .uIModule(UIModule())
                .build()
        TrackingProvider.init(this)
        TrackingProvider.trackEvent("STARTUP", "APP STARTUP", "SYSTEM")
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun getSystemService(name: String): Any {
        if (component != null && name.equals(DaggerService.SERVICE_NAME)) {
            return component as SabenzaComponent
        }
        return super.getSystemService(name)
    }
}
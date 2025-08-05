package mad.apps.sabenza.dependancy

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import mad.apps.sabenza.SabenzaApplication

object TrackingProvider {
    private lateinit var firebaseAnalytics : FirebaseAnalytics

    fun init(sabenzaApplication: SabenzaApplication) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(sabenzaApplication.applicationContext)
    }

    fun trackScreen(context : Context, screenName: String) {
        firebaseAnalytics.setCurrentScreen(context as Activity, screenName, null)
    }

    fun trackEvent(eventID : String, eventName : String, eventCategory:String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, eventID)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventName)
        firebaseAnalytics.logEvent(eventCategory, bundle)
    }

}
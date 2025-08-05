package mad.apps.sabenza.service.notifications

import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.noveogroup.android.log.LoggerManager


class EnhancedFirebaseService : FirebaseInstanceIdService() {

    override fun onCreate() {
        super.onCreate()
        LoggerManager.getLogger().e("Firebase Messaging Token: " + FirebaseInstanceId.getInstance().token)
    }

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        LoggerManager.getLogger().e("Refreshed token: " + refreshedToken!!)
    }

}

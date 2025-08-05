package mad.apps.sabenza.framework.util

import com.google.firebase.crash.FirebaseCrash
import com.noveogroup.android.log.LoggerManager

object ExceptionTracker {
    fun trackException(e: Throwable) {
        LoggerManager.getLogger().e(e)
        FirebaseCrash.report(e)
    }
}
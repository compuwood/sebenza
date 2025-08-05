package mad.apps.sabenza.ui.util

import android.content.Context
import android.widget.Toast
import com.noveogroup.android.log.LoggerManager

object MissingFeature {

    fun show(context : Context) {
        Toast.makeText(context, "Need to implement feature", Toast.LENGTH_LONG).show()
        LoggerManager.getLogger().e("Need to implement feature")
    }

    fun show(context: Context, feature: String) {
        Toast.makeText(context, "Need to implement feature: " + feature, Toast.LENGTH_LONG).show()
        LoggerManager.getLogger().e("Need to implement feature: " + feature)
    }

    fun idle(context: Context) {
        Toast.makeText(context, "Show Idle State", Toast.LENGTH_SHORT).show()
    }

    fun busy(context: Context) {
        Toast.makeText(context, "Show Busy State", Toast.LENGTH_SHORT).show()
    }

    fun error(context: Context, error: String) {
        Toast.makeText(context, "Need to show error state for error: " + error, Toast.LENGTH_LONG).show()
        LoggerManager.getLogger().e("Need to show error state for error: " + error)
    }

    fun log() {
        LoggerManager.getLogger().e("Missing Feature Here")
    }

    fun log(message : String) {
        LoggerManager.getLogger().e("Missing Feature: $message")
    }

    fun <T>missingValue(message: String, value: T) : T {
        LoggerManager.getLogger().e("Missing value: " + message + " with value: " + value.toString())
        return value;
    }

}
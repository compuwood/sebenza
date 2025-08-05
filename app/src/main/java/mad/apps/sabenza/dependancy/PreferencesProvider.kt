package mad.apps.sabenza.dependancy

import android.content.SharedPreferences
import android.preference.PreferenceManager
import mad.apps.sabenza.SabenzaApplication

class PreferencesProvider(application: SabenzaApplication) {

    private val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    }

    fun getString(key : String) : String {
        return sharedPrefs.getString(key, "")
    }

    fun storeString(key: String, value: String) {
        val editor = sharedPrefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun hasString(key: String): Boolean {
        return sharedPrefs.contains(key) && sharedPrefs.getString(key, "").isNotEmpty()
    }

    fun clearString(key: String) {
        if (sharedPrefs.contains(key)) {
            sharedPrefs.edit().remove(key).apply()
        }
    }

}
package mad.apps.sabenza.framework.ui

import android.content.Context
import com.bluelinelabs.conductor.Router

interface ViewInterface {

    fun getContext() : Context

    fun getRouter() : Router
}
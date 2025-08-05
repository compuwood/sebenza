package mad.apps.sabenza.framework.ui

import android.view.View

interface ViewPresenterInterface<T : View> {

    fun takeView(view: T)

    fun dropView()

}
package mad.apps.sabenza.framework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller

abstract class ViewBindingController : Controller {

    constructor() : super()
    constructor(args: Bundle?) : super(args)

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflateView(inflater, container)
        ViewBinder.setup(this, view)
        onBindView(view)
        return view
    }

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup): View

    override fun onDestroyView(view: View) {
        ViewBinder.tearDown(this)
        super.onDestroyView(view)

    }

    open fun onBindView(view: View) { }

}
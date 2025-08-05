package mad.apps.sabenza.framework.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mad.apps.sabenza.dependancy.TrackingProvider

abstract class
BaseViewController(args: Bundle?) : ViewBindingController(args) {

    constructor() : this(null)

    abstract fun layout() : Int

    final override fun onBindView(view: View) {

    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(layout(), container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        initView(view)
        TrackingProvider.trackScreen(view.context, javaClass.canonicalName)
    }

    open fun initView(view: View) {

    }

    fun getContext() : Context {
        return view!!.context
    }

    private var viewArguments: Map<String, Any>? = null

    fun withViewArgs(vararg viewArguments : Pair<String, Any>) : BaseViewController {
        this.viewArguments = viewArguments.toMap()
        return this
    }

    protected fun hasViewArgument(argName: String) : Boolean {
        return (viewArguments?.contains(argName) ?: false)
    }

    protected fun <T>fetchViewArgument(argName: String) : T {
        return viewArguments?.get(argName) as T
    }

}
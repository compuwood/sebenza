package mad.apps.sabenza.ui.util

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.WindowManager

fun getScreenDensity(context: Context): Float {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(metrics)
    return metrics.density
}

fun convertDpToPixel(dp: Float, context: Context): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return px.toInt()
}

fun convertPixelsToDp(px: Float, context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val dp = px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return dp
}

class NonScrollLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean) : LinearLayoutManager(context, orientation, reverseLayout) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}
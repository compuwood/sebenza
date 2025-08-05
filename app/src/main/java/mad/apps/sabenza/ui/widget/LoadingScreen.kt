package mad.apps.sabenza.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.RelativeLayout
import mad.apps.sabenza.R

class LoadingScreen(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.loading_screen, this, true)
        val typedAttrs = context.obtainStyledAttributes(attrs, R.styleable.LoadingScreen, 0 , 0)
        val showBackground = typedAttrs.getBoolean(R.styleable.LoadingScreen_showBackground, true)
        if (!showBackground) {
            val container = findViewById<ViewGroup>(R.id.loading_screen_container)
            container.background = null

            val params = container.layoutParams
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT
            container.layoutParams = params
        }
    }

    fun show() {
        if (this.visibility != View.VISIBLE) {
            var anim = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 250
            this.startAnimation(anim)
            this.visibility = View.VISIBLE
        }
    }

    fun hide() {
        if (this.visibility != View.GONE) {
            var anim = AlphaAnimation(1.0f, 0.0f)
            anim.duration = 250
            this.startAnimation(anim)
            this.visibility = View.GONE
        }
    }
}
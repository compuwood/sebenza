package mad.apps.sabenza.ui.widget.tablayout

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.util.TabName
import mad.apps.sabenza.ui.util.convertDpToPixel

open class TabContainerView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    val tabLayout by bindView<TabLayout>(R.id.container_tab_layout)
    val viewPager by bindView<ViewPager>(R.id.view_pager)

    val childLayout: Int
    var isEmployer: Boolean = false

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.TabContainerView, 0, 0)
        this.childLayout = attributes.getResourceId(R.styleable.TabContainerView_childLayout, 0)
        this.isEmployer = attributes.getBoolean(R.styleable.TabContainerView_isEmployer, false)

        LayoutInflater.from(context).inflate(R.layout.tab_container_contents, this, true)

        if (isEmployer) {
            val params = tabLayout.layoutParams as MarginLayoutParams
            params.setMargins(0, 0, convertDpToPixel(78f, context), 0)
            tabLayout.layoutParams = params
        }
    }

    override fun onDetachedFromWindow() {
        viewPager.adapter = null
        if (listener != null) {
            viewPager.removeOnPageChangeListener(listener)
        }

        super.onDetachedFromWindow()
    }

    private var listener: ViewPager.OnPageChangeListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        val adapter = buildAdapter()
        viewPager.adapter = adapter

        if (adapter.count > 1) {
            tabLayout.setupWithViewPager(viewPager)
            for (tabIndex in 0..tabLayout.tabCount-1) { tabLayout.getTabAt(tabIndex)?.setCustomView(R.layout.tab_textview) }
        } else {
            tabLayout.visibility = View.GONE
        }

        viewPager.addOnPageChangeListener(listener)

        viewPager.setCurrentItem(0, false)

    }

    open fun buildAdapter() : TabPagerAdapter {

        if (isEmployer) {
            return TabPagerAdapter(childLayout, arrayListOf(TabName.UNFILLED, TabName.FILLED)  )
        }
        return TabPagerAdapter(childLayout,arrayListOf(TabName.FORYOU, TabName.APPLIED, TabName.CONFIRMED))
    }

}
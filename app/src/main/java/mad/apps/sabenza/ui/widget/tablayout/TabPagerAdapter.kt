package mad.apps.sabenza.ui.widget.tablayout

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mad.apps.sabenza.ui.util.TabName
import java.util.*

class TabPagerAdapter(var childLayout: Int, var configs : ArrayList<TabName>) : PagerAdapter() {

    override fun getCount(): Int {
        return configs.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(childLayout, container, false)

        (view as? ProjectsJobsContainerView)?.setTabType(configs[position])

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (configs[position]) {
            TabName.FILLED -> return "Filled"
            TabName.UNFILLED -> return "Unfilled"
            TabName.FORYOU -> return "For You"
            TabName.APPLIED -> return "Applied"
            TabName.CONFIRMED -> return "Confirmed"
        }
    }
}
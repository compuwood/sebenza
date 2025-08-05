package mad.apps.sabenza.ui

import android.support.test.rule.ActivityTestRule
import android.view.View
import android.view.ViewGroup
import mad.apps.sabenza.SabenzaActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule

abstract class BaseUITest {

    @JvmField
    @Rule
    var activityTestRule: ActivityTestRule<SabenzaActivity> = object : ActivityTestRule<SabenzaActivity>(SabenzaActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
        }
    }

    protected fun uiDelay() {
        Thread.sleep(500)
    }

    protected fun networkDelay() {
        Thread.sleep(3000)
    }

    protected fun observationDelay() {
        Thread.sleep(4000)
    }
    protected fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
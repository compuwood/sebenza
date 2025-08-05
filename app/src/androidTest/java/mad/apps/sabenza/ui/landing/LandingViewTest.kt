package mad.apps.sabenza.ui.landing

import android.support.test.runner.AndroidJUnit4
import mad.apps.sabenza.ui.BaseUITest
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.landing.view.*
import mad.apps.sabenza.R
import org.junit.Rule
import java.text.DecimalFormat

@RunWith(AndroidJUnit4::class)
class LandingViewTest : BaseUITest() {

    @Test
    fun testSplashScreenIsVisible() {
        onView(withId(R.id.splash_sabenza_logo)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun testGetStartedButton() {
        onView(withId(R.id.get_started_button)).perform(click())
        uiDelay()
        onView(withId(R.id.goto_employer_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.goto_employee_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }



}
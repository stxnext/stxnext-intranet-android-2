package intranet2.stxnext.com.test;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.AboutActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Łukasz Ciupa on 20.03.17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AboutScreenTest {

    @Rule
    public ActivityTestRule<AboutActivity> activityTestRule = new ActivityTestRule<AboutActivity>(AboutActivity.class);

    @Test
    public void screenLoads_checkAboutScreenLoaded() {
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.about))));
        checkProperLocationDisplayed();
        onView(allOf(withId(R.id.call_button), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.map_button), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.navigation_buttons), isDisplayed())).check(matches(isDisplayed()));
    }

    @Test
    public void swipeViewPager_checkProperOfficeDisplayed() {
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        checkProperLocationDisplayed();
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        checkProperLocationDisplayed();
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        checkProperLocationDisplayed();
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        checkProperLocationDisplayed();
        onView(withId(R.id.viewpager)).perform(swipeRight());
        checkProperLocationDisplayed();
        onView(withId(R.id.viewpager)).perform(swipeRight());
        checkProperLocationDisplayed();
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        checkProperLocationDisplayed();
    }

    private void checkProperLocationDisplayed() {
        onView(allOf(isDescendantOfA(withId(R.id.sliding_tabs)), withText(activityTestRule.getActivity().getPageTitle()))).check(matches(isDisplayed()));
        onView(allOf(isDescendantOfA(withId(R.id.user_info_container)), withText(activityTestRule.getActivity().getPageTitle()))).check(matches(isDisplayed()));
        onView(allOf(isDescendantOfA(withId(R.id.user_info_container)), withText(activityTestRule.getActivity().getOffice().getStreet()))).check(matches(isDisplayed()));
    }



}

package intranet2.stxnext.com.test;

import android.os.Build;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.AbsencesActivity;
import com.stxnext.intranet2.utils.Session;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Łukasz Ciupa on 15.03.17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AbsencesScreenTest {

    private static final int LAST_ELEMENT_POSITION = 5;

    @Rule
    public ActivityTestRule<AbsencesActivity> mActivityRule = new ActivityTestRule<>(
            AbsencesActivity.class);

    @Before
    public void logAndSetPermissions() {
        Session session = Session.getInstance(mActivityRule.getActivity());
        if (!session.isLogged()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getInstrumentation().getUiAutomation().executeShellCommand(
                        "pm grant " + getTargetContext().getPackageName()
                                + " android.permission.READ_PHONE_STATE");
            }
            session.setUserId("100");
        }
    }

    @Test
    public void screenLoads_checkAbsencesLoaded() throws InterruptedException {
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.absences))));
        onView(withText(mActivityRule.getActivity().getCurrentPageTitle())).check(matches(isDisplayed()));
        Thread.sleep(300); // setting of count text view is delayed
        onView(withId(R.id.count_text_view)).check(matches(withText(String.valueOf(mActivityRule.getActivity().getCurrentElementsCount()))));
    }

    @Test
    public void scrollToLastElement_checkItsText() throws InterruptedException {
        // There is a need to define that we mean recycle view that is displayed because
        // there are other ones inside view pager with the same view hierarchy.
        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.scrollToPosition(LAST_ELEMENT_POSITION));
        String lastEmployeeName = "Bert Lawnmower";
        onView(withText(lastEmployeeName)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnEmployee_checkProfileActivityDisplayed() throws InterruptedException {
        String employeeName = "Mieszko Wrightwheel";
        onView(allOf(withId(R.id.item_container), hasDescendant(withText(employeeName)), isDisplayed())).perform(click());
        // Our mocked profile has got John Smith user name
        employeeName = "John Smith";
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(employeeName))));
        onView(withId(R.id.user_info_container)).check(matches(hasDescendant(withText(employeeName))));
    }

    @Test
    public void swipeViewPager_checkProperListsDisplayed() throws InterruptedException {
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        checkProperListAndCountDisplayed();
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        checkProperListAndCountDisplayed();
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.viewpager)).perform(swipeRight());
        onView(withId(R.id.viewpager)).perform(swipeRight());
        checkProperListAndCountDisplayed();
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.viewpager)).perform(swipeRight());
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.viewpager)).perform(swipeRight());
        checkProperListAndCountDisplayed();
    }

    private void checkProperListAndCountDisplayed() throws InterruptedException {
        onView(withText(mActivityRule.getActivity().getCurrentPageTitle())).check(matches(isDisplayed()));
        Thread.sleep(300); // setting of count text view is delayed
        onView(withId(R.id.count_text_view)).check(matches(withText(String.valueOf(mActivityRule.getActivity().getCurrentElementsCount()))));
    }


}

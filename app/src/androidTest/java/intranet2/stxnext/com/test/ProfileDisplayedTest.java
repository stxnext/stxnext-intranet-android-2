package intranet2.stxnext.com.test;

import android.os.Build;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.MyProfileActivity;
import com.stxnext.intranet2.utils.Session;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static intranet2.stxnext.com.test.matcher.ImageViewHasDrawableMatcher.hasDrawable;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Łukasz Ciupa on 09.03.17.
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileDisplayedTest {

    private static String USER_NAME = "John Smith";

    @Rule
    public ActivityTestRule<MyProfileActivity> mActivityRule = new ActivityTestRule<>(
            MyProfileActivity.class);

    @Before
    public void logAndSetPermissions() {
        Session session = Session.getInstance(mActivityRule.getActivity());
        if (!session.isLogged()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getInstrumentation().getUiAutomation().executeShellCommand(
                        "pm grant " + getTargetContext().getPackageName()
                                + " android.permission.READ_PHONE_STATE");
            }
            onView(withId(R.id.sign_in_button)).perform(click());
        }
    }

    @Test
    public void applicationLoads_checkProfileLoaded() {
        onView(withId(R.id.worked_hours_container)).check(matches(isDisplayed()));
        boolean isSuperHeroMode = Session.getInstance(mActivityRule.getActivity()).isSuperHeroModeEnabled();
        if (isSuperHeroMode) {
            onView(withId(R.id.profile_image_view)).check(matches(allOf(isEnabled(), isDisplayed())));
        } else {
            onView(withId(R.id.profile_image_view_standard)).check(matches(allOf(isEnabled(), isDisplayed(), hasDrawable())));
        }
        onView(withId(R.id.user_info_container)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.first_name_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.first_name_text_view)).check(matches(withText(USER_NAME)));
        assertThat(mActivityRule.getActivity().getSupportActionBar().getTitle().toString(), is(USER_NAME));
    }

    @Test
    public void clickOnWoredHours_ShowsWorkedHoursActivity() {
        onView(withId(R.id.worked_hours_container)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.worked_hours_heading))));
    }


}

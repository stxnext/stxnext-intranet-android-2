package intranet2.stxnext.com.test;

import android.os.Build;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.SettingsActivity;
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
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by Łukasz Ciupa on 20.03.17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsScreenTest {

    @Rule
    public ActivityTestRule<SettingsActivity> activityTestRule = new ActivityTestRule<SettingsActivity>(SettingsActivity.class);

    @Before
    public void logAndSetPermissions() {
        Session session = Session.getInstance(activityTestRule.getActivity());
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
    public void screenLoads_checkSettingsScreenLoaded() {
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.settings))));
        onView(withId(R.id.superhero_mode_container)).check(matches(isDisplayed()));
        onView(withId(R.id.time_report_notification_container)).check(matches(isDisplayed()));
        onView(withId(R.id.call_notification_container)).check(matches(isDisplayed()));
        onView(withId(R.id.logout_button)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void clickSuperheroSwitch_checkIsSwitched() {
        onView(withId(R.id.superhero_switch)).check(matches(not(isChecked())));
        onView(withId(R.id.superhero_switch)).perform(click());
        onView(withId(R.id.superhero_switch)).check(matches(isChecked()));
        onView(withId(R.id.superhero_switch)).perform(click());
        onView(withId(R.id.superhero_switch)).check(matches(not(isChecked())));
    }

}

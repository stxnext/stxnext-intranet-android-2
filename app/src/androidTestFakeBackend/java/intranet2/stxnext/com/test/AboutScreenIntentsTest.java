package intranet2.stxnext.com.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.AboutActivity;
import com.stxnext.intranet2.model.Office;
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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Łukasz Ciupa on 20.03.17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AboutScreenIntentsTest {

    private static String PACKAGE_ANDROID_DIALER = "com.google.android.dialer";

    @Rule
    public IntentsTestRule<AboutActivity> activityRule = new IntentsTestRule<>(
            AboutActivity.class);

    @Before
    public void logAndSetPermissions() {
        Session session = Session.getInstance(activityRule.getActivity());
        if (!session.isLogged()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getInstrumentation().getUiAutomation().executeShellCommand(
                        "pm grant " + getTargetContext().getPackageName()
                                + " android.permission.READ_PHONE_STATE");
            }
            session.setUserId("100");
        }
    }

    @Before
    public void stubAllExternalIntents() {
        // Stub all external intents so there is no starting of external apps
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickCallButton_checkActionDialIntentIsRun() {
        onView(allOf(withId(R.id.call_button), isDisplayed())).perform(click());
        checkActionDialIntent();
    }

    @Test
    public void swipeThenclickCallButton_checkActionDialIntentIsRun() {
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(allOf(withId(R.id.call_button), isDisplayed())).perform(click());
        checkActionDialIntent();
    }

    private void checkActionDialIntent() {
        intended(allOf(
                hasAction(Intent.ACTION_DIAL),
                hasData(Uri.parse("tel:" + activityRule.getActivity().getOffice().getTel())),
                toPackage(PACKAGE_ANDROID_DIALER)));
    }

    @Test
    public void clickMapButton_checkMapActionViewIntentIsRun() {
        onView(allOf(withId(R.id.map_button), isDisplayed())).perform(click());
        Office office = activityRule.getActivity().getOffice();
        checkMapActionViewIntent(office);
    }

    @Test
    public void swipeThenClickMapButton_checkMapActionViewIntentIsRun() {
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(allOf(withId(R.id.map_button), isDisplayed())).perform(click());
        Office office = activityRule.getActivity().getOffice();
        checkMapActionViewIntent(office);
    }

    private void checkMapActionViewIntent(Office office) {
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("http://maps.google.com/maps?q=loc:"
                        + office.getLat() + ", " + office.getLon()))
        ));
    }

    @Test
    public void clickNavigationButton_checkNavigationActionViewIntentIsRun() {
        onView(allOf(withId(R.id.navigation_buttons), isDisplayed())).perform(click());
        Office office = activityRule.getActivity().getOffice();
        checkNavigationActionViewIntent(office);
    }

    @Test
    public void swipeThenClickNavigationButton_checkNavigationActionViewIntentIsRun() {
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(allOf(withId(R.id.navigation_buttons), isDisplayed())).perform(click());
        Office office = activityRule.getActivity().getOffice();
        checkNavigationActionViewIntent(office);
    }

    private void checkNavigationActionViewIntent(Office office) {
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("http://maps.google.com/maps?daddr="
                        + office.getLat() + ", " + office.getLon()))
        ));
    }
}

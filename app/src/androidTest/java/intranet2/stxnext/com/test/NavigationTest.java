package intranet2.stxnext.com.test;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.ImageButton;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.MyProfileActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by Łukasz Ciupa on 13.03.17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NavigationTest {

    @Rule
    public ActivityTestRule<MyProfileActivity> activityTestRule = new ActivityTestRule<MyProfileActivity>(MyProfileActivity.class);

    @Test
    public void clickOnAppHomeIcon_OpensNavigation() {
        onView(ViewMatchers.withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)));
        // Open Drawer
        onView(allOf(
                isAssignableFrom(ImageButton.class),
                withParent(isAssignableFrom(Toolbar.class)))).perform(click());
        // Check if drawer is open
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT))); // Left drawer is open open.
    }

    private void openDrawer() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer
    }

    @Test
    public void clickOnAbsencesNavigationItem_ShowsAbsencesScreen() {
        openDrawer();
        onView(withText(R.string.absences)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.absences))));
    }

    @Test
    public void clickOnEmployeesNavigationItem_ShowsEmployeesScreen() {
        openDrawer();
        onView(withText(R.string.employees_list)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.employees_list))));
    }

    @Test
    public void clickOnTeamsNavigationItem_ShowsTeamsScreen() {
        openDrawer();
        onView(withText(R.string.teams_list)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.teams_list))));
    }

    @Test
    public void clickOnSettingsNavigationItem_ShowsSettingsScreen() {
        openDrawer();
        onView(withText(R.string.settings)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.settings))));
    }

    @Test
    public void clickOnOfficesNavigationItem_ShowsOfficesScreen() {
        openDrawer();
        onView(withText(R.string.about)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.about))));
    }

    @Test
    public void clickOnFloatingButton_ShowsFloatingMenuFragment() {
        onView(withId(R.id.floating_button)).perform(click());
        onView(withId(R.id.floating_view_container)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFloatingButtonThenLateOption_ShowsReportLateActivity() throws InterruptedException {
        onView(withId(R.id.floating_button)).perform(click());
        Thread.sleep(500); // It's not necessary if animations are set off in device's settings
        onView(withId(R.id.floating_view_container)).check(matches(isDisplayed()));
        onView(withId(R.id.floating_late_option)).perform(click());
        // It's necessary even if animations are turned off in device's settings because
        // there is a Handler().postDelayed() in MyPorofileActivity.onFloatingMenuItemClick
        // which causes that espresso runs tests anyway. So IdlingResource should be needed for
        // animations which is not a good idea for code's cleanliness
        Thread.sleep(500);
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.late))));
    }

    @Test
    public void clickOnFloatingButtonThenAddHoursOption_ShowsAddHoursActivity() throws InterruptedException {
        onView(withId(R.id.floating_button)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.floating_view_container)).check(matches(isDisplayed()));
        onView(withId(R.id.floating_add_hours)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.add_hours))));
    }

    @Test
    public void clickOnFloatingButtonOutOfOfficeOption_ShowsOutOfOfficeActivity() throws InterruptedException {
        onView(withId(R.id.floating_button)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.floating_view_container)).check(matches(isDisplayed()));
        onView(withId(R.id.floating_out_of_office_option)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.report_out_office))));
    }

    @Test
    public void clickOnFloatingAbsenceOption_ShowsAbsenceActivity() throws InterruptedException {
        onView(withId(R.id.floating_button)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.floating_view_container)).check(matches(isDisplayed()));
        onView(withId(R.id.floating_absence_option)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.submit_absence))));
    }

}

package intranet2.stxnext.com.test;

import android.os.Build;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.EmployeesActivity;
import com.stxnext.intranet2.utils.Session;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static intranet2.stxnext.com.test.matcher.FirstOccurenceMatcher.first;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Łukasz Ciupa on 17.03.17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EmployeesScreenTest {

    private static final int LAST_ELEMENT_POSITION = 32;

    @Rule
    public ActivityTestRule<EmployeesActivity> mActivityRule = new ActivityTestRule<>(
            EmployeesActivity.class);

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
    public void screenLoads_checkEmployeesScreenLoaded() {
        String employee = "John Smith";
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.employees_list))));
        onView(withText(employee)).check(matches(isDisplayed()));
    }

    @Test
    public void scrollToLastElement_checkItsText() {
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(LAST_ELEMENT_POSITION));
        String lastEmployee = "Jack Evening";
        onView(withText(lastEmployee)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnEmployee_checkProfileActivityDisplayed() throws InterruptedException {
        String employeeName = "Lucas Vega";
        onView(allOf(withId(R.id.item_container), hasDescendant(withText(employeeName)))).perform(click());
        // Our mocked profile has got John Smith user name
        employeeName = "John Smith";
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(employeeName))));
        onView(withId(R.id.user_info_container)).check(matches(hasDescendant(withText(employeeName))));
    }

    @Test
    public void clickOnSearchAndSearchForEmployee_checkIfOthersAreNotDisplayed() throws InterruptedException {
        String searchText = "Lucas";
        onView(withId(R.id.menu_search)).perform(click());
        onView(withId(R.id.search_container)).check(matches(isDisplayed()));
        onView(withId(R.id.search_edit_text)).perform(typeText(searchText), closeSoftKeyboard());
        onView(first(withText("Lucas Vega"))).check(matches(isDisplayed()));
        onView(withText("John Smith")).check(doesNotExist());
        pressBack();
        Thread.sleep(500);
        onView(withText("John Smith")).check(matches(isDisplayed()));
    }


}

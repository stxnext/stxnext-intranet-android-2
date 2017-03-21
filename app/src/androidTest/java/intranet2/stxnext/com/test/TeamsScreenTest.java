package intranet2.stxnext.com.test;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.TeamsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static intranet2.stxnext.com.test.matcher.FirstOccurenceMatcher.first;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Łukasz Ciupa on 20.03.17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TeamsScreenTest {

    @Rule
    public ActivityTestRule<TeamsActivity> activityTestRule = new ActivityTestRule<TeamsActivity>(TeamsActivity.class);

    @Test
    public void screenLoads_checkTeamsScreenLoaded() {
        String team = "Mobile Team";
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.teams_list))));
        onView(withText(team)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnTeam_checkTeamActivityDisplayed() {
        String team = "Mobile Team";
        onView(allOf(withId(R.id.item_container), hasDescendant(withText(team)))).perform(click());
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(team))));
        String employee = "John Smith";
        onView(first(withText(employee))).check(matches(isDisplayed()));
        onView(withId(R.id.team_image)).check(matches(isDisplayed()));
    }
}

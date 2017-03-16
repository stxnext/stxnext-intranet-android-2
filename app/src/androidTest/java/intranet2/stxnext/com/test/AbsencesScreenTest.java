package intranet2.stxnext.com.test;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.AbsencesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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

    @Test
    public void screenLoads_checkAbsencesLoaded() throws InterruptedException {
        onView(withId(R.id.toolbar)).check(matches(withChild(withText(R.string.absences))));
        onView(withText(mActivityRule.getActivity().getCurrentPageTitle())).check(matches(isDisplayed()));
        Thread.sleep(300); // setting of count text view is delayed
        onView(withId(R.id.count_text_view)).check(matches(withText(String.valueOf(mActivityRule.getActivity().getCurrentElementsCount()))));
    }

    @Test
    public void scrollToLastElement_checkItsText() throws InterruptedException {
        // There is a need define that we mean recycle that is displayed because
        // there are other ones inside view pager with the same view hierarchy.
        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.scrollToPosition(LAST_ELEMENT_POSITION));
        String lastEmployeeSurname = "Bert Lawnmower";
        onView(withText(lastEmployeeSurname)).check(matches(isDisplayed()));
    }



}

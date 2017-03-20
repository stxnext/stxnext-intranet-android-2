package intranet2.stxnext.com.test.matcher;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import org.hamcrest.Description;

/**
 * Created by Łukasz Ciupa on 20.03.17.
 */

public class SwitchCompatIsOnMatcher {

    public static BoundedMatcher<View, SwitchCompat> isOn() {
        return new BoundedMatcher<View, SwitchCompat>(SwitchCompat.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(SwitchCompat switchCompat) {
                return false;
            }
        };
    }
}

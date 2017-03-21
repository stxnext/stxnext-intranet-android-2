package intranet2.stxnext.com.test.matcher;

/**
 * Created by Łukasz Ciupa on 10.03.17.
 */

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A custom {@link Matcher} for Espresso that checks if an {@link ImageView} has a drawable applied
 * to it.
 */
public class ImageViewHasDrawableMatcher {

    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }
}
package intranet2.stxnext.com.test.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Łukasz Ciupa on 17.03.17.
 */

public class FirstOccurenceMatcher {

    static public <T> Matcher<T> first(final Matcher<T> matcher) {
        checkNotNull(matcher);
        return new TypeSafeMatcher<T>() {

            boolean isFirst = true;

            @Override
            public boolean matchesSafely(T entry) {
                if (isFirst && matcher.matches(entry)) {
                    isFirst = false;
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("First: ");
                matcher.describeTo(description);
            }
        };
    }

}

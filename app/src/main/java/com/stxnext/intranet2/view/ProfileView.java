package com.stxnext.intranet2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.stxnext.intranet2.R;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class ProfileView extends FrameLayout {

    private ImageView imageView;

    public ProfileView(Context context) {
        super(context);
    }

    public ProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        imageView = (ImageView) findViewById(R.id.profile_image_view);
        if (imageView == null) {
            throw new RuntimeException("ImageView with id profile_image_view not found in ProfileView");
        }

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        if (scrollView == null) {
            throw new RuntimeException("ScrollView with id scroll_view not found in ProfileView");
        }

        final View floatingButton = findViewById(R.id.floating_button);
        final int maxFloatingButtonPosition = getContext().getResources().getDimensionPixelSize(R.dimen.max_floating_button_position);
        final int floatinButtonPosition = getContext().getResources().getDimensionPixelSize(R.dimen.profile_floating_button_position);
        final int fbDiff = floatinButtonPosition - maxFloatingButtonPosition;

        final View toolbar = findViewById(R.id.toolbar);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int translationY = scrollView.getScrollY();
                imageView.setTranslationY(-translationY / 4);
                toolbar.setTranslationY(-translationY / 4);

                if (floatingButton != null) {
                    if (translationY < fbDiff) {
                        floatingButton.setTranslationY(-translationY);
                    }

                }
            }
        });

    }

}

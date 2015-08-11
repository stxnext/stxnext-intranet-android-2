package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.DrawerAdapter;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.fragment.FloatingMenuFragment;
import com.stxnext.intranet2.model.DrawerMenuItems;
import com.stxnext.intranet2.utils.STXToast;
import com.stxnext.intranet2.utils.Session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Bartosz Kosarzycki on 2015-08-10.
 */

public class PicturePreviewActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private String pictureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_picture_preview);
        //todo: THIS SHOULD BE HIGH RESOLUTION PHOTO - IS it available from the backend?
        pictureUrl = "https://intranet.stxnext.pl" + getIntent().getExtras().getString("pictureUrl");
        //Picasso.with(this).invalidate(pictureUrl);
        loadViews();
    }

    private void loadViews() {
        profileImageView = (ImageView) findViewById(R.id.profile_image_view);
        final View outerLayout = findViewById(R.id.activity_picture_preview_outerLayout);

        Picasso.with(this)
                .load(pictureUrl)
                .placeholder(R.drawable.avatar_placeholder)
                .into(profileImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        profileImageView.animate().alpha(1).setDuration(200);
                    }

                    @Override
                    public void onError() {
                        profileImageView.animate().alpha(1).setDuration(200);
                    }
                });

        ViewTreeObserver observer = outerLayout .getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                outerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                onViewCreated();
            }
        });
    }

    private void onViewCreated() {
        RelativeLayout outerLayout = (RelativeLayout) this.findViewById(R.id.activity_picture_preview_outerLayout);
        final int width = outerLayout.getWidth();
        final int height = outerLayout.getHeight();
        profileImageView.animate().scaleX(2.5f).scaleY(2.5f).setDuration(400).setStartDelay(180);
    }

    @Override public void onBackPressed() {
        final int BACK_ANIM_DURATION_MILLIS = 300;
        profileImageView.animate().scaleX(1f).scaleY(1f).setDuration(BACK_ANIM_DURATION_MILLIS);

        final PicturePreviewActivity selfRef = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                selfRef.supportFinishAfterTransition();
            }
        }, (int)2.0*BACK_ANIM_DURATION_MILLIS);
    }

    public void onOutOfPictureClick(View v) {
        onBackPressed();
    }
}

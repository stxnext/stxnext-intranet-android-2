package com.stxnext.intranet2.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
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
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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

import io.fabric.sdk.android.Fabric;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */

public class CommonProfileActivity extends AppCompatActivity implements UserApiCallback {

    protected ImageView profileImageView;
    protected boolean superHeroModeEnabled;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initializeProfileImageView() {
        superHeroModeEnabled = Session.getInstance(this).isSuperHeroModeEnabled();
        if (superHeroModeEnabled) {
            findViewById(R.id.standard_profile_header_container).setVisibility(View.GONE);
            profileImageView = (ImageView) findViewById(R.id.profile_image_view);

        } else {
            profileImageView = (ImageView) findViewById(R.id.profile_image_view_standard);
        }

        if (!superHeroModeEnabled) {
            profileImageView.setAlpha(0.6f);
        }
    }

    public void onProfilePictureClick(View v) {

        if (currentUser != null) {
            Intent intent = new Intent(this, PicturePreviewActivity.class);
            intent.putExtra("pictureUrl", currentUser.getPhoto());
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this, profileImageView, "profileImageView").toBundle());
            } else {
                startActivity(intent);
            }

        } else
            Toast.makeText(CommonProfileActivity.this, "User not loaded", Toast.LENGTH_SHORT).show();
    }

    @Override public void onUserReceived(User user) {
        this.currentUser = user;
    }

    @Override public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {}
    @Override public void onOutOfOfficeResponse(boolean entry) {}
    @Override public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {}
    @Override public void onRequestError() {}
}

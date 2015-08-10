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
 * Created by Bartosz Kosarzycki on 2015-08-10.
 */

public class PicturePreviewActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private String pictureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_picture_preview);
        pictureUrl = getIntent().getExtras().getString("pictureUrl");
        loadViews();
    }

    private void loadViews() {
        profileImageView = (ImageView) findViewById(R.id.profile_image_view);

        Picasso.with(this)
                .load("https://intranet.stxnext.pl" + pictureUrl)
                .placeholder(R.drawable.avatar_placeholder)
                .into(profileImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        profileImageView.animate().alpha(1).setDuration(500);
                    }

                    @Override
                    public void onError() {
                        profileImageView.animate().alpha(1).setDuration(500);
                    }
                });
    }


}

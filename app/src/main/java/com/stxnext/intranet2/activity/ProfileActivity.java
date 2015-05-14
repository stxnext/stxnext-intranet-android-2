package com.stxnext.intranet2.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.DrawerAdapter;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.fragment.FloatingMenuFragment;
import com.stxnext.intranet2.model.DrawerMenuItems;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ProfileActivity extends AppCompatActivity implements UserApiCallback {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        findViewById(R.id.floating_button).setVisibility(View.GONE);
        findViewById(R.id.worked_hours_container).setVisibility(View.GONE);
        findViewById(R.id.edit_profile_button).setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserApi userApi = new UserApiImpl(this);
        userApi.requestForUser(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    @Override
    public void onUserReceived(User user) {

    }

}

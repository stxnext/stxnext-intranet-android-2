package com.stxnext.intranet2.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.ViewGroup;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.fragment.FloatingMenuFragment;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ProfileActivity extends AppCompatActivity
        implements UserApiCallback, FloatingMenuFragment.OnFlotingMenuItemClickListener {

    private static final String TAG = "ProfileActivity";
    private static final int LOGIN_REQUEST = 1;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setTitle("bLA BAL BA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureDrawer(toolbar);
        setContentView(R.layout.acitivty_profile);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        prepareFloatinButton();

        UserApi userApi = new UserApiImpl(this);
        userApi.requestForUser(null);

        if (isLogged()) {
            loadProfile();
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST);
        }

    }

    private void configureDrawer(final Toolbar toolbar) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        String[] drawerElements = {"Nieobecności", "Lista pracowników", "Settingsy"};
        ArrayAdapter<String> drawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerElements);
        drawerList.setAdapter(drawerAdapter);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                drawerLayout, /* DrawerLayout object */
                toolbar, /* we use our own toolbar */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */) {

            public void onDrawerClosed(View view) {
                // getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                // getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }
    private void prepareFloatinButton() {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.floating_button);
        final View plusView = viewGroup.getChildAt(0);
        if (plusView != null) {
            viewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plusView.animate().rotationBy(135).setDuration(200).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Fragment fragment = getFragmentManager().findFragmentByTag("Asdasd");

                            if (fragment == null || !fragment.isAdded()) {
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.floating_menu_container, new FloatingMenuFragment(), "Asdasd")
                                        .setCustomAnimations(
                                                android.R.animator.fade_in, android.R.animator.fade_out,
                                                android.R.animator.fade_in, android.R.animator.fade_out)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                getFragmentManager().popBackStackImmediate();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_REQUEST:
                if (resultCode == LoginActivity.LOGIN_OK) {
                    loadProfile();
                } else {

                }
                break;
            default:
                break;
        }
    }

    //TODO
    private void loadProfile() {
        Log.d(TAG, "loadProfile()");
    }

    //TODO
    private boolean isLogged() {
        return false;
    }

    @Override
    public void onUserReceived(User user) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onFloatingMenuItemClick(int option) {

    }
}

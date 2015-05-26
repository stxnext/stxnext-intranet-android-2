package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.DrawerAdapter;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.fragment.FloatingMenuFragment;
import com.stxnext.intranet2.model.DrawerMenuItems;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.Session;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class MyProfileActivity extends AppCompatActivity
        implements UserApiCallback,
        FloatingMenuFragment.OnFloatingMenuItemClickListener {

    private static String FLOATING_MENU_TAG = "floating_menu";

    private static final int LOGIN_REQUEST = 1;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ViewGroup floatingButton;
    private View plusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        configureDrawer();
        prepareFloatingButton();

        if (isLogged()) {
            loadProfile();
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST);
        }

    }

    private void configureDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        final DrawerAdapter drawerAdapter = new DrawerAdapter(this);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerMenuItems option = drawerAdapter.getItem(position);
                switch (option) {
                    case ABSENCES:
                        startActivity(new Intent(MyProfileActivity.this, AbsenceActivity.class));
                        break;
                    case EMPLOYEES:
                        startActivity(new Intent(MyProfileActivity.this, EmployeesActivity.class));
                        break;
                    case SETTINGS:
                        startActivity(new Intent(MyProfileActivity.this, SettingsActivity.class));
                        break;
                }

                drawerLayout.closeDrawers();
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void prepareFloatingButton() {
        floatingButton = (ViewGroup) findViewById(R.id.floating_button);
        plusView = floatingButton.getChildAt(0);
        if (plusView != null) {
            floatingButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    toggleFloatingMenu();
                }
            });
        }
    }

    private void toggleFloatingMenu() {
        FloatingMenuFragment fragment = (FloatingMenuFragment) getFragmentManager().findFragmentByTag(FLOATING_MENU_TAG);
        if (fragment == null || !fragment.isAdded()) {
            plusView.animate()
                    .rotationBy(225)
                    .setDuration(200)
                    .setInterpolator((new LinearOutSlowInInterpolator()));

            int[] floatingButtonPosition = new int[2];
            floatingButton.getLocationInWindow(floatingButtonPosition);
            int floatingButtonTransition = floatingButtonPosition[1] + floatingButton.getHeight();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.floating_menu_container, FloatingMenuFragment.newInstance(floatingButtonTransition), FLOATING_MENU_TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragment.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_REQUEST:
                if (resultCode == LoginActivity.LOGIN_OK) {
                    loadProfile();
                } else if (resultCode == LoginActivity.LOGIN_FAILED) {
                    Toast.makeText(this, R.string.login_failure, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //TODO
    private void loadProfile() {
        Log.d(Config.TAG, "loadProfile()");
        UserApi userApi = new UserApiImpl(this);
        userApi.requestForUser(null);
    }

    //TODO
    private boolean isLogged() {
        return Session.getInstance(this).isLogged();
    }

    @Override
    public void onUserReceived(User user) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onFloatingMenuItemClick(int option) {
        toggleFloatingMenu();
    }

    @Override
    public void onFloatingMenuClose() {
        plusView.animate()
                .rotationBy(225)
                .setDuration(200)
                .setInterpolator((new LinearOutSlowInInterpolator()));
    }

    @Override
    public void onBackPressed() {
        FloatingMenuFragment fragment = (FloatingMenuFragment) getFragmentManager().findFragmentByTag(FLOATING_MENU_TAG);
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers();
        } else if (fragment != null && fragment.isAdded()) {
            fragment.close();
        } else {
            super.onBackPressed();
        }
    }
}

package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
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
        implements UserApiCallback, FloatingMenuFragment.OnFloatingMenuItemClickListener {

    private static String FLOATING_MENU_TAG = "floating_menu";

    private static final int LOGIN_REQUEST = 1;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ViewGroup floatingButton;
    private View plusView;

    private TextView firstNameTextView;
    private TextView roleTextView;
    private TextView officeTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView skypeTextView;
    private TextView ircTextView;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_root);
        loadViews();

        setSupportActionBar(toolbar);

        configureDrawer();
        prepareFloatingButton();

        if (isLogged()) {
            loadProfile();
        } else {
            runLoginActivity();
        }

    }

    private void loadViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        firstNameTextView = (TextView) findViewById(R.id.first_name_text_view);
        roleTextView = (TextView) findViewById(R.id.role_text_view);
        officeTextView = (TextView) findViewById(R.id.office_text_view);
        emailTextView = (TextView) findViewById(R.id.email_text_view);
        phoneTextView = (TextView) findViewById(R.id.phone_text_view);
        skypeTextView = (TextView) findViewById(R.id.skype_text_view);
        ircTextView = (TextView) findViewById(R.id.irc_text_view);
        profileImageView = (ImageView) findViewById(R.id.profile_image_view);
    }

    private void runLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, LOGIN_REQUEST);
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
                        startActivity(new Intent(MyProfileActivity.this, AbsencesActivity.class));
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
                } else if (resultCode == LoginActivity.LOGIN_CANCELED) {
                    Toast.makeText(this, "Login canceled.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void loadProfile() {
        Log.d(Config.TAG, "loadProfile()");
        UserApi userApi = new UserApiImpl(this, this);
        userApi.requestForUser(Session.getInstance(this).getUserId());
    }

    private boolean isLogged() {
        return Session.getInstance(this).isLogged();
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
    public void onFloatingMenuItemClick(final int option) {
        toggleFloatingMenu();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Class intentClass = null;
                switch (option) {
                    case FloatingMenuFragment.LATE:
                        intentClass = ReportLateActivity.class;
                        break;
                    case FloatingMenuFragment.HOLIDAY:
                        intentClass = ReportHolidayActivity.class;
                        break;
                    case FloatingMenuFragment.OUT_OF_OFFICE:
                        intentClass = ReportOutOfOfficeActivity.class;
                        break;

                }

                Intent intent = new Intent(MyProfileActivity.this, intentClass);
                startActivity(intent);
            }
        }, 300);

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

    @Override
    public void onUserReceived(User user) {
        String userName = user.getFirstName() + " " + user.getLastName();
        getSupportActionBar().setTitle(userName);
        firstNameTextView.setText(userName);
        roleTextView.setText(user.getRole());
        officeTextView.setText(user.getLocalization());
        emailTextView.setText(user.getEmail());

        String imageAddress = "https://intranet.stxnext.pl" + user.getPhoto();
        Picasso.with(this).load(imageAddress).placeholder(R.drawable.avatar_placeholder)
                .resizeDimen(R.dimen.profile_image_height, R.dimen.profile_image_height)
                .centerCrop()
                .into(profileImageView);

        if (user.getPhoneNumber() != "null") {
            phoneTextView.setText(user.getPhoneNumber());
        }
        if (user.getSkype() != "null") {
            skypeTextView.setText(user.getSkype());
        }
        if (user.getIrc() != "null") {
            ircTextView.setText(user.getIrc());
        }
    }

    @Override
    public void onAbsenceDaysLeft(int absenceDaysLeft) {
        // nothing to do
    }

    @Override
    public void onLatenessResponse(String latenessResponse) {
        // nothing to do
    }
}

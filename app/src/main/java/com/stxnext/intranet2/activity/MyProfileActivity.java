package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.StxActionBarDrawerToggle;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.DrawerAdapter;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;
import com.stxnext.intranet2.fragment.FloatingMenuFragment;
import com.stxnext.intranet2.model.DrawerMenuItems;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.NotificationUtils;
import com.stxnext.intranet2.utils.STXToast;
import com.stxnext.intranet2.utils.Session;

import java.util.Calendar;
import java.util.List;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */

public class MyProfileActivity extends CommonProfileActivity
        implements UserApiCallback, FloatingMenuFragment.OnFloatingMenuItemClickListener {

    private static final int LOGIN_REQUEST = 1;

    private static final int SETTINGS_REQUEST = 1232;

    private static String FLOATING_MENU_TAG = "floating_menu";

    private StxActionBarDrawerToggle drawerToggle;
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

    private View progressView;
    private View userInfoCardView;
    private View workedHoursContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

//        super.initializeProfileImageView();
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

    @Override
    public void initializeContentView() {
        setContentView(R.layout.activity_profile_root);
    }

    private void loadViews() {
        progressView = findViewById(R.id.progress_container);
        progressView.setVisibility(View.VISIBLE);

        userInfoCardView = findViewById(R.id.user_info_container);
        userInfoCardView.setAlpha(0);
        userInfoCardView.setScaleX(0.6f);
        userInfoCardView.setScaleY(0.6f);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        firstNameTextView = (TextView) findViewById(R.id.first_name_text_view);
        roleTextView = (TextView) findViewById(R.id.role_text_view);
        officeTextView = (TextView) findViewById(R.id.office_text_view);
        teamsTextView = (TextView) findViewById(R.id.teams_text_view);
        teamLabel = (TextView) findViewById(R.id.team_label);
        emailTextView = (TextView) findViewById(R.id.email_text_view);
        phoneTextView = (TextView) findViewById(R.id.phone_text_view);
        skypeTextView = (TextView) findViewById(R.id.skype_text_view);
        ircTextView = (TextView) findViewById(R.id.irc_text_view);

        workedHoursContainer = findViewById(R.id.worked_hours_container);
        workedHoursContainer.setVisibility(View.VISIBLE);
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
                    case TEAMS:
                        startActivity(new Intent(MyProfileActivity.this, TeamsActivity.class));
                        break;
                    case SETTINGS:
                        startActivityForResult(new Intent(MyProfileActivity.this, SettingsActivity.class), SETTINGS_REQUEST);
                        break;
                    case ABOUT:
                        startActivityForResult(new Intent(MyProfileActivity.this, AboutActivity.class), SETTINGS_REQUEST);
                        break;
                }

                drawerLayout.closeDrawers();
            }
        });

        drawerToggle = new StxActionBarDrawerToggle(this,
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
        switch (requestCode) {
            case LOGIN_REQUEST:
                if (resultCode == LoginActivity.LOGIN_OK) {
                    loadProfile();
                } else if (resultCode == LoginActivity.LOGIN_FAILED) {
                    STXToast.show(this, R.string.login_failure);
                } else if (resultCode == LoginActivity.LOGIN_CANCELED) {
                    finish();
                }
                break;
            case SETTINGS_REQUEST:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(this, MyProfileActivity.class));
                    finish();
                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadProfile() {
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            drawerToggle.toggle();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFloatingMenuItemClick(final int option) {
        toggleFloatingMenu();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;

                switch (option) {
                    case FloatingMenuFragment.LATE:
                        intent = new Intent(MyProfileActivity.this, ReportLateActivity.class);
                        break;
                    case FloatingMenuFragment.HOLIDAY:
                        intent = new Intent(MyProfileActivity.this, ReportHolidayActivity.class);
                        break;
                    case FloatingMenuFragment.OUT_OF_OFFICE:
                        intent = new Intent(MyProfileActivity.this, ReportOutOfOfficeActivity.class);
                        break;
                    case FloatingMenuFragment.ADD_HOURS:
                        intent = new Intent(MyProfileActivity.this, AddHoursActivity.class);
                        if (timeToAddTextView != null && timeToAddTextView.getText().toString() != null && !timeToAddTextView.getText().toString().isEmpty())
                        intent.putExtra("timeToAdd", timeToAddTextView.getText().toString());
                        break;
                }

                ActivityCompat.startActivity(MyProfileActivity.this, intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MyProfileActivity.this).toBundle());
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
    public void onUserReceived(final User user) {
        super.onUserReceived(user);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressView.setVisibility(View.GONE);
                if (user != null) {
                    String firstName = user.getFirstName();
                    String userName = firstName + " " + user.getLastName();
                    getSupportActionBar().setTitle(userName);
                    firstNameTextView.setText(userName);
                    roleTextView.setText(user.getRoles() != null && user.getRoles().size() > 0 ? user.getRoles().get(0) : "");
                    officeTextView.setText(user.getLocalization());
                    emailTextView.setText(user.getEmail());

                    if (isFemaleName(firstName)) {
                        ImageView superheroImageView = (ImageView) findViewById(R.id.superhero_image_view);
                        superheroImageView.setImageResource(R.drawable.mrs_superhero_profile);
                    }

                    String imageAddress = "https://intranet-staging.stxnext.pl" + user.getPhoto();
                    try {
                        Picasso.with(MyProfileActivity.this)
                                .load(imageAddress)
                                .placeholder(R.drawable.avatar_placeholder)
                                .resizeDimen(R.dimen.profile_image_height, R.dimen.profile_image_height)
                                .centerCrop()
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
                    } catch (OutOfMemoryError err) { Log.w("MEM", err.toString()); }

                    if (!"null".equals(user.getPhoneNumber())) {
                        phoneTextView.setText(user.getPhoneNumber());
                    }
                    if (!"null".equals(user.getSkype())) {
                        skypeTextView.setText(user.getSkype());
                    }
                    if (!"null".equals(user.getIrc())) {
                        ircTextView.setText(user.getIrc());
                    }

                    userInfoCardView.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .alpha(1)
                            .setDuration(600)
                            .setStartDelay(80)
                            .setInterpolator(new OvershootInterpolator());

                    findViewById(R.id.worked_hours_container).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(Config.getTag(this), "Time report click");
                            Intent timeReportActivityIntent = new Intent(MyProfileActivity.this, TimeReportActivity.class);
                            timeReportActivityIntent.putExtra(TimeReportActivity.USER_ID_TAG, user.getId());
                            startActivity(timeReportActivityIntent);
                        }
                    });
                    fillWorkedHours(user);
                    fillTeams(user);
                    NotificationUtils.setTimeReportAlarmManagerIfNeeded(MyProfileActivity.this);
                } else {
                    Session.getInstance(MyProfileActivity.this).logout();
                    runLoginActivity();
                }
            }
        });

    }

    private boolean isFemaleName(String firstName) {
        return firstName.substring(firstName.length() - 1, firstName.length()).equals("a");
    }

    @Override
    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {

    }

    @Override
    public void onOutOfOfficeResponse(boolean entry) {

    }

    @Override
    public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {
        // nothing to do
    }

    @Override
    public void onTimeReportReceived(List<TimeReportDay> timeReportDays, Calendar month) {

    }

    @Override
    public void onRequestError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressView.setVisibility(View.GONE);
                STXToast.show(MyProfileActivity.this, R.string.reqest_error);
            }
        });
    }
}

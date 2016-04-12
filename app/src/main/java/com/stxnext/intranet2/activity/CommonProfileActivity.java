package com.stxnext.intranet2.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.backend.model.workedHour.WorkedHours;
import com.stxnext.intranet2.backend.retrofit.WorkedHoursService;
import com.stxnext.intranet2.backend.service.TeamCacheService;
import com.stxnext.intranet2.rest.IntranetRestAdapter;
import com.stxnext.intranet2.utils.Session;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */

public abstract class CommonProfileActivity extends AppCompatActivity implements UserApiCallback {

    private static final int PHONE_STATE_REQUEST_KEY = 3;

    protected ImageView profileImageView;
    protected boolean superHeroModeEnabled;
    private User currentUser;

    private LinearLayout workedHoursTodayFromInnerContainer;
    private TextView todayFromTextView;
    protected TextView timeToAddTextView;

    private TextView todayNumberTextView;
    private TextView monthNumberTextView;
    private TextView quarterNumberTextView;

    private TextView todayOverhoursTextView;
    private TextView monthOverhoursTextView;
    private TextView quarterOverhoursTextView;
    private ImageView workedHoursRefreshHoursCardIv;
    private ScrollView scrollView;

    protected TextView teamsTextView;
    protected TextView teamLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeContentView();
        initializeProfileImageView();
    }

    public abstract void initializeContentView();

    protected void fillTeams(User user) {
        TeamCacheService teamCacheService = TeamCacheService.getInstance(this);
        teamCacheService.getTeamsForUser(Long.parseLong(user.getId()), new TeamCacheService.OnTeamsReceivedCallback() {
            @Override
            public void onReceived(List<Team> teams) {
                if (teams != null && teams.size() > 0) {
                    String teamsString = buildTeamsString(teams);
                    teamsTextView.setText(teamsString);
                    if (teams.size() > 1)
                        teamLabel.setText(getString(R.string.teams));
                }
            }
        });
    }

    private String buildTeamsString(List<Team> teams) {
        StringBuilder teamsStringBuilder = new StringBuilder();
        Iterator<Team> teamsIterator = teams.iterator();
        while (teamsIterator.hasNext()) {
            Team team = teamsIterator.next();
            teamsStringBuilder.append(team.getName());
            if (teamsIterator.hasNext())
                teamsStringBuilder.append(", ");
        }
        return teamsStringBuilder.toString();
    }

    public void fillWorkedHours(final User user) {
        workedHoursTodayFromInnerContainer = (LinearLayout) findViewById(R.id.worked_hours_today_from_inner_ll);
        workedHoursRefreshHoursCardIv =  (ImageView) findViewById(R.id.worked_hours_refresh_hours_card_iv);

        todayFromTextView = (TextView) findViewById(R.id.worked_hours_today_from);
        timeToAddTextView = (TextView) findViewById(R.id.worked_hours_time_to_add);

        todayNumberTextView = (TextView) findViewById(R.id.today_number);
        monthNumberTextView = (TextView) findViewById(R.id.month_number);
        quarterNumberTextView = (TextView) findViewById(R.id.quarter_number);

        todayOverhoursTextView = (TextView) findViewById(R.id.today_overhours);
        monthOverhoursTextView = (TextView) findViewById(R.id.month_overhours);
        quarterOverhoursTextView = (TextView) findViewById(R.id.quarter_overhours);

        workedHoursTodayFromInnerContainer.setAlpha(0.0f);
        workedHoursTodayFromInnerContainer.setScaleX(0.6f);
        workedHoursTodayFromInnerContainer.setScaleY(0.6f);

        downloadTodayHoursBckg(user);

        workedHoursRefreshHoursCardIv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { downloadTodayHoursBckg(user); rotateRefreshImageView(); }
        });
    }

    private void rotateRefreshImageView() {
        RotateAnimation rotate = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        rotate.setDuration(600);
        //rotate.setRepeatCount(Animation.INFINITE);
        workedHoursRefreshHoursCardIv.startAnimation(rotate);
    }

    private void downloadTodayHoursBckg(final User user) {
        final WorkedHoursService workedHoursService = IntranetRestAdapter.build()
                .create(WorkedHoursService.class);
        workedHoursService.getUserWorkedHours(Integer.parseInt(user.getId()), new Callback<WorkedHours>() {
            @Override
            public void success(WorkedHours workedHours, Response response) {
                setTodayHoursValues(workedHours);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(CommonProfileActivity.class.getName(), "Tried to download data - NO SESSION (cookies)");
            }
        });
    }

    private void checkPermisiion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                showInformationAboutPerminsion();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_REQUEST_KEY);
            }
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) && !canDrawOverlays(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showDrawOverlaysInfo();
        } else {
            drawOverlaysInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PHONE_STATE_REQUEST_KEY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDrawOverlaysInfo();
                }
            }
        }
    }

    private void showDrawOverlaysInfo() {
        if (scrollView != null && !canDrawOverlays(this)) {
            final Snackbar snackbar = Snackbar.make(scrollView, "Skonfiguruj wyświetlanie okna aby zobaczyć powiadomienia o dzwoniącym", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Pokaż", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getApplicationContext().getPackageName())));
                        }
                    });
            snackbar.show();
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Session.getInstance(CommonProfileActivity.this).setOverlayersShowed(false);
                    snackbar.dismiss();
                    return false;
                }
            });
        }
    }

    private void showInformationAboutPerminsion() {
        if (scrollView != null) {
            final Snackbar snackbar = Snackbar.make(scrollView, "Upawnienie jest wymagane aby wyświetlać powiadomienia", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Pokaż", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(CommonProfileActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_REQUEST_KEY);
                        }
                    });
            snackbar.show();
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Session.getInstance(CommonProfileActivity.this).setOverlayersShowed(false);
                    snackbar.dismiss();
                    return false;
                }
            });
        }
    }

    private boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                return true;
            }
        }
        return false;
    }

    private void drawOverlaysInfo() {
        if (scrollView != null && !Session.getInstance(this).isOverlayersShowed()) {
            final Snackbar snackbar = Snackbar.make(scrollView, "Teraz będziesz wyświetlać powadomienia jak ktoś zadzwoni", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Session.getInstance(CommonProfileActivity.this).setOverlayersShowed(true);
                    snackbar.dismiss();
                    return false;
                }
            });
        }
    }

    private void setTodayHoursValues(WorkedHours workedHours) {
        DecimalFormat df = new DecimalFormat("0.00");

        String arrivalString = workedHours.getToday().getArrival();
        if (arrivalString != null && !arrivalString.equals("00:00")) {
            workedHoursTodayFromInnerContainer.setVisibility(View.VISIBLE);
            workedHoursTodayFromInnerContainer.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .alpha(1)
                    .setDuration(600)
                    .setStartDelay(80)
                    .setInterpolator(new OvershootInterpolator());
        }

        todayFromTextView.setText(arrivalString);
        timeToAddTextView.setText(df.format(workedHours.getToday().getRemaining()) + "h");

        todayNumberTextView.setText(df.format(workedHours.getToday().getSum()));
        monthNumberTextView.setText(df.format(workedHours.getMonth().getSum()));
        quarterNumberTextView.setText(df.format(workedHours.getQuarter().getSum()));

        todayOverhoursTextView.setText(df.format(workedHours.getToday().getDiff()));
        if (workedHours.getToday().getDiff() < 0) {
            todayOverhoursTextView.setTextColor(ContextCompat.getColor(getApplicationContext() ,android.R.color.holo_red_light));
        }
        monthOverhoursTextView.setText(df.format(workedHours.getMonth().getDiff()));
        if (workedHours.getMonth().getDiff() < 0) {
            monthOverhoursTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
        }
        quarterOverhoursTextView.setText(df.format(workedHours.getQuarter().getDiff()));
        if (workedHours.getQuarter().getDiff() < 0) {
            quarterOverhoursTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
        }
    }

    protected void initializeProfileImageView() {
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        superHeroModeEnabled = Session.getInstance(this).isSuperHeroModeEnabled();
        if (superHeroModeEnabled) {
            findViewById(R.id.standard_profile_header_container).setVisibility(View.GONE);
            profileImageView = (ImageView) findViewById(R.id.profile_image_view);
        } else {
            profileImageView = (ImageView) findViewById(R.id.profile_image_view_standard);
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
                profileImageView.setVisibility(View.INVISIBLE);
                startActivity(intent);
            }

        } else
            Toast.makeText(CommonProfileActivity.this, "User not loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermisiion();

        if (profileImageView != null && profileImageView.getVisibility() == View.INVISIBLE)
            profileImageView.setVisibility(View.VISIBLE);
    }

    @Override public void onUserReceived(User user) {
        this.currentUser = user;
    }

    @Override public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {}
    @Override public void onOutOfOfficeResponse(boolean entry) {}
    @Override public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {}
    @Override public void onRequestError() {}
}

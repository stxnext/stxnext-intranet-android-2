package com.stxnext.intranet2.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.backend.model.workedHour.WorkedHours;
import com.stxnext.intranet2.backend.retrofit.WorkedHoursService;
import com.stxnext.intranet2.backend.service.TeamCacheService;
import com.stxnext.intranet2.rest.IntranetRestAdapter;
import com.stxnext.intranet2.utils.Session;

import org.androidannotations.api.BackgroundExecutor;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */

public abstract class CommonProfileActivity extends AppCompatActivity implements UserApiCallback {

    protected ImageView profileImageView;
    protected boolean superHeroModeEnabled;
    private User currentUser;

    private CardView workedHoursCardViewContainer;
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

    protected TextView teamsTextView;
    protected TextView teamLabel;

    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private RestAdapter restAdapter;
    private WorkedHoursService workedHoursService;

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
        workedHoursCardViewContainer = (CardView) findViewById(R.id.worked_hours_container);
        workedHoursTodayFromInnerContainer = (LinearLayout) findViewById(R.id.worked_hours_today_from_inner_ll);
        workedHoursRefreshHoursCardIv = (ImageView) findViewById(R.id.worked_hours_refresh_hours_card_iv);

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

        restAdapter = IntranetRestAdapter.build();
        workedHoursService = restAdapter.create(WorkedHoursService.class);
        downloadTodayHoursBckg(user);

        workedHoursRefreshHoursCardIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadTodayHoursBckg(user);
                rotateRefreshImageView();
            }
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
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {
                                       @Override
                                       public void execute() {
                                           try {
                                               if (user != null)
                                                   downloadTodayHours(user);
                                           } catch (Throwable e) {
                                               Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                                           }
                                       }
                                   }
        );
    }

    private void downloadTodayHours(User user) {
        if (user != null && user.getId() != null) {
            try {
                final WorkedHours workedHours = workedHoursService.getUserWorkedHours(Integer.parseInt(user.getId()));
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTodayHoursValues(workedHours);
                    }
                });
            } catch (Exception exc) {
                //There is no session with the server
                Log.w(CommonProfileActivity.class.getName(), "Tried to download data - NO SESSION (cookies)");
            }
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
            todayOverhoursTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
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
        superHeroModeEnabled = Session.getInstance(this).isSuperHeroModeEnabled();
        if (superHeroModeEnabled) {
            findViewById(R.id.standard_profile_header_container).setVisibility(View.GONE);
            profileImageView = (ImageView) findViewById(R.id.profile_image_view);
        } else {
            profileImageView = (ImageView) findViewById(R.id.profile_image_view_standard);
        }
    }


    public void onProfilePictureClick() {
        final View image = findViewById(R.id.scroll_view_profile_image_view_mapper);
        RxView.clicks(image).throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentUser != null) {
                            Intent intent = new Intent(CommonProfileActivity.this, PicturePreviewActivity.class);
                            intent.putExtra("pictureUrl", currentUser.getPhoto());
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ActivityCompat.startActivity(CommonProfileActivity.this, intent,
                                        ActivityOptionsCompat.makeSceneTransitionAnimation(CommonProfileActivity.this, profileImageView, "profileImageView").toBundle());

                            } else {
                                profileImageView.setVisibility(View.INVISIBLE);
                                startActivity(intent);
                            }

                        } else
                            Toast.makeText(CommonProfileActivity.this, "User not loaded", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (profileImageView != null && profileImageView.getVisibility() == View.INVISIBLE)
            profileImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUserReceived(User user) {
        this.currentUser = user;
    }

    @Override
    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {
    }

    @Override
    public void onOutOfOfficeResponse(boolean entry) {
    }

    @Override
    public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {
    }

    @Override
    public void onRequestError() {
    }
}

package com.stxnext.intranet2.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.workedHour.WorkedHours;
import com.stxnext.intranet2.backend.retrofit.WorkedHoursService;
import com.stxnext.intranet2.rest.IntranetRestAdapter;
import com.stxnext.intranet2.utils.Session;

import org.androidannotations.api.BackgroundExecutor;

import java.text.DecimalFormat;

import retrofit.RestAdapter;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */

public abstract class CommonProfileActivity extends AppCompatActivity implements UserApiCallback {

    protected ImageView profileImageView;
    protected boolean superHeroModeEnabled;
    private User currentUser;

    private TextView todayFromTextView;
    protected TextView timeToAddTextView;

    private TextView todayNumberTextView;
    private TextView monthNumberTextView;
    private TextView quarterNumberTextView;

    private TextView todayOverhoursTextView;
    private TextView monthOverhoursTextView;
    private TextView quarterOverhoursTextView;

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

    public void fillWorkedHours(final User user) {
        todayFromTextView = (TextView) findViewById(R.id.worked_hours_today_from);
        timeToAddTextView = (TextView) findViewById(R.id.worked_hours_time_to_add);

        todayNumberTextView = (TextView) findViewById(R.id.today_number);
        monthNumberTextView = (TextView) findViewById(R.id.month_number);
        quarterNumberTextView = (TextView) findViewById(R.id.quarter_number);

        todayOverhoursTextView = (TextView) findViewById(R.id.today_overhours);
        monthOverhoursTextView = (TextView) findViewById(R.id.month_overhours);
        quarterOverhoursTextView = (TextView) findViewById(R.id.quarter_overhours);

        restAdapter = IntranetRestAdapter.build();
        workedHoursService = restAdapter.create(WorkedHoursService.class);
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {
               @Override
               public void execute() {
                   try {
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

        todayFromTextView.setText(workedHours.getToday().getArrival());
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

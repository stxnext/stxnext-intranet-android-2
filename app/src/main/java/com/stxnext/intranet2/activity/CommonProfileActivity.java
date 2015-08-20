package com.stxnext.intranet2.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.workedHour.WorkedHours;
import com.stxnext.intranet2.utils.Session;

import java.text.DecimalFormat;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */

public abstract class CommonProfileActivity extends AppCompatActivity implements UserApiCallback {

    protected ImageView profileImageView;
    protected boolean superHeroModeEnabled;
    private User currentUser;

    private TextView todayNumberTextView;
    private TextView monthNumberTextView;
    private TextView quarterNumberTextView;

    private TextView todayOverhoursTextView;
    private TextView monthOverhoursTextView;
    private TextView quarterOverhoursTextView;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeContentView();
        fillWorkedHours();
    }

    public abstract void initializeContentView();

    public void fillWorkedHours() {
        todayNumberTextView = (TextView) findViewById(R.id.today_number);
        monthNumberTextView = (TextView) findViewById(R.id.month_number);
        quarterNumberTextView = (TextView) findViewById(R.id.quarter_number);

        todayOverhoursTextView = (TextView) findViewById(R.id.today_overhours);
        monthOverhoursTextView = (TextView) findViewById(R.id.month_overhours);
        quarterOverhoursTextView = (TextView) findViewById(R.id.quarter_overhours);

        handler.post(new Runnable() {
            @Override
            public void run() {
                downloadTodayHours();
            }
        });
    }

    private void downloadTodayHours() {
        //todo: download from backend server
        String jsonString = "{\n" +
                "\t\"today\" :  {\n" +
                "\t\t\"sum\" : 6.00,\n" +
                "\t\t\"diff\" : 0.00 \n" +
                "\t},\n" +
                "\t\"month\": {\n" +
                "\t\t\"sum\" : 66.66,\n" +
                "\t\t\"diff\" : -6.66 \n" +
                "\t},\n" +
                "\t\"quarter\": {\n" +
                "\t\t\"sum\" : 333.33,\n" +
                "\t\t\"diff\" : -3.33 \n" +
                "\t}\n" +
                "}";

        final WorkedHours workedHours = new Gson().fromJson(jsonString, WorkedHours.class);

        handler.post(new Runnable() {
            @Override
            public void run() {
                setTodayHoursValues(workedHours);
            }
        });
    }

    private void setTodayHoursValues(WorkedHours workedHours) {
        DecimalFormat df = new DecimalFormat("#.00");
        todayNumberTextView.setText(df.format(workedHours.getToday().getSum()));
        monthNumberTextView.setText(df.format(workedHours.getMonth().getSum()));
        quarterNumberTextView.setText(df.format(workedHours.getQuarter().getSum()));

        todayOverhoursTextView.setText(df.format(workedHours.getToday().getDiff()));
        monthOverhoursTextView.setText(df.format(workedHours.getMonth().getDiff()));
        quarterOverhoursTextView.setText(df.format(workedHours.getQuarter().getDiff()));
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

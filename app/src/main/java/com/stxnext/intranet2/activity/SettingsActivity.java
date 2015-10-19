package com.stxnext.intranet2.activity;

import android.animation.LayoutTransition;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.dialog.TimePickerDialogFragment;
import com.stxnext.intranet2.utils.NotificationUtils;
import com.stxnext.intranet2.utils.Session;

import java.util.Calendar;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class SettingsActivity extends AppCompatActivity implements TimePickerDialogFragment.OnTimeSetListener {

    private View timeOfNotification;
    private TextView timeOfNotificationValue;
    private int notificationHour = 17;
    private int notificationMinute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!getResources().getBoolean(R.bool.superheromode_available)) {
            findViewById(R.id.superhero_mode_container).setVisibility(View.GONE);
        }

        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getInstance(SettingsActivity.this).logout();
                setResult(RESULT_OK);
                finish();
            }
        });

        final Session session = Session.getInstance(this);
        SwitchCompat superHeroModeOption = (SwitchCompat) findViewById(R.id.superhero_switch);
        superHeroModeOption.setChecked(session.isSuperHeroModeEnabled());
        superHeroModeOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                session.enableSuperHeroMode(isChecked);
                setResult(RESULT_OK);
            }
        });

        setTransitionAnimationsForNotificationLayout();

        timeOfNotification = (View) findViewById(R.id.time_of_notification);
        if (!Session.getInstance(this).isTimeReportNotification()) {
            timeOfNotification.setVisibility(View.GONE);
        }
        SwitchCompat timeReportNotificationSwitch = (SwitchCompat) findViewById(R.id.time_report_notification_switch);
        timeReportNotificationSwitch.setChecked(session.isTimeReportNotification());
        timeReportNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                session.setTimeReportNotification(isChecked);
                if (isChecked) {
                    timeOfNotification.setVisibility(View.VISIBLE);
                } else {
                    timeOfNotification.setVisibility(View.GONE);
                }
                setResult(RESULT_OK);
            }
        });

        timeOfNotificationValue = (TextView) findViewById(R.id.time_of_notification_value);
        setTimeReportNotificationHour();
        timeOfNotificationValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogFragment.show(getFragmentManager(), notificationHour, notificationMinute, TimePickerDialogFragment.TIME_FROM);
            }
        });


        try {
            TextView versionInfo = (TextView) findViewById(R.id.version_info);
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionInfo.setText(String.format("STX Next Intranet %s\n\n%s", version, versionInfo.getText().toString()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setTimeReportNotificationHour() {
        String notificationHourString = Session.getInstance(this).getTimeReportNotificationHour();
        timeOfNotificationValue.setText(notificationHourString);
        String[] hourSplitted = notificationHourString.split(":");
        if (hourSplitted != null && hourSplitted.length == 2) {
            String hourString = hourSplitted[0];
            notificationHour = Integer.valueOf(hourString).intValue();
            String minuteString = hourSplitted[1];
            notificationMinute = Integer.valueOf(minuteString).intValue();
        }
    }

    /**
     * Sets animations when there are changes inside layout.
     */
    private void setTransitionAnimationsForNotificationLayout() {
        LinearLayout timeReportNotificationLayout = (LinearLayout) findViewById(R.id.time_report_notification_layout);
        LayoutTransition layoutTransition = new LayoutTransition();
        // There is need to disable animation when view disappears because it is badly implemented.
        layoutTransition.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        timeReportNotificationLayout.setLayoutTransition(layoutTransition);
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
    public void onTimeSet(int hour, int minute, int type) {
        notificationHour = hour;
        notificationMinute = minute;
        Session.getInstance(this).setTimeReportNoticationHour(hour, minute);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        timeOfNotificationValue.setText(DateFormat.format("kk:mm", calendar));
        NotificationUtils.setTimeReportAlarmManagerIfNeeded(this);
    }
}

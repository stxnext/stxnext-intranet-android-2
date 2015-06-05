package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.utils.STXToast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tomasz Konieczny on 2015-05-27.
 */
public class ReportLateActivity extends AppCompatActivity implements UserApiCallback {

    private View submitButton;
    private UserApi userApi;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_late_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        userApi = new UserApiImpl(this, this);
        progressView = findViewById(R.id.progress_container);

        final TextView hourLabel = (TextView) findViewById(R.id.hour_label);
        final View labelBackground = findViewById(R.id.hour_label_background);
        final View wtfView = findViewById(R.id.wtf_image);
        final SeekBar properSeekBar = (SeekBar) findViewById(R.id.time_seek_bar);

        SeekBar seekBarStub = (SeekBar) findViewById(R.id.time_seek_bar_stub);
        seekBarStub.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private boolean hideTip = true;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (hideTip) {
                        hideTip = false;
                        final View tipView = findViewById(R.id.tip_container);
                        tipView.animate().alpha(0).scaleX(1.2f).scaleY(1.2f).translationX(300).setDuration(300);
                    }

                    float percentage = ((float) progress / seekBar.getMax());
                    properSeekBar.setProgress(progress);
                    wtfView.setAlpha(percentage / 2);
                    labelBackground.setAlpha(percentage);
                    hourLabel.setText(convertIntToTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (submitButton == null) {
                    submitButton = findViewById(R.id.submit_button);
                    submitButton.animate().alpha(1f).setDuration(300);
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitLateness(hourLabel);
                        }
                    });
                } else if (submitButton.getVisibility() == View.INVISIBLE) {
                    submitButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int mod = progress % 5;
                if (mod > 0) {
                    if (mod >= 3) {
                        int diff = 5 - mod;
                        progress += diff;
                    } else {
                        progress -= mod;
                    }

                    onProgressChanged(seekBar, progress, true);
                }

                if (progress == 0) {
                    submitButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void submitLateness(TextView hourLabel) {
        progressView.setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();
        Date submissionDate = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        Date startHour = calendar.getTime();
        String endHourString = hourLabel.getText().toString();
        String[] splittedEndHourString = endHourString.split(":");
        int hour = 9;
        int minute = 1;
        if (splittedEndHourString.length == 2) {
            String hourString = splittedEndHourString[0];
            String minuteString = splittedEndHourString[1];
            hour = Integer.parseInt(hourString);
            minute = Integer.parseInt(minuteString);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Date endHour = calendar.getTime();
        userApi.submitOutOfOffice(false, submissionDate, startHour, endHour, getString(R.string.i_will_be_late));
    }

    private String convertIntToTime(int progress) {
        int hour = 9;

        while (progress >= 60) {
            hour++;
            progress -= 60;
        }

        String minutes = String.valueOf(progress);
        String hours = String.valueOf(hour);

        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }

        return String.format("%s:%s", hours, minutes);
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

    @Override
    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {

    }

    @Override
    public void onOutOfOfficeResponse(boolean entry) {
        STXToast.show(this, R.string.saved);
        finish();
    }

    @Override
    public void onRequestError() {
        STXToast.show(this, R.string.reqest_error);
        progressView.setVisibility(View.GONE);
    }

    @Override
    public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {

    }

}

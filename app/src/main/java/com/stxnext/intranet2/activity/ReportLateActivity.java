package com.stxnext.intranet2.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
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
    private CardView textInputLayout;

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
        textInputLayout = (CardView)findViewById(R.id.input_layout_lateness_reason);

        final TextView hourLabel = (TextView) findViewById(R.id.hour_label);
        final EditText latenessReasonEditText = (EditText) findViewById(R.id.lateness_reason);
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
                            submitLateness(hourLabel, latenessReasonEditText);
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

    private void submitLateness(TextView hourLabel, EditText latenessReasonEditText) {
        String latenessReasonText = latenessReasonEditText.getText().toString();

        if (latenessReasonText.isEmpty() || latenessReasonText.length() < 4) {
            Toast.makeText(this, getString(R.string.give_lateness_reason), Toast.LENGTH_SHORT).show();
            return;
        }

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
        userApi.submitOutOfOffice(false, submissionDate, startHour, endHour, latenessReasonText);
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
        } else if (item.getItemId() == R.id.menu_give_lateness_reason) {
            animateReasonLatenessFrame();
            return true;
        }

        return false;
    }

    private void animateReasonLatenessFrame() {
        if (textInputLayout.getVisibility() == View.VISIBLE) {
            AnimatorSet anim = (AnimatorSet)AnimatorInflater
                    .loadAnimator(this, R.animator.disappear_lateness_frame);
            anim.setTarget(textInputLayout);
            anim.start();

            anim.addListener(new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animation) {}
                @Override public void onAnimationCancel(Animator animation) {}
                @Override public void onAnimationRepeat(Animator animation) {}
                @Override public void onAnimationEnd(Animator animation) {
                    textInputLayout.setVisibility(View.INVISIBLE);
                }
            });

            return;
        }

        textInputLayout.setVisibility(View.VISIBLE);
        AnimatorSet anim = (AnimatorSet)AnimatorInflater
                .loadAnimator(this, R.animator.appear_lateness_frame);
        anim.setTarget(textInputLayout);
        anim.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.menu_give_lateness_reason, Menu.NONE, R.string.give_lateness_reason);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onUserReceived(User user) {

    }

    @Override
    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {

    }

    @Override
    public void onOutOfOfficeResponse(final boolean entry) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                STXToast.show(ReportLateActivity.this, R.string.saved);
                finish();
            }
        });
    }

    @Override
    public void onRequestError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                STXToast.show(ReportLateActivity.this, R.string.reqest_error);
                progressView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {

    }

}

package com.stxnext.intranet2.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.stxnext.intranet2.R;

/**
 * Created by Tomasz Konieczny on 2015-05-27.
 */
public class RequestLateActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                        tipView.animate().alpha(0).scaleX(1.5f).scaleY(1.5f).translationX(300).setDuration(300);
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
            }
        });
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

}

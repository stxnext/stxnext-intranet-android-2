package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.dialog.DatePickerDialogFragment;
import com.stxnext.intranet2.dialog.TimePickerDialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ReportOutOfOfficeActivity extends AppCompatActivity implements
        TimePickerDialogFragment.OnTimeSetListener,
        DatePickerDialogFragment.OnDatePickListener, UserApiCallback {

    private Calendar date;
    private int fromHour = 9;
    private int fromMinute = 0;

    private int toHour = 17;
    private int toMinute = 0;

    private TextView dateLabel;
    private TextView fromLabel;
    private TextView toLabel;
    private Switch workFromHomeSwitch;
    private EditText explanationEditText;

    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_out_of_office);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userApi = new UserApiImpl(this, this);

        date = Calendar.getInstance();
        dateLabel = (TextView) findViewById(R.id.date_label);
        dateLabel.setText(DateFormat.format("dd.MM.yyyy", date));
        dateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment.show(
                        getFragmentManager(),
                        date.get(Calendar.DAY_OF_MONTH),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.YEAR), 0);
            }
        });

        fromLabel = (TextView) findViewById(R.id.from_label);
        fromLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment.show(getFragmentManager(), fromHour, fromMinute, TimePickerDialogFragment.TIME_FROM);
            }
        });

        toLabel = (TextView) findViewById(R.id.to_label);
        toLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment.show(getFragmentManager(), toHour, toMinute, TimePickerDialogFragment.TIME_TO);
            }
        });

        workFromHomeSwitch = (Switch) findViewById(R.id.work_from_home_switch);

        explanationEditText = (EditText) findViewById(R.id.explanation_edit_text);

        findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        CharSequence dateFormat = DateFormat.format("HH:mm", calendar);
        switch (type) {
            case TimePickerDialogFragment.TIME_FROM:
                fromMinute = minute;
                fromHour = hour;
                fromLabel.setText(dateFormat);
                break;
            case TimePickerDialogFragment.TIME_TO:
                toMinute = minute;
                toHour = hour;
                toLabel.setText(dateFormat);
                break;
        }
    }


    @Override
    public void onDatePicked(int dayOfMonth, int month, int year, int type) {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateLabel.setText(DateFormat.format("dd.MM.yyyy", date));
    }

    private void submit() {
        if (date.getTimeInMillis() < (System.currentTimeMillis() - (1000 * 60 * 60 * 24))) {
            Toast.makeText(this, R.string.validation_day_difference_warning, Toast.LENGTH_SHORT).show();
        } else if (toHour < fromHour) {
            Toast.makeText(this, R.string.validation_hour_difference_warning, Toast.LENGTH_SHORT).show();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date.getTime());
            calendar.set(Calendar.HOUR_OF_DAY, fromHour);
            calendar.set(Calendar.MINUTE, fromMinute);
            Date startHour = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, toHour);
            calendar.set(Calendar.MINUTE, toMinute);
            Date endHour = calendar.getTime();
            userApi.submitLateness(workFromHomeSwitch.isChecked(), date.getTime(), startHour, endHour, explanationEditText.getText().toString());
        }
    }

    @Override
    public void onUserReceived(User user) {

    }

    @Override
    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {

    }

    // TODO add waiting sign before it
    @Override
    public void onLatenessResponse(boolean entry) {
        Toast.makeText(this, R.string.added, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {

    }
}

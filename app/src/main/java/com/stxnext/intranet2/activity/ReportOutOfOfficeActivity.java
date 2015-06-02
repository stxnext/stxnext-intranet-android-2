package com.stxnext.intranet2.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.dialog.DatePickerDialogFragment;
import com.stxnext.intranet2.dialog.TimePickerDialogFragment;

import java.util.Calendar;

/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ReportOutOfOfficeActivity extends AppCompatActivity implements
        TimePickerDialogFragment.OnTimeSetListener,
        DatePickerDialogFragment.OnDatePickListener {

    private Calendar date;
    private int fromHour = 9;
    private int fromMinute = 0;

    private int toHour = 17;
    private int toMinute = 0;

    private TextView dateLabel;
    private TextView fromLabel;
    private TextView toLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_out_of_office);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
}

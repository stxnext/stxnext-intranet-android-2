package com.stxnext.intranet2.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.HolidayTypeSpinnerAdapter;
import com.stxnext.intranet2.dialog.DatePickerDialogFragment;
import com.stxnext.intranet2.dialog.TimePickerDialogFragment;

import java.util.Calendar;

/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ReportHolidayActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_holiday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner typeSpinner = (Spinner) findViewById(R.id.absence_type_spinner);
        typeSpinner.setAdapter(new HolidayTypeSpinnerAdapter(ReportHolidayActivity.this));
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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
}

package com.stxnext.intranet2.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.HolidayTypeSpinnerAdapter;
import com.stxnext.intranet2.dialog.DatePickerDialogFragment;
import com.stxnext.intranet2.dialog.TimePickerDialogFragment;
import com.stxnext.intranet2.model.HolidayTypes;

import java.util.Calendar;
import java.util.Date;

import static com.stxnext.intranet2.R.id.selected_amount_label;

/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ReportHolidayActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, DatePickerDialogFragment.OnDatePickListener, View.OnClickListener {

    private TextView dateFromLabel;
    private TextView dateToLabel;
    private TextView selectedAmountLabel;
    private TextView remainingDaysLabel;
    private TextView allDaysLabel;
    private Button submitButton;

    private HolidayTypes type;
    private Calendar dateFrom;
    private Calendar dateTo;

    //TODO: We need to receive this values!
    private int allDays = 24;
    private int remainingDays = 12;
    private int selectedAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_holiday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preapreDateViews();
        prepareSpinner();
    }

    private void preapreDateViews() {
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        selectedAmountLabel = (TextView) findViewById(selected_amount_label);
        remainingDaysLabel = (TextView) findViewById(R.id.remaining_amount_label);
        allDaysLabel = (TextView) findViewById(R.id.all_days_label);

        selectedAmountLabel.setText(String.valueOf(selectedAmount));
        remainingDaysLabel.setText(String.valueOf(remainingDays));
        allDaysLabel.setText(String.valueOf(allDays));

        dateFromLabel = (TextView) findViewById(R.id.from_label);
        dateToLabel = (TextView) findViewById(R.id.to_label);

        Calendar calendar = Calendar.getInstance();
        dateFrom = calendar;
        dateTo = calendar;

        CharSequence today = DateFormat.format("dd.MM", calendar);
        dateFromLabel.setText(today);
        dateToLabel.setText("-");

        dateFromLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment.show(
                        getFragmentManager(),
                        dateFrom.get(Calendar.DAY_OF_MONTH),
                        dateFrom.get(Calendar.MONTH),
                        dateFrom.get(Calendar.YEAR),
                        DatePickerDialogFragment.DATE_TYPE_FROM);
            }
        });

        dateToLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment.show(
                        getFragmentManager(),
                        dateTo.get(Calendar.DAY_OF_MONTH),
                        dateTo.get(Calendar.MONTH),
                        dateTo.get(Calendar.YEAR),
                        DatePickerDialogFragment.DATE_TYPE_TO);
            }
        });
    }

    private void prepareSpinner() {
        Spinner typeSpinner = (Spinner) findViewById(R.id.absence_type_spinner);
        HolidayTypeSpinnerAdapter adapter = new HolidayTypeSpinnerAdapter(ReportHolidayActivity.this);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setSelection(0);
        typeSpinner.setOnItemSelectedListener(this);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.type = HolidayTypes.values()[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        type = null;
    }

    @Override
    public void onDatePicked(int dayOfMonth, int month, int year, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        CharSequence pickedDate = DateFormat.format("dd.MM", calendar);

        switch (type) {
            case DatePickerDialogFragment.DATE_TYPE_FROM:
                dateFrom = calendar;
                dateFromLabel.setText(pickedDate);
                break;
            case DatePickerDialogFragment.DATE_TYPE_TO:
                dateTo = calendar;
                dateToLabel.setText(pickedDate);
                break;
        }

        calculateSummary();
    }

    private void calculateSummary() {
        int monthFrom = dateFrom.get(Calendar.MONTH);
        int monthTo = dateTo.get(Calendar.MONTH);

        if (monthFrom == monthTo) {
            int dayFrom = dateFrom.get(Calendar.DAY_OF_MONTH);
            int dayTo = dateTo.get(Calendar.DAY_OF_MONTH);

            int diff = dayTo - dayFrom;
            if (diff >= 0) {
                int absenceDays = diff + 1;
                selectedAmount = absenceDays;
                int remaining = remainingDays - absenceDays;
                remainingDaysLabel.setText(String.valueOf(remaining));
            } else {
                selectedAmount = 0;
                remainingDaysLabel.setText(String.valueOf(remainingDays));
            }

            selectedAmountLabel.setText(String.valueOf(selectedAmount));
        } else {
            Toast toast = Toast.makeText(this, R.string.different_month_holiday_warning, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (remainingDays < selectedAmount) {
            Toast.makeText(this, R.string.validation_to_many_days, Toast.LENGTH_SHORT).show();
        } else if (selectedAmount <= 0) {
            Toast.makeText(this, R.string.validation_zero_days, Toast.LENGTH_SHORT).show();
        } else {
            //TODO: Request to server for holidays
        }
    }
}

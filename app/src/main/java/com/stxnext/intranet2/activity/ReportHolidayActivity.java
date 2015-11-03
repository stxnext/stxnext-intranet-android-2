package com.stxnext.intranet2.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.HolidayTypeSpinnerAdapter;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;
import com.stxnext.intranet2.dialog.DatePickerDialogFragment;
import com.stxnext.intranet2.model.HolidayTypes;
import com.stxnext.intranet2.utils.HolidayUtils;
import com.stxnext.intranet2.utils.STXToast;
import com.stxnext.intranet2.utils.Session;

import java.util.Calendar;
import java.util.List;

import static com.stxnext.intranet2.R.id.selected_amount_label;

/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ReportHolidayActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, DatePickerDialogFragment.OnDatePickListener, View.OnClickListener, UserApiCallback {

    private TextView dateFromLabel;
    private TextView dateToLabel;
    private TextView selectedAmountLabel;
    private TextView remainingDaysLabel;
    private View progressView;

    private HolidayTypes type;
    private Calendar dateFrom;
    private Calendar dateTo;

    private int allDays = 0;
    private int remainingDays = 0;
    private int selectedAmount = 0;

    private EditText explanationEditText;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_holiday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userApi = new UserApiImpl(this, this);
        progressView = findViewById(R.id.progress_container);

        Integer absenceDaysLeft = Session.getInstance(this).getAbsenceDaysLeft();
        Integer daysMandated = Session.getInstance(this).getDaysMandated();
        if (absenceDaysLeft == null) {
            userApi.getAbsenceDaysLeft();
            progressView.setVisibility(View.VISIBLE);
        } else {
            remainingDays = absenceDaysLeft;
            allDays = daysMandated;
            prepareDateViews();
        }
        prepareSpinner();
    }

    private void prepareDateViews() {
        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        selectedAmountLabel = (TextView) findViewById(selected_amount_label);
        remainingDaysLabel = (TextView) findViewById(R.id.remaining_amount_label);
        TextView allDaysLabel = (TextView) findViewById(R.id.all_days_label);

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
                        DatePickerDialogFragment.DATE_TYPE_FROM,
                        System.currentTimeMillis() - 1000);
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
                        DatePickerDialogFragment.DATE_TYPE_TO,
                        dateFrom.getTimeInMillis());
            }
        });

        explanationEditText = (EditText) findViewById(R.id.explanation_edit_text);
        findViewById(R.id.explanation_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explanationEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(explanationEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        progressView.setVisibility(View.GONE);
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
        int absenceDays = HolidayUtils.getWorkingDays(dateFrom.getTime(), dateTo.getTime());
        if (absenceDays >= 0) {
            selectedAmount = absenceDays;
            int remaining = remainingDays - absenceDays;
            remainingDaysLabel.setText(String.valueOf(remaining));
        } else {
            selectedAmount = 0;
            remainingDaysLabel.setText(String.valueOf(remainingDays));
        }

        selectedAmountLabel.setText(String.valueOf(selectedAmount));
    }

    @Override
    public void onClick(View v) {

        int monthFrom = dateFrom.get(Calendar.MONTH);
        int monthTo = dateTo.get(Calendar.MONTH);

        if (monthFrom != monthTo) {
            STXToast.show(this, R.string.different_month_holiday_warning);
        } else if (remainingDays < selectedAmount) {
            STXToast.show(this, R.string.validation_to_many_days);
        } else if (selectedAmount <= 0) {
            STXToast.show(this, R.string.validation_zero_days);
        } else if (explanationEditText.getText().length() == 0) {
            STXToast.show(this, R.string.validation_no_explanation);
        } else {
            submitHoliday();
        }
    }

    private void submitHoliday() {
        progressView.setVisibility(View.VISIBLE);
        userApi.submitHolidayAbsence(type, dateFrom.getTime(), dateTo.getTime(), explanationEditText.getText().toString());
    }


    @Override
    public void onUserReceived(User user) {

    }

    @Override
    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                STXToast.show(ReportHolidayActivity.this, R.string.saved);
                finish();
            }
        });
    }

    @Override
    public void onOutOfOfficeResponse(boolean entry) {

    }

    @Override
    public void onRequestError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                STXToast.show(ReportHolidayActivity.this, R.string.reqest_error);
                progressView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAbsenceDaysLeftReceived(final int mandated, final int days, final int absenceDaysLeft) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Session.getInstance(ReportHolidayActivity.this).setAbsenceDaysLeft(absenceDaysLeft);
                Session.getInstance(ReportHolidayActivity.this).setDaysMandated(mandated);
                remainingDays = absenceDaysLeft;
                allDays = mandated;
                prepareDateViews();
            }
        });
    }

    @Override
    public void onTimeReportReceived(List<TimeReportDay> timeReportDays) {

    }
}

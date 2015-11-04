package com.stxnext.intranet2.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;
import com.stxnext.intranet2.model.DaysShorcuts;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Łukasz Ciupa on 2015-09-02.
 */
public class TimeReportActivity extends AppCompatActivity implements UserApiCallback {

    public static final String USER_ID_TAG = "userId";
    private String userId= null;
    private LinearLayout viewContainer;
    private Toolbar toolbar;
    private TextView timeReportTitle;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_report);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewContainer = (LinearLayout) findViewById(R.id.container);
        timeReportTitle = (TextView) findViewById(R.id.time_report_title);
        Calendar month = Calendar.getInstance();
        timeReportTitle.setText(getString(R.string.time_report_for) + " " + DateFormat.format("MM.yyyy", month));
        progressView = findViewById(R.id.progress_container);
        progressView.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            userId = getIntent().getStringExtra(USER_ID_TAG);
            UserApi userApi = new UserApiImpl(this, this);
            userApi.getTimeReport(userId, DateFormat.format("MM.yyyy", month).toString());
            month.add(Calendar.MONTH, -1);
            userApi.getTimeReport(userId, DateFormat.format("MM.yyyy", month).toString());
        }
    }

    private TableLayout createTimeTable(List<TimeReportDay> timeReportDays) {
        TableLayout tableLayout = new TableLayout(getApplicationContext());
        tableLayout.setStretchAllColumns(true);
        if (timeReportDays.size() > 0) {
            TableRow tableRowDayNumbers = new TableRow(getApplicationContext());
            TableRow tableRowDays = new TableRow(getApplicationContext());

            int numberOfDays = timeReportDays.size();
            int dayOfFirstWeek = timeReportDays.get(0).getDayOfWeek();
            createDaysNames(tableLayout);
            tableLayout.addView(tableRowDayNumbers);
            tableLayout.addView(tableRowDays);
            insertEmptyCells(tableRowDayNumbers, tableRowDays, dayOfFirstWeek);
            int currentColumnPosition = 0;
            for (int day = 1; day <= numberOfDays; day++) {
                // If this is new week create new row
                if (isBeginningOfWeek(day, dayOfFirstWeek)) {
                    currentColumnPosition = 0;
                    tableRowDayNumbers = new TableRow(getApplicationContext());
                    tableLayout.addView(tableRowDayNumbers);
                    tableRowDays = new TableRow(getApplicationContext());
                    tableLayout.addView(tableRowDays);
                }
                TimeReportDay timeReportDay = timeReportDays.get(day - 1);
                TextView dayNumber = createDayNumberCell(day, timeReportDay.getIsWorkingDay());
                tableRowDayNumbers.addView(dayNumber);
                TextView hoursWorkedCell = createHoursWorkedCell(timeReportDay);
                tableRowDays.addView(hoursWorkedCell);
                currentColumnPosition++;
            }
            insertEmptyCells(tableRowDayNumbers, tableRowDays, getEmptyColumnsLeft(currentColumnPosition));
        }
        return tableLayout;
    }

    @Deprecated
    @NonNull
    private TableLayout createTimeTableDefault(List<TimeReportDay> timeReportDays) {
        TableLayout tableLayout = new TableLayout(getApplicationContext());
        tableLayout.setStretchAllColumns(true);
        TableRow tableRowDayNumbers = new TableRow(getApplicationContext());
        TableRow tableRowDays = new TableRow(getApplicationContext());

        Calendar actualMonth = Calendar.getInstance();
        int numberOfDays = actualMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        actualMonth.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfFirstWeek = actualMonth.get(Calendar.DAY_OF_WEEK);
        dayOfFirstWeek = recalculateDayOfFirstWeek(dayOfFirstWeek);
        createDaysNames(tableLayout);
        tableLayout.addView(tableRowDayNumbers);
        tableLayout.addView(tableRowDays);
        insertEmptyCells(tableRowDayNumbers, tableRowDays, dayOfFirstWeek);
        int currentColumnPosition = 0;
        for (int day = 1; day <= numberOfDays; day++) {
            // If this is new week create new row
            if (isBeginningOfWeek(day, dayOfFirstWeek)) {
                currentColumnPosition = 0;
                tableRowDayNumbers = new TableRow(getApplicationContext());
                tableLayout.addView(tableRowDayNumbers);
                tableRowDays = new TableRow(getApplicationContext());
                tableLayout.addView(tableRowDays);
            }
            TimeReportDay timeReportDay = timeReportDays.get(day - 1);
            TextView dayNumber = createDayNumberCell(day, timeReportDay.getIsWorkingDay());
            tableRowDayNumbers.addView(dayNumber, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            DecimalFormatSymbols otherSymbols = DecimalFormatSymbols.getInstance();
            otherSymbols.setDecimalSeparator('.');
            TextView hoursWorkedCell = createHoursWorkedCell(timeReportDay);
            tableRowDays.addView(hoursWorkedCell, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            currentColumnPosition++;
        }
        insertEmptyCells(tableRowDayNumbers, tableRowDays, getEmptyColumnsLeft(currentColumnPosition));
        return tableLayout;
    }

    private int getEmptyColumnsLeft(int currentColumnPosition) {
        return 7 - currentColumnPosition;
    }

    @NonNull
    private TextView createHoursWorkedCell(TimeReportDay timeReportDay) {
        String hoursWorkedValue = ".";
        if (timeReportDay.getTime() != null && timeReportDay.getTime() > 0) {
            DecimalFormatSymbols formattingSymbols = DecimalFormatSymbols.getInstance();
            formattingSymbols.setDecimalSeparator('.');
            DecimalFormat numberFormat = new DecimalFormat("0.00", formattingSymbols);
            hoursWorkedValue = numberFormat.format(timeReportDay.getTime());
        }
        TextView hoursWorked = new TextView(getApplicationContext());
        hoursWorked.setGravity(Gravity.CENTER);
        if (timeReportDay.getIsWorkingDay())
            hoursWorked.setBackgroundResource(R.drawable.cell_graylight_bounds);
        else
            hoursWorked.setBackgroundResource(R.drawable.cell_graylighter_bounds);
        hoursWorked.setText(hoursWorkedValue);
        hoursWorked.setTextColor(Color.BLACK);
        hoursWorked.setPadding(3, 3, 3, 3);
        if (timeReportDay.getLateEntry()) {
            hoursWorked.setBackgroundColor(Color.RED);
        }
        return hoursWorked;
    }

    private TextView createDayNumberCell(int day, boolean isWorkingDay) {
        TextView dayNumber = new TextView(getApplicationContext());
        dayNumber.setGravity(Gravity.CENTER);
        if (isWorkingDay)
            dayNumber.setBackgroundColor(ContextCompat.getColor(this, R.color.stxnext_green));
        else
            dayNumber.setBackgroundColor(ContextCompat.getColor(this, R.color.stxnext_green_lighter));
        dayNumber.setText(String.valueOf(day));
        dayNumber.setTextColor(Color.BLACK);
        dayNumber.setPadding(3, 3, 3, 3);
        return dayNumber;
    }

    private void createDaysNames(TableLayout tableLayout) {
        TableRow tableRowNames = new TableRow(getApplicationContext());
        for (int i = 0; i < 7; i++) {
            TextView dayName = new TextView(getApplicationContext());
            if (isWeekendDay(i)) {
                dayName.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light_little_darker));
            } else {
                dayName.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light_darker));
            }
            dayName.setGravity(Gravity.CENTER);
            dayName.setTextColor(Color.BLACK);
            dayName.setPadding(3, 3, 3, 3);
            dayName.setText(getString(DaysShorcuts.values()[i].getDayShortcut()));
            tableRowNames.addView(dayName, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        }
        tableLayout.addView(tableRowNames);
    }

    private boolean isWeekendDay(int day) {
        return day > 4;
    }

    private void insertEmptyCells(TableRow tableRowDayNumbers, TableRow tableRowDays, int daysToFill) {
        for(int i = 0; i < daysToFill; i++) {
            TextView dayNumber = new TextView(getApplicationContext());
            dayNumber.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light));
            dayNumber.setPadding(3, 3, 3, 3);
            tableRowDayNumbers.addView(dayNumber);
            TextView hoursWorked = new TextView(getApplicationContext());
            hoursWorked.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light));
            hoursWorked.setPadding(3, 3, 3, 3);
            tableRowDays.addView(hoursWorked);
        }
    }

    /**
     * Recalculates day of first week as to monday is 0, sunday is 6.
     * @param dayOfFirstWeek
     * @return
     */
    private int recalculateDayOfFirstWeek(int dayOfFirstWeek) {
        dayOfFirstWeek -=2;
        if (dayOfFirstWeek == -1) {
            dayOfFirstWeek = 6;
        }
        return dayOfFirstWeek;
    }

    private boolean isBeginningOfWeek(int day, int daysShift) {
        return (day - 1 + daysShift) % 7 == 0;
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
    public synchronized void onTimeReportReceived(List<TimeReportDay> timeReportDays) {
        progressView.setVisibility(View.GONE);
        TableLayout tableLayout = createTimeTable(timeReportDays);
        viewContainer.addView(tableLayout);
    }

    @Override
    public void onUserReceived(User user) {

    }

    @Override
    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {

    }

    @Override
    public void onOutOfOfficeResponse(boolean entry) {

    }

    @Override
    public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {

    }

    @Override
    public void onRequestError() {

    }
}

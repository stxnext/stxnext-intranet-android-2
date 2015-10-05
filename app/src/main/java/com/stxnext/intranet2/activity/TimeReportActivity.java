package com.stxnext.intranet2.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.model.DaysShorcuts;

import java.util.Calendar;

/**
 * Created by Łukasz Ciupa on 2015-09-02.
 */
public class TimeReportActivity extends AppCompatActivity {

    public static final String USER_ID_TAG = "userId";
    private FrameLayout viewContainer;
    private Toolbar toolbar;
    private TextView timeReportTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_report);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewContainer = (FrameLayout) findViewById(R.id.container);
        timeReportTitle = (TextView) findViewById(R.id.time_report_title);
        Calendar month = Calendar.getInstance();
        timeReportTitle.setText(getString(R.string.time_report_for) + " " + DateFormat.format("MM.yyyy", month));

        TableLayout tableLayout = createTimeTable();
        viewContainer.addView(tableLayout);
    }

    @NonNull
    private TableLayout createTimeTable() {
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
            TextView dayNumber = createDayNumberCell(day);
            tableRowDayNumbers.addView(dayNumber);

            TextView hoursWorked = createHoursWorkedCell("8.00");
            tableRowDays.addView(hoursWorked);
            currentColumnPosition++;
        }
        insertEmptyCells(tableRowDayNumbers, tableRowDays, getEmptyColumnsLeft(currentColumnPosition));
        return tableLayout;
    }

    private int getEmptyColumnsLeft(int currentColumnPosition) {
        return 7 - currentColumnPosition;
    }

    @NonNull
    private TextView createHoursWorkedCell(String hoursWorkedValue) {
        TextView hoursWorked = new TextView(getApplicationContext());
        hoursWorked.setBackgroundResource(R.drawable.cell_graylight_bounds);
        hoursWorked.setText(hoursWorkedValue);
        hoursWorked.setTextColor(Color.BLACK);
        hoursWorked.setPadding(3, 3, 3, 3);
        return hoursWorked;
    }

    private TextView createDayNumberCell(int day) {
        TextView dayNumber = new TextView(getApplicationContext());
        dayNumber.setBackgroundColor(ContextCompat.getColor(this, R.color.stxnext_green));
        dayNumber.setText(String.valueOf(day));
        dayNumber.setTextColor(Color.BLACK);
        dayNumber.setPadding(3, 3, 3, 3);
        return dayNumber;
    }

    private void createDaysNames(TableLayout tableLayout) {
        TableRow tableRowNames = new TableRow(getApplicationContext());
        for (int i = 0; i < 7; i++) {
            TextView dayName = new TextView(getApplicationContext());
            dayName.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light_darker));
            dayName.setTextColor(Color.BLACK);
            dayName.setPadding(3, 3, 3, 3);
            dayName.setText(getString(DaysShorcuts.values()[i].getDayShortcut()));
            tableRowNames.addView(dayName);
        }
        tableLayout.addView(tableRowNames);
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

}
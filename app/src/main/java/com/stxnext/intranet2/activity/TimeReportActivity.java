package com.stxnext.intranet2.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiTimeReportCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;
import com.stxnext.intranet2.model.DaysShorcuts;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Łukasz Ciupa on 2015-09-02.
 */
public class TimeReportActivity extends AppCompatActivity {

    public static final String USER_ID_TAG = "userId";
    private String userId= null;
    private Toolbar toolbar;
    private View progressView;
    private List<TimeReportDayContainer> timeReportDayViews = new ArrayList<TimeReportDayContainer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_report);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final LinearLayout timeReportsLLayout = (LinearLayout) findViewById(R.id.time_reports);
        Calendar month = Calendar.getInstance();
        progressView = findViewById(R.id.progress_container);
        progressView.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            userId = getIntent().getStringExtra(USER_ID_TAG);

            UserApi userApi = new UserApiImpl(this, null);
            UserApiTimeReportCallback userApiCallback = new UserApiTimeReportCallback() {
                @Override public void onTimeReportReceived(List<TimeReportDay> timeReportDays, Calendar month) {
                    TimeReportActivity.this.onTimeReportReceived(timeReportDays, month, timeReportsLLayout);
                }
            };

            userApi.getTimeReport(userId, month, userApiCallback);
            month = Calendar.getInstance();
            month.add(Calendar.MONTH, -1);
            userApi.getTimeReport(userId, month, userApiCallback);
            month = Calendar.getInstance();
            month.add(Calendar.MONTH, -2);
            userApi.getTimeReport(userId, month, userApiCallback);
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
            createDayOfWeekHeaderRow(tableLayout);
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
        createDayOfWeekHeaderRow(tableLayout);
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
        dayNumber.setTextColor(ContextCompat.getColor(this, R.color.stxnext_cardview_white));
        dayNumber.setPadding(3, 3, 3, 3);
        return dayNumber;
    }

    private void createDayOfWeekHeaderRow(TableLayout tableLayout) {
        TableRow tableRowNames = new TableRow(getApplicationContext());
        tableRowNames.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < 7; i++) {
            TextView dayName = new TextView(getApplicationContext());
            if (isWeekendDay(i)) {
                dayName.setBackgroundColor(ContextCompat.getColor(this,  R.color.stxnext_cardview_white));
            } else {
                dayName.setBackgroundColor(ContextCompat.getColor(this,  R.color.stxnext_cardview_white));
            }
            dayName.setGravity(Gravity.CENTER);
            dayName.setTextColor(ContextCompat.getColor(this, R.color.stxnext_green));
            dayName.setPadding(3, 3, 3, 3);
            dayName.setText(getString(DaysShorcuts.values()[i].getDayShortcut()));
            dayName.setTypeface(Typeface.DEFAULT_BOLD);
            dayName.setWidth(0);
            tableRowNames.addView(dayName, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }

        LinearLayout.LayoutParams innerLP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //innerLP.gravity = Gravity.CENTER;

        LinearLayout outerLinearLayout = new LinearLayout(getBaseContext());
        outerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        outerLinearLayout.addView(tableRowNames, innerLP);

        outerLinearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.stxnext_green_dark));
        outerLinearLayout.setPadding(0, 0, 0, 7);
        LinearLayout.LayoutParams outerLP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        tableLayout.addView(outerLinearLayout, outerLP);
    }

    private boolean isWeekendDay(int day) {
        return day > 4;
    }

    private void insertEmptyCells(TableRow tableRowDayNumbers, TableRow tableRowDays, int daysToFill) {
        for(int i = 0; i < daysToFill; i++) {
            TextView dayNumber = new TextView(getApplicationContext());
            dayNumber.setBackgroundColor(ContextCompat.getColor(this,  R.color.stxnext_cardview_white));
            dayNumber.setPadding(3, 3, 3, 3);
            tableRowDayNumbers.addView(dayNumber);
            TextView hoursWorked = new TextView(getApplicationContext());
            hoursWorked.setBackgroundColor(ContextCompat.getColor(this,   R.color.stxnext_cardview_white));
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

    private synchronized void onTimeReportReceived(List<TimeReportDay> timeReportDays, Calendar month, LinearLayout timeReportsLLayout) {
        TableLayout tableLayout = createTimeTable(timeReportDays);
        TimeReportDayContainer timeReportDayContainer = new TimeReportDayContainer(month, tableLayout);
        timeReportDayViews.add(timeReportDayContainer);
        if (timeReportDayViews.size() > 2) {
            Comparator<TimeReportDayContainer> comparator = new Comparator<TimeReportDayContainer>() {
                @Override
                public int compare(TimeReportDayContainer lhs, TimeReportDayContainer rhs) {
                    if ((lhs.month.getTimeInMillis() < rhs.month.getTimeInMillis())) {
                        return -1;
                    } else if ((lhs.month.getTimeInMillis() == rhs.month.getTimeInMillis())) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            };
            comparator = Collections.reverseOrder(comparator);
            Collections.sort(timeReportDayViews, comparator);
            for (int i = 0; i < timeReportDayViews.size(); i++) {
                TimeReportDayContainer containerElement = timeReportDayViews.get(i);
                TextView timeReportTitle = new TextView(this);
                timeReportTitle.setGravity(Gravity.CENTER);
                timeReportTitle.setText(getString(R.string.time_report_for) + " " + DateFormat.format("MM.yyyy", containerElement.month));
                timeReportTitle.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.stxnext_green));
                timeReportTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                timeReportTitle.setTypeface(null, Typeface.BOLD);
                timeReportTitle.setPadding(0, getResources().getDimensionPixelSize(R.dimen.time_report_title_padding_top), 0, 0);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, getResources().getDimensionPixelSize(R.dimen.time_report_title_text_view_size));
                layoutParams.gravity = Gravity.CENTER;

                LinearLayout outCardLL = new LinearLayout(this);
                CardView card = new CardView(this);
                LinearLayout linearLayout = new LinearLayout(TimeReportActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(timeReportTitle, layoutParams);
                linearLayout.addView(containerElement.timeReportDayView);
                card.addView(linearLayout);

                card.setContentPadding(30, 30, 30, 40);
                card.setCardElevation(6.0f);
                card.setRadius(20.0f);

                LinearLayout.LayoutParams outLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                int bottomMarg = i == timeReportDayViews.size() - 1 ? 20 : 0;
                int sideMarg = ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
                    ? 16 : 3;

                outLP.setMargins(sideMarg, 20, sideMarg, bottomMarg);
                card.setLayoutParams(outLP);

                outCardLL.addView(card);
                timeReportsLLayout.addView(outCardLL, outLP);
            }
            progressView.setVisibility(View.GONE);
        }
    }

    private class TimeReportDayContainer {

        public Calendar month;
        public TableLayout timeReportDayView;

        public TimeReportDayContainer(Calendar month, TableLayout timeReportDayView) {
            this.month = month;
            this.timeReportDayView = timeReportDayView;
        }
    }
}

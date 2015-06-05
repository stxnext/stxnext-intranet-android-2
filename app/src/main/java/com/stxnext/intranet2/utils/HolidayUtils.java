package com.stxnext.intranet2.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tomasz Konieczny on 2015-06-05.
 */
public class HolidayUtils {

    private static final int CURRENT_YEAR = 2015;

    private static final List<DateTime> holidayDays;

    static {
        holidayDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        //1 stycznia
        calendar.set(CURRENT_YEAR, Calendar.JANUARY, 1);
        holidayDays.add(new DateTime(calendar.getTime()));

        //6 stycznia
        calendar.set(CURRENT_YEAR, Calendar.JANUARY, 6);
        holidayDays.add(new DateTime(calendar.getTime()));

        //Wielkanoc
        calendar.set(CURRENT_YEAR, Calendar.APRIL, 5);
        holidayDays.add(new DateTime(calendar.getTime()));

        //Wielkanoc
        calendar.set(CURRENT_YEAR, Calendar.APRIL, 6);
        holidayDays.add(new DateTime(calendar.getTime()));

        //1 maja
        calendar.set(CURRENT_YEAR, Calendar.MAY, 1);
        holidayDays.add(new DateTime(calendar.getTime()));

        //3 maja
        calendar.set(CURRENT_YEAR, Calendar.MAY, 3);
        holidayDays.add(new DateTime(calendar.getTime()));

        //Zielone świątki
        calendar.set(CURRENT_YEAR, Calendar.MAY, 24);
        holidayDays.add(new DateTime(calendar.getTime()));

        //Boże ciało
        calendar.set(CURRENT_YEAR, Calendar.JUNE, 4);
        holidayDays.add(new DateTime(calendar.getTime()));

        //Święto Wojska Polskiego
        calendar.set(CURRENT_YEAR, Calendar.AUGUST, 15);
        holidayDays.add(new DateTime(calendar.getTime()));

        //Wszystkich świętych
        calendar.set(CURRENT_YEAR, Calendar.NOVEMBER, 1);
        holidayDays.add(new DateTime(calendar.getTime()));

        //Święto Niepodległości
        calendar.set(CURRENT_YEAR, Calendar.NOVEMBER, 11);
        holidayDays.add(new DateTime(calendar.getTime()));

        //Boże Narodzenie
        calendar.set(CURRENT_YEAR, Calendar.DECEMBER, 25);
        holidayDays.add(new DateTime(calendar.getTime()));

        //Boże Narodzenie
        calendar.set(CURRENT_YEAR, Calendar.DECEMBER, 26);
        holidayDays.add(new DateTime(calendar.getTime()));
    }

    public static int getWorkingDays(Date dateFrom, Date dateTo) {
        int workingDays = 0;
        final DateTime jodaDateFrom = new DateTime(dateFrom);
        final DateTime jodaDateTo = new DateTime(dateTo);

        DateTime tempDate = jodaDateFrom;
        while(tempDate.compareTo(jodaDateTo) <= 0) {
            if (tempDate.getDayOfWeek() != DateTimeConstants.SATURDAY
                    && tempDate.getDayOfWeek() != DateTimeConstants.SUNDAY
                    && !isHoliday(tempDate)) {
                workingDays++;
            }

            tempDate = tempDate.plusDays(1);
        }

        return workingDays;
    }

    private static boolean isHoliday(DateTime date) {
        for (DateTime holiday : holidayDays) {
            if (holiday.compareTo(date) == 0) {
                return true;
            }
        }

        return false;
    }

}

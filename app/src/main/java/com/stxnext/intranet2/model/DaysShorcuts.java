package com.stxnext.intranet2.model;

import com.stxnext.intranet2.R;

import org.joda.time.Days;

/**
 * Created by Lukasz Ciupa on 11.09.2015.
 */
public enum DaysShorcuts {

    MONDAY(R.string.monday_short),
    TUESDAY(R.string.tuesday_short),
    WEDNESDAY(R.string.wednesday_short),
    THURSDAY(R.string.thursday_short),
    FRIDAY(R.string.friday_short),
    SATURDAY(R.string.saturday_short),
    SUNDAY(R.string.sunday_short);

    private final int dayShortcut;

    DaysShorcuts(int dayShortcut) {
        this.dayShortcut = dayShortcut;
    }

    public int getDayShortcut() {
        return dayShortcut;
    }
}

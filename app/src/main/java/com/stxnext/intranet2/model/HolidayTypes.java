package com.stxnext.intranet2.model;

import com.stxnext.intranet2.R;

/**
 * Created by Lukasz Ciupa on 2015-05-31.
 */
public enum HolidayTypes {

    PLANNED("planowany", R.string.planned_leave),
    LEAVE_AT_REQUEST("zadanie", R.string.leave_at_request),
    ILLNESS("l4", R.string.illness),
    COMPASSIONATE("okolicznosciowy", R.string.compassionate_leave),
    OTHER("inne", R.string.other);

    private String absenceName;
    private int resourceId;

    HolidayTypes(String absenceName, int resourceId) {
        this.absenceName = absenceName;
        this.resourceId = resourceId;
    }

    public String getAbsenceName() {
        return this.absenceName;
    }

    public int getResourceId() {
        return resourceId;
    }
}

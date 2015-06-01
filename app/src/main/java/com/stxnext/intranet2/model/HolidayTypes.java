package com.stxnext.intranet2.model;

import com.stxnext.intranet2.R;

/**
 * Created by Lukasz Ciupa on 2015-05-31.
 */
public enum HolidayTypes {
    PLANNED("planowany", R.string.absence_planned),
    LEAVE_AT_REQUEST("zadanie",R.string.absence_leave_at_request),
    ILLNESS("l4",R.string.absence_illness),
    COMPASSIONATE("okolicznosciowy",R.string.absence_compassionate),
    OTHER("inne",R.string.absence_compassionate);

    private String absenceName;
    private int resourceId;
    HolidayTypes(String absenceName, int resourceId){
        this.absenceName = absenceName;
        this.resourceId = resourceId;
    }

    public String getAbsenceName(){
        return this.absenceName;
    }

    public int getResourceId() {
        return resourceId;
    }
}

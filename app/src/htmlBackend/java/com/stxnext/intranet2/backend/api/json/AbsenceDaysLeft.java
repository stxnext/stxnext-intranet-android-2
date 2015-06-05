package com.stxnext.intranet2.backend.api.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lukasz Ciupa on 2015-06-02.
 */
public class AbsenceDaysLeft {

    @SerializedName("mandated")
    private int mandated;

    @SerializedName("days")
    private int days;

    @SerializedName("left")
    private int left;

    public int getMandated() {
        return mandated;
    }

    public int getDays() {
        return days;
    }

    public int getLeft() {
        return left;
    }
}

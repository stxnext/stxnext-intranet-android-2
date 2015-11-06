package com.stxnext.intranet2.backend.model.team;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lukasz on 06.11.2015.
 */
public class Project {

    @SerializedName("this_month_worked_hours") private Double thisMonthWorkedHours;
    private Client client;
    private long id;
    private String name;
    private @SerializedName("last_month_worked_hours") Double lastMonthWorkedHours;


}

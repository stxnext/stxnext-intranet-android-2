package com.stxnext.intranet2.backend.model.team;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lukasz on 06.11.2015.
 */
@DatabaseTable(tableName = "project")
public class Project {

    @DatabaseField(id = true)
    private long id;
    @DatabaseField
    @SerializedName("this_month_worked_hours")
    private Double thisMonthWorkedHours;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Client client;
    @DatabaseField
    private String name;
    @DatabaseField
    @SerializedName("last_month_worked_hours")
    private Double lastMonthWorkedHours;


}

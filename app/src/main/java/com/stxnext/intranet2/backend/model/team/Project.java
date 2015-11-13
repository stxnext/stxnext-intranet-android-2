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
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Team team;
    @DatabaseField
    @SerializedName("this_month_worked_hours")
    private Double thisMonthWorkedHours;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Client client;
    @DatabaseField
    private String name;
    @DatabaseField
    @SerializedName("last_month_worked_hours")
    private Double lastMonthWorkedHours;

    public Project() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getThisMonthWorkedHours() {
        return thisMonthWorkedHours;
    }

    public void setThisMonthWorkedHours(Double thisMonthWorkedHours) {
        this.thisMonthWorkedHours = thisMonthWorkedHours;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLastMonthWorkedHours() {
        return lastMonthWorkedHours;
    }

    public void setLastMonthWorkedHours(Double lastMonthWorkedHours) {
        this.lastMonthWorkedHours = lastMonthWorkedHours;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}

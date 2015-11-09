package com.stxnext.intranet2.backend.model.time;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bkosarzycki on 06.11.15.
 */
public class TimeEntryPost {

    @SerializedName("project_id") long  projectId;

    @SerializedName("ticket_id") Integer ticketId;

    float time;

    String description;

    String date;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

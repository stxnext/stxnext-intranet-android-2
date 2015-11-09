package com.stxnext.intranet2.backend.model.time;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bkosarzycki on 06.11.15.
 */
public class TimeEntryResponse {

    @SerializedName("added") String addedDate;

    @SerializedName("user_id")  int userId;

    @SerializedName("ticket_id") int ticketId;

    @SerializedName("modified") String modifiedDate;

    String date;

    float time;

    long  id;

    @SerializedName("desc") String description;

    //"project": {"client_name": "Client2", "project_name": "Football"},


    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

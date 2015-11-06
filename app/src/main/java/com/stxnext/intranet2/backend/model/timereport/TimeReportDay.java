package com.stxnext.intranet2.backend.model.timereport;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lukasz on 02.11.2015.
 */
public class TimeReportDay {

    @SerializedName("is_working_day") private Boolean isWorkingDay;
    @SerializedName("day_no") private int dayNumber;
    private Double time;
    @SerializedName("day_of_week") private int dayOfWeek;
    @SerializedName("late_entry") private Boolean lateEntry;
    private String date;

    public Boolean getIsWorkingDay() {
        return isWorkingDay;
    }

    public void setIsWorkingDay(Boolean isWorkingDay) {
        this.isWorkingDay = isWorkingDay;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Boolean getLateEntry() {
        return lateEntry;
    }

    public void setLateEntry(Boolean lateEntry) {
        this.lateEntry = lateEntry;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TimeReportDay{" +
                "isWorkingDay=" + isWorkingDay +
                ", dayNumber=" + dayNumber +
                ", time=" + time +
                ", dayOfWeek=" + dayOfWeek +
                ", lateEntry=" + lateEntry +
                ", date='" + date + '\'' +
                '}';
    }
}

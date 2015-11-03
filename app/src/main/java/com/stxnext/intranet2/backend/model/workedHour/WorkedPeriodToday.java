package com.stxnext.intranet2.backend.model.workedHour;

/**
 * Created by bkosarzycki on 20.08.15.
 */
public class WorkedPeriodToday extends WorkedPeriod {

    private String arrival;
    private float remaining;
    private float present;

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public float getRemaining() {
        return remaining;
    }

    public void setRemaining(float remaining) {
        this.remaining = remaining;
    }

    public float getPresent() {
        return present;
    }

    public void setPresent(float present) {
        this.present = present;
    }
}

package com.stxnext.intranet2.backend.model.workedHour;

/**
 * Created by bkosarzycki on 20.08.15.
 */
public class WorkedHours {

    private WorkedPeriodToday today;
    private WorkedPeriod month;
    private WorkedPeriod quarter;

    public WorkedPeriodToday getToday() {
        return today;
    }

    public void setToday(WorkedPeriodToday today) {
        this.today = today;
    }

    public WorkedPeriod getMonth() {
        return month;
    }

    public void setMonth(WorkedPeriod month) {
        this.month = month;
    }

    public WorkedPeriod getQuarter() {
        return quarter;
    }

    public void setQuarter(WorkedPeriod quarter) {
        this.quarter = quarter;
    }
}

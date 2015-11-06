package com.stxnext.intranet2.backend.model.timereport;

import java.util.List;

/**
 * Created by Lukasz on 02.11.2015.
 * Gson mapping class.
 */
public class TimeReport {

    private List<TimeReportDay> days;

    public List<TimeReportDay> getDays() {
        return days;
    }

    public void setDays(List<TimeReportDay> days) {
        this.days = days;
    }
}

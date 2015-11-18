package com.stxnext.intranet2.backend.callback;

import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Bartosz Kosarzycki
 */
public interface UserApiTimeReportCallback {
    void onTimeReportReceived(List<TimeReportDay> timeReportDays, Calendar month);
}

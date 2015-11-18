package com.stxnext.intranet2.backend.callback;

import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public interface UserApiCallback extends UserApiTimeReportCallback {

    void onUserReceived(User user);

    void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request);

    void onOutOfOfficeResponse(boolean entry);

    void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft);

    void onRequestError();
}

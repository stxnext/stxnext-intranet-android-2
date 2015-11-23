package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiTimeReportCallback;
import com.stxnext.intranet2.model.HolidayTypes;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public interface UserApi extends EmployeesCommonApi {

    void requestForUser(String userId);

    void submitOutOfOfficeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation);

    void submitWorkFromHomeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation);

    void submitOutOfOffice(boolean workFromHome, Date submissionDate, Date startHour, Date endHour, String explanation);

    void submitHolidayAbsence(HolidayTypes absenceType, Date endDate, Date startDate, String remarks);

    void getAbsenceDaysLeft();

    void getTimeReport(String userId, Calendar month, UserApiTimeReportCallback callback);

}

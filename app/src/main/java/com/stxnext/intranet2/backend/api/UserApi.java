package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.model.HolidayTypes;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public abstract class UserApi extends EmployeesCommonApi {

    protected final UserApiCallback apiCallback;
    protected Context context;

    public UserApi(Context context, UserApiCallback callback) {
        this.apiCallback = callback;
        this.context = context;
    }

    public abstract void requestForUser(String userId);

    public abstract void submitOutOfOfficeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation);

    public abstract void submitWorkFromHomeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation);

    public abstract void submitOutOfOffice(boolean workFromHome, Date submissionDate, Date startHour, Date endHour, String explanation);

    public abstract void submitHolidayAbsence(HolidayTypes absenceType, Date endDate, Date startDate, String remarks);

    public abstract void getAbsenceDaysLeft();

    public abstract void getTimeReport(String userId, Calendar month);

}

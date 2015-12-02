package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.google.common.collect.Lists;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiTimeReportCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.model.HolidayTypes;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class UserApiImpl implements UserApi {

    protected final UserApiCallback apiCallback;
    protected Context context;

    public UserApiImpl(Context context, UserApiCallback callback) {
        this.context = context;
        this.apiCallback = callback;
    }

    @Override
    public void requestForUser(String userId) {
        User user = new User("0", "John", "Smith", "john.smith", "+48 921 231 212",
                "Poznań", Lists.newArrayList("Programmer"), "john.smith@stxnext.pl", "johny", "Mobile Team", null);
        apiCallback.onUserReceived(user);
    }

    @Override
    public void submitOutOfOfficeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation) {

    }

    @Override
    public void submitWorkFromHomeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation) {

    }

    @Override
    public void submitOutOfOffice(boolean workFromHome, Date submissionDate, Date startHour, Date endHour, String explanation) {

    }

    @Override
    public void submitHolidayAbsence(HolidayTypes absenceType, Date endDate, Date startDate, String remarks) {
        apiCallback.onAbsenceResponse(true, true, true);
    }

    @Override
    public void getAbsenceDaysLeft() {
        apiCallback.onAbsenceDaysLeftReceived(14, 0, 5);
    }

    @Override
    public void getTimeReport(String userId, Calendar month, UserApiTimeReportCallback callback) {

    }
}

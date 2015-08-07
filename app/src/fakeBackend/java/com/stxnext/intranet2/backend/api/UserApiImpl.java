package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.model.HolidayTypes;

import java.util.Date;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class UserApiImpl extends UserApi {

    public UserApiImpl(Context context, UserApiCallback callback) {
        super(context, callback);
    }

    @Override
    public void requestForUser(String userId) {
        User user = new User(null, "John", "Smith", "john.smith", "+48 921 231 212",
                "Poznań", "Programmer", "john.smith@stxnext.pl", "johny", "Mobile Team", null);
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

    }

    @Override
    public void getAbsenceDaysLeft() {

    }
}

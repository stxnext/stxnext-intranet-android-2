package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.UserImpl;
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
        User user = new UserImpl(null, "Marian", "Kowalski", "mariano.kowalsky", "+48 600 211 321",
                "Poznań", "Programista", "marian.kowalski@stxnext.pl", "marianno", "Team Mobilny", null);
        apiCallback.onUserReceived(user);
    }

    @Override
    public void submitOutOfOfficeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation) {

    }

    @Override
    public void submitWorkFromHomeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation) {

    }

    @Override
    public void submitHolidayAbsence(HolidayTypes absenceType, Date endDate, Date startDate, String remarks) {

    }
}

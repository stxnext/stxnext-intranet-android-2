package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.model.HolidayTypes;

import java.util.Date;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public abstract class UserApi {

    protected final UserApiCallback apiCallback;
    protected Context context;

    public UserApi(Context context, UserApiCallback callback) {
        this.apiCallback = callback;
        this.context = context;
    }

    public abstract void requestForUser(String userId);

    public abstract void submitOutOfOfficeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation);

    public abstract void submitWorkFromHomeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation);

    public abstract void submitHolidayAbsence(HolidayTypes absenceType, Date endDate, Date startDate, String remarks);

}

package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public abstract class EmployeesApi {

    protected final EmployeesApiCallback apiCallback;
    protected Context context;

    public EmployeesApi(Context context, EmployeesApiCallback callback) {
        this.apiCallback = callback;
        this.context = context;
    }

    public abstract void requestForEmployees(boolean forceRequest);

    public abstract void requestForOutOfOfficeAbsenceEmployees();

    public abstract void requestForWorkFromHomeAbsenceEmpolyees();

    public abstract void requestForHolidayAbsenceEmployees();

}

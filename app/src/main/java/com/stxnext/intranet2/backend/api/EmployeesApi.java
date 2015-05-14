package com.stxnext.intranet2.backend.api;

import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public abstract class EmployeesApi {

    protected final EmployeesApiCallback apiCallback;

    public EmployeesApi(EmployeesApiCallback callback) {
        this.apiCallback = callback;
    }

    public abstract void requestForEmployees();

    public abstract void requestForOutOfOfficeAbsenceEmpolyees();

    public abstract void requestForWorkFromHomeAbsenceEmpolyees();

    public abstract void requestForHolidayAbsenceEmpolyees();

}

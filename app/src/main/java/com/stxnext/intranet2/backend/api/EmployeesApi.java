package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public interface EmployeesApi extends EmployeesCommonApi {

    void requestForEmployees(boolean forceRequest);

    void requestForOutOfOfficeAbsenceEmployees();

    void requestForWorkFromHomeAbsenceEmpolyees();

    void requestForHolidayAbsenceEmployees();

}

package com.stxnext.intranet2.backend.api;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public interface EmployeesApi {

    void requestForEmployees(boolean forceRequest);

    void requestForOutOfOfficeAbsenceEmployees();

    void requestForWorkFromHomeAbsenceEmpolyees();

    void requestForHolidayAbsenceEmployees();

}

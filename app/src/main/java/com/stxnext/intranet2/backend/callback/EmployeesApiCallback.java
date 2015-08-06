package com.stxnext.intranet2.backend.callback;

import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.UserImpl;

import java.util.List;
import java.util.Set;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public interface EmployeesApiCallback {

    void onEmployeesListReceived(List<UserImpl> employees);

    void onAbsenceEmployeesListReceived(Set<Absence> absenceEmployees);

}

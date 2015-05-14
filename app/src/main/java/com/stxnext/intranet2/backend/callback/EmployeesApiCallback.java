package com.stxnext.intranet2.backend.callback;

import com.stxnext.intranet2.backend.model.User;

import java.util.List;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public interface EmployeesApiCallback {

    void onEmployeesListReceived(List<User> employees);

}

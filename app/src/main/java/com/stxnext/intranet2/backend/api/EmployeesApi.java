package com.stxnext.intranet2.backend.api;

import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiCallback;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public abstract class EmployeesApi {

    protected final EmployeesApiCallback apiCallback;

    public EmployeesApi(EmployeesApiCallback callback) {
        this.apiCallback = callback;
    }

    public abstract void requestForEmployees();

}

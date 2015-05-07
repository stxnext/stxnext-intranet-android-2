package com.stxnext.intranet2.backend.api;

import com.stxnext.intranet2.backend.callback.UserApiCallback;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public abstract class UserApi {

    protected final UserApiCallback apiCallback;

    public UserApi(UserApiCallback callback) {
        this.apiCallback = callback;
    }

    public abstract void requestForUser(String userId);

}

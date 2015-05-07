package com.stxnext.intranet2.backend.api;

import com.stxnext.intranet2.backend.callback.UserApiCallback;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class UserApiImpl extends UserApi {

    public UserApiImpl(UserApiCallback callback) {
        super(callback);
    }

    @Override
    public void requestForUser(String userId) {
        apiCallback.onUserReceived(null);
    }
}

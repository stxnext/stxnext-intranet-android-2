package com.stxnext.intranet2.backend.api;

import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.UserImpl;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class UserApiImpl extends UserApi {

    public UserApiImpl(UserApiCallback callback) {
        super(callback);
    }

    @Override
    public void requestForUser(String userId) {
        User user = new UserImpl(null, "Marian", "Kowalski", "mariano.kowalsky", "+48 600 211 321",
                "Poznań", "Programista", "marian.kowalski@stxnext.pl", "marianno", "Team Mobilny", null);
        apiCallback.onUserReceived(user);
    }
}

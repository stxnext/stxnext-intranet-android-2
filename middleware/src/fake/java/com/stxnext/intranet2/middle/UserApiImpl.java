package com.stxnext.intranet2.middle;

import com.stxnext.intranet2.middle.api.callback.UserApiCallback;
import com.stxnext.intranet2.middle.api.request.UserApi;
import com.stxnext.intranet2.middle.model.impl.UserImpl;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class UserApiImpl extends UserApi {

    public UserApiImpl(UserApiCallback callback) {
        super(callback);
    }

    @Override
    public void requestForUser(String userId) {
        apiCallback.onUserReceived(new UserImpl("Marian", "Kowalski", "marian.kowalski",
                "+48 600 211 232", "Poznañ", "Programista", "marian.kowalski@stxnext.pl",
                "mariano.kowalo", "Team Mobilny", null));
    }

}

package com.stxnext.intranet2.backend.callback;

import com.stxnext.intranet2.backend.model.User;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public interface UserApiCallback {

    void onUserReceived(User user);

}

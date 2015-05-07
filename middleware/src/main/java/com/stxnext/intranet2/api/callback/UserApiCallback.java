package com.stxnext.intranet2.api.callback;

import com.stxnext.intranet2.model.User;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public interface UserApiCallback {

    void onUserReceived(User user);

}

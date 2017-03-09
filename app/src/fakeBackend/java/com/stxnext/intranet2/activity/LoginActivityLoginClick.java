package com.stxnext.intranet2.activity;

import android.view.View;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.utils.Session;

public class LoginActivityLoginClick {

    public static View.OnClickListener createLoginClick(final LoginActivity loginActivity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session session = Session.getInstance(loginActivity);
                session.setUserId("100");

                loginActivity.findViewById(R.id.login_failed_label).setVisibility(View.INVISIBLE);
                loginActivity.onActivityResult(LoginActivity.RC_WEB_SIGN_IN, LoginActivity.LOGIN_OK, null);
            }
        };
    }
}

package com.stxnext.intranet2.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.LoginActivity;
import com.stxnext.intranet2.activity.MyProfileActivity;
import com.stxnext.intranet2.utils.Session;

public class LoginActivityLoginClick {

    public static View.OnClickListener createLoginClick(final Activity context, final int RC_WEB_SIGN_IN_ID) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session session = Session.getInstance(context);
                session.setUserId("100");

                context.findViewById(R.id.login_failed_label).setVisibility(View.INVISIBLE);
                Intent webLoginIntent = new Intent(context, MyProfileActivity.class);
                context.startActivityForResult(webLoginIntent, LoginActivity.RC_WEB_SIGN_IN);
            }
        };
    }
}

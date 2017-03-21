package com.stxnext.intranet2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.common.collect.Lists;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.LoginActivity;
import com.stxnext.intranet2.activity.LoginWebActivity;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.impl.AbsenceImpl;
import com.stxnext.intranet2.backend.model.impl.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class LoginActivityLoginClick {

    public static View.OnClickListener createLoginClick(final Activity context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.findViewById(R.id.login_failed_label).setVisibility(View.INVISIBLE);
                Intent webLoginIntent = new Intent(context, LoginWebActivity.class);
                context.startActivityForResult(webLoginIntent, LoginActivity.RC_WEB_SIGN_IN);
            }
        };
    }
}

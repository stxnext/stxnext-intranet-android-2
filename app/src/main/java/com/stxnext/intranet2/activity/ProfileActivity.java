package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.api.request.UserApi;

/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        UserApi userApi;
    }


}

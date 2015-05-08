package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.stxnext.intranet2.R;

public class LoginActivity extends ActionBarActivity {

    public static final int LOGIN_OK = 1;
    public static final int LOGIN_FAILED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.setResult(LOGIN_OK);
                LoginActivity.this.finish();
            }
        });
    }


}

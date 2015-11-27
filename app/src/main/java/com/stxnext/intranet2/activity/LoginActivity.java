package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.login.LoginActivityLoginClick;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.Session;

public class LoginActivity extends AppCompatActivity {

    public static final int LOGIN_OK = 1;
    public static final int LOGIN_FAILED = 2;
    public static final int LOGIN_CANCELED = 3;

    public static final int RC_WEB_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(
                LoginActivityLoginClick.createLoginClick(this)
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Config.TAG, "onActivityResult()");
        switch (requestCode) {
            case RC_WEB_SIGN_IN: {
                if (resultCode == LOGIN_OK) {
                    setResult(resultCode);
                    finish();
                } else if (resultCode == LOGIN_FAILED) {
                    Session.getInstance(this).logout();
                    findViewById(R.id.login_failed_label).setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(LoginActivity.LOGIN_CANCELED);
        super.onBackPressed();
    }
}

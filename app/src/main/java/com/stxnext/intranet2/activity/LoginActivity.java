package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.GooglePlusConnectionManager;

public class LoginActivity extends AppCompatActivity implements GooglePlusConnectionManager.GooglePlusConnectionCallback {

    public static final int LOGIN_OK = 1;
    public static final int LOGIN_FAILED = 2;
    public static final int LOGIN_CANCELED = 3;

    private static final int RC_SIGN_IN = 0;
    private static final int RC_WEB_SIGN_IN = 1;

    private GooglePlusConnectionManager googlePlusConnectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        googlePlusConnectionManager = new GooglePlusConnectionManager(this, this);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //Google plus client implementation.
//                googlePlusConnectionManager.signIn();
                Intent webLoginIntent = new Intent(LoginActivity.this, LoginWebActivity.class);
                startActivityForResult(webLoginIntent, RC_WEB_SIGN_IN);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        googlePlusConnectionManager.signOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Config.TAG, "onActivityResult()");
        switch (requestCode) {
            case RC_SIGN_IN:
                // There was onConnectionFailed(final ConnectionResult connectionResult)
                // user has fixed problems with connection, so connect again.
                googlePlusConnectionManager.connect();
                break;
            case RC_WEB_SIGN_IN:
                // LoginWebActivity has returned
                setResult(resultCode);
                finish();
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setResult(LOGIN_OK);
                finish();
            }
        });
    }

    @Override
    public void onLoginFailed() {

    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    startIntentSenderForResult(connectionResult.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(Config.TAG, "Sign in intent could not be sent: " + e.getLocalizedMessage());
                    googlePlusConnectionManager.retry();
                }
            }
        });
    }
}

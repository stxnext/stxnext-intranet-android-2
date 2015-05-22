package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.stxnext.intranet2.App;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.Session;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "Intranet2";
    
    public static final int LOGIN_OK = 1;
    public static final int LOGIN_FAILED = 2;

    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient googleApiClient;
    private boolean signingIntentInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        googleApiClient = buildGoogleApiClient();
    }

    private GoogleApiClient buildGoogleApiClient() {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);

        return builder.build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        getToken();
    }

    private void getToken() {
        final String email = Plus.AccountApi.getAccountName(googleApiClient);
        String scopes = "oauth2: " + Scopes.PLUS_LOGIN;
        Thread tokenObtainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String scopes = "oauth2: " + Scopes.PLUS_LOGIN;
                    String token = GoogleAuthUtil.getToken(LoginActivity.this, email, scopes);
                    Session.getInstance().setGooglePlusToken(token);
                    onGooglePlusTokenSucceeded();
                } catch (UserRecoverableAuthException e) {
                    Log.e(App.TAG, "Error: UserRecoverableAuthException G+ Sign in");
                    Log.w(App.TAG, "Class: " + ((Object) this).getClass().getName() + "; method: onConnected; UserRecoverableAuthException");
                    onGooglePlusTokenFailure();
                } catch (GoogleAuthException e) {
                    Log.e(App.TAG, "Error: GoogleAuthException G+ Sign in");
                    Log.w(App.TAG, "Class: " + ((Object) this).getClass().getName() + "; method: onConnected; GoogleAuthException");
                    onGooglePlusTokenFailure();
                } catch (IOException e) {
                    Log.e(App.TAG, "Error: IOException G+ Sign in");
                    Log.w(App.TAG, "Class: " + ((Object) this).getClass().getName() + "; method: onConnected; IOException");
                    onGooglePlusTokenFailure();
                }
            }
        });
        tokenObtainThread.start();
    }

    private void onGooglePlusTokenSucceeded() {
        LoginActivity.this.setResult(LOGIN_OK);
        LoginActivity.this.finish();
    }

    private void onGooglePlusTokenFailure() {
        LoginActivity.this.setResult(LOGIN_FAILED);
        LoginActivity.this.finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(Config.TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            Log.w(Config.TAG, "API Unavailable.");
        } else if (!signingIntentInProgress && connectionResult.hasResolution()) {
            try {
                startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
                signingIntentInProgress = true;
            } catch (IntentSender.SendIntentException e) {
                Log.i(Config.TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                signingIntentInProgress = false;
                googleApiClient.connect();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Config.TAG, "onActivityResult()");
        switch (requestCode) {
            case RC_SIGN_IN:
                signingIntentInProgress = false;
                if (!googleApiClient.isConnecting()) {
                    googleApiClient.connect();
                }
                break;
        }
    }
}

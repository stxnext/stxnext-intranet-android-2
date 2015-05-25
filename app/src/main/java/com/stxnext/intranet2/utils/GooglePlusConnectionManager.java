package com.stxnext.intranet2.utils;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.IOException;

/**
 * Created by Tomasz Konieczny on 2015-05-25.
 */
public class GooglePlusConnectionManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final Context context;
    private final GoogleApiClient googleApiClient;
    private final GooglePlusConnectionCallback callback;

    private boolean connectionUnresolvable = false;

    public GooglePlusConnectionManager(Context context, GooglePlusConnectionCallback callback) {
        this.context = context;
        this.callback = callback;

        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        ;
    }

    public void signIn() {
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    public void signOut() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    public boolean retry() {
        boolean result = connectionUnresolvable;
        if (!connectionUnresolvable) {
            connectionUnresolvable = true;
            signIn();
        }

        return result;
    }

    @Override
    public void onConnected(Bundle bundle) {
        obtainToken();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            Log.w(Config.TAG, "API Unavailable.");
        } else {
            callback.onConnectionFailed(connectionResult);
        }
    }

    private void obtainToken() {
        final String email = Plus.AccountApi.getAccountName(googleApiClient);
        Thread tokenObtainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String scopes = "oauth2: " + Scopes.PLUS_LOGIN;
                    String token = GoogleAuthUtil.getToken(context, email, scopes);
                    Session.getInstance(context).setGooglePlusToken(token);
                    callback.onLoggedIn();
                } catch (UserRecoverableAuthException e) {
                    Log.e(Config.TAG, "UserRecoverableAuthException G+ Sign in");
                    callback.onLoginFailed();
                } catch (GoogleAuthException e) {
                    Log.e(Config.TAG, "GoogleAuthException G+ Sign in");
                    callback.onLoginFailed();
                } catch (IOException e) {
                    Log.e(Config.TAG, "IOException G+ Sign in");
                    callback.onLoginFailed();
                }
            }
        });

        tokenObtainThread.start();
    }

    public interface GooglePlusConnectionCallback {

        void onLoggedIn();

        void onLoginFailed();

        void onConnectionFailed(ConnectionResult connectionResult);

    }
}

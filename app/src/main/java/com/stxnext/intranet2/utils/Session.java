package com.stxnext.intranet2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.PersistentCookieStore;

/**
 * Created by Lukasz Ciupa on 2015-05-20.
 */
public class Session {

    private static Session instance = null;

    private String googlePlusToken = null;
    private String authorizationCode = null;
    private SharedPreferences preferences = null;
    private PersistentCookieStore cookieStore = null;
    private Boolean logged = null;
    private static final String PREFERENCES_NAME = "com.stxnext.intranet2";
    private static final String TOKEN_PREFERENCE = "token";
    private static final String CODE_PREFERENCE = "code";
    private static final String LOGGED_PREFERENCE = "logged";

    private Session(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        cookieStore = new PersistentCookieStore(context);
    }

    public static Session getInstance(Context context) {
        if (instance == null) {
            instance = new Session(context);
        }
        return instance;
    }

    public void logout() {
        this.googlePlusToken = null;
        preferences.edit().clear().commit();
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
        preferences.edit().putString(CODE_PREFERENCE, authorizationCode).commit();
    }

    public String getAuthorizationCode() {
        if (authorizationCode == null) {
            authorizationCode = preferences.getString(CODE_PREFERENCE, null);
        }
        return authorizationCode;
    }

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }

    public void setGooglePlusToken(String googlePlusToken) {
        this.googlePlusToken = googlePlusToken;
        preferences.edit().putString(TOKEN_PREFERENCE, googlePlusToken).apply();
    }

    public String getGooglePlusToken() {
        if (googlePlusToken == null) {
            googlePlusToken = preferences.getString(TOKEN_PREFERENCE, null);
        }

        return googlePlusToken;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
        preferences.edit().putBoolean(LOGGED_PREFERENCE, logged).commit();
    }

    public boolean isLogged() {
        if (logged == null) {
            logged = preferences.getBoolean(LOGGED_PREFERENCE, false);
        }
        return logged;
    }
}

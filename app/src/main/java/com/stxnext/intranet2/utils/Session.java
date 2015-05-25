package com.stxnext.intranet2.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lukasz Ciupa on 2015-05-20.
 */
public class Session {

    private static Session instance = null;

    private String googlePlusToken = null;
    private SharedPreferences preferences = null;
    private static final String PREFERENCES_NAME = "com.stxnext.intranet2";
    private static final String TOKEN_PREFERENCE = "token";

    private Session(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static Session getInstance(Context context) {
        if (instance == null) {
            instance = new Session(context);
        }
        return instance;
    }

    public void setGooglePlusToken(String googlePlusToken) {
        this.googlePlusToken = googlePlusToken;
        preferences.edit().putString(TOKEN_PREFERENCE, googlePlusToken).commit();
    }

    public String getGooglePlusToken() {
        if (googlePlusToken == null) {
            googlePlusToken = preferences.getString(TOKEN_PREFERENCE, null);
        }

        return googlePlusToken;
    }

    public boolean isLogged() {
        return getGooglePlusToken() != null;
    }
}

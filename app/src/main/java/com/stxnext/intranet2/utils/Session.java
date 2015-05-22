package com.stxnext.intranet2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.stxnext.intranet2.App;

/**
 * Created by Lukasz Ciupa on 2015-05-20.
 */
public class Session {

    private static Session instance = null;

    private String googlePlusToken = null;
    private SharedPreferences preferences = null;
    private static final String PREFERENCES_NAME = "com.stxnext.intranet2";
    private static final String TOKEN_PREFERENCE = "token";

    private Session() {
        preferences = App.getAppContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setGooglePlusToken(String googlePlusToken) {
        this.googlePlusToken = googlePlusToken;
        preferences.edit().putString(TOKEN_PREFERENCE, googlePlusToken).commit();
    }

    public String getGooglePlusToken() {
        if (googlePlusToken != null) return googlePlusToken;
        googlePlusToken = preferences.getString(TOKEN_PREFERENCE, null);
        return googlePlusToken;
    }

    public boolean isLogged() {
        if (googlePlusToken != null) return true;
        googlePlusToken = preferences.getString(TOKEN_PREFERENCE, null);
        return (googlePlusToken != null);
    }
}

package com.stxnext.intranet2;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;

import com.pixplicity.easyprefs.library.Prefs;


/**
 * Created by bkosarzycki on 26.08.15.
 */
public class IntranetApp extends Application {

    static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        boolean isDebuggable =  ( 0 != ( getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (BuildConfig.DEBUG && isDebuggable) {
            STXStetho.init(this);
        }
    }

    public static Context getContext() {
        return mContext;
    }
}
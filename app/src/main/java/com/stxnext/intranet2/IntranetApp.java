package com.stxnext.intranet2;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.okhttp.OkHttpClient;


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
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());

            OkHttpClient client = new OkHttpClient();
            client.networkInterceptors().add(new StethoInterceptor());
        }


    }

    public static Context getContext() {
        return mContext;
    }
}
;
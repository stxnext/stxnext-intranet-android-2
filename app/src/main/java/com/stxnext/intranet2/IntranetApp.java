package com.stxnext.intranet2;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.stxnext.intranet2.database.DatabaseHelper;
import com.stxnext.intranet2.utils.DBManager;


/**
 * Created by bkosarzycki on 26.08.15.
 */
public class IntranetApp extends Application {

    static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();
        //create database for db-versioning (on app startup)
        DBManager.getInstance(mContext).getEmployees();

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

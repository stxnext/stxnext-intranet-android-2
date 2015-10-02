package com.stxnext.intranet2;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.stxnext.intranet2.broadcast.AlarmManagerService;

import java.util.Calendar;


/**
 * Created by bkosarzycki on 26.08.15.
 */
public class IntranetApp extends Application {

    static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();

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

        setRecurringAlarmManagerIfNeeded();
    }

    private void setRecurringAlarmManagerIfNeeded() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 19); // For 7 PM
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pi = PendingIntent.getService(mContext, 0,
                new Intent(mContext, AlarmManagerService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
    }

    public static Context getContext() {
        return mContext;
    }
}

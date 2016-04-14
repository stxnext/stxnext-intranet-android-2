package com.stxnext.intranet2;

import android.app.Application;
import android.content.pm.ApplicationInfo;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Mariusz on 13.04.2016.
 */
public class STXStetho {
    public static void init(Application app) {
        boolean isDebuggable =  ( 0 != ( app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (BuildConfig.DEBUG && isDebuggable)
        Stetho.initialize(
                Stetho.newInitializerBuilder(app)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(app))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(app))
                        .build());

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
    }
}

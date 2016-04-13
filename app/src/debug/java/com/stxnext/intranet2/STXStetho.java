package com.stxnext.intranet2;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Mariusz on 13.04.2016.
 */
public class STXStetho {
    public static void init(Application app) {
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

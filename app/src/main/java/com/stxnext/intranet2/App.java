package com.stxnext.intranet2;

import android.app.Application;
import android.content.Context;

/**
 * Created by Lukasz Ciupa on 2015-05-21.
 */
public class App extends Application {

    public static final String TAG = "Intranet";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}

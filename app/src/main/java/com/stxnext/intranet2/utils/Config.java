package com.stxnext.intranet2.utils;

/**
 * Created by Tomasz Konieczny on 2015-05-21.
 */
public class Config {

    public static final String TAG = "STX Intranet 2";

    public static String getTag(Object object) {
        return TAG + ":" + object.getClass().getName();
    }

}

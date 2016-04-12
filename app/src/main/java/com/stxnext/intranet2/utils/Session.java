package com.stxnext.intranet2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.format.DateFormat;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.Calendar;

/**
 * Created by Lukasz Ciupa on 2015-05-20.
 */
public class Session {

    private static Session instance = null;

    private String authorizationCode = null;
    private SharedPreferences preferences = null;
    private String userId = null;
    private CookieManager cookieManager = null;
    private Integer absenceDaysLeft = null;
    private Integer daysMandated = null;
    private Context context = null;

    private static final String PREFERENCES_NAME = "com.stxnext.intranet2";
    private static final String SUPERHERO_MODE_PREFERENCE = "com.stxnext.intranet2";
    private static final String TIME_REPORT_NOTIFICATION_PREFERENCE = "time_report_notification";
    private static final String TIME_REPORT_NOTIFICATION_HOUR_PREFERENCE = "time_report_notification_hour";
    private static final String CAN_DRAWS_OVERLAYERS_SHOWED = "draws_over_showed";
    private static final String CODE_PREFERENCE = "code";
    private static final String USER_ID_PREFERENCE = "user_id";

    private Session(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (isLogged()) {
            initializeOkHttpCookieHandler();
        }
    }

    public static Session getInstance(Context context) {
        if (instance == null) {
            instance = new Session(context);
        }
        return instance;
    }

    public void logout() {
        userId = null;
        preferences.edit()
                .remove(USER_ID_PREFERENCE)
                .remove(SUPERHERO_MODE_PREFERENCE)
                .remove(CODE_PREFERENCE)
                .clear()
                .commit();
        clearManagerCookieStore();
        NotificationUtils.disableTimeReportAlarmManager(context);
    }

    public void clearManagerCookieStore() {
        CookieStore managerCookieStore = cookieManager.getCookieStore();
        managerCookieStore.removeAll();
    }

    /**
     * Has to be executed after initialization of webView (best before starting loading page).
     * When executed on logout() then there is a strange error where webKitCookieManager is not null
     * but causes crash of whole appliaction on executing of its methods.
     */
    public void clearWebKitCookieStore() {
        android.webkit.CookieManager webKitCookieManager = android.webkit.CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webKitCookieManager.removeAllCookies(null);
        } else {
            if (webKitCookieManager.hasCookies()) {
                webKitCookieManager.removeAllCookie();
            }
        }
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
        preferences.edit().putString(CODE_PREFERENCE, authorizationCode).commit();
    }

    public String getAuthorizationCode() {
        if (authorizationCode == null) {
            authorizationCode = preferences.getString(CODE_PREFERENCE, null);
        }
        return authorizationCode;
    }

    public CookieManager getCookieManager() {
        if (cookieManager == null) {
            cookieManager = new CookieManager(new com.stxnext.intranet2.utils.PersistentCookieStore(context.getApplicationContext()), CookiePolicy.ACCEPT_ALL);
        }
        return cookieManager;
    }

    /**
     * Sets globally cookie manager (with cookie store) for okhttp clients.
     */
    public void initializeOkHttpCookieHandler() {
        CookieHandler.setDefault(getCookieManager());
    }

    public void setUserId(String userId) {
        this.userId = userId;
        preferences.edit().putString(USER_ID_PREFERENCE, userId).apply();
    }

    public String getUserId() {
        if (userId == null) {
            userId = preferences.getString(USER_ID_PREFERENCE, null);
        }
        return userId;
    }


    public void enableSuperHeroMode(boolean enabled) {
        preferences.edit().putBoolean(SUPERHERO_MODE_PREFERENCE, enabled).apply();
    }

    public boolean isSuperHeroModeEnabled() {
        return preferences.getBoolean(SUPERHERO_MODE_PREFERENCE, false);
    }

    public void setOverlayersShowed(boolean showed){
        preferences.edit().putBoolean(CAN_DRAWS_OVERLAYERS_SHOWED, showed).apply();
    }

    public boolean isOverlayersShowed(){
        return preferences.getBoolean(CAN_DRAWS_OVERLAYERS_SHOWED, false);
    }

    public void setTimeReportNotification(boolean enabled) {
        preferences.edit().putBoolean(TIME_REPORT_NOTIFICATION_PREFERENCE, enabled).apply();
    }

    public boolean isTimeReportNotification() {
        return preferences.getBoolean(TIME_REPORT_NOTIFICATION_PREFERENCE, true);
    }

    public void setTimeReportNoticationHour(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        CharSequence hourValue = DateFormat.format("kk:mm", calendar);
        preferences.edit().putString(TIME_REPORT_NOTIFICATION_HOUR_PREFERENCE, hourValue.toString()).apply();
    }

    public String getTimeReportNotificationHour() {
        String defaultValue = "17:00";
        return preferences.getString(TIME_REPORT_NOTIFICATION_HOUR_PREFERENCE, defaultValue);
    }

    public void setAbsenceDaysLeft(int absenceDaysLeft) {
        this.absenceDaysLeft = absenceDaysLeft;
    }

    public Integer getAbsenceDaysLeft() {
        return absenceDaysLeft;
    }

    public void setDaysMandated(int daysMandated) {
        this.daysMandated = daysMandated;
    }

    public Integer getDaysMandated() {
        return daysMandated;
    }

    public boolean isLogged() {
        return getUserId() != null;
    }
}

package com.stxnext.intranet2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.cookie.Cookie;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Lukasz Ciupa on 2015-05-20.
 */
public class Session {

    private static Session instance = null;

    private String googlePlusToken = null;
    private String authorizationCode = null;
    private SharedPreferences preferences = null;
    // Stores cookies in sharedPreferences. Used by AsyncHttpClient.
    private PersistentCookieStore cookieStore = null;
    private Boolean logged = null;
    private String userId = null;
    private CookieManager cookieManager = null;
    private Context context;
    private Integer absenceDaysLeft = null;
    private Integer daysMandated = null;
    private Integer days;

    private static final String PREFERENCES_NAME = "com.stxnext.intranet2";
    private static final String TOKEN_PREFERENCE = "token";
    private static final String CODE_PREFERENCE = "code";
    private static final String LOGGED_PREFERENCE = "logged";
    private static final String USER_ID_PREFERENCE = "id";

    private Session(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        cookieStore = new PersistentCookieStore(context);
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
        this.googlePlusToken = null;
        preferences.edit().clear().commit();
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

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }

    public void clearCookieStore() {
        cookieStore.clear();
    }

    /**
     * Needed for OkHTTPRequest and Picasso. Invoked once by initializeOkHttpCookieHandler().
     * @return
     */
    private CookieManager getCookieManager() {
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        if (cookieManager.getCookieStore().getCookies().size() == 0 && isLogged()) {
            initializeManagerCookieStore();
        }
        return cookieManager;
    }

    /**
     * Loads cookies from cookieStore (PersistentCookieStore) to Needed for OkHTTPRequest and Picasso.
     */
    public void initializeOkHttpCookieHandler() {
        CookieHandler.setDefault(getCookieManager());
    }

    private void initializeManagerCookieStore() {
        List<Cookie> cookies = cookieStore.getCookies();
        CookieStore managerCookieStore = cookieManager.getCookieStore();

        for (Cookie apacheCookie : cookies) {
            HttpCookie httpCookie = convertApacheCookieToHttpCookie(apacheCookie);
            managerCookieStore.add(null, httpCookie);
        }
    }

    private HttpCookie convertApacheCookieToHttpCookie(Cookie apacheCookie) {
        HttpCookie httpCookie = new HttpCookie(apacheCookie.getName(), apacheCookie.getValue());
        httpCookie.setComment(apacheCookie.getComment());
        httpCookie.setCommentURL(apacheCookie.getCommentURL());
        httpCookie.setDomain(apacheCookie.getDomain());
        Date currentDate = Calendar.getInstance().getTime();
        Date expiryDate = apacheCookie.getExpiryDate();
        if (expiryDate != null) {
            long maxAgeMilliseconds = expiryDate.getTime() - currentDate.getTime();
            float maxAgeSecondsFloat = maxAgeMilliseconds/1000;
            long maxAgeSeconds = (long) maxAgeSecondsFloat;
            httpCookie.setMaxAge(maxAgeSeconds);
        } else {
            httpCookie.setMaxAge(6000);
        }
        httpCookie.setPath(apacheCookie.getPath());
        StringBuilder portsStringBuilder = new StringBuilder();
        int[] ports = apacheCookie.getPorts();
        if (ports != null) {
            for (int i = 0; i < ports.length; ++i) {
                portsStringBuilder.append(ports[i]);
                // if this is not last one
                if (i < ports.length - 1) {
                    portsStringBuilder.append(",");
                }
            }
            httpCookie.setPortlist(portsStringBuilder.toString());
        } else {
            httpCookie.setPortlist(null);
        }
        httpCookie.setVersion(apacheCookie.getVersion());
        return httpCookie;
    }

    public void setGooglePlusToken(String googlePlusToken) {
        this.googlePlusToken = googlePlusToken;
        preferences.edit().putString(TOKEN_PREFERENCE, googlePlusToken).apply();
    }

    public String getGooglePlusToken() {
        if (googlePlusToken == null) {
            googlePlusToken = preferences.getString(TOKEN_PREFERENCE, null);
        }

        return googlePlusToken;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
        preferences.edit().putBoolean(LOGGED_PREFERENCE, logged).commit();
    }

    public void setUserId(String userId) {
        this.userId = userId;
        preferences.edit().putString(USER_ID_PREFERENCE, userId).commit();
    }

    public String getUserId() {
        if (userId == null) {
            userId = preferences.getString(USER_ID_PREFERENCE, null);
        }
        return userId;
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

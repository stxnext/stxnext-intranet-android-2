package com.stxnext.intranet2.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by Lukasz Ciupa on 2015-05-26.
 */
public class ConnectionManager {

    private static final String LOGIN_URL = "https://accounts.google.com/o/oauth2/auth?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar.readonly&redirect_uri=https%3A%2F%2Fintranet.stxnext.pl%2Fauth%2Fcallback&response_type=code&client_id=83120712902.apps.googleusercontent.com&access_type=offline";

    private Context context;
    private ConnectionCallback callback;
    private WebView webView;
    private AsyncHttpClient httpClient;

    public ConnectionManager(Context context, WebView webView, ConnectionCallback callback) {

        this.context = context;
        this.webView = webView;
        this.callback = callback;
        httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(Session.getInstance(context).getCookieStore());
    }

    public void signIn() {
        Session.getInstance(context).clearWebKitCookieStore();
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(Config.getTag(this), "onPageStarted, url:" + url);
                if (url.contains("code=")) {
                    webView.stopLoading();
                    String code = url.substring(url.indexOf("code=") + "code=".length());
                    Session.getInstance(context).setAuthorizationCode(code);
                    authorize();
                } else {
                    Log.d(Config.getTag(this), "Url doesn't contain code");
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(Config.getTag(this), "onPageFinished, url" + url);
                super.onPageFinished(view, url);
            }
        };
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(LOGIN_URL);

    }

    private void authorize() {

        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(Config.getTag(this), "callback onSuccess");
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                getUserId();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(Config.getTag(this), "callback failure");
                callback.onLoginFailed();
            }
        };
        httpClient.get("https://intranet.stxnext.pl/auth/callback?code=" + Session.getInstance(context).getAuthorizationCode(), asyncHttpResponseHandler);
    }

    private void getUserId() {
        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(Config.getTag(this), "getUserId onSuccess");
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                if (response.contains("id")) {
                    int beginIndex = response.indexOf("\"id\"") + "\"id\": ".length();
                    String id = response.substring(beginIndex);
                    id = id.substring(0, id.indexOf(","));
                    Session.getInstance(context).setUserId(id);
                    Session.getInstance(context).initializeOkHttpCookieHandler();
                    callback.onLoggedIn();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(Config.getTag(this), "getUserId onFailure");
                callback.onLoginFailed();
            }
        };
        httpClient.get("https://intranet.stxnext.pl/user/edit", asyncHttpResponseHandler);
    }

    public interface ConnectionCallback {

        void onLoggedIn();

        void onLoginFailed();

    }

}

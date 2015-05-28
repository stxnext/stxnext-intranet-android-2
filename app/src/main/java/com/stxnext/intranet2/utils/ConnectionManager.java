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

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.contains("code=")) {
                    webView.stopLoading();
                    String code = url.substring(url.indexOf("code=") + "code=".length());
                    Session.getInstance(context).setAuthorizationCode(code);
                    authorize();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
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
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                Session.getInstance(context).setLogged(true);
                callback.onLoggedIn();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onLoginFailed();
            }
        };
        httpClient.get("https://intranet.stxnext.pl/auth/callback?code=" + Session.getInstance(context).getAuthorizationCode(), asyncHttpResponseHandler);
    }

    public interface ConnectionCallback {

        void onLoggedIn();

        void onLoginFailed();

    }

}

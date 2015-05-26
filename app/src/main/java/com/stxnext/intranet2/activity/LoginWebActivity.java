package com.stxnext.intranet2.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.utils.Session;

/**
 * Created by Lukasz Ciupa on 2015-05-26.
 */
public class LoginWebActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "https://accounts.google.com/o/oauth2/auth?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar.readonly&redirect_uri=https%3A%2F%2Fintranet.stxnext.pl%2Fauth%2Fcallback&response_type=code&client_id=83120712902.apps.googleusercontent.com&access_type=offline";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_login);
        loadViews();
        logIn();
    }

    private void logIn() {
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.contains("code=")) {
                    webView.stopLoading();
                    String code = url.substring(url.indexOf("code=") + "code=".length());
                    Session.getInstance(LoginWebActivity.this).setAuthorizationCode(code);
                    setResult(LoginActivity.LOGIN_OK);
                    finish();
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

    private void loadViews() {
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        setResult(LoginActivity.LOGIN_CANCELED);
        super.onBackPressed();
    }
}

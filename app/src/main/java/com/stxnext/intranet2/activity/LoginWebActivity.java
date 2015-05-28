package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.utils.ConnectionManager;

/**
 * Created by Lukasz Ciupa on 2015-05-26.
 */
public class LoginWebActivity extends AppCompatActivity implements ConnectionManager.ConnectionCallback {


    private WebView webView;
    private ConnectionManager connectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_login);
        loadViews();
        logIn();
    }

    private void logIn() {
        connectionManager = new ConnectionManager(this, webView, this);
        connectionManager.signIn();
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

    @Override
    public void onLoggedIn() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setResult(LoginActivity.LOGIN_OK);
                finish();
            }
        });
    }

    @Override
    public void onLoginFailed() {

    }
}

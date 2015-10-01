package com.stxnext.intranet2.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.ConnectionManager;

/**
 * Created by Lukasz Ciupa on 2015-05-26.
 */
public class LoginWebActivity extends AppCompatActivity implements ConnectionManager.ConnectionCallback {

    private WebView webView;
    private View progressContainer;
    private DonutProgress progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_login);
        progressContainer = findViewById(R.id.progress_container);
        progressBar = (DonutProgress) findViewById(R.id.progress_bar);

        loadViews();
        logIn();
    }

    private void logIn() {
        ConnectionManager connectionManager = new ConnectionManager(this, webView, this);
        connectionManager.signIn();
        Log.d(Config.getTag(this), "Login end.");
    }

    private void loadViews() {
        WebChromeClient client = new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressContainer.animate()
                            .alpha(0)
                            .setStartDelay(100)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progressContainer.setVisibility(View.GONE);
                        }
                    });
                }
            }

        };
        webView = (WebView) findViewById(R.id.web_view);
        webView.setWebChromeClient(client);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setResult(LoginActivity.LOGIN_FAILED);
                finish();
            }
        });
    }
}

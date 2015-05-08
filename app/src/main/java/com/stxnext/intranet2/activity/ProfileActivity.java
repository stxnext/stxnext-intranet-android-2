package com.stxnext.intranet2.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.fragment.FloatingMenuFragment;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ProfileActivity extends AppCompatActivity
        implements UserApiCallback, FloatingMenuFragment.OnFlotingMenuItemClickListener {

    private static final String TAG = "ProfileActivity";
    private static final int LOGIN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_profile);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        prepareFloatinButton();

        UserApi userApi = new UserApiImpl(this);
        userApi.requestForUser(null);

        Intent loginIntent = new Intent(this, LoginActivity.class);
        if (isLogged()) {
            loadProfile();
        } else {
            startActivityForResult(loginIntent, LOGIN_REQUEST);
        }

    }

    private void prepareFloatinButton() {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.floating_button);
        final View plusView = viewGroup.getChildAt(0);
        if (plusView != null) {
            viewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plusView.animate().rotationBy(135).setDuration(200).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Fragment fragment = getFragmentManager().findFragmentByTag("Asdasd");

                            if (fragment == null || !fragment.isAdded()) {
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.floating_menu_container, new FloatingMenuFragment(), "Asdasd")
                                        .setCustomAnimations(
                                                android.R.animator.fade_in, android.R.animator.fade_out,
                                                android.R.animator.fade_in, android.R.animator.fade_out)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                getFragmentManager().popBackStackImmediate();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_REQUEST:
                if (resultCode == LoginActivity.LOGIN_OK) {
                    loadProfile();
                } else {

                }
                break;
            default:
                break;
        }
    }

    //TODO
    private void loadProfile() {
        Log.d(TAG, "loadProfile()");
    }

    //TODO
    private boolean isLogged() {
        return false;
    }

    @Override
    public void onUserReceived(User user) {

    }

    @Override
    public void onFloatingMenuItemClick(int option) {

    }
}

package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ProfileActivity extends AppCompatActivity implements UserApiCallback {

    public static final String USER_ID_TAG ="userId";
    private Toolbar toolbar;
    private TextView firstNameTextView;
    private TextView roleTextView;
    private TextView officeTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView skypeTextView;
    private TextView ircTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        firstNameTextView = (TextView) findViewById(R.id.first_name_text_view);
        roleTextView = (TextView) findViewById(R.id.role_text_view);
        officeTextView = (TextView) findViewById(R.id.office_text_view);
        emailTextView = (TextView) findViewById(R.id.email_text_view);
        phoneTextView = (TextView) findViewById(R.id.phone_text_view);
        skypeTextView = (TextView) findViewById(R.id.skype_text_view);
        ircTextView = (TextView) findViewById(R.id.irc_text_view);
        findViewById(R.id.floating_button).setVisibility(View.GONE);
        findViewById(R.id.worked_hours_container).setVisibility(View.GONE);
        findViewById(R.id.edit_profile_button).setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String userId = getIntent().getStringExtra(USER_ID_TAG);
        UserApi userApi = new UserApiImpl(this, this);
        userApi.requestForUser(userId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    @Override
    public void onUserReceived(User user) {
        firstNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        roleTextView.setText(user.getRole());
        officeTextView.setText(user.getLocalization());
        emailTextView.setText(user.getEmail());
        phoneTextView.setText(user.getPhoneNumber());
        if (user.getSkype() != "null") {
            skypeTextView.setText(user.getSkype());
        }
        if (user.getIrc() != "null") {
            ircTextView.setText(user.getIrc());
        }
    }

}

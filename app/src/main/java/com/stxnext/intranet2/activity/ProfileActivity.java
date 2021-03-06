package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;
import com.stxnext.intranet2.dialog.ContactDialogFragment;

import java.util.Calendar;
import java.util.List;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ProfileActivity extends CommonProfileActivity implements UserApiCallback {

    public static final String USER_ID_TAG ="userId";
    private TextView firstNameTextView;
    private TextView roleTextView;
    private TextView officeTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView skypeTextView;
    private TextView ircTextView;
    private ImageView superheroImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        firstNameTextView = (TextView) findViewById(R.id.first_name_text_view);
        roleTextView = (TextView) findViewById(R.id.role_text_view);
        officeTextView = (TextView) findViewById(R.id.office_text_view);
        teamsTextView = (TextView) findViewById(R.id.teams_text_view);
        teamLabel = (TextView) findViewById(R.id.team_label);
        emailTextView = (TextView) findViewById(R.id.email_text_view);
        phoneTextView = (TextView) findViewById(R.id.phone_text_view);
        skypeTextView = (TextView) findViewById(R.id.skype_text_view);
        ircTextView = (TextView) findViewById(R.id.irc_text_view);
        superheroImageView = (ImageView) findViewById(R.id.superhero_image_view);
        findViewById(R.id.floating_button).setVisibility(View.GONE);

//        super.initializeProfileImageView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String userId = getIntent().getStringExtra(USER_ID_TAG);
        // this can be also a MyProfileActivity, then here userId is null.
        if (userId != null) {
            UserApi userApi = new UserApiImpl(this, this);
            userApi.requestForUser(userId);
        }

        onProfilePictureClick();

    }

    @Override
    public void initializeContentView() {
        setContentView(R.layout.activity_profile);
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
    public void onUserReceived(final User user) {
        super.onUserReceived(user);

        final String firstName = user.getFirstName();
        final String userName = firstName + " " + user.getLastName();
        getSupportActionBar().setTitle(userName);
        firstNameTextView.setText(userName);
        roleTextView.setText(user.getRoles() != null && user.getRoles().size() > 0 ? user.getRoles().get(0) : "");
        officeTextView.setText(user.getLocalization());
        emailTextView.setText(user.getEmail());

        if (isFemaleName(firstName)) {
            superheroImageView.setImageResource(R.drawable.mrs_superhero_profile);
        }

        String imageAddress = "https://intranet.stxnext.pl" + user.getPhoto();
        Picasso.with(this).load(imageAddress).placeholder(R.drawable.avatar_placeholder)
                .resizeDimen(R.dimen.profile_photo_size, R.dimen.profile_photo_size)
                .centerCrop()
                .into(profileImageView);

        if (!"null".equals(user.getPhoneNumber())) {
            phoneTextView.setText(user.getPhoneNumber());
        }
        if (!"null".equals(user.getSkype())) {
            skypeTextView.setText(user.getSkype());
        }
        if (!"null".equals(user.getIrc())) {
            ircTextView.setText(user.getIrc());
        }

        findViewById(R.id.user_info_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactDialogFragment.show(
                        getFragmentManager(),
                        firstName,
                        user.getPhoneNumber(),
                        user.getEmail());
            }
        });

        fillTeams(user);
    }

    private boolean isFemaleName(String firstName) {
        return firstName.substring(firstName.length() - 1, firstName.length()).equals("a");
    }

    @Override
    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {
        // nothing to do
    }

    @Override
    public void onOutOfOfficeResponse(boolean latenessResponse) {
        // nothing to do
    }

    @Override
    public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {
        // nothing to do
    }

    @Override
    public void onTimeReportReceived(List<TimeReportDay> timeReportDays, Calendar month) {

    }

    @Override
    public void onRequestError() {

    }
}

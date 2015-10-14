package com.stxnext.intranet2.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.utils.Session;


/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!getResources().getBoolean(R.bool.superheromode_available)) {
            findViewById(R.id.superhero_mode_container).setVisibility(View.GONE);
        }

        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getInstance(SettingsActivity.this).logout();
                setResult(RESULT_OK);
                finish();
            }
        });

        final Session session = Session.getInstance(this);
        SwitchCompat superHeroModeOption = (SwitchCompat) findViewById(R.id.superhero_switch);
        superHeroModeOption.setChecked(session.isSuperHeroModeEnabled());
        superHeroModeOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                session.enableSuperHeroMode(isChecked);
                setResult(RESULT_OK);
            }
        });

        SwitchCompat timeReportNotification = (SwitchCompat) findViewById(R.id.time_report_notification_switch);
        timeReportNotification.setChecked(session.isTimeReportNotification());
        timeReportNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                session.setTimeReportNotification(isChecked);
                setResult(RESULT_OK);
            }
        });

        try {
            TextView versionInfo = (TextView) findViewById(R.id.version_info);
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionInfo.setText(String.format("STX Next Intranet %s\n\n%s", version, versionInfo.getText().toString()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }



}

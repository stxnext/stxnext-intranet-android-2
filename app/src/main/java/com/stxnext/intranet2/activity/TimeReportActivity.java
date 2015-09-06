package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stxnext.intranet2.R;

/**
 * Created by Łukasz Ciupa on 2015-09-02.
 */
public class TimeReportActivity extends AppCompatActivity {

    public static final String USER_ID_TAG = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_report);

    }
}

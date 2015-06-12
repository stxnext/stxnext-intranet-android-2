package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.OfficeInfoPagerAdapter;

/**
 * Created by Tomasz Konieczny on 2015-06-11.
 */
public class AboutActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private OfficeInfoPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentAdapter = new OfficeInfoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);

        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.sliding_tabs);
        tabStrip.setTabIndicatorColor(getResources().getColor(R.color.stxnext_green));
        tabStrip.setTextColor(getResources().getColor(R.color.stxnext_green_dark));
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

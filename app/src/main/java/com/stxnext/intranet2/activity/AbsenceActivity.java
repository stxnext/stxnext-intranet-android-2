package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.FragmentAdapter;
import com.stxnext.intranet2.fragment.HolidayFragment;
import com.stxnext.intranet2.fragment.OutOfOfficeFragment;
import com.stxnext.intranet2.fragment.WorkFromHomeFragment;

import java.util.ArrayList;

public class AbsenceActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<Fragment> viewPagerFragments = new ArrayList<Fragment>();
        viewPagerFragments.add(new HolidayFragment());
        viewPagerFragments.add(new WorkFromHomeFragment());
        viewPagerFragments.add(new OutOfOfficeFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), viewPagerFragments);
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

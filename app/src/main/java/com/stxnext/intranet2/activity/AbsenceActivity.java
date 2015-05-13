package com.stxnext.intranet2.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.FragmentAdapter;
import com.stxnext.intranet2.fragment.HolidayFragment;
import com.stxnext.intranet2.fragment.OutOfOfficeFragment;
import com.stxnext.intranet2.fragment.WorkFromHomeFragment;
import com.stxnext.intranet2.view.SlidingTabLayout;

import java.util.ArrayList;

public class AbsenceActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<Fragment> viewPagerFragments = new ArrayList<Fragment>();
        viewPagerFragments.add(new HolidayFragment());
        viewPagerFragments.add(new WorkFromHomeFragment());
        viewPagerFragments.add(new OutOfOfficeFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), viewPagerFragments);
        viewPager.setAdapter(fragmentAdapter);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);
    }
}

package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.AbsencesFragmentPagerAdapter;
import com.stxnext.intranet2.fragment.AbsencesListFragment;
import com.stxnext.intranet2.model.AbsencesTypes;

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
        ArrayList<AbsencesListFragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(AbsencesListFragment.newInstance(AbsencesTypes.OUT_OF_OFFICE));
        viewPagerFragments.add(AbsencesListFragment.newInstance(AbsencesTypes.WORK_FROM_HOME));
        viewPagerFragments.add(AbsencesListFragment.newInstance(AbsencesTypes.HOLIDAY));
        final AbsencesFragmentPagerAdapter fragmentAdapter = new AbsencesFragmentPagerAdapter(this, getSupportFragmentManager(), viewPagerFragments);
        viewPager.setAdapter(fragmentAdapter);

        final TextView countTextView = (TextView) findViewById(R.id.count_text_view);
        countTextView.setText("7");
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                final int count = fragmentAdapter.getEmployeesCount(position);
                countTextView.animate().rotationBy(720).setDuration(400).setInterpolator(new LinearOutSlowInInterpolator());
                countTextView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        countTextView.setText(String.valueOf(count));
                    }
                }, 150);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

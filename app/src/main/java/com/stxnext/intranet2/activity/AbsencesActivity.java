package com.stxnext.intranet2.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.AbsencesFragmentPagerAdapter;
import com.stxnext.intranet2.fragment.AbsencesListFragment;
import com.stxnext.intranet2.model.AbsencesTypes;

import java.util.ArrayList;

public class AbsencesActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener,
        AbsencesListFragment.OnAbsencesListDownloadedCallback {

    private ViewPager viewPager;
    private AbsencesFragmentPagerAdapter fragmentAdapter;
    private TextView countTextView;
    private boolean pendingAnimation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        countTextView = (TextView) findViewById(R.id.count_text_view);
        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.sliding_tabs);

        ArrayList<AbsencesListFragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(AbsencesListFragment.newInstance(AbsencesTypes.OUT_OF_OFFICE));
        viewPagerFragments.add(AbsencesListFragment.newInstance(AbsencesTypes.WORK_FROM_HOME));
        viewPagerFragments.add(AbsencesListFragment.newInstance(AbsencesTypes.HOLIDAY));

        tabStrip.setTabIndicatorColor(ContextCompat.getColor(this , R.color.stxnext_green));
        tabStrip.setTextColor(ContextCompat.getColor(this, R.color.stxnext_green_dark));

        fragmentAdapter = new AbsencesFragmentPagerAdapter(this, getSupportFragmentManager(), viewPagerFragments);
        viewPager.setAdapter(fragmentAdapter);
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
    protected void onResume() {
        super.onResume();
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        final int count = fragmentAdapter.getEmployeesCount(position);
        if (pendingAnimation) {
            countTextView.animate().cancel();
            countTextView.setRotation(0);
            pendingAnimation = false;
        }
        AnimatorListenerAdapter animationListener = new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                pendingAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pendingAnimation = false;
            }
        };

        if (count > 0) {
            if (countTextView.getAlpha() < 1) {
                countTextView.setScaleX(0.5f);
                countTextView.setScaleY(0.5f);
                countTextView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1)
                        .setDuration(250)
                        .setListener(animationListener)
                        .setInterpolator(new AccelerateDecelerateInterpolator());
            } else {
                countTextView.animate()
                        .rotationBy(720)
                        .setDuration(400)
                        .setInterpolator(new LinearOutSlowInInterpolator())
                        .setListener(animationListener);
            }
        } else if (countTextView.getAlpha() != 0) {
            countTextView.animate()
                    .alpha(0)
                    .scaleX(0.5f)
                    .scaleY(0.5f)
                    .setDuration(250)
                    .setInterpolator(new LinearOutSlowInInterpolator());
        }

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

    @Override
    public void onAbsencesDownloaded() {
        onPageSelected(viewPager.getCurrentItem());
    }

}

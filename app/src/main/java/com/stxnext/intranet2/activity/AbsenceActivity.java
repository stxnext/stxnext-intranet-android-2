package com.stxnext.intranet2.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.FragmentAdapter;
import com.stxnext.intranet2.fragment.HolidayFragment;
import com.stxnext.intranet2.fragment.OutOfOfficeFragment;
import com.stxnext.intranet2.fragment.WorkFromHomeFragment;

public class AbsenceActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(FragmentAdapter.HOLIDAY_FRAGMENT_POSITION);

        ActionBar.Tab tab = actionBar.newTab()
                .setText(R.string.absence)
                .setTabListener(new TabListener<HolidayFragment>(
                        this, "absence", HolidayFragment.class, FragmentAdapter.HOLIDAY_FRAGMENT_POSITION, viewPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.work_from_home)
                .setTabListener(new TabListener<WorkFromHomeFragment>(
                        this, "emplyeesList", WorkFromHomeFragment.class, FragmentAdapter.WORK_FROM_HOME_FRAGMENT_POSITION, viewPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.out_of_office)
                .setTabListener(new TabListener<OutOfOfficeFragment>(
                        this, "emplyeesList", OutOfOfficeFragment.class, FragmentAdapter.OUT_OF_OFFICE_FRAGMENT_POSITION, viewPager));
        actionBar.addTab(tab);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final int mViewPagerPosition;
        private final ViewPager mViewPager;

        /** Constructor used each time a new tab is created.
         * @param activity  The host Activity, used to instantiate the fragment
         * @param tag  The identifier tag for the fragment
         * @param clz  The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz, int viewPagerPosition, ViewPager viewPager) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mViewPagerPosition = viewPagerPosition;
            mViewPager = viewPager;
        }

    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
//            if (mFragment == null) {
//                // If not, instantiate and add it to the activity
//                mFragment = Fragment.instantiate(mActivity, mClass.getName());
//                ft.add(R.id.content, mFragment, mTag);
//            } else {
//                // If it exists, simply attach it in order to show it
//                ft.attach(mFragment);
//            }

            mViewPager.setCurrentItem(mViewPagerPosition);
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//            if (mFragment != null) {
//                // Detach the fragment, because another one is being attached
//                ft.detach(mFragment);
//            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }
}

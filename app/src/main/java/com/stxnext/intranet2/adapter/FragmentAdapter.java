package com.stxnext.intranet2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stxnext.intranet2.fragment.HolidayFragment;
import com.stxnext.intranet2.fragment.OutOfOfficeFragment;
import com.stxnext.intranet2.fragment.WorkFromHomeFragment;

/**
 * Created by Lukasz Ciupa on 2015-05-12.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    public static int HOLIDAY_FRAGMENT_POSITION = 0;
    public static int WORK_FROM_HOME_FRAGMENT_POSITION = 1;
    public static int OUT_OF_OFFICE_FRAGMENT_POSITION = 2;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HolidayFragment();
            case 1:
                return new WorkFromHomeFragment();
            case 2:
                return new OutOfOfficeFragment();
            default:
                return null;
        }
    }
}

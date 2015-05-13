package com.stxnext.intranet2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stxnext.intranet2.fragment.HolidayFragment;
import com.stxnext.intranet2.fragment.OutOfOfficeFragment;
import com.stxnext.intranet2.fragment.WorkFromHomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukasz Ciupa on 2015-05-12.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    //TODO change to resources string
    private static final String WORK_FROM_HOME_FRAGMENT_TITLE = "Work from home";
    private static final String HOLIDAY_FRAGMENT_TITLE = "Holiday";
    private static final String OUT_OF_OFFICE_FRAGMENT_TITLE = "Out of office";

    List<Fragment> fragments = new ArrayList<Fragment>();

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    //TODO maybe change to have titles inside fragments
    /**
     * Return the title of the item at {@code position}. This is important as what this method
     * returns is what is displayed in the {@link SlidingTabLayout}.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (fragments.get(position) instanceof HolidayFragment)
                return HOLIDAY_FRAGMENT_TITLE;
        else if (fragments.get(position) instanceof WorkFromHomeFragment)
                return WORK_FROM_HOME_FRAGMENT_TITLE;
        else if (fragments.get(position) instanceof OutOfOfficeFragment)
                return OUT_OF_OFFICE_FRAGMENT_TITLE;
        else return null;
    }
}

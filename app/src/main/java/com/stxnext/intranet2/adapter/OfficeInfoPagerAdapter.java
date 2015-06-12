package com.stxnext.intranet2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stxnext.intranet2.fragment.OfficeInfoFragment;
import com.stxnext.intranet2.model.Office;

/**
 * Created by Lukasz Ciupa on 2015-05-12.
 */
public class OfficeInfoPagerAdapter extends FragmentPagerAdapter {

    private final Office[] offices;

    public OfficeInfoPagerAdapter(FragmentManager fm) {
        super(fm);
        this.offices = Office.values();
    }

    @Override
    public int getCount() {
        return offices.length;
    }

    @Override
    public Fragment getItem(int position) {
        return OfficeInfoFragment.newInstance(offices[position]);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return offices[position].getCity();
    }

    public Office getOffice(int position) {
        return offices[position];
    }

}

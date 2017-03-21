package com.stxnext.intranet2.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stxnext.intranet2.fragment.AbsencesListFragment;
import com.stxnext.intranet2.model.AbsencesTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukasz Ciupa on 2015-05-12.
 */
public class AbsencesFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    List<AbsencesListFragment> fragments = new ArrayList<>();

    public AbsencesFragmentPagerAdapter(Context context, FragmentManager fm, List<AbsencesListFragment> fragments) {
        super(fm);
        this.context = context;
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

    @Override
    public CharSequence getPageTitle(int position) {
        Bundle bundle = fragments.get(position).getArguments();
        if (bundle != null) {
            AbsencesTypes type = (AbsencesTypes) bundle.getSerializable(AbsencesListFragment.TYPE_ARG);
            if (type != null) {
                return context.getString(type.getTitle());
            }
        }

        return "";
    }

    public int getEmployeesCount(int position) {
        return fragments.get(position).getCount();
    }

    public AbsencesTypes getItemType(int position) {
        return fragments.get(position).getType();
    }
}

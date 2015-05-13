package com.stxnext.intranet2.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.model.AbsencesTypes;

public class AbsencesListFragment extends Fragment {

    public static final String TYPE_ARG = "type";

    private AbsencesTypes type;

    public static AbsencesListFragment newInstance(AbsencesTypes type) {
        AbsencesListFragment fragment = new AbsencesListFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(TYPE_ARG, type);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getArguments() != null) {
            this.type = (AbsencesTypes) getArguments().getSerializable(TYPE_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_absence, container, false);
    }
}

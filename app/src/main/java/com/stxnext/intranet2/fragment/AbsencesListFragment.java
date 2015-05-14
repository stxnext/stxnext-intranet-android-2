package com.stxnext.intranet2.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.AbsencesListAdapter;
import com.stxnext.intranet2.model.AbsencesTypes;

public class AbsencesListFragment extends Fragment {

    public static final String TYPE_ARG = "type";

    private AbsencesTypes type;
    private View view;

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
        view = inflater.inflate(R.layout.fragment_absence, container, false);
        RecyclerView recycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        AbsencesListAdapter absencesListAdapter = new AbsencesListAdapter();
        recycleView.setAdapter(absencesListAdapter);
        return view;
    }
}

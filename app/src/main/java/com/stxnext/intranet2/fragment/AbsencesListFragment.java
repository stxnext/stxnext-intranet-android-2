package com.stxnext.intranet2.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.ProfileActivity;
import com.stxnext.intranet2.adapter.AbsencesListAdapter;
import com.stxnext.intranet2.backend.api.EmployeesApi;
import com.stxnext.intranet2.backend.api.EmployeesApiImpl;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.model.AbsencesTypes;

import java.util.List;

public class AbsencesListFragment extends Fragment implements EmployeesApiCallback, AbsencesListAdapter.OnItemClickListener {

    public static final String TYPE_ARG = "type";

    private AbsencesTypes type;
    private View view;
    private RecyclerView recycleView;
    private Context context;

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
        context = activity;
        if (getArguments() != null) {
            this.type = (AbsencesTypes) getArguments().getSerializable(TYPE_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_absence, container, false);
        recycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        EmployeesApi employeesApi = new EmployeesApiImpl(getActivity(), this);
        switch (type) {
            case HOLIDAY:
                employeesApi.requestForHolidayAbsenceEmpolyees();
                break;
            case WORK_FROM_HOME:
                employeesApi.requestForWorkFromHomeAbsenceEmpolyees();
                break;
            case OUT_OF_OFFICE:
                employeesApi.requestForOutOfOfficeAbsenceEmployees();
                break;
        }
        return view;
    }

    @Override
    public void onEmployeesListReceived(List<User> employees) {
        //nothing to do
    }

    @Override
    public void onAbsenceEmployeesListReceived(List<Absence> absenceEmployees) {
        AbsencesListAdapter absencesListAdapter = new AbsencesListAdapter(context, absenceEmployees, this);
        recycleView.setAdapter(absencesListAdapter);
    }

    @Override
    public void onItemClick(String userId) {
        startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(ProfileActivity.USER_ID_TAG, userId));
    }

    public int getCount() {
        return recycleView.getAdapter().getItemCount();
    }

}

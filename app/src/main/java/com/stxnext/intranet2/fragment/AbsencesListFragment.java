package com.stxnext.intranet2.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.model.AbsencesTypes;

import java.util.LinkedHashSet;
import java.util.List;

public class AbsencesListFragment extends Fragment implements EmployeesApiCallback, AbsencesListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TYPE_ARG = "type";
    private static String TAG = AbsencesListFragment.class.getName();

    private AbsencesTypes type;
    private RecyclerView recycleView;
    private Context context;
    private OnAbsencesListDownloadedCallback callback;
    private SwipeRefreshLayout swipeRefreshView;

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

        try {
            this.callback = (OnAbsencesListDownloadedCallback) getActivity();
        } catch (ClassCastException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_absence, container, false);
        recycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        swipeRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshView.setColorSchemeResources(
                R.color.stxnext_green_dark,
                R.color.stxnext_green,
                R.color.stxnext_green_darkest);
        swipeRefreshView.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshView.post(new Runnable() {
            @Override
            public void run() {
                if (recycleView.getAdapter() == null) {
                    swipeRefreshView.setRefreshing(true);
                }
            }
        });

        requestForData();
    }

    @Override
    public void onEmployeesListReceived(List<User> employees) {
        //nothing to do
    }

    @Override
    public void onAbsenceEmployeesListReceived(final LinkedHashSet<Absence> absenceEmployees) {
        Activity activity = getActivity();
        if (activity != null)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AbsencesListAdapter absencesListAdapter = new AbsencesListAdapter(context, absenceEmployees, type, AbsencesListFragment.this);
                    recycleView.setAdapter(absencesListAdapter);
                    callback.onAbsencesDownloaded(type);
                    if (swipeRefreshView.isRefreshing()) {
                        swipeRefreshView.setRefreshing(false);
                    }
                }
            });
        else
            Log.w(TAG, "Couldn't find activity in Absences list fragment!");
    }

    @Override
    public void onItemClick(String userId) {
        startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(ProfileActivity.USER_ID_TAG, userId));
    }

    public int getCount() {
        RecyclerView.Adapter adapter = recycleView.getAdapter();
        return adapter != null ? adapter.getItemCount() : 0;
    }

    @Override
    public void onRefresh() {
        requestForData();
    }

    private void requestForData() {
        EmployeesApi employeesApi = new EmployeesApiImpl(getActivity(), this);
        switch (type) {
            case HOLIDAY:
                employeesApi.requestForHolidayAbsenceEmployees();
                break;
            case WORK_FROM_HOME:
                employeesApi.requestForWorkFromHomeAbsenceEmpolyees();
                break;
            case OUT_OF_OFFICE:
                employeesApi.requestForOutOfOfficeAbsenceEmployees();
                break;
        }
    }

    public AbsencesTypes getType() {
        return type;
    }


    public interface OnAbsencesListDownloadedCallback {

        void onAbsencesDownloaded(AbsencesTypes type);

    }

}

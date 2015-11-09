package com.stxnext.intranet2.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.ProjectSpinnerAdapter;
import com.stxnext.intranet2.backend.model.project.Project;
import com.stxnext.intranet2.backend.model.project.ProjectResponse;
import com.stxnext.intranet2.backend.retrofit.ProjectListService;
import com.stxnext.intranet2.rest.IntranetRestAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RestAdapter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by bkosarzycki on 02.11.15.
 */

public class AddHoursActivity extends AppCompatActivity {

    @Bind(R.id.projects_spinner) AppCompatSpinner mProjectsSpinner;

    private final static String TAG = AddHoursActivity.class.getName();
    private ProjectSpinnerAdapter mAdapter;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private RestAdapter restAdapter;
    private ProjectListService mProjectListService;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hours);
        ButterKnife.bind(this);
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAdapter = new ProjectSpinnerAdapter(this, android.R.layout.simple_list_item_1);

        restAdapter = IntranetRestAdapter.build();
        mProjectListService = new ProjectListService(); //todo: change to retrofit //restAdapter.create(ProjectListService.class);

        getListOfProjects();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void getListOfProjects() {
        mAdapter.clear();

        mSubscriptions.add(
            mProjectListService.getProjects()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    new Observer<ProjectResponse>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "Retrofit get projects list call completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "Error in downloading list of projects: " + e.toString());
                        }

                        @Override
                        public void onNext(ProjectResponse projectResponse) {
                            mAdapter.setProjects(projectResponse.getProjects());
                            Toast.makeText(mContext, "Added items: " + projectResponse.getProjects().size(), Toast.LENGTH_SHORT).show();
                            mProjectsSpinner.setAdapter(mAdapter);
                        }
                    }
            )
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        mSubscriptions = getNewCompositeSubIfUnsubscribed(mSubscriptions);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mSubscriptions != null)
            mSubscriptions.unsubscribe();
    }

    public CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed())
            return new CompositeSubscription();

        return subscription;
    }
}

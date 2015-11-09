package com.stxnext.intranet2.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.pixplicity.easyprefs.library.Prefs;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.ProjectSpinnerAdapter;
import com.stxnext.intranet2.backend.model.project.Project;
import com.stxnext.intranet2.backend.model.project.ProjectResponse;
import com.stxnext.intranet2.backend.model.time.TimeEntryPost;
import com.stxnext.intranet2.backend.model.time.TimeEntryResponse;
import com.stxnext.intranet2.backend.retrofit.ProjectListService;
import com.stxnext.intranet2.backend.retrofit.TimeEntriesService;
import com.stxnext.intranet2.rest.IntranetRestAdapter;
import com.stxnext.intranet2.sort.ProjectOrdering;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RestAdapter;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.text.TextUtils.isEmpty;

/**
 * Created by bkosarzycki on 02.11.15.
 */

public class AddHoursActivity extends AppCompatActivity {

    @Bind(R.id.projects_spinner) AppCompatSpinner mProjectsSpinner;
    @Bind(R.id.activity_add_hours_send_fab) FloatingActionButton mSendFab;

    @Bind(R.id.activity_add_hours_ticket_id) AppCompatEditText mTicketIdET;
    @Bind(R.id.activity_add_hours_time_value) AppCompatEditText mTimeValueET;
    @Bind(R.id.activity_add_hours_description) AppCompatEditText mDescriptionET;

    @Bind(R.id.activity_add_hours_snackbarPosition) CoordinatorLayout mSnackBarCoordinatorLayoutView;


    private final String SHARED_PREF_ADD_HOURS_SELECTED_PROJECT = "add_hours_selected_project_id";
    private final String SHARED_PREF_ADD_HOURS_ENTERED_DESCRIPTION = "add_hours_entered_description";

    private Observable<CharSequence> mTicketIdChangeObservable;
    private Observable<CharSequence> mTimeValueChangeObservable;
    private Observable<CharSequence> mDescriptionChangeObservable;
    private Subscription mEditTextChangeSubscription = null;

    private final static String TAG = AddHoursActivity.class.getName();
    private ProjectSpinnerAdapter mAdapter;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private RestAdapter restAdapter;
    private ProjectListService mProjectListService;
    private TimeEntriesService mTimeEntriesService;
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
        mProjectListService = restAdapter.create(ProjectListService.class);
        mTimeEntriesService = restAdapter.create(TimeEntriesService.class);

        String timeToAdd = getIntent().getExtras().getString("timeToAdd");
        if (timeToAdd != null && !timeToAdd.isEmpty() && validateFloat(timeToAdd.replace("h", ""),  0.01f, 24.0f))
            mTimeValueET.setText(timeToAdd.replace("h", ""));

        getListOfProjects();
        createEditTextObservables();
    }

    @OnClick(R.id.activity_add_hours_send_fab)
    protected void sendFABClick() {
        mSendFab.setVisibility(View.GONE);
        String projDescr =  mDescriptionET.getText().toString().trim();
        String ticketIdString = mTicketIdET.getText().toString();
        int spinnerSelPos = mProjectsSpinner.getSelectedItemPosition();
        Prefs.putInt(SHARED_PREF_ADD_HOURS_SELECTED_PROJECT, spinnerSelPos);
        Prefs.putString(SHARED_PREF_ADD_HOURS_ENTERED_DESCRIPTION, projDescr);

        TimeEntryPost tep = new TimeEntryPost();
        tep.setProjectId(mAdapter.getProject(spinnerSelPos).getId());
        tep.setDescription(projDescr);
        tep.setTime(Float.parseFloat(mTimeValueET.getText().toString()));
        if (ticketIdString != null && !ticketIdString.isEmpty())
            tep.setTicketId(Integer.parseInt(mTicketIdET.getText().toString()));

        mSubscriptions.add(
                mTimeEntriesService.postUserTime(tep)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        new Observer<TimeEntryResponse>() {
                            @Override public void onCompleted() {
                                Log.d(TAG, "Retrofit post time entry call completed");
                                createSnackbar("Wysyłanie powiodło się");
                            }
                            @Override public void onError(Throwable e) {
                                Log.e(TAG, e.toString());
                                createSnackbar("Błąd wysyłania godzin");
                            }
                            @Override public void onNext(TimeEntryResponse timeEntryResponse) {}
                        }
                )
        );
    }

    @OnClick(R.id.activity_add_hours_load_previous_description)
    protected void loadPrevDescriptionClick() {
        String prevDescr = Prefs.getString(SHARED_PREF_ADD_HOURS_ENTERED_DESCRIPTION, null);
        if (prevDescr != null && !prevDescr.isEmpty())
            mDescriptionET.setText(prevDescr);
        else
            createSnackbar(R.string.add_hours_no_previous_description);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void createEditTextObservables() {
        mTicketIdChangeObservable = RxTextView.textChanges(mTicketIdET).skip(0);
        mTimeValueChangeObservable = RxTextView.textChanges(mTimeValueET).skip(0);
        mDescriptionChangeObservable = RxTextView.textChanges(mDescriptionET).skip(0);

        combineLatestEvents();
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
                                        createSnackbar(R.string.add_hours_connection_error);
                                    }

                                    @Override
                                    public void onNext(ProjectResponse projectResponse) {
                                        List<Project> projectList = projectResponse.getProjects();
                                        projectList = ProjectOrdering.natural().sortedCopy(projectList);
                                        projectList.add(0, new Project().withName("Choose a project..."));
                                        mAdapter.setProjects(projectList);
                                        mProjectsSpinner.setAdapter(mAdapter);
                                        int prevSelectedProjectId = Prefs.getInt(SHARED_PREF_ADD_HOURS_SELECTED_PROJECT, -1);
                                        if (prevSelectedProjectId > 0)
                                            mProjectsSpinner.setSelection(prevSelectedProjectId);
                                        else
                                            mProjectsSpinner.setSelection(0);
                                    }
                                }
                        )
        );
    }

    private void createSnackbar(int textResourceId) {
        final Snackbar snack = Snackbar
                .make(mSnackBarCoordinatorLayoutView, textResourceId , Snackbar.LENGTH_LONG);
        snack.setAction(R.string.add_hours_connection_error_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack.dismiss();
            }
        });
        snack.show();
    }

    private void createSnackbar(String text) {
        final Snackbar snack = Snackbar
                .make(mSnackBarCoordinatorLayoutView, text , Snackbar.LENGTH_LONG);
        snack.setAction(R.string.add_hours_connection_error_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack.dismiss();
            }
        });
        snack.show();
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

    private void combineLatestEvents() {
        mEditTextChangeSubscription = Observable.combineLatest(mTicketIdChangeObservable, mTimeValueChangeObservable, mDescriptionChangeObservable,
                new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence ticketIdCharSeq,
                                        CharSequence timeValueCharSeq,
                                        CharSequence descriptionCharSeq) {

                        boolean ticketIdValid = isEmpty(ticketIdCharSeq)
                                || (!isEmpty(ticketIdCharSeq) && validateInt(ticketIdCharSeq.toString(), 1, 99999) );
                        if (!ticketIdValid)
                            mTicketIdET.setError("Invalid ticket id!");

                        boolean timeValueValid = !isEmpty(timeValueCharSeq) && timeValueCharSeq.length() >= 4 && timeValueCharSeq.length() <= 5
                                                    && validateFloat(timeValueCharSeq.toString(), 0.01f, 24.0f);
                        if (!timeValueValid)
                            mTimeValueET.setError("Invalid time value!");

                        boolean descriptionValid = !isEmpty(descriptionCharSeq) && descriptionCharSeq.length() > 6;
                        if (!descriptionValid)
                            mDescriptionET.setError("Invalid description!");
                        else
                            mDescriptionET.setError(null);

                        return ticketIdValid && timeValueValid && descriptionValid;
                    }
                })
                .subscribe(new Observer<Boolean>() {
                    @Override public void onCompleted() { Log.d(TAG, "Combine latest completed"); }
                    @Override public void onError(Throwable e) { Log.d(TAG, "Combine latest error: " + e.toString()); }

                    @Override public void onNext(Boolean formValid) {
                        if (formValid)
                            mSendFab.setVisibility(View.VISIBLE);
                         else
                            mSendFab.setVisibility(View.GONE);
                    }
                });
    }

    private boolean validateFloat(String s, float rangeBeg, float rangeEnd) {
        double res; try { res = Double.parseDouble(s); } catch (Exception exc) { return false; }
        return res >= rangeBeg && res <= rangeEnd;
    }

    private boolean validateInt(String s, int rangeBeg, int rangeEnd) {
        long res; try { res = Integer.parseInt(s); } catch (Exception exc) { return false; }
        return res >= rangeBeg && res <= rangeEnd;
    }
}

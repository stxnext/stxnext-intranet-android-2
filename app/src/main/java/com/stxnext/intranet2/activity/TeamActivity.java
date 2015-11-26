package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.common.primitives.Longs;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.EmployeesListAdapter;
import com.stxnext.intranet2.backend.api.UserApi;
import com.stxnext.intranet2.backend.api.UserApiImpl;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;
import com.stxnext.intranet2.backend.service.TeamCacheService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Łukasz Ciupa on 25.11.2015.
 */
public class TeamActivity extends AppCompatActivity {

    public static final String TEAM_ID_TAG = "teamId";

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private EmployeesListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TeamCacheService teamCacheService;
    private long teamId;
    private List<Long> userIds;
    private List<User> users = new ArrayList<>();
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        teamId = getIntent().getLongExtra(TEAM_ID_TAG, -1);
        if (teamId != -1) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeResources(
                    R.color.stxnext_green_dark,
                    R.color.stxnext_green,
                    R.color.stxnext_green_darkest);
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            teamCacheService = TeamCacheService.getInstance(this);
            teamCacheService.getTeam(teamId, new TeamCacheService.OnTeamReceivedCallback() {
                @Override
                public void onReceived(Team team) {
                    long[] userIdsLong = team.getUsers();
                    userIds = Longs.asList(userIdsLong);
                    getUsers(userIds);
                }
            });
        }

    }

    /**
     * Gets users and loads recycler view.
     * @param userIds
     */
    private void getUsers(final List<Long> userIds) {
        if (userIds != null && userIds.size() > 0) {
            Long userId = userIds.get(0);
            userApi = new UserApiImpl(this, new UserApiCallback() {
                @Override
                public void onUserReceived(User user) {
                    users.add(user);
                    userIds.remove(Long.parseLong(user.getId()));
                    if (userIds.size() > 0) {
                        getUsers(userIds);
                    } else {
                        initializeEmployessList(users);
                    }
                }

                @Override
                public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {

                }

                @Override
                public void onOutOfOfficeResponse(boolean entry) {

                }

                @Override
                public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {

                }

                @Override
                public void onRequestError() {

                }

                @Override
                public void onTimeReportReceived(List<TimeReportDay> timeReportDays, Calendar month) {

                }
            });

            userApi.requestForUser(userId.toString());

        } else {
            initializeEmployessList(users);
        }


    }

    private void initializeEmployessList(final List<User> users) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TeamCacheService teamCacheService = TeamCacheService.getInstance(TeamActivity.this);
                teamCacheService.getUserToTeamsMap(new TeamCacheService.OnUserToTeamsMapReceivedCallback() {
                    @Override
                    public void onReceived(Map<Long, List<Team>> userToTeamsMap) {
                        adapter = new EmployeesListAdapter(TeamActivity.this, users, userToTeamsMap, new EmployeesListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(String userId) {

                            }
                        });
                        recyclerView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}

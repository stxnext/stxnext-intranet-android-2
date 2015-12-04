package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.TeamsListAdapter;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.backend.service.TeamCacheService;

import java.util.List;

/**
 * Created by Łukasz Ciupa on 30.11.2015.
 */
public class TeamsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TeamsListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.stxnext_green_dark,
                R.color.stxnext_green,
                R.color.stxnext_green_darkest);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (recyclerView.getAdapter() == null) {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
        TeamCacheService teamCacheService = TeamCacheService.getInstance(this);
        teamCacheService.getTeams(new TeamCacheService.OnTeamsReceivedCallback() {
            @Override
            public void onReceived(List<Team> teams) {
                adapter = new TeamsListAdapter(TeamsActivity.this, teams, new TeamsListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Long teamId) {
                        Intent intent = new Intent(TeamsActivity.this, TeamActivity.class);
                        intent.putExtra(TeamActivity.TEAM_ID_TAG, teamId);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
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

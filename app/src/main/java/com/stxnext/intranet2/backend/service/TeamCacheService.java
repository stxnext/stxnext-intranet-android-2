package com.stxnext.intranet2.backend.service;

import android.content.Context;
import android.util.Log;

import com.stxnext.intranet2.backend.api.TeamApi;
import com.stxnext.intranet2.backend.api.TeamApiImpl;
import com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback;
import com.stxnext.intranet2.backend.model.team.Client;
import com.stxnext.intranet2.backend.model.team.Project;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Łukasz Ciupa on 09.11.2015.
 * Class used to manage data for TeamApi. Data is get from api if requested
 * for the first time and then
 * saved to db. Later when data is needed it can be get from db.
 */
public class TeamCacheService {

    Context context;

    /**
     * This is used to cache user to teams data as to not get this every time
     * from db.
     */
    private static Map<Integer, List<String>> userToTeamsMap;

    public TeamCacheService(Context context) {
        this.context = context;
    }

    public void getTeamForUser(int userId) {
        TeamApi teamApi = new TeamApiImpl();
        teamApi.requestForTeams(new OnTeamsReceivedCallback() {
            @Override
            public void onReceived(List<Team> teams) {
                Log.d(Config.getTag(TeamCacheService.this), "First team name: " + teams.get(0).getName());
                Team team = teams.get(0);
                Project[] projects = team.getProjects();
                if (projects != null && projects.length > 0) {
                    Project project = projects[0];
                    Client client = project.getClient();
                    DBManager.getInstance(context).persistClient(client);
                }
            }
        });

    }
}

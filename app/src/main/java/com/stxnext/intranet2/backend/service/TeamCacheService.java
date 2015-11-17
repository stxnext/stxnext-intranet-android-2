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

import java.util.Collection;
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

    public void getTeamForUser(long userId, OnTeamReceivedCallback callback) {
        List<Team> teams = DBManager.getInstance(context).getTeams();
        if (teams != null && teams.size() > 0) {
            for (Team team : teams) {
                Collection<Project> projects = team.getProjects();
    //            Iterator<Project> iterator = projectsForeignCollection.iterator();
                for (Project project: projects) {
                    Log.d(Config.getTag(this), "FromDB: Team: " + team.getName() + ", project: " + project.getName());
                }
            }
        } else {
            TeamApi teamApi = new TeamApiImpl();
            teamApi.requestForTeams(new OnTeamsReceivedCallback() {
                @Override
                public void onReceived(List<Team> teams) {
                    persistTeamsInDB(teams);
                }
            });
        }
    }

    private void persistTeamsInDB(List<Team> teams) {
        for (Team team : teams) {
            Log.d(Config.getTag(TeamCacheService.this), "Team name: " + team.getName());
            DBManager.getInstance(context).persistTeam(team);
            Collection<Project> projects = team.getProjects();
            if (projects != null) {
                for (Project project : projects) {
                    Client client = project.getClient();
                    DBManager.getInstance(context).persistClient(client);
                    project.setTeam(team);
                    DBManager.getInstance(context).persistProject(project);
                    // it's for gc, not to keep back reference anymore
                    project.setTeam(null);
                }
            }
        }
    }

    public interface OnTeamReceivedCallback {

        void onReceived(Team team);

    }
}

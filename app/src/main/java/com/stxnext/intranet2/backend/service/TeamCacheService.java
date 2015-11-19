package com.stxnext.intranet2.backend.service;

import android.content.Context;
import android.util.Log;

import com.stxnext.intranet2.backend.api.TeamApi;
import com.stxnext.intranet2.backend.api.TeamApiImpl;
import com.stxnext.intranet2.backend.model.team.Client;
import com.stxnext.intranet2.backend.model.team.Project;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.backend.model.team.TeamProject;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Łukasz Ciupa on 09.11.2015.
 * Class used to manage data for TeamApi. Data is get from api if requested
 * for the first time and then
 * saved to db. Later when data is needed it can be get from db.
 */
public class TeamCacheService {

    private static TeamCacheService instance = null;

    Context context;

    /**
     * This is used to cache user to teams data as to not get this every time
     * from db.
     */
    private Map<Long, List<Team>> userToTeamsMap = new HashMap<>();

    public static TeamCacheService getInstance(Context context) {
        if (instance == null) {
            instance = new TeamCacheService(context);
        }
        return instance;
    }

    private TeamCacheService(Context context) {
        this.context = context;
    }

    public void getTeamsForUser(final long userId, final OnTeamsReceivedCallback callback) {
        if (userToTeamsMap.size() > 0) {
            List<Team> teamsForUser = userToTeamsMap.get(userId);
            if (teamsForUser == null)
                teamsForUser = new ArrayList<>();
            callback.onReceived(teamsForUser);
            return;
        } else {
            TeamApi teamApi = new TeamApiImpl();
            teamApi.requestForTeams(new com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback() {
                @Override
                public void onReceived(List<Team> teams) {
                    synchronized (TeamCacheService.this) {
                        clearTeamsInDB();
                        persistTeamsInDB(teams);
                        createUserToTeamsMap(teams);
                        List<Team> teamsForUser = userToTeamsMap.get(userId);
                        if (teamsForUser == null)
                            teamsForUser = new ArrayList<>();
                        callback.onReceived(teamsForUser);
                    }
                }
            });
        }
    }

    private void createUserToTeamsMap(List<Team> teams) {
        for (Team team : teams) {
            long[] userIds = team.getUsers();
            for (long userId : userIds) {
                List<Team> teamsForUser = userToTeamsMap.get(userId);
                if (teamsForUser != null) {
                    teamsForUser.add(team);
                }  else {
                    teamsForUser = new ArrayList<>();
                    teamsForUser.add(team);
                    userToTeamsMap.put(userId, teamsForUser);
                }
            }
        }
    }

    /**
     * Finds in teams list Teams to which given user (userId) belongs.
     * @param userId user for whom there is a search performed
     * @param teams teams in which there is the search performed.
     * @return List of teams to which given user belongs.
     *
     */
    private List<Team> findTeamsForUser(long userId, List<Team> teams) {
        List<Team> teamsForUser = new ArrayList<>();
        for (Team team : teams) {
            long[] ids = team.getUsers();
            if (ids != null) {
                for (long id : ids) {
                    if (id == userId) {
                        teamsForUser.add(team);
                        break;
                    }
                }
            }
        };
        return teamsForUser;
    }

    private void persistTeamsInDB(List<Team> teams) {
        for (Team team : teams) {
            Log.d(Config.getTag(TeamCacheService.this), "Team name: " + team.getName());
            DBManager.getInstance(context).persistTeam(team);
            Collection<Project> projects = team.getProjectsGson();
            if (projects != null) {
                for (Project project : projects) {
                    Client client = project.getClient();
                    DBManager.getInstance(context).persistClient(client);
                    DBManager.getInstance(context).persistProject(project);
                    TeamProject teamProject = new TeamProject(team, project);
                    DBManager.getInstance(context).persistTeamProject(teamProject);
                }
            }
        }
    }

    private void clearTeamsInDB() {
        DBManager.getInstance(context).deleteTeams();
    }

    public interface OnTeamsReceivedCallback {

        void onReceived(List<Team> teams);

    }
}

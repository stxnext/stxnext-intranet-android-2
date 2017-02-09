package com.stxnext.intranet2.backend.service;

import android.content.Context;
import android.util.Log;

import com.stxnext.intranet2.backend.api.TeamApi;
import com.stxnext.intranet2.backend.api.TeamApiImpl;
import com.stxnext.intranet2.backend.model.team.Project;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.backend.model.team.TeamProject;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    TeamApi teamApi;
    boolean dataReloaded = false;

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
        teamApi = new TeamApiImpl();
    }

    public void getUserToTeamsMap(final OnUserToTeamsMapReceivedCallback callback) {
        if (userToTeamsMap.size() > 0) {
            callback.onReceived(userToTeamsMap);
        } else {
            teamApi.requestForTeams(new com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback() {
                @Override
                public void onReceived(List<Team> teams) {
                    synchronized (TeamCacheService.this) {
                        reloadTeamsData(teams);
                        callback.onReceived(userToTeamsMap);
                    }
                }
            });
        }
    }

    /**
     * Resets teams in DB and creates
     * @param teams
     */
    private synchronized void reloadTeamsData(List<Team> teams) {
        clearTeamsInDB();
        persistTeamsInDB(teams);
        createUserToTeamsMap(teams);
        dataReloaded = true;
    }

    public void getTeamsForUser(final long userId, final OnTeamsReceivedCallback callback) {
        if (userToTeamsMap.size() > 0) {
            List<Team> teamsForUser = userToTeamsMap.get(userId);
            if (teamsForUser == null)
                teamsForUser = new ArrayList<>();
            callback.onReceived(teamsForUser);
        } else {
            teamApi.requestForTeams(new com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback() {
                @Override
                public void onReceived(List<Team> teams) {
                    reloadTeamsData(teams);
                    List<Team> teamsForUser = userToTeamsMap.get(userId);
                    if (teamsForUser == null)
                        teamsForUser = new ArrayList<>();
                    callback.onReceived(teamsForUser);
                }
            });
        }
    }

    private void createUserToTeamsMap(List<Team> teams) {
        userToTeamsMap.clear();
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
            Collection<Project> projects = team.getProjectsGson();
            if (projects != null) {
                Collection<TeamProject> teamProjects = new ArrayList<>();
                for (Project project : projects) {
                    TeamProject teamProject = new TeamProject(team, project);
                    teamProjects.add(teamProject);
                }
                team.setTeamToProjectLinks(teamProjects);
            }
            DBManager.getInstance(context).getTeamRepository().saveOrUpdateComplex(team);
        }
    }

    public void getTeams(final OnTeamsReceivedCallback callback) {
        if (isTeamCacheFirstRun()) {
            teamApi.requestForTeams(new com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback() {
                @Override
                public void onReceived(List<Team> teams) {
                    reloadTeamsData(teams);
                    sortTeams(teams);
                    callback.onReceived(teams);
                }
            });
        } else {
            List<Team> teams = DBManager.getInstance(context).getTeamRepository().getTeams();
            sortTeams(teams);
            callback.onReceived(teams);
        }
    }

    private void sortTeams(List<Team> teams) {
        Locale polishLocale = new Locale("pl_PL");
        final Collator polishCollator = Collator.getInstance(polishLocale);
        Comparator<Team> comparator = new Comparator<Team>() {
            @Override
            public int compare(Team team1, Team team2) {
                return polishCollator.compare(team1.getName(), team2.getName());
            }
        };
        Collections.sort(teams, comparator);
    }

    public void getTeam(final long teamId, final OnTeamReceivedCallback callback) {
        if (isTeamCacheFirstRun()) {
            teamApi.requestForTeams(new com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback() {
                @Override
                public void onReceived(List<Team> teams) {
                    reloadTeamsData(teams);
                    Team team = DBManager.getInstance(context).getTeamRepository().getTeam(teamId);
                    callback.onReceived(team);
                }
            });
        } else {
            Team team = DBManager.getInstance(context).getTeamRepository().getTeam(teamId);
            callback.onReceived(team);
        }
    }

    private boolean isTeamCacheFirstRun() {
        return !dataReloaded;
    }

    private void clearTeamsInDB() {
        DBManager.getInstance(context).getTeamRepository().deleteAllTeams();
    }

    public interface OnTeamsReceivedCallback {

        void onReceived(List<Team> teams);

    }

    public interface OnTeamReceivedCallback {

        void onReceived(Team team);

    }

    public interface OnUserToTeamsMapReceivedCallback {

        void onReceived(Map<Long, List<Team>> userToTeamsMap);
    }
}

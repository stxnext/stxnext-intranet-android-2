package com.stxnext.intranet2.database.repo;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.stxnext.intranet2.backend.model.team.Client;
import com.stxnext.intranet2.backend.model.team.Project;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.backend.model.team.TeamProject;
import com.stxnext.intranet2.database.DatabaseHelper;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Łukasz Ciupa on 20.11.2015.
 */
public class TeamRepository {

    private Dao<Team, Long> teamDao;
    private Dao<TeamProject, Long> teamProjectDao;
    private Dao<Client, Long> clientDao;
    private Dao<Project, Long> projectDao;

    public TeamRepository(DatabaseHelper databaseHelper) {
        try {
            teamDao = databaseHelper.getTeamDao();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveOrUpdate(Team team) {
        try {
            teamDao.createOrUpdate(team);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method saves team and all of its related objects: projects, clients and
     * relations between them.
     * @param team
     */
    public void saveOrUpdateComplex(Team team) {
        try {
            teamDao.createOrUpdate(team);
            DeleteBuilder<TeamProject, Long> deleteBuilder = teamProjectDao.deleteBuilder();
            deleteBuilder.where().eq(TeamProject.TEAM_ID_FIELD_NAME, team.getId());
            teamProjectDao.delete(deleteBuilder.prepare());
            Collection<TeamProject> teamProjects = team.getTeamToProjectLinks();
            if (teamProjects != null) {
                for (TeamProject teamProject : teamProjects) {
                    Project project = teamProject.getProject();
                    Client client = project.getClient();
                    clientDao.createOrUpdate(client);
                    projectDao.createOrUpdate(project);
                    teamProjectDao.createOrUpdate(teamProject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}


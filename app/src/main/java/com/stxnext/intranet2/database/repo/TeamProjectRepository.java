package com.stxnext.intranet2.database.repo;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.stxnext.intranet2.backend.model.team.TeamProject;
import com.stxnext.intranet2.database.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Łukasz Ciupa on 23.11.2015.
 */
public class TeamProjectRepository {

    private DatabaseHelper dbHelper;
    private Dao<TeamProject, Long> teamProjectDao;

    public TeamProjectRepository(DatabaseHelper databaseHelper) {
        try {
            dbHelper = databaseHelper;
            teamProjectDao = databaseHelper.getTeamProjectDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves in db teamproject (connection between team and project). If there is such a teamproject
     * (with the the same team id and project id) then no action is necessary.
     * @param teamProject
     */
    public void save(TeamProject teamProject) {
        try {
            // Teamproject has additional id (orm lite needs it) so there is need to check if there
            // is such an entry to not to double it in db.
            Dao<TeamProject, Long> teamProjectDao = dbHelper.getTeamProjectDao();
            QueryBuilder<TeamProject, Long> queryBuilder = teamProjectDao.queryBuilder();
            queryBuilder.where().eq(TeamProject.TEAM_ID_FIELD_NAME, teamProject.getTeam().getId()).and().eq(TeamProject.PROJECT_ID_FIELD_NAME, teamProject.getProject().getId());
            List<TeamProject> teamProjects = teamProjectDao.query(queryBuilder.prepare());
            if (!isTeamProjectInDB(teamProjects))
                teamProjectDao.createOrUpdate(teamProject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isTeamProjectInDB(List<TeamProject> teamProjects) {
        return teamProjects != null && teamProjects.size() > 0;
    }
}

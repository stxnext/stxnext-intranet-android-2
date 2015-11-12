package com.stxnext.intranet2.utils;

import android.content.Context;

import com.google.common.base.Optional;
import com.j256.ormlite.dao.Dao;
import com.stxnext.intranet2.backend.api.EmployeesCommonApi;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.team.Client;
import com.stxnext.intranet2.backend.model.team.Project;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.database.DatabaseHelper;
import com.stxnext.intranet2.database.repo.UserRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Lukasz Ciupa on 2015-05-28.
 */
public class DBManager {

    private static DBManager instance = null;
    private static boolean isLoaded;
    private static UserRepository userRepository;
    private DatabaseHelper dbHelper;

    private DBManager() {}

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager();
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            userRepository = new UserRepository(dbHelper);

            //if isLoaded is false on app start - contacts are refreshed
            if (!isLoaded)
                EmployeesCommonApi.downloadUsers(context, Optional.<EmployeesApiCallback>absent());
        }
        return instance;
    }

    public void persistEmployees(List<User> employees) {
        if (!isLoaded) {
            for (User user : employees)
                if (!user.getId().isEmpty() && userRepository.getUser(Integer.parseInt(user.getId())) == null)
                    userRepository.saveOrUpdateUser(user);
            isLoaded = true;
        }
    }

    public List<User> getEmployees() {
        return userRepository.getUsers();
    }

    public User getUser(String userId) {
        return userRepository.getUser(Integer.parseInt(userId));
    }

    public Client getClient(long clientId) {
        try {
            Dao<Client, Long> clientDao = dbHelper.getClientDao();
            return clientDao.queryForId(clientId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void persistClient(Client client) {
        try {
            Dao<Client, Long> clientDao = dbHelper.getClientDao();
            clientDao.createOrUpdate(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void persistProject(Project project) {
        try {
            Dao<Project, Long> projectDao = dbHelper.getProjectDao();
            projectDao.createOrUpdate(project);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void persistTeam(Project project) {
        try {
            Dao<Team, Long> projectDao = dbHelper.getTeamDao();
            projectDao.createOrUpdate(projectDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //todo: this is never used, remove?
    public boolean isEmployeesLoaded() {
        return isLoaded;
    }
}

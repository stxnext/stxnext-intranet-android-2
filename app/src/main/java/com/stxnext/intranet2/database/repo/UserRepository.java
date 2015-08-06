package com.stxnext.intranet2.database.repo;

import com.j256.ormlite.dao.Dao;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.UserImpl;
import com.stxnext.intranet2.database.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by bkosarzycki on 04.08.15.
 */
public class UserRepository {

    private Dao<UserImpl, Integer> userDao;

    public UserRepository(final DatabaseHelper databaseHelper) {
        try {
            userDao = databaseHelper.getUserDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveOrUpdateUser(final UserImpl person) {
        try {
            userDao.createOrUpdate(person);
        }
        catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserImpl> getUsers() {
        try {
            return userDao.queryForAll();
        }
        catch (final SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public UserImpl getUser(int userId) {
        try {
            return userDao.queryForId(userId);
        }
        catch (final SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

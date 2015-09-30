package com.stxnext.intranet2.database.repo;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.database.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by bkosarzycki on 04.08.15.
 */
public class UserRepository {

    private static String TAG = "UserRepository";
    private Dao<User, Integer> userDao;

    public UserRepository(final DatabaseHelper databaseHelper) {
        try {
            userDao = databaseHelper.getUserDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveOrUpdateUser(final User person) {
        try {
            userDao.createOrUpdate(person);
        }
        catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        try {
            return userDao.queryForAll();
        }
        catch (final SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getUser(int userId) {
        try {
            return userDao.queryForId(userId);
        }
        catch (final SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return null;
    }
}

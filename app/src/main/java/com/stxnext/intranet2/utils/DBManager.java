package com.stxnext.intranet2.utils;

import android.app.Application;
import android.content.Context;

import com.stxnext.intranet2.activity.EmployeesActivity;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.UserImpl;
import com.stxnext.intranet2.database.DatabaseHelper;
import com.stxnext.intranet2.database.repo.UserRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lukasz Ciupa on 2015-05-28.
 */
public class DBManager {

    private static DBManager instance = null;
    private static boolean isLoaded;
    private static UserRepository userRepository;

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager();
            userRepository = new UserRepository(new DatabaseHelper(context));
        }
        return instance;
    }

    public void persistEmployees(List<UserImpl> employees) {
        if (!isLoaded) {
            for (UserImpl user : employees)
                if (!user.getId().isEmpty() && userRepository.getUser(Integer.parseInt(user.getId())) == null)
                    userRepository.saveOrUpdateUser(user);
            isLoaded = true;
        }
    }

    public List<UserImpl> getEmployees() {
        return userRepository.getUsers();
    }

    public User getUser(String userId) {
        return userRepository.getUser(Integer.parseInt(userId));
    }

    //todo: this is never used, remove?
    public boolean isEmployeesLoaded() {
        return isLoaded;
    }
}

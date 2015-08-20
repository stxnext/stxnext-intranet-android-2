package com.stxnext.intranet2.utils;

import android.content.Context;

import com.google.common.base.Optional;
import com.stxnext.intranet2.backend.api.EmployeesCommonApi;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.database.DatabaseHelper;
import com.stxnext.intranet2.database.repo.UserRepository;

import java.util.List;

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

            //isLoaded is false on app start - contacts are refreshed
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

    //todo: this is never used, remove?
    public boolean isEmployeesLoaded() {
        return isLoaded;
    }
}

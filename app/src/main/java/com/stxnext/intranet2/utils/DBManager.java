package com.stxnext.intranet2.utils;

import com.stxnext.intranet2.backend.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lukasz Ciupa on 2015-05-28.
 */
public class DBManager {

    private static DBManager instance = null;

    Map<String, User> employeesMap = new LinkedHashMap();

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private DBManager() {

    }

    public void persistEmployees(List<User> employees) {
        employeesMap.clear();
        for (User user : employees) {
            employeesMap.put(user.getId(), user);
        }
    }

    public List<User> getEmployees() {
        return new ArrayList(employeesMap.values());
    }

    public User getUser(String userId) {
        return employeesMap.get(userId);
    }

    public boolean isEmployeesLoaded() {
        return (employeesMap.size() > 0);
    }
}

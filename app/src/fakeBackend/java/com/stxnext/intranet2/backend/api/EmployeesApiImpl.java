package com.stxnext.intranet2.backend.api;

import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.UserImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class EmployeesApiImpl extends EmployeesApi {

    public EmployeesApiImpl(EmployeesApiCallback callback) {
        super(callback);
    }

    @Override
    public void requestForEmployees() {
        List<User> list = new ArrayList<>();
        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));

        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));

        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));

        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));

        apiCallback.onEmployeesListReceived(list);
    }
}

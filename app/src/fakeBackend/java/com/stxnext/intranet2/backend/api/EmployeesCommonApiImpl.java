package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.impl.UserRestWrapper;
import com.stxnext.intranet2.utils.DBManager;

import java.lang.reflect.Type;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by bkosarzycki on 23.11.15.
 */
public class EmployeesCommonApiImpl implements EmployeesCommonApi {

    protected final EmployeesApiCallback apiCallback;
    protected Context context;

    public EmployeesCommonApiImpl(Context context, EmployeesApiCallback callback) {
        this.apiCallback = callback;
        this.context = context;
    }

    public void downloadUser(final Context context, final Optional<UserApiCallback> apiCallback, final String userId) {
        User user = new User("1", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", "");
        if (apiCallback != null && apiCallback.isPresent())
            apiCallback.get().onUserReceived(user);
    }

    public void downloadUsers(final Context context, final Optional<EmployeesApiCallback> apiCallback) {
        downloadUsersWorker(context,
                apiCallback != null && apiCallback.isPresent() ? apiCallback.get() : null);
    }

    /**
     *
     * @param apiCallback  EmployeesApiCallback or UserApiCallback
     */
    private void downloadUsersWorker(final Context context, final EmployeesApiCallback apiCallback) {


        List<User> list = new ArrayList<>();
        list.add(new User("0", "John", "Smith", "john.smith", "+48 921 231 212",
                "Poznań", Lists.newArrayList("Programmer"), "john.smith@stxnext.pl", "johny", "Mobile Team", null));
        list.add(new User("1", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("2", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""));
        list.add(new User("3", "Mario", "Step", "", "", "", Lists.newArrayList("Chemist"), "", "", "", ""));
        list.add(new User("4", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""));
        list.add(new User("5", "Bert", "Lawnmower", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("6", "David", "Studentzky", "", "", "", Lists.newArrayList("Senior iOS Developer"), "", "", "", ""));
        list.add(new User("7", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("8", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""));

        list.add(new User("9", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("10", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""));
        list.add(new User("11", "Mario", "Step", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("12", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""));
        list.add(new User("13", "Bert", "Lawnmower", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("14", "David", "Studentzky", "", "", "", Lists.newArrayList("Senior iOS Developer"), "", "", "", ""));
        list.add(new User("15", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("16", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""));

        list.add(new User("17", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("18", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""));
        list.add(new User("19", "Mario", "Step", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("20", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""));
        list.add(new User("21", "Bert", "Lawnmower", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("22", "David", "Studentzky", "", "", "", Lists.newArrayList("Senior iOS Developer"), "", "", "", ""));
        list.add(new User("23", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("24", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""));

        list.add(new User("25", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("26", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""));
        list.add(new User("27", "Mario", "Step", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("28", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""));
        list.add(new User("29", "Bert", "Lawnmower", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("30", "David", "Studentzky", "", "", "", Lists.newArrayList("Senior iOS Developer"), "", "", "", ""));
        list.add(new User("31", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("33", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""));

                CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList(list);
                sortUsersByFirstName(Lists.newArrayList(users)); //concurrent.CopyOnWriteArrayList$CowIterator doesn't support set(Object o) operation which replaces the current object in the array
                DBManager.getInstance(context).persistEmployees(users);

                if (apiCallback != null)
                    apiCallback.onEmployeesListReceived(users);


    }

    /**
     * CopyOnWriteArrayList is used to enable concurrent modification of the list that the iterator in a for-loop is operating on,
     * in particular the 'remove' of the current element operation.
     *
     * @param users
     */
    private void removeClients(CopyOnWriteArrayList<User> users) {
        for (User u : users)
            if (u.isClient())
                users.remove(u);
    }

    protected List<User> processJsonEmployees(String jsonEmployeesString) {
        List<User> users = new ArrayList<>();
        try {
            Type userListType = new TypeToken<UserRestWrapper>() {}.getType();
            users = ((UserRestWrapper) new Gson().fromJson(jsonEmployeesString, userListType)).users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    protected void sortUsersByFirstName(List<User> users) {
        Locale polishLocale = new Locale("pl_PL");
        final Collator polishCollator = Collator.getInstance(polishLocale);

        Comparator<User> comparator = new Comparator<User>() {

            @Override
            public int compare(User user1, User user2) {
                return polishCollator.compare(user1.getFirstName(), user2.getFirstName());
            }
        };
        Collections.sort(users, comparator);
    }
}

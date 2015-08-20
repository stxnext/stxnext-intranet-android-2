package com.stxnext.intranet2.backend.api;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.impl.UserRestWrapper;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;
import com.stxnext.intranet2.utils.Session;

import org.apache.http.Header;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by bkosarzycki on 06.08.15.
 */
public abstract class EmployeesCommonApi {

    public static void downloadUser(final Context context, final Optional<UserApiCallback> apiCallback, final String userId) {
        EmployeesApiCallback employeesApiCallback = new EmployeesApiCallback() {
            @Override public void onEmployeesListReceived(List<User> employees) {
                User user = DBManager.getInstance(context).getUser(userId);
                if (apiCallback != null && apiCallback.isPresent())
                    apiCallback.get().onUserReceived(user);
            }

            @Override public void onAbsenceEmployeesListReceived(LinkedHashSet<Absence> absenceEmployees) {}
        };

        downloadUsersWorker(context, employeesApiCallback);
    }

    public static void downloadUsers(final Context context, final Optional<EmployeesApiCallback> apiCallback) {
        downloadUsersWorker(context,
                apiCallback != null && apiCallback.isPresent() ? apiCallback.get() : null);
    }

    /**
     *
     * @param apiCallback  EmployeesApiCallback or UserApiCallback
     */
    private static void downloadUsersWorker(final Context context, final EmployeesApiCallback apiCallback) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(Session.getInstance(context).getCookieStore());

        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList(processJsonEmployees(response));
                removeClients(users);
                sortUsersByFirstName(Lists.newArrayList(users)); //concurrent.CopyOnWriteArrayList$CowIterator doesn't support set(Object o) operation which replaces the current object in the array
                DBManager.getInstance(context).persistEmployees(users);

                if (apiCallback != null)
                        apiCallback.onEmployeesListReceived(users);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        };

        httpClient.get("https://intranet.stxnext.pl/api/users?full=1&inactive=0", asyncHttpResponseHandler);
    }

    /**
     * CopyOnWriteArrayList is used to enable concurrent modification of the list that the iterator in a for-loop is operating on,
     * in particular the 'remove' of the current element operation.
     *
     * @param users
     */
    private static void removeClients(CopyOnWriteArrayList<User> users) {
        for (User u : users)
            if (u.isClient())
                users.remove(u);
    }

    protected static List<User> processJsonEmployees(String jsonEmployeesString) {
        List<User> users = new ArrayList<>();
        try {
            Type userListType = new TypeToken<UserRestWrapper>() {}.getType();
            users = ((UserRestWrapper) new Gson().fromJson(jsonEmployeesString, userListType)).users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    protected static void sortUsersByFirstName(List<User> users) {
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

package com.stxnext.intranet2.backend.api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;
import com.stxnext.intranet2.utils.Session;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by bkosarzycki on 06.08.15.
 */
public abstract class EmployeesCommonApi {


    protected void downlUsersFromHTTP(final Context context, final Object apiCallback, final String userId) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(Session.getInstance(context).getCookieStore());

        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                List<User> users = processJsonEmployees(response);
                sortUsersByFirstName(users);
                DBManager.getInstance(context).persistEmployees(users);

                if (apiCallback instanceof EmployeesApiCallback)
                    ((EmployeesApiCallback)apiCallback).onEmployeesListReceived(users);
                else {
                    User user = DBManager.getInstance(context).getUser(userId);
                    ((UserApiCallback) apiCallback).onUserReceived(user);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        };

        httpClient.get("https://intranet.stxnext.pl/api/users?full=1&inactive=0", asyncHttpResponseHandler);
    }

    protected List<User> processJsonEmployees(String jsonEmployeesString) {
        List<User> users = new ArrayList<>();
        try {
            JSONObject mainObject = new JSONObject(jsonEmployeesString);
            JSONArray usersJSONArray = mainObject.getJSONArray("users");
            for (int i = 0; i < usersJSONArray.length(); ++i) {
                JSONObject userJSONObject = usersJSONArray.getJSONObject(i);
                if (isEmployee(userJSONObject)) {
                    User user = parseUser(userJSONObject);
                    users.add(user);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }

    private boolean isEmployee(JSONObject userJSONObject) throws JSONException {
        return !userJSONObject.getBoolean("is_client");
    }

    private User parseUser(JSONObject userJSONObject) throws JSONException {
        int id = userJSONObject.getInt("id");
        String name = userJSONObject.getString("name");
        String[] nameSplitted = name.split(" ");
        String firstName;
        String lastName = "";
        if (nameSplitted.length == 2) {
            firstName = nameSplitted[0];
            lastName = nameSplitted[1];
        } else {
            firstName = name;
        }
        String skype = userJSONObject.getString("skype");
        String phone = userJSONObject.getString("phone");
        String city = "";
        JSONArray locationJSONArray = userJSONObject.getJSONArray("location");
        if (locationJSONArray.length() >= 2) {
            city = locationJSONArray.getString(1);
        }
        String role = "";
        JSONArray rolesJSONArray = userJSONObject.getJSONArray("roles");
        if (rolesJSONArray.length() > 0) {
            role = rolesJSONArray.getString(0);
            role = role.substring(0, 1).toUpperCase() + role.substring(1, role.length()).toLowerCase();
        }

        String email = userJSONObject.getString("email");
        String irc = userJSONObject.getString("irc");
        String avatarUrl = userJSONObject.getString("avatar_url");
        User user = new User(String.valueOf(id), firstName, lastName, skype, phone,
                city, role, email, irc, "Team Mobilny", avatarUrl);
        return user;
    }

    private void sortUsersByFirstName(List<User> users) {
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

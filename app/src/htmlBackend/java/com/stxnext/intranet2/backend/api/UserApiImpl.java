package com.stxnext.intranet2.backend.api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.UserImpl;
import com.stxnext.intranet2.model.HolidayTypes;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;
import com.stxnext.intranet2.utils.Session;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class UserApiImpl extends UserApi {

    public UserApiImpl(Context context, UserApiCallback callback) {
        super(context, callback);
    }

    @Override
    public void requestForUser(String userId) {
        if (userId != null) {
            User userFromDB = DBManager.getInstance().getUser(userId);
            if (userFromDB == null) {
                getUser(userId);
            } else {
                apiCallback.onUserReceived(userFromDB);
            }
        } else {
            User user = new UserImpl(null, "Marian", "Kowalski", "mariano.kowalsky", "+48 600 211 321",
                    "Poznań", "Programista", "marian.kowalski@stxnext.pl", "marianno", "Team Mobilny", null);
            apiCallback.onUserReceived(user);
        }
    }

    private void getUser(final String userId) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(Session.getInstance(context).getCookieStore());
        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                List<User> users = processJsonEmployees(response);
                DBManager.getInstance().persistEmployees(users);
                User user = DBManager.getInstance().getUser(userId);
                apiCallback.onUserReceived(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        httpClient.get("https://intranet.stxnext.pl/api/users?full=1&inactive=0", asyncHttpResponseHandler);
    }

    private List<User> processJsonEmployees(String jsonEmployeesString) {
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
        User user = new UserImpl(String.valueOf(id), firstName, lastName, skype, phone,
                city, role, email, irc, "Team Mobilny", avatarUrl);
        return user;
    }

    private boolean isEmployee(JSONObject userJSONObject) throws JSONException {
        return !userJSONObject.getBoolean("is_client");
    }

    //TODO
    //Need json:
    // {"lateness":{"late_end":"09:05","popup_explanation":"Test aplikacji.","work_from_home":"false","late_start":"09:00","popup_date":"31/05/2015"}}
    @Override
    public void submitOutOfOfficeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation) {
    }
    //TODO
    // {"lateness":{"late_end":"09:05","popup_explanation":"Test aplikacji.","work_from_home":"true","late_start":"09:00","popup_date":"31/05/2015"}}
    @Override
    public void submitWorkFromHomeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation) {

    }
    //TODO
    // JSON to gain:
    // {"absence":{"popup_type":"planowany","popup_date_end":"05/06/2015","popup_remarks":"Wolne.","popup_date_start":"05/06/2015"}}
    @Override
    public void submitHolidayAbsence(HolidayTypes absenceType, Date endDate, Date startDate, String remarks) {
        JSONObject mainObject = new JSONObject();
        JSONObject absenceObject = new JSONObject();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            absenceObject.put("popup_type", absenceType.getAbsenceName())
                    .put("popup_date_end", dateFormat.format(endDate))
                    .put("popup_remarks", remarks)
                    .put("popup_date_start", dateFormat.format(startDate));
            mainObject.put("absence", absenceObject);
            Log.d(Config.TAG, mainObject.toString());
            Log.d(Config.TAG, dateFormat.format(endDate));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

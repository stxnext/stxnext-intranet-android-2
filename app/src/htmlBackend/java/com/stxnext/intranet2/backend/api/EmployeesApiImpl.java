package com.stxnext.intranet2.backend.api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.AbsenceImpl;
import com.stxnext.intranet2.backend.model.impl.UserImpl;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;
import com.stxnext.intranet2.utils.Session;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Lukasz Ciupa on 2015-05-22.
 */
public class EmployeesApiImpl extends EmployeesApi {

    public EmployeesApiImpl(Context context, EmployeesApiCallback callback) {
        super(context, callback);
    }

    @Override
    public void requestForEmployees() {
        List<User> employees = DBManager.getInstance().getEmployees();
        if (employees == null) {
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setCookieStore(Session.getInstance(context).getCookieStore());

            AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d(Config.TAG, response);
                    List<User> users = processJsonEmployees(response);
                    DBManager.getInstance().persistEmployees(users);
                    apiCallback.onEmployeesListReceived(users);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            };

            httpClient.get("https://intranet.stxnext.pl/api/users?full=1&inactive=0", asyncHttpResponseHandler);
        } else {
            apiCallback.onEmployeesListReceived(employees);
        }
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
        if (nameSplitted != null && nameSplitted.length == 2) {
            firstName = nameSplitted[0];
            lastName = nameSplitted[1];
        } else {
            firstName = name;
        }
        String role = "";
        JSONArray rolesJSONArray = userJSONObject.getJSONArray("roles");
        String avatarUrl = userJSONObject.getString("avatar_url");
        if (rolesJSONArray.length() > 0) role = rolesJSONArray.getString(0);
        User user = new UserImpl(String.valueOf(id), firstName, lastName, "", "", "", role, "", "", "", avatarUrl);
        return user;
    }

    private boolean isEmployee(JSONObject userJSONObject) throws JSONException {
        return !userJSONObject.getBoolean("is_client");
    }

    @Override
    public void requestForOutOfOfficeAbsenceEmpolyees() {

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(Session.getInstance(context).getCookieStore());

        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                List<Absence> absences = processJsonOutOfOfficeAbsences(response);
                apiCallback.onAbsenceEmployeesListReceived(absences);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        httpClient.get("https://intranet.stxnext.pl/api/presence", asyncHttpResponseHandler);
    }

    private List<Absence> processJsonOutOfOfficeAbsences(String jsonAbsencesString) {
        List<Absence> absences = new ArrayList<>();
        try {
            JSONObject absencesMainObject = new JSONObject(jsonAbsencesString);
            JSONArray latesTommorrowJSONArray = absencesMainObject.getJSONArray("lates_tomorrow");
            List<JSONObject> latesTommorowOutOfOffice = filterOutOfOfficeAbsences(latesTommorrowJSONArray);
            Calendar absenceCalendar = Calendar.getInstance();
            absenceCalendar.add(Calendar.DATE, 1);
            Date tommorowDate = absenceCalendar.getTime();
            parseAbsencesWithDay(latesTommorowOutOfOffice, absences, tommorowDate);

            JSONArray latesJSONArray = absencesMainObject.getJSONArray("lates");
            List<JSONObject> latesOutOfOffice = filterOutOfOfficeAbsences(latesJSONArray);
            absenceCalendar = Calendar.getInstance();
            Date todayDate = absenceCalendar.getTime();
            parseAbsencesWithDay(latesOutOfOffice, absences, todayDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return absences;
    }

    private List<JSONObject> filterOutOfOfficeAbsences(JSONArray latesJSONArray) throws JSONException {
        List<JSONObject> outOfOfficeAbsences = new ArrayList<>();
        for (int i = 0; i < latesJSONArray.length(); ++i) {
            JSONObject lateAbsenceJSONObject = latesJSONArray.getJSONObject(i);
            if (!isWorkFromHome(lateAbsenceJSONObject)) {
                outOfOfficeAbsences.add(lateAbsenceJSONObject);
            }
        }
        return outOfOfficeAbsences;
    }

    /**
     * Parses JSONObjects with defined day (hour will be get from json). It adds parsed elements to
     * absences list.
     * @param absencesJSONObjects Objects to parse.
     * @param absences List where parsed absences will be added.
     * @param day Day of absence.
     */
    private void parseAbsencesWithDay(List<JSONObject> absencesJSONObjects, final List<Absence> absences, Date day) throws JSONException {
        for (JSONObject absenceJSONObject : absencesJSONObjects) {
            final Absence absence = parsePartlyAbsence(absenceJSONObject, day);
            User user = absence.getUser();
            String userId = user.getId();
            User dbUser = DBManager.getInstance().getUser(userId);
            if (dbUser == null) {
                UserApi userApi = new UserApiImpl(context, new UserApiCallback() {

                    @Override
                    public void onUserReceived(User user) {
                        absence.setUser(user);
                        absences.add(absence);
                    }
                });
                userApi.requestForUser(userId);
            } else {
                absence.setUser(dbUser);
                absences.add(absence);
            }

        }
    }

    /**
     * Parses absence, but does not fill user data (despite of userId) - to impelement in later
     * step.
     * @param absenceJSONObject
     * @param day
     * @return
     * @throws JSONException
     */
    private Absence parsePartlyAbsence(JSONObject absenceJSONObject, Date day) throws JSONException {
        String explanation = absenceJSONObject.getString("explanation");
        String start = absenceJSONObject.getString("start");
        Date absenceFrom = addTimeToDay(day, start);
        String end = absenceJSONObject.getString("end");
        Date absenceTo = addTimeToDay(day, end);
        String userId = absenceJSONObject.getString("id");
        // TODO get real user info
//        User user = DBManager.getInstance().getUser(userId);
        Absence absenceWithNoUser = new AbsenceImpl(new UserImpl(userId, "", "", "", "", "", "", "", "", "", ""), absenceFrom, absenceTo, explanation);
        return absenceWithNoUser;
    }

    private Date addTimeToDay(Date day, String hourMinuteTime) {
        String[] timeSplitted = hourMinuteTime.split(":");
        int hour = 0;
        int minute = 0;
        if (timeSplitted != null && timeSplitted.length == 2) {
            String hourString = timeSplitted[0];
            hour = Integer.valueOf(hourString);
            String minuteString = timeSplitted[1];
            minute = Integer.valueOf(minuteString);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    private boolean isWorkFromHome(JSONObject lateAbsenceJSONObject) throws JSONException {
        return lateAbsenceJSONObject.getBoolean("work_from_home");
    }

    @Override
    public void requestForWorkFromHomeAbsenceEmpolyees() {

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(Session.getInstance(context).getCookieStore());

        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                List<Absence> absences = processJsonWorkFromHomeAbsences(response);
                apiCallback.onAbsenceEmployeesListReceived(absences);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        httpClient.get("https://intranet.stxnext.pl/api/presence", asyncHttpResponseHandler);
//
//        List<Absence> absenceList = new ArrayList<>();
//        Date absenceFrom = Calendar.getInstance().getTime();
//        Calendar absenceToCalendar = Calendar.getInstance();
//        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
//        Date absenceTo = absenceToCalendar.getTime();
//        absenceList.add(new AbsenceImpl(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Chemist", "", "", "", ""), absenceFrom, absenceTo, "Źle się czuje, pracuję z domu"));
//        absenceList.add(new AbsenceImpl(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""), absenceFrom, absenceTo, "Córka chora, musiałem zostać w domu"));
//        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Chory, dostępny po tel."));
//        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo, "Jeżdżę po urzędach"));
//        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }

    private List<Absence> processJsonWorkFromHomeAbsences(String jsonAbsencesString) {
        List<Absence> absences = new ArrayList<>();
        try {
            JSONObject absencesMainObject = new JSONObject(jsonAbsencesString);
            JSONArray latesTommorowJSONArray = absencesMainObject.getJSONArray("lates_tomorrow");
            List<JSONObject> latesTommorowWorkFromHome = filterWorkFromHomeAbsences(latesTommorowJSONArray);
            Calendar absenceCalendar = Calendar.getInstance();
            absenceCalendar.add(Calendar.DATE, 1);
            Date tommorowDate = absenceCalendar.getTime();
            parseAbsencesWithDay(latesTommorowWorkFromHome, absences, tommorowDate);

            JSONArray latesJSONArray = absencesMainObject.getJSONArray("lates");
            List<JSONObject> latesWorkFromHome = filterWorkFromHomeAbsences(latesJSONArray);
            absenceCalendar = Calendar.getInstance();
            Date todayDate = absenceCalendar.getTime();
            parseAbsencesWithDay(latesWorkFromHome, absences, todayDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return absences;
    }

    private List<JSONObject> filterWorkFromHomeAbsences(JSONArray latesJSONArray) throws JSONException {
        List<JSONObject> workFromHomeAbsences = new ArrayList<>();
        for (int i = 0; i < latesJSONArray.length(); ++i) {
            JSONObject lateAbsenceJSONObject = latesJSONArray.getJSONObject(i);
            if (isWorkFromHome(lateAbsenceJSONObject)) {
                workFromHomeAbsences.add(lateAbsenceJSONObject);
            }
        }
        return workFromHomeAbsences;
    }

    @Override
    public void requestForHolidayAbsenceEmpolyees() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(Session.getInstance(context).getCookieStore());

        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                List<Absence> absences = processJsonHolidayAbsences(response);
                apiCallback.onAbsenceEmployeesListReceived(absences);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        httpClient.get("https://intranet.stxnext.pl/api/presence", asyncHttpResponseHandler);
    }

    private List<Absence> processJsonHolidayAbsences(String jsonAbsenceString) {
        List<Absence> absences = new ArrayList<>();
        try {
            List<JSONObject> holidayAbsencesJSONObjects = getHolidayAbsences(jsonAbsenceString);
            parseHolidayAbsences(holidayAbsencesJSONObjects, absences);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return absences;
    }

    private List<JSONObject> getHolidayAbsences(String jsonAbsenceString) throws JSONException {
        List<JSONObject> absencesJSONObjects = new ArrayList<>();
        JSONObject absencesMainObject = new JSONObject(jsonAbsenceString);
        JSONArray absencesJSONArray = absencesMainObject.getJSONArray("absences");
        extractJSONObjectsFromJSONArray(absencesJSONObjects, absencesJSONArray);
        absencesJSONArray = absencesMainObject.getJSONArray("absences_tomorrow");
        extractJSONObjectsFromJSONArray(absencesJSONObjects, absencesJSONArray);
        return absencesJSONObjects;
    }

    private void parseHolidayAbsences(List<JSONObject> absencesJSONObjects, final List<Absence> absences) throws JSONException {

        for (JSONObject absenceJSONObject : absencesJSONObjects) {
            final Absence absence = parsePartlyHolidayAbsence(absenceJSONObject);
            User user = absence.getUser();
            String userId = user.getId();
            User dbUser = DBManager.getInstance().getUser(userId);
            if (dbUser == null) {
                UserApi userApi = new UserApiImpl(context, new UserApiCallback() {

                    @Override
                    public void onUserReceived(User user) {
                        absence.setUser(user);
                        absences.add(absence);
                    }
                });
                userApi.requestForUser(userId);
            } else {
                absence.setUser(dbUser);
                absences.add(absence);
            }

        }
    }

    private Absence parsePartlyHolidayAbsence(JSONObject absenceJSONObject) throws JSONException {
        String explanation = absenceJSONObject.getString("remarks");
        String start = absenceJSONObject.getString("start");
        String end = absenceJSONObject.getString("end");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yy");
        Date absenceFrom = null;
        Date absenceTo = null;
        try {
            absenceFrom = dateFormatter.parse(start);
            absenceTo = dateFormatter.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String userId = absenceJSONObject.getString("id");
        Absence absenceWithNoUser = new AbsenceImpl(new UserImpl(userId, "", "", "", "", "", "", "", "", "", ""), absenceFrom, absenceTo, explanation);
        return absenceWithNoUser;
    }

    private void extractJSONObjectsFromJSONArray(List<JSONObject> absencesJSONObjects, JSONArray absencesJSONArray) throws JSONException {
        for (int i = 0; i < absencesJSONArray.length(); ++i) {
            absencesJSONObjects.add(absencesJSONArray.getJSONObject(i));
        }
    }
}

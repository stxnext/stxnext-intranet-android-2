package com.stxnext.intranet2.backend.api;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.impl.AbsenceImpl;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lukasz Ciupa on 2015-05-22.
 */
public class EmployeesApiImpl extends EmployeesApi {

    private Context context;

    public EmployeesApiImpl(Context context, EmployeesApiCallback callback) {
        super(context, callback);
        this.context = context;
    }

    @Override
    public void requestForEmployees(boolean forceRequest) {
        List<User> employees = DBManager.getInstance(context).getEmployees();
        if (employees == null || forceRequest)
            downloadUsers(context, Optional.of(apiCallback));
        else {
            sortUsersByFirstName(employees);
            apiCallback.onEmployeesListReceived(employees);
        }
    }

    @Override
    public void requestForOutOfOfficeAbsenceEmployees() {

        Callback okHttpCallback = new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(Config.getTag(this), "requestForOutOfOfficeAbsenceEmployees failure.");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.d(Config.TAG, responseString);
                List<Absence> absences = processJsonOutOfOfficeAbsences(responseString);
                sortAbsencesByUserFirstName(absences);
                apiCallback.onAbsenceEmployeesListReceived(new LinkedHashSet<Absence>(absences));
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://intranet.stxnext.pl/api/presence")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(okHttpCallback);
    }

    private void sortAbsencesByUserFirstName(List<Absence> absences) {
        Locale polishLocale = new Locale("pl_PL");
        final Collator polishCollator = Collator.getInstance(polishLocale);
        Comparator<Absence> comparator = new Comparator<Absence>() {
            @Override
            public int compare(Absence absence1, Absence absence2) {
                return polishCollator.compare(absence1.getUser().getFirstName(), absence2.getUser().getFirstName());
            }
        };
        Collections.sort(absences, comparator);
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
            User dbUser = DBManager.getInstance(context).getUser(userId);
            if (dbUser == null) {
                UserApi userApi = new UserApiImpl(context, new UserApiCallback() {

                    @Override
                    public void onUserReceived(User user) {
                        absence.setUser(user);
                        absences.add(absence);
                    }

                    @Override
                    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {

                    }

                    @Override
                    public void onOutOfOfficeResponse(boolean entry) {

                    }

                    @Override
                    public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {

                    }

                    @Override
                    public void onTimeReportReceived(List<TimeReportDay> timeReportDays, Calendar month) {

                    }

                    @Override
                    public void onRequestError() {

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
        Absence absenceWithNoUser = new AbsenceImpl(new User(userId, "", "", "", "", "", Lists.newArrayList(""), "", "", "", ""), absenceFrom, absenceTo, explanation);
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

        Callback okHttpCallback = new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(Config.getTag(this), "requestForWorkFromHomeAbsenceEmpolyees() failure.");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.d(Config.TAG, responseString);
                List<Absence> absences = processJsonWorkFromHomeAbsences(responseString);
                sortAbsencesByUserFirstName(absences);
                apiCallback.onAbsenceEmployeesListReceived(new LinkedHashSet<Absence>(absences));
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://intranet.stxnext.pl/api/presence")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(okHttpCallback);
    }

    private List<Absence> processJsonWorkFromHomeAbsences(String jsonAbsencesString) {
        List<Absence> absences = new ArrayList<>();
        try {
            JSONObject absencesMainObject = new JSONObject(jsonAbsencesString);
            JSONArray latestTomorrowJSONArray = absencesMainObject.getJSONArray("lates_tomorrow");
            List<JSONObject> latestTomorrowWorkFromHome = filterWorkFromHomeAbsences(latestTomorrowJSONArray);
            Calendar absenceCalendar = Calendar.getInstance();
            absenceCalendar.add(Calendar.DATE, 1);
            Date tomorrowDate = absenceCalendar.getTime();
            parseAbsencesWithDay(latestTomorrowWorkFromHome, absences, tomorrowDate);

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
    public void requestForHolidayAbsenceEmployees() {

        Callback okHttpCallback = new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(Config.getTag(this), "requestForHolidayAbsenceEmployees failure.");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.d(Config.TAG, responseString);
                List<Absence> absences = processJsonHolidayAbsences(responseString);
                sortAbsencesByUserFirstName(absences);
                apiCallback.onAbsenceEmployeesListReceived(new LinkedHashSet<Absence>(absences));
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://intranet.stxnext.pl/api/presence")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(okHttpCallback);
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
            User dbUser = DBManager.getInstance(context).getUser(userId);
            if (dbUser == null) {
                UserApi userApi = new UserApiImpl(context, new UserApiCallback() {

                    @Override
                    public void onUserReceived(User user) {
                        absence.setUser(user);
                        absences.add(absence);
                    }

                    @Override
                    public void onAbsenceResponse(boolean hours, boolean calendarEntry, boolean request) {
                        // nothing to do
                    }

                    @Override
                    public void onOutOfOfficeResponse(boolean entry) {

                    }

                    @Override
                    public void onAbsenceDaysLeftReceived(int mandated, int days, int absenceDaysLeft) {

                    }

                    @Override
                    public void onTimeReportReceived(List<TimeReportDay> timeReportDays, Calendar month) {

                    }

                    @Override
                    public void onRequestError() {

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
        Absence absenceWithNoUser = new AbsenceImpl(new User(userId, "", "", "", "", "", Lists.newArrayList(""), "", "", "", ""), absenceFrom, absenceTo, explanation);
        return absenceWithNoUser;
    }

    private void extractJSONObjectsFromJSONArray(List<JSONObject> absencesJSONObjects, JSONArray absencesJSONArray) throws JSONException {
        for (int i = 0; i < absencesJSONArray.length(); ++i) {
            absencesJSONObjects.add(absencesJSONArray.getJSONObject(i));
        }
    }

}

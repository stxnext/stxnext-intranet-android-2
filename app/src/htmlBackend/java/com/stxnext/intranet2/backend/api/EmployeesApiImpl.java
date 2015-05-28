package com.stxnext.intranet2.backend.api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.AbsenceImpl;
import com.stxnext.intranet2.backend.model.impl.UserImpl;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.Session;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(Session.getInstance(context).getCookieStore());

        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                List<User> users = processJsonEmployees(response);
                apiCallback.onEmployeesListReceived(users);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };

        httpClient.get("https://intranet.stxnext.pl/api/users?full=1&inactive=0", asyncHttpResponseHandler);

//        List<User> list = new ArrayList<>();
//        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
//        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Chemist", "", "", "", ""));
//        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
//        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
//        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));
//
//        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
//        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
//        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
//        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));
//
//        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
//        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
//        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
//        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));
//
//        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
//        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
//        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
//        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
//        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));

//        apiCallback.onEmployeesListReceived(list);
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
        String lastName;
        if (nameSplitted != null && nameSplitted.length == 2) {
            firstName = nameSplitted[0];
            lastName = nameSplitted[1];
        } else {
            throw new JSONException("Wrong name value format");
        }
        String role = "";
        JSONArray rolesJSONArray = userJSONObject.getJSONArray("roles");
        if (rolesJSONArray.length() > 0) role = rolesJSONArray.getString(0);
        User user = new UserImpl(String.valueOf(id), firstName, lastName, "", "", "", role, "", "", "", "");
        return user;
    }

    private boolean isEmployee(JSONObject userJSONObject) throws JSONException {
        return !userJSONObject.getBoolean("is_client");
    }

    @Override
    public void requestForOutOfOfficeAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Wyjechałem do Softaxu, pod telefonem"));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo, "Brak dostępu do telefonu, internet w razie potrzeby"));
        absenceList.add(new AbsenceImpl(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Chemist", "", "", "", ""), absenceFrom, absenceTo, "Pralka się zepsuła, czekam na majstra"));
        absenceList.add(new AbsenceImpl(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""), absenceFrom, absenceTo, "Sprawy urzędowe"));
        absenceList.add(new AbsenceImpl(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""), absenceFrom, absenceTo, "Odbiór nadgodzin"));
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Poszedłem zgłosić coś na policję"));
        absenceList.add(new AbsenceImpl(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""), absenceFrom, absenceTo, "Na konferencji"));
        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }

    @Override
    public void requestForWorkFromHomeAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Chemist", "", "", "", ""), absenceFrom, absenceTo, "Źle się czuje, pracuję z domu"));
        absenceList.add(new AbsenceImpl(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""), absenceFrom, absenceTo, "Córka chora, musiałem zostać w domu"));
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Chory, dostępny po tel."));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo, "Jeżdżę po urzędach"));
        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }

    @Override
    public void requestForHolidayAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Wakacje w Ugandzie, brak dostępu do telefonu i internetu"));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Pod telefonem"));
        absenceList.add(new AbsenceImpl(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""), absenceFrom, absenceTo, "Brak dostępu do internetu"));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Tel. tylko roaming"));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo, "Za granicą"));
        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }
}

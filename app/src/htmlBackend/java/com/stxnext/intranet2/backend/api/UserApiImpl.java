package com.stxnext.intranet2.backend.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stxnext.intranet2.backend.api.json.AbsenceDaysLeft;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.model.HolidayTypes;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;
import com.stxnext.intranet2.utils.Session;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class UserApiImpl extends UserApi {

    private static final String API_URL = "https://intranet.stxnext.pl/";

    public UserApiImpl(Context context, UserApiCallback callback) {
        super(context, callback);
        this.context = context;
    }

    @Override
    public void requestForUser(String userId) {
        if (userId != null) {
            User userFromDB = DBManager.getInstance(context).getUser(userId);
            if (userFromDB == null) {
                getUser(userId);
            } else {
                apiCallback.onUserReceived(userFromDB);
            }
        } else {
            User user = new User(null, "Marian", "Kowalski", "mariano.kowalsky", "+48 600 211 321",
                    "Poznań", "Programista", "marian.kowalski@stxnext.pl", "marianno", "Team Mobilny", null);
            apiCallback.onUserReceived(user);
        }
    }

    private void getUser(final String userId) {
        downlUsersFromHTTP(context, apiCallback, userId);
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

    //TODO probably to delete
    //Need json:
    // {"lateness":{"late_end":"09:05","popup_explanation":"Test aplikacji.","work_from_home":"false","late_start":"09:00","popup_date":"31/05/2015"}}
    @Override
    public void submitOutOfOfficeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation) {
    }
    //TODO probably to delete
    // {"lateness":{"late_end":"09:05","popup_explanation":"Test aplikacji.","work_from_home":"true","late_start":"09:00","popup_date":"31/05/2015"}}
    @Override
    public void submitWorkFromHomeAbsence(Date submissionDate, Date startHour, Date endHour, String explanation) {

    }

    //TODO
    //Need example jsons:
    // {"lateness":{"late_end":"09:05","popup_explanation":"Test aplikacji.","work_from_home":"false","late_start":"09:00","popup_date":"31/05/2015"}}
    // {"lateness":{"late_end":"09:05","popup_explanation":"Test aplikacji.","work_from_home":"true","late_start":"09:00","popup_date":"31/05/2015"}}
    public void submitOutOfOffice(boolean workFromHome, Date submissionDate, Date startHour, Date endHour, String explanation) {
        JSONObject mainObject = new JSONObject();
        JSONObject absenceObject = new JSONObject();
        try {
            SimpleDateFormat submissionDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat hourDateFormat = new SimpleDateFormat("kk:mm");
            absenceObject.put("popup_date", submissionDateFormat.format(submissionDate))
                    .put("late_start", hourDateFormat.format(startHour))
                    .put("late_end", hourDateFormat.format(endHour))
                    .put("popup_explanation", explanation)
                    .put("work_from_home", (new Boolean(workFromHome)).toString());
            mainObject.put("lateness", absenceObject);
            String jsonString = mainObject.toString().replace("\\","") ;
            Log.d(Config.TAG, jsonString);

            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setCookieStore(Session.getInstance(context).getCookieStore());
            StringEntity postEntity = new StringEntity(jsonString, HTTP.UTF_8);
            AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String latenessResponse = new String(responseBody);
                    try {
                        JSONObject latenessJSONObject = new JSONObject(latenessResponse);
                        boolean entry = latenessJSONObject.getBoolean("entry");
                        apiCallback.onOutOfOfficeResponse(entry);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    apiCallback.onRequestError();
                }
            };

            httpClient.post(context, "https://intranet.stxnext.pl/api/lateness", postEntity, "application/json", asyncHttpResponseHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
            String jsonString = mainObject.toString().replace("\\","") ;
            Log.d(Config.TAG, jsonString);

            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setCookieStore(Session.getInstance(context).getCookieStore());
//            httpClient.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, "application/json");
            StringEntity postEntity = new StringEntity(jsonString, HTTP.UTF_8);
            AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d(Config.TAG, response);
                    try {
                        JSONObject holidayAbsenceResult = new JSONObject(response);
                        boolean hours = holidayAbsenceResult.getBoolean("hours");
                        boolean calendarEntry = holidayAbsenceResult.getBoolean("calendar_entry");
                        boolean request = holidayAbsenceResult.getBoolean("request");
                        apiCallback.onAbsenceResponse(hours, calendarEntry, request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    apiCallback.onRequestError();
                }
            };

            httpClient.post(context, "https://intranet.stxnext.pl/api/absence", postEntity, "application/json", asyncHttpResponseHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getAbsenceDaysLeft() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(Session.getInstance(context).getCookieStore());
        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d(Config.TAG, response);
                Gson gson = new Gson();
                AbsenceDaysLeft absenceDaysLeft = gson.fromJson(response, AbsenceDaysLeft.class);
                apiCallback.onAbsenceDaysLeftReceived(absenceDaysLeft.getMandated(), absenceDaysLeft.getDays(), absenceDaysLeft.getLeft());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(Config.TAG, "Failure");
                apiCallback.onRequestError();
            }
        };

        SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String url = String.format(API_URL + "api/absence_days?date_start=%s&type=planowany",
                defaultDateFormat.format(new Date()));
//        try {
//            url = URLEncoder.encode(url, "UTF-8");
//            httpClient.get(url, asyncHttpResponseHandler);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        httpClient.get(url, asyncHttpResponseHandler);
    }

}

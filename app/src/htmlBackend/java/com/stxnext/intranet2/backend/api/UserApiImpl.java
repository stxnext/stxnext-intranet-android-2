package com.stxnext.intranet2.backend.api;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.stxnext.intranet2.backend.api.json.AbsenceDaysLeft;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiTimeReportCallback;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.timereport.TimeReportDay;
import com.stxnext.intranet2.backend.retrofit.WorkedHoursService;
import com.stxnext.intranet2.model.HolidayTypes;
import com.stxnext.intranet2.rest.IntranetRestAdapter;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

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
                    "Poznań", Lists.newArrayList("Programista"), "marian.kowalski@stxnext.pl", "marianno", "Team Mobilny", null);
            apiCallback.onUserReceived(user);
        }
    }

    private void getUser(final String userId) {
        downloadUser(context, Optional.of(apiCallback), userId);
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
            String jsonString = mainObject.toString().replace("\\","");
            Log.d(Config.TAG, jsonString);

            Callback okHttpCallback = new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(Config.getTag(this), "submitOutOfOffice post query error.");
                    apiCallback.onRequestError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String latenessResponse = response.body().string();
                    try {
                        JSONObject latenessJSONObject = new JSONObject(latenessResponse);
                        boolean entry = latenessJSONObject.getBoolean("entry");
                        apiCallback.onOutOfOfficeResponse(entry);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody body = RequestBody.create(jsonMediaType, jsonString);
            Request request = new Request.Builder()
                    .url("https://intranet.stxnext.pl/api/lateness")
                    .post(body)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(okHttpCallback);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

            Callback okHttpCallback = new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(Config.getTag(this), "submitHolidayAbsence post query error.");
                    apiCallback.onRequestError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String responseString = response.body().string();
                    Log.d(Config.TAG, responseString);
                    try {
                        JSONObject holidayAbsenceResult = new JSONObject(responseString);
                        boolean hours = holidayAbsenceResult.getBoolean("hours");
                        boolean calendarEntry = holidayAbsenceResult.getBoolean("calendar_entry");
                        boolean request = holidayAbsenceResult.getBoolean("request");
                        apiCallback.onAbsenceResponse(hours, calendarEntry, request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody body = RequestBody.create(jsonMediaType, jsonString);
            Request request = new Request.Builder()
                    .url("https://intranet.stxnext.pl/api/absence")
                    .post(body)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(okHttpCallback);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAbsenceDaysLeft() {
        SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String url = String.format(API_URL + "api/absence_days?date_start=%s&type=planowany",
                defaultDateFormat.format(new Date()));

        Callback okHttpCallback = new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(Config.TAG, "Failure");
                apiCallback.onRequestError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.d(Config.TAG, responseString);
                Gson gson = new Gson();
                AbsenceDaysLeft absenceDaysLeft = gson.fromJson(responseString, AbsenceDaysLeft.class);
                apiCallback.onAbsenceDaysLeftReceived(absenceDaysLeft.getMandated(), absenceDaysLeft.getDays(), absenceDaysLeft.getLeft());
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(okHttpCallback);
    }

    /**
     * Returns time report for given month.
     * @param userId
     * @param month Calendar instance with properly month and year set.
     */
    @Override
    public void getTimeReport(String userId, final Calendar month, final UserApiTimeReportCallback userApiCallback) {
        String monthYearString = DateFormat.format("MM.yyyy", month).toString();
        RestAdapter restAdapter = IntranetRestAdapter.build();
        WorkedHoursService workedHoursService = restAdapter.create(WorkedHoursService.class);
        retrofit.Callback<List<TimeReportDay>> callback = new retrofit.Callback<List<TimeReportDay>>() {

            @Override
            public void success(List<TimeReportDay> timeReportDays, retrofit.client.Response response) {
                Log.d(Config.getTag(UserApiImpl.this), "time report json: " + timeReportDays.get(0).toString());
                userApiCallback.onTimeReportReceived(timeReportDays, month);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(Config.getTag(UserApiImpl.this), "Error getting time report json values");
            }
        };
        workedHoursService.getTimeReport(Integer.parseInt(userId), monthYearString, callback);
    }

}

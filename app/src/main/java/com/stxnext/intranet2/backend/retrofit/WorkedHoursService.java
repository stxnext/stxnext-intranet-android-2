package com.stxnext.intranet2.backend.retrofit;

import com.stxnext.intranet2.backend.model.workedHour.WorkedHours;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by bkosarzycki on 21.08.15.
 */
public interface WorkedHoursService {

    @GET("/api/worked_hours")
    WorkedHours getUserWorkedHours(@Query("user_id") int user);
}

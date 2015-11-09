package com.stxnext.intranet2.backend.retrofit;

import com.stxnext.intranet2.backend.model.project.ProjectResponse;
import com.stxnext.intranet2.backend.model.time.TimeEntryPost;
import com.stxnext.intranet2.backend.model.time.TimeEntryResponse;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by bkosarzycki on 02.11.15.
 */
public interface TimeEntriesService {

    @POST("/api/user_times")
    Observable<TimeEntryResponse> postUserTime(@Body TimeEntryPost postBody);
}


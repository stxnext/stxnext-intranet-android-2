package com.stxnext.intranet2.backend.retrofit;

import com.google.gson.Gson;
import com.stxnext.intranet2.backend.model.project.ProjectResponse;
import com.stxnext.intranet2.backend.model.workedHour.WorkedHours;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by bkosarzycki on 02.11.15.
 */
public interface ProjectListService {

    @GET("/api/projects")
    Observable<ProjectResponse> getProjects();
}


package com.stxnext.intranet2.backend.retrofit;

import com.stxnext.intranet2.backend.model.team.Teams;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Łukasz Ciupa on 09.11.2015.
 */
public interface TeamsService {

    @GET("/api/teams")
    void getTeams(Callback<Teams> callback);
}

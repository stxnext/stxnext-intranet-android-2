package com.stxnext.intranet2.backend.api;

import android.util.Log;

import com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback;
import com.stxnext.intranet2.backend.model.team.Teams;
import com.stxnext.intranet2.backend.retrofit.TeamsService;
import com.stxnext.intranet2.rest.IntranetRestAdapter;
import com.stxnext.intranet2.utils.Config;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Łukasz Ciupa on 09.11.2015.
 */
public class TeamApiImpl extends TeamApi {

    @Override
    public void requestForTeams(final OnTeamsReceivedCallback callback) {
        RestAdapter restAdapter = IntranetRestAdapter.build();
        TeamsService teamsService = restAdapter.create(TeamsService.class);
        Callback<Teams> restCallback = new Callback<Teams>() {
            @Override
            public void success(Teams teams, Response response) {
                Log.d(Config.getTag(TeamApiImpl.this), "Success getting teams json");
                callback.onReceived(teams.getTeams());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(Config.getTag(this), "Error getting teams json values");
            }
        };
        teamsService.getTeams(restCallback);
    }
}

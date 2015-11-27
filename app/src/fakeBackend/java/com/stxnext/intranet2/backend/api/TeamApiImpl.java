package com.stxnext.intranet2.backend.api;

import android.util.Log;

import com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.backend.model.team.Teams;
import com.stxnext.intranet2.backend.retrofit.TeamsService;
import com.stxnext.intranet2.rest.IntranetRestAdapter;
import com.stxnext.intranet2.utils.Config;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Łukasz Ciupa on 09.11.2015.
 */
public class TeamApiImpl implements TeamApi {

    @Override
    public void requestForTeams(final OnTeamsReceivedCallback callback) {

        List<Team> teams = new ArrayList<>();

        callback.onReceived(teams);

    }
}

package com.stxnext.intranet2.backend.api;

import com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback;
import com.stxnext.intranet2.backend.model.team.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Łukasz Ciupa on 09.11.2015.
 */
public class TeamApiImpl implements TeamApi {

    @Override
    public void requestForTeams(final OnTeamsReceivedCallback callback) {
        List<Team> teams = new ArrayList<>();
        Team team = new Team();
        team.setId(1);
        team.setHasAvatar(false);
        team.setUsers(new long[]{0, 1, 3});
        team.setName("Mobile Team");
        teams.add(team);
        callback.onReceived(teams);
    }
}

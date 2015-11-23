package com.stxnext.intranet2.backend.callback.team;

import com.stxnext.intranet2.backend.model.team.Team;

import java.util.List;

/**
 * Created by Lukasz on 09.11.2015.
 */
public interface OnTeamsReceivedCallback {

    void onReceived(List<Team> teams);

}

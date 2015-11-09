package com.stxnext.intranet2.backend.api;

import com.stxnext.intranet2.backend.callback.team.OnTeamsReceivedCallback;

/**
 * Created by Lukasz on 09.11.2015.
 */
public abstract class TeamApi {

    public abstract void requestForTeams(OnTeamsReceivedCallback callback);

}

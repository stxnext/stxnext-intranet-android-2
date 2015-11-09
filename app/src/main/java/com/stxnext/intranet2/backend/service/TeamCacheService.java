package com.stxnext.intranet2.backend.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Łukasz Ciupa on 09.11.2015.
 * Class used to manage data for TeamApi. Data is get from api if requested
 * for the first time and then
 * saved to db. Later when data is needed it can be get from db.
 */
public class TeamCacheService {

    /**
     * This is used to cache user to teams data as to not get this every time
     * from db.
     */
    private static Map<Integer, List<String>> userToTeamsMap;

    public void getTeamForUser(int userId) {

    }


}

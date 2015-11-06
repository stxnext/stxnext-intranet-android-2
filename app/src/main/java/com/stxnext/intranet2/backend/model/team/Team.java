package com.stxnext.intranet2.backend.model.team;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lukasz on 06.11.2015.
 */
public class Team {

    private int[] users;
    @SerializedName("has_avatar") private Boolean hasAvatar;
    private String img;
    private long id;
    private Project[] projects;
    private String name;

}

package com.stxnext.intranet2.backend.model.team;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lukasz on 06.11.2015.
 */
public class Team {

    private long[] users;
    @SerializedName("has_avatar") private Boolean hasAvatar;
    private String img;
    private long id;
    private Project[] projects;
    private String name;

    public long[] getUsers() {
        return users;
    }

    public Boolean hasAvatar() {
        return hasAvatar;
    }

    public String getImg() {
        return img;
    }

    public long getId() {
        return id;
    }

    public Project[] getProjects() {
        return projects;
    }

    public String getName() {
        return name;
    }
}

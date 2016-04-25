package com.stxnext.intranet2.backend.model.team;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.stxnext.intranet2.adapter.json.TeamAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lukasz Ciupa on 06.11.2015.
 */
@DatabaseTable(tableName = "team")
public class Team {

    @DatabaseField(id = true)
    private long id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    @JsonAdapter(TeamAdapter.class)
    private long[] users;
    @DatabaseField
    @SerializedName("has_avatar")
    private Boolean hasAvatar;
    @DatabaseField
    private String img;
    // For Orm lite only
    @ForeignCollectionField
    private Collection<TeamProject> teamToProjectLinks;
    // Only for Gson, filled only when json is parsed
    @SerializedName("projects_details")
    private Collection<Project> projectsGson;
    @DatabaseField
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long[] getUsers() {
        return users;
    }

    public void setUsers(long[] users) {
        this.users = users;
    }

    public Boolean hasAvatar() {
        return hasAvatar;
    }

    public void setHasAvatar(Boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    /**
     * It's only used after parsing from json.
     * @return
     */
    public Collection<Project> getProjectsGson() {
        return projectsGson;
    }

    /**
     * Its only used after parsing from json.
     * @param projectsGson
     */
    public void setProjectsGson(Collection<Project> projectsGson) {
        this.projectsGson = projectsGson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<TeamProject> getTeamToProjectLinks() {
        return teamToProjectLinks;
    }

    public void setTeamToProjectLinks(Collection<TeamProject> teamToProjectLinks) {
        this.teamToProjectLinks = teamToProjectLinks;
    }

    public Collection<Project> getProjects() {
        Collection<Project> projects = new ArrayList<>();
        for (TeamProject teamProject : teamToProjectLinks) {
            projects.add(teamProject.getProject());
        }
        return projects;
    }
}

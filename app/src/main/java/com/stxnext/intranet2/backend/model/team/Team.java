package com.stxnext.intranet2.backend.model.team;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lukasz Ciupa on 06.11.2015.
 */
@DatabaseTable(tableName = "team")
public class Team {

    @DatabaseField(id = true)
    private long id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private long[] users;
    @DatabaseField
    @SerializedName("has_avatar")
    private Boolean hasAvatar;
    @DatabaseField
    private String img;
    // For Orm lite
//    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @ForeignCollectionField
    ForeignCollection<Project> projectsORMLite;
    // For Gson, filled only when json is parsed
    private Project[] projectsGson;
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

    public Boolean getHasAvatar() {
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

    public Project[] getProjects() {
        return projectsGson;
    }

    public void setProjects(Project[] projects) {
        this.projectsGson = projects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ForeignCollection<Project> getProjectsORMLite() {
        return projectsORMLite;
    }

    public void setProjectsORMLite(ForeignCollection<Project> projectsORMLite) {
        this.projectsORMLite = projectsORMLite;
    }
}

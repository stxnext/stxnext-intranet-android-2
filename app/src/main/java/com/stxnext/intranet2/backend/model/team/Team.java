package com.stxnext.intranet2.backend.model.team;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
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
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Project[] projects;
    @DatabaseField
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

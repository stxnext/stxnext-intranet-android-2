package com.stxnext.intranet2.backend.model.impl;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by Tomasz on 2015-05-07.
 */

@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(id = true) private String id;
    @DatabaseField private String name;
    @DatabaseField private String skype;
    @DatabaseField @SerializedName("phone") private String phoneNumber;
    @DatabaseField private String localization;
    @DatabaseField(dataType = DataType.SERIALIZABLE) private String[] roles;
    @DatabaseField private String email;
    @DatabaseField private String irc;
    @DatabaseField private String team;
    @DatabaseField @SerializedName("avatar_url") private String photo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!name.equals(user.name)) return false;
        if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null) return false;
        if (localization != null ? !localization.equals(user.localization) : user.localization != null) return false;
        //if (role != null ? !role.equals(user.role) : user.role != null) return false;
        return !(team != null ? !team.equals(user.team) : user.team != null);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (localization != null ? localization.hashCode() : 0);
        //result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        return result;
    }

    public User() {}

    public User(String id, String firstName, String lastName, String skype, String phoneNumber,
                String localization, List<String> roles, String email,
                String irc, String team, String photo) {
        this.id = id;
        this.name = firstName + " " + lastName;
        this.skype = skype;
        this.phoneNumber = phoneNumber;
        this.localization = localization;
        this.roles = roles.toArray(new String[roles.size()]);
        this.email = email;
        this.irc = irc;
        this.team = team;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return name!= null && !name.isEmpty() ? name.split(" ")[0] : null;
    }

    public String getLastName() {
        return name!= null && !name.isEmpty() ? name.split(" ")[1] : null;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public List<String> getRoles() {
        return roles != null && roles.length > 0 ? Lists.newArrayList(roles) : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIrc() {
        return irc;
    }

    public void setIrc(String irc) {
        this.irc = irc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}

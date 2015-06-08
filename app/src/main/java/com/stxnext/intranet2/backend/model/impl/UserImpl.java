package com.stxnext.intranet2.backend.model.impl;

import com.stxnext.intranet2.backend.model.User;

/**
 * Created by Tomasz on 2015-05-07.
 */
public class UserImpl implements User {

    private String id;
    private String firstName;
    private String lastName;
    private String skype;
    private String phoneNumber;
    private String localization;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserImpl user = (UserImpl) o;

        if (!firstName.equals(user.firstName)) return false;
        if (!lastName.equals(user.lastName)) return false;
        if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null) return false;
        if (localization != null ? !localization.equals(user.localization) : user.localization != null) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;
        return !(team != null ? !team.equals(user.team) : user.team != null);

    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (localization != null ? localization.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        return result;
    }

    private String role;
    private String email;
    private String irc;
    private String team;
    private String photo;

    public UserImpl() {}

    public UserImpl(String id, String firstName, String lastName, String skype, String phoneNumber,
                    String localization, String role, String email,
                    String irc, String team, String photo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.skype = skype;
        this.phoneNumber = phoneNumber;
        this.localization = localization;
        this.role = role;
        this.email = email;
        this.irc = irc;
        this.team = team;
        this.photo = photo;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    @Override
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getIrc() {
        return irc;
    }

    public void setIrc(String irc) {
        this.irc = irc;
    }

    @Override
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}

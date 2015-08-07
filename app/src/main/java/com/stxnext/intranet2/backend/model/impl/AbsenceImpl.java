package com.stxnext.intranet2.backend.model.impl;

import com.stxnext.intranet2.backend.model.Absence;

import java.util.Date;

/**
 * Created by Tomasz Konieczny on 2015-05-15.
 */
public class AbsenceImpl implements Absence {

    private User user;
    private Date absenceFrom;
    private Date absenceTo;
    private String description;

    public AbsenceImpl(User user, Date absenceFrom, Date absenceTo, String description) {
        this.user = user;
        this.absenceFrom = absenceFrom;
        this.absenceTo = absenceTo;
        this.description = description;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Date getAbsenceFrom() {
        return absenceFrom;
    }

    @Override
    public Date getAbsenceTo() {
        return absenceTo;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbsenceImpl absence = (AbsenceImpl) o;

        if (!user.equals(absence.user)) return false;
        if (!absenceFrom.equals(absence.absenceFrom)) return false;
        if (!absenceTo.equals(absence.absenceTo)) return false;
        return description.equals(absence.description);

    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + absenceFrom.hashCode();
        result = 31 * result + absenceTo.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}

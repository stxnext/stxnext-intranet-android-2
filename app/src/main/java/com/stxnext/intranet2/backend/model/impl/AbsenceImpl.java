package com.stxnext.intranet2.backend.model.impl;

import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.impl.UserImpl;

import java.util.Date;

/**
 * Created by Tomasz Konieczny on 2015-05-15.
 */
public class AbsenceImpl implements Absence {

    private UserImpl user;
    private Date absenceFrom;
    private Date absenceTo;
    private String description;

    public AbsenceImpl(UserImpl user, Date absenceFrom, Date absenceTo, String description) {
        this.user = user;
        this.absenceFrom = absenceFrom;
        this.absenceTo = absenceTo;
        this.description = description;
    }

    @Override
    public UserImpl getUser() {
        return user;
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
}

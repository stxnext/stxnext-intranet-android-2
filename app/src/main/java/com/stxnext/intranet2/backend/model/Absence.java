package com.stxnext.intranet2.backend.model;

import java.util.Date;

/**
 * Created by OGIT on 2015-05-14.
 */
public interface Absence {

    User getUser();
    Date getAbsenceFrom();
    Date getAbsenceTo();
    String getDescription();
}

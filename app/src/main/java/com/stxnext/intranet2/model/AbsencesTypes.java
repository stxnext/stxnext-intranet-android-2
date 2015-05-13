package com.stxnext.intranet2.model;

import com.stxnext.intranet2.R;

/**
 * Created by Tomasz Konieczny on 2015-05-13.
 */
public enum AbsencesTypes {

    HOLIDAY(R.string.holiday),
    WORK_FROM_HOME(R.string.work_from_home),
    OUT_OF_OFFICE(R.string.out_of_office);

    private final int title;

    AbsencesTypes(int title) {
        this.title = title;
    }

    public int getTitle() {
        return title;
    }
}

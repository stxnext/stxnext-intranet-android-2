package com.stxnext.intranet2.model;

import com.stxnext.intranet2.R;

/**
 * Created by Tomasz Konieczny on 2015-05-12.
 */
public enum DrawerMenuItems {

    ABSENCES(R.string.absences),
    EMPLOYEES(R.string.employees_list),
    SETTINGS(R.string.settings),
    ABOUT(R.string.about);

    private final int title;

    DrawerMenuItems(int titleRes) {
        this.title = titleRes;
    }

    public int getTitle() {
        return title;
    }
}

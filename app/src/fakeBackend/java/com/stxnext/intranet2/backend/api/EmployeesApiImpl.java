package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.impl.AbsenceImpl;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.utils.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Tommy Necessary on 2015-05-07.
 */
public class EmployeesApiImpl extends EmployeesCommonApiImpl implements EmployeesApi {

    public EmployeesApiImpl(Context context, EmployeesApiCallback callback) {
        super(context, callback);
    }

    @Override
    public void requestForEmployees(boolean forceRequest) {

        List<User> employees = DBManager.getInstance(context).getEmployees();
        if (employees == null || forceRequest)
            downloadUsers(context, Optional.of(apiCallback));
        else {
            sortUsersByFirstName(employees);
            apiCallback.onEmployeesListReceived(employees);
        }
    }

    @Override
    public void requestForOutOfOfficeAbsenceEmployees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new User("7", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Holiday in Uganda"));
        absenceList.add(new AbsenceImpl(new User("1", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Available via email"));
        absenceList.add(new AbsenceImpl(new User("4", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""), absenceFrom, absenceTo, "Out of office"));
        absenceList.add(new AbsenceImpl(new User("6", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Unavailable"));
        absenceList.add(new AbsenceImpl(new User("2", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""), absenceFrom, absenceTo, "USA"));
        absenceList.add(new AbsenceImpl(new User("5", "Bert", "Lawnmower", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Illness"));
        apiCallback.onAbsenceEmployeesListReceived(new LinkedHashSet<Absence>(absenceList));
    }

    @Override
    public void requestForWorkFromHomeAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new User("3", "Mario", "Step", "", "", "", Lists.newArrayList("Chemist"), "", "", "", ""), absenceFrom, absenceTo, "Work from home"));
        absenceList.add(new AbsenceImpl(new User("8", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""), absenceFrom, absenceTo, "Feel bad"));
        absenceList.add(new AbsenceImpl(new User("7", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Illness"));
        absenceList.add(new AbsenceImpl(new User("2", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""), absenceFrom, absenceTo, "Busy"));
        absenceList.add(new AbsenceImpl(new User("5", "Bert", "Lawnmower", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Illness"));
        apiCallback.onAbsenceEmployeesListReceived(new LinkedHashSet<Absence>(absenceList));
    }

    @Override
    public void requestForHolidayAbsenceEmployees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new User("7", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Holiday in Uganda"));
        absenceList.add(new AbsenceImpl(new User("1", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Available via email"));
        absenceList.add(new AbsenceImpl(new User("4", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""), absenceFrom, absenceTo, "Out of office"));
        absenceList.add(new AbsenceImpl(new User("6", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Unavailable"));
        absenceList.add(new AbsenceImpl(new User("2", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""), absenceFrom, absenceTo, "USA"));
        absenceList.add(new AbsenceImpl(new User("5", "Bert", "Lawnmower", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Illness"));
        apiCallback.onAbsenceEmployeesListReceived(new LinkedHashSet<Absence>(absenceList));
    }
}

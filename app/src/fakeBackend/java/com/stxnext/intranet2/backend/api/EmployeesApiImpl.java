package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.google.common.collect.Lists;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.impl.AbsenceImpl;
import com.stxnext.intranet2.backend.model.impl.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Tommy Necessary on 2015-05-07.
 */
public class EmployeesApiImpl extends EmployeesApi {

    public EmployeesApiImpl(Context context, EmployeesApiCallback callback) {
        super(context, callback);
    }

    @Override
    public void requestForEmployees(boolean forceRequest) {

        List<User> list = new ArrayList<>();
        list.add(new User("12sad3", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("15saa3", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""));
        list.add(new User("13dsa3", "Mario", "Step", "", "", "", Lists.newArrayList("Chemist"), "", "", "", ""));
        list.add(new User("14das3", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""));
        list.add(new User("112d33", "David", "Studentzky", "", "", "", Lists.newArrayList("Senior iOS Developer"), "", "", "", ""));
        list.add(new User("121233", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("1sa243", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""));

        list.add(new User("12sad3", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("15saa3", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""));
        list.add(new User("13dsa3", "Mario", "Step", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("14das3", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""));
        list.add(new User("112d33", "David", "Studentzky", "", "", "", Lists.newArrayList("Senior iOS Developer"), "", "", "", ""));
        list.add(new User("121233", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("1sa243", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""));

        list.add(new User("12sad3", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("15saa3", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""));
        list.add(new User("13dsa3", "Mario", "Step", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("14das3", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""));
        list.add(new User("112d33", "David", "Studentzky", "", "", "", Lists.newArrayList("Senior iOS Developer"), "", "", "", ""));
        list.add(new User("121233", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("1sa243", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""));

        list.add(new User("12sad3", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("15saa3", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""));
        list.add(new User("13dsa3", "Mario", "Step", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("14das3", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""));
        list.add(new User("112d33", "David", "Studentzky", "", "", "", Lists.newArrayList("Senior iOS Developer"), "", "", "", ""));
        list.add(new User("121233", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""));
        list.add(new User("1sa243", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""));

        apiCallback.onEmployeesListReceived(list);
    }

    @Override
    public void requestForOutOfOfficeAbsenceEmployees() {
        requestForEmployees(false);
    }

    @Override
    public void requestForWorkFromHomeAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new User("13dsa3", "Mario", "Step", "", "", "", Lists.newArrayList("Chemist"), "", "", "", ""), absenceFrom, absenceTo, "Work from home"));
        absenceList.add(new AbsenceImpl(new User("1sa243", "Jack", "Evening", "", "", "", Lists.newArrayList("COO"), "", "", "", ""), absenceFrom, absenceTo, "Feel bad"));
        absenceList.add(new AbsenceImpl(new User("121233", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Illness"));
        absenceList.add(new AbsenceImpl(new User("15saa3", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""), absenceFrom, absenceTo, "Busy"));
        apiCallback.onAbsenceEmployeesListReceived(new LinkedHashSet<Absence>(absenceList));
    }

    @Override
    public void requestForHolidayAbsenceEmployees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new User("121233", "Mieszko", "Wrightwheel", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Holiday in Uganda"));
        absenceList.add(new AbsenceImpl(new User("12sad3", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Available via email"));
        absenceList.add(new AbsenceImpl(new User("14das3", "Paolo", "Citizen", "", "", "", Lists.newArrayList("iOS Developer"), "", "", "", ""), absenceFrom, absenceTo, "Out of office"));
        absenceList.add(new AbsenceImpl(new User("12sad3", "Lucas", "Vega", "", "", "", Lists.newArrayList("Android Developer"), "", "", "", ""), absenceFrom, absenceTo, "Unavailable"));
        absenceList.add(new AbsenceImpl(new User("15saa3", "Tommy", "Necessary", "", "", "", Lists.newArrayList("Team Leader"), "", "", "", ""), absenceFrom, absenceTo, "USA"));
        apiCallback.onAbsenceEmployeesListReceived(new LinkedHashSet<Absence>(absenceList));
    }
}

package com.stxnext.intranet2.backend.api;

import android.content.Context;

import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.AbsenceImpl;
import com.stxnext.intranet2.backend.model.impl.UserImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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
        list.add(new UserImpl("12sad3", "Lucas", "Vega", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tommy", "Necessary", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mario", "Step", "", "", "", "Chemist", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paolo", "Citizen", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "David", "Studentzky", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Wrightwheel", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jack", "Evening", "", "", "", "COO", "", "", "", ""));

        list.add(new UserImpl("12sad3", "Lucas", "Vega", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tommy", "Necessary", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mario", "Step", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paolo", "Citizen", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "David", "Studentzky", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Wrightwheel", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jack", "Evening", "", "", "", "COO", "", "", "", ""));

        list.add(new UserImpl("12sad3", "Lucas", "Vega", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tommy", "Necessary", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mario", "Step", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paolo", "Citizen", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "David", "Studentzky", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Wrightwheel", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jack", "Evening", "", "", "", "COO", "", "", "", ""));

        list.add(new UserImpl("12sad3", "Lucas", "Vega", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tommy", "Necessary", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mario", "Step", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paolo", "Citizen", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "David", "Studentzky", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Wrightwheel", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jack", "Evening", "", "", "", "COO", "", "", "", ""));

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
        absenceList.add(new AbsenceImpl(new UserImpl("13dsa3", "Mario", "Step", "", "", "", "Chemist", "", "", "", ""), absenceFrom, absenceTo, "Work from home"));
        absenceList.add(new AbsenceImpl(new UserImpl("1sa243", "Jack", "Evening", "", "", "", "COO", "", "", "", ""), absenceFrom, absenceTo, "Feel bad"));
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Wrightwheel", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Illness"));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tommy", "Necessary", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo, "Busy"));
        apiCallback.onAbsenceEmployeesListReceived(new HashSet<Absence>(absenceList));
    }

    @Override
    public void requestForHolidayAbsenceEmployees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Wrightwheel", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Holiday in Uganda"));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Lucas", "Vega", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Available via email"));
        absenceList.add(new AbsenceImpl(new UserImpl("14das3", "Paolo", "Citizen", "", "", "", "iOS Developer", "", "", "", ""), absenceFrom, absenceTo, "Out of office"));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Lucas", "Vega", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Unavailable"));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tommy", "Necessary", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo, "USA"));
        apiCallback.onAbsenceEmployeesListReceived(new HashSet<Absence>(absenceList));
    }
}

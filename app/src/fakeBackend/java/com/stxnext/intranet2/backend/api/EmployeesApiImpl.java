package com.stxnext.intranet2.backend.api;

import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.backend.model.impl.AbsenceImpl;
import com.stxnext.intranet2.backend.model.impl.UserImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tomasz Konieczny on 2015-05-07.
 */
public class EmployeesApiImpl extends EmployeesApi {

    public EmployeesApiImpl(EmployeesApiCallback callback) {
        super(callback);
    }

    @Override
    public void requestForEmployees() {
        List<User> list = new ArrayList<>();
        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Chemist", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));

        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));

        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));

        list.add(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""));
        list.add(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""));
        list.add(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""));
        list.add(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""));
        list.add(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""));

        apiCallback.onEmployeesListReceived(list);
    }

    @Override
    public void requestForOutOfOfficeAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Chemist", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo));
        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }

    @Override
    public void requestForWorkFromHomeAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Chemist", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo));
        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }

    @Override
    public void requestForHolidayAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""), absenceFrom, absenceTo));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo));
        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }
}

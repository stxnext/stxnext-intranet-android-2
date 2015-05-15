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
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Wyjechałem do Softaxu, pod telefonem"));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo, "Brak dostępu do telefonu, internet w razie potrzeby"));
        absenceList.add(new AbsenceImpl(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Chemist", "", "", "", ""), absenceFrom, absenceTo, "Pralka się zepsuła, czekam na majstra"));
        absenceList.add(new AbsenceImpl(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""), absenceFrom, absenceTo, "Sprawy urzędowe"));
        absenceList.add(new AbsenceImpl(new UserImpl("112d33", "Dawid", "Żakowski", "", "", "", "Senior iOS Developer", "", "", "", ""), absenceFrom, absenceTo, "Odbiór nadgodzin"));
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Poszedłem zgłosić coś na policję"));
        absenceList.add(new AbsenceImpl(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""), absenceFrom, absenceTo, "Na konferencji"));
        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }

    @Override
    public void requestForWorkFromHomeAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new UserImpl("13dsa3", "Mariusz", "Krok", "", "", "", "Chemist", "", "", "", ""), absenceFrom, absenceTo, "Źle się czuje, pracuję z domu"));
        absenceList.add(new AbsenceImpl(new UserImpl("1sa243", "Jacek", "Wieczorek", "", "", "", "COO", "", "", "", ""), absenceFrom, absenceTo, "Córka chora, musiałem zostać w domu"));
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Chory, dostępny po tel."));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo, "Jeżdżę po urzędach"));
        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }

    @Override
    public void requestForHolidayAbsenceEmpolyees() {
        List<Absence> absenceList = new ArrayList<>();
        Date absenceFrom = Calendar.getInstance().getTime();
        Calendar absenceToCalendar = Calendar.getInstance();
        absenceToCalendar.add(Calendar.DAY_OF_MONTH, 2);
        Date absenceTo = absenceToCalendar.getTime();
        absenceList.add(new AbsenceImpl(new UserImpl("121233", "Mieszko", "Stelmach", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Wakacje w Ugandzie, brak dostępu do telefonu i internetu"));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Pod telefonem"));
        absenceList.add(new AbsenceImpl(new UserImpl("14das3", "Paweł", "Urbanowicz", "", "", "", "iOS Developer", "", "", "", ""), absenceFrom, absenceTo, "Brak dostępu do internetu"));
        absenceList.add(new AbsenceImpl(new UserImpl("12sad3", "Łukasz", "Ciupa", "", "", "", "Android Developer", "", "", "", ""), absenceFrom, absenceTo, "Tel. tylko roaming"));
        absenceList.add(new AbsenceImpl(new UserImpl("15saa3", "Tomasz", "Konieczny", "", "", "", "Team Leader", "", "", "", ""), absenceFrom, absenceTo, "Za granicą"));
        apiCallback.onAbsenceEmployeesListReceived(absenceList);
    }
}

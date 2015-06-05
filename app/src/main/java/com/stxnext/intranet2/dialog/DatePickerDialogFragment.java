package com.stxnext.intranet2.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * Created by Tomasz Konieczny on 2015-05-29.
 */
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String ARG_TYPE = "type";
    private static final String ARG_DAY = "day";
    private static final String ARG_MONTH = "month";
    private static final String ARG_YEAR = "year";
    private static final String ARG_MIN_DATE = "min_date";

    public static final int DATE_TYPE_FROM = 0;
    public static final int DATE_TYPE_TO = 1;

    private OnDatePickListener listener;

    private int type = 0;

    public static void show(FragmentManager fragmentManager, int dayOfMonth, int month, int year, int type, long minDate) {
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_DAY, dayOfMonth);
        bundle.putInt(ARG_MONTH, month);
        bundle.putInt(ARG_YEAR, year);
        bundle.putInt(ARG_TYPE, type);
        bundle.putLong(ARG_MIN_DATE, minDate);

        fragment.setArguments(bundle);

        fragment.show(fragmentManager, "dialog_date_picker");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            listener = (OnDatePickListener) getActivity();

            Bundle arguments = getArguments();
            if (arguments != null) {
                this.type = arguments.getInt(ARG_TYPE);
                int dayOfMonth = arguments.getInt(ARG_DAY);
                int month = arguments.getInt(ARG_MONTH);
                int year = arguments.getInt(ARG_YEAR);
                long minDate = arguments.getLong(ARG_MIN_DATE);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }

                datePicker.getDatePicker().setMinDate(minDate);
                return datePicker;
            } else {
                throw new RuntimeException("No arguments set");
            }

        } catch (ClassCastException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        listener.onDatePicked(dayOfMonth, monthOfYear, year, type);
    }

    public interface OnDatePickListener {

        void onDatePicked(int dayOfMonth, int month, int year, int type);

    }

}

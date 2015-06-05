package com.stxnext.intranet2.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * Created by Tomasz Konieczny on 2015-05-29.
 */
public class TimePickerDialogFragment extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    public static final int TIME_FROM = 0;
    public static final int TIME_TO = 1;

    private static final String ARG_MINUTE = "minute";
    private static final String ARG_HOUR = "hour";
    private static final String ARG_TYPE = "type";

    private int type;
    private OnTimeSetListener listener;

    public static void show(FragmentManager fragmentManager, int minute, int hour, int type) {
        TimePickerDialogFragment timePickerDialogFragment = new TimePickerDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_MINUTE, minute);
        bundle.putInt(ARG_HOUR, hour);
        bundle.putInt(ARG_TYPE, type);

        timePickerDialogFragment.setArguments(bundle);

        timePickerDialogFragment.show(fragmentManager, "time_picker");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (OnTimeSetListener) getActivity();
        } catch (ClassCastException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            this.type = getArguments().getInt(ARG_TYPE);
            int hour = getArguments().getInt(ARG_MINUTE);
            int minute = getArguments().getInt(ARG_HOUR);

            TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }

            return dialog;
        } else {
            throw new RuntimeException("This implementation needs minute and hour values!");
        }

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.onTimeSet(hourOfDay, minute, type);
    }

    public interface OnTimeSetListener {

        void onTimeSet(int hour, int minute, int type);

    }

}

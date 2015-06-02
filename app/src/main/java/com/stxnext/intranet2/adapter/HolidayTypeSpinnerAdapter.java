package com.stxnext.intranet2.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.stxnext.intranet2.model.HolidayTypes;

/**
 * Created by Tomasz Konieczny on 2015-06-02.
 */
public class HolidayTypeSpinnerAdapter extends ArrayAdapter<HolidayTypes> {

    public HolidayTypeSpinnerAdapter(Context context) {
        super(context, -1, HolidayTypes.values());
    }
}

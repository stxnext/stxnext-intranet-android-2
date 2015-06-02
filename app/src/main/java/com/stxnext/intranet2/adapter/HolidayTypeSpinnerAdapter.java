package com.stxnext.intranet2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.model.HolidayTypes;

/**
 * Created by Tomasz Konieczny on 2015-06-02.
 */
public class HolidayTypeSpinnerAdapter extends ArrayAdapter<HolidayTypes> {

    public HolidayTypeSpinnerAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item, HolidayTypes.values());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_item, null);
        }

        Integer nameRes = getItem(position).getResourceId();
        ((TextView) convertView).setText(nameRes);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getDropDownView(position, convertView, parent);
    }
}

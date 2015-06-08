package com.stxnext.intranet2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.model.DrawerMenuItems;

/**
 * Created by Tomasz Konieczny on 2015-05-12.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerMenuItems> {

    public DrawerAdapter(Context context) {
        super(context, -1, DrawerMenuItems.values());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_item_layout, null);
        }

        TextView label = (TextView) convertView.findViewById(R.id.drawer_label);
        label.setText(getItem(position).getTitle());

        return convertView;
    }
}

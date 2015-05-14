package com.stxnext.intranet2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stxnext.intranet2.R;

/**
 * Created by Lukasz Ciupa on 2015-05-14.
 */
public class AbsencesListAdapter extends RecyclerView.Adapter<AbsencesListAdapter.ViewHolder> {

    public AbsencesListAdapter() {

    }

    @Override
    public AbsencesListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.absences_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ViewHolder(View v) {
            super(v);
        }
    }
}

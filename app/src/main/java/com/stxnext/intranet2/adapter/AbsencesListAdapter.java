package com.stxnext.intranet2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;
import com.stxnext.intranet2.model.AbsencesTypes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Lukasz Ciupa on 2015-05-14.
 */
public class AbsencesListAdapter extends RecyclerView.Adapter<AbsencesListAdapter.ViewHolder> {

    private final List<Absence> absences;
    private final AbsencesTypes type;
    private OnItemClickListener listener;
    private Context context;
    private final Calendar today;

    public AbsencesListAdapter(Context context, Set<Absence> absences, AbsencesTypes type, OnItemClickListener listener) {
        this.absences = new ArrayList<>(absences);
        this.listener = listener;
        this.context = context;
        this.type = type;
        this.today = Calendar.getInstance();

        Collections.sort(this.absences, new Comparator<Absence>() {
            @Override
            public int compare(Absence absence, Absence absence2) {
                return absence.getAbsenceFrom().compareTo(absence2.getAbsenceFrom());
            }
        });
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
        return absences.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Absence absence = absences.get(position);
        final User user = absence.getUser();
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(user.getId());
            }
        });

        holder.nameTextView.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        holder.descriptionTextView.setText(absence.getDescription());

        Calendar dateFrom = Calendar.getInstance();
        dateFrom.setTime(absence.getAbsenceFrom());

        Date dateTo = absence.getAbsenceTo();
        CharSequence dateFromValue = "";
        CharSequence dateToValue = "";
        switch (type) {
            case HOLIDAY:
                dateFromValue = DateFormat.format("dd.MM", dateFrom);
                dateToValue = DateFormat.format("dd.MM", dateTo);
                break;
            case OUT_OF_OFFICE:
            case WORK_FROM_HOME:
                dateFromValue = DateFormat.format("kk:mm", dateFrom);
                dateToValue = DateFormat.format("kk:mm", dateTo);

                boolean isNextDay = dateFrom.get(Calendar.DAY_OF_MONTH) != today.get(Calendar.DAY_OF_MONTH);
                if (isNextDay) {
                    holder.tomorrowMarker.setVisibility(View.VISIBLE);
                } else {
                    holder.tomorrowMarker.setVisibility(View.GONE);
                }
                break;
        }

        holder.fromLabel.setText(dateFromValue);
        holder.toLabel.setText(dateToValue);

        String imageAddress = "https://intranet.stxnext.pl" + user.getPhoto();
        Picasso.with(context).load(imageAddress).placeholder(R.drawable.avatar_placeholder).fit().into(holder.avatarImageView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View container;
        public ImageView avatarImageView;
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView fromLabel;
        public TextView toLabel;
        public View tomorrowMarker;

        public ViewHolder(View view) {
            super(view);
            this.container = view.findViewById(R.id.item_container);
            this.avatarImageView = (ImageView) view.findViewById(R.id.user_avatar);
            this.nameTextView = (TextView) view.findViewById(R.id.user_name_text_view);
            this.descriptionTextView = (TextView) view.findViewById(R.id.user_desc_text_view);
            this.fromLabel = (TextView) view.findViewById(R.id.from_label);
            this.toLabel = (TextView) view.findViewById(R.id.to_label);
            this.tomorrowMarker = (TextView) view.findViewById(R.id.tomorrow_marker);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(String userId);

    }
}

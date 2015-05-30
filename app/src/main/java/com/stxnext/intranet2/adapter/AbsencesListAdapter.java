package com.stxnext.intranet2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;

import java.util.List;

/**
 * Created by Lukasz Ciupa on 2015-05-14.
 */
public class AbsencesListAdapter extends RecyclerView.Adapter<AbsencesListAdapter.ViewHolder> {

    private final List<Absence> absences;
    private OnItemClickListener listener;
    private Context context;

    public AbsencesListAdapter(Context context, List<Absence> absences, OnItemClickListener listener) {
        this.absences = absences;
        this.listener = listener;
        this.context = context;
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
        String imageAddress = "https://intranet.stxnext.pl" + user.getPhoto();
        Picasso.with(context).load(imageAddress).placeholder(R.drawable.avatar_placeholder).into(holder.avatarImageView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View container;
        public ImageView avatarImageView;
        public TextView nameTextView;
        public TextView descriptionTextView;

        public ViewHolder(View view) {
            super(view);
            this.container = view.findViewById(R.id.item_container);
            this.avatarImageView = (ImageView) view.findViewById(R.id.user_avatar);
            this.nameTextView = (TextView) view.findViewById(R.id.user_name_text_view);
            this.descriptionTextView = (TextView) view.findViewById(R.id.user_desc_text_view);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(String userId);

    }
}

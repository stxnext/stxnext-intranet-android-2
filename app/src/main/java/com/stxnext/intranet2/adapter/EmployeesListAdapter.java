package com.stxnext.intranet2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.model.User;

import java.util.List;

/**
 * Created by OGIT on 2015-05-13.
 */
public class EmployeesListAdapter extends RecyclerView.Adapter<EmployeesListAdapter.ViewHolder> {

    private final List<User> users;
    private OnItemClickListener listener;

    public EmployeesListAdapter(List<User> users, OnItemClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @Override
    public EmployeesListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.employees_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = users.get(position);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(user.getId());
            }
        });

        holder.nameTextView.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        holder.roleTextView.setText(user.getRole());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View container;
        public ImageView avatarImageView;
        public TextView nameTextView;
        public TextView roleTextView;

        public ViewHolder(View view) {
            super(view);
            this.container = view.findViewById(R.id.item_container);
            this.avatarImageView = (ImageView) view.findViewById(R.id.user_avatar);
            this.nameTextView = (TextView) view.findViewById(R.id.user_name_text_view);
            this.roleTextView = (TextView) view.findViewById(R.id.user_role_text_view);
        }

    }

    public interface OnItemClickListener {

        void onItemClick(String userId);

    }

}

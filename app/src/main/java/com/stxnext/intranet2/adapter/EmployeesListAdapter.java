package com.stxnext.intranet2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by OGIT on 2015-05-13.
 */
public class EmployeesListAdapter extends RecyclerView.Adapter<EmployeesListAdapter.ViewHolder> implements Filterable {

    private final List<User> users;
    private List<User> filteredUsers;
    private OnItemClickListener listener;
    private Context context;
    private Filter filter;
    private Map<Long, List<Team>> userToTeamsMap;

    public EmployeesListAdapter(Context context, List<User> users, Map<Long, List<Team>> userToTeamsMap, OnItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.filteredUsers = users;
        this.listener = listener;
        this.userToTeamsMap = userToTeamsMap;
        this.filter = prepareFilter();
    }

    private Filter prepareFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<User> filteredUsers = new ArrayList<>();
                String filterText = constraint.toString().toLowerCase().trim();
                for (User user : users) {
                    String firstName = user.getFirstName() != null && !user.getFirstName().isEmpty() ? user.getFirstName().toLowerCase().trim() : "";
                    String lastName = user.getLastName() != null && !user.getLastName().isEmpty() ? user.getLastName().toLowerCase().trim()  : "";
                    String phoneNumber = user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty() ? user.getPhoneNumber().replaceAll(" ", "").replace("-", "")  : "";
                    List<Team> userTeams = userToTeamsMap.get(Long.parseLong(user.getId()));
                    if (firstName.contains(filterText)
                            || lastName.contains(filterText)
                            || phoneNumber.contains(filterText.replaceAll(" ", ""))
                            || isTeamInSearch(userTeams, filterText)) {
                        filteredUsers.add(user);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredUsers;
                results.count = filteredUsers.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredUsers = (List<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private boolean isTeamInSearch(List<Team> userTeams, String filterText) {
        if (userTeams != null) {
            for (Team team : userTeams) {
                if (team.getName() != null && team.getName().toLowerCase().trim().contains(filterText))
                    return true;
            }
        }
        return false;
    }

    @Override
    public EmployeesListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.employees_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return filteredUsers.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = filteredUsers.get(position);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(user.getId());
            }
        });

        holder.nameTextView.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        holder.roleTextView.setText(user.getRoles() != null && user.getRoles().size() > 0 ? user.getRoles().get(0) : "");
        String imageAddress = "https://intranet-staging.stxnext.pl" + user.getPhoto();
        Picasso.with(context).load(imageAddress).placeholder(R.drawable.avatar_placeholder).into(holder.avatarImageView);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void restore() {
        this.filteredUsers = users;
        notifyDataSetChanged();
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

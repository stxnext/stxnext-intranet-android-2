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
import com.stxnext.intranet2.backend.model.team.Team;

import java.util.List;

/**
 * Created by Łukasz Ciupa on 24.11.2015.
 */
public class TeamsListAdapter extends RecyclerView.Adapter<TeamsListAdapter.ViewHolder> {

    private List<Team> teams;
    private OnItemClickListener listener;
    private Context context;

    public TeamsListAdapter(Context context, List<Team> teams, OnItemClickListener listener) {
        this.context = context;
        this.teams = teams;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teams_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Team team = teams.get(position);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(team.getId());
            }
        });
        holder.nameTextView.setText(team.getName());
        String imageAddress = "https://intranet.stxnext.pl" + team.getImg();
        Picasso.with(context).load(imageAddress).into(holder.teamImageView);
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View container;
        public ImageView teamImageView;
        public TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            this.container = view.findViewById(R.id.item_container);
            this.teamImageView = (ImageView) view.findViewById(R.id.team_image);
            this.nameTextView = (TextView) view.findViewById(R.id.team_name_text_view);
        }

    }

    public interface OnItemClickListener {

        void onItemClick(Long teamId);
    }

}

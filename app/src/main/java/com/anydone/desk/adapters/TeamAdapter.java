package com.anydone.desk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anydone.desk.R;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.utils.GlobalUtils;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamHolder> {
    private static final String TAG = "TeamAdapter";
    private List<Tags> tagsList;
    private Context mContext;
    private OnItemClickListener listener;

    public TeamAdapter(List<Tags> tagsList, Context mContext) {
        this.tagsList = tagsList;
        this.mContext = mContext;
    }

    public void setData(List<Tags> tagsList) {
        this.tagsList = tagsList;
    }

    @NonNull
    @Override
    public TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_team_row, parent, false);
        return new TeamHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamHolder holder, int position) {
        Tags tags = tagsList.get(position);
        holder.tvTeam.setText(tags.getLabel());
    }

    @Override
    public int getItemCount() {
        if (tagsList != null) {
            return tagsList.size();
        } else return 0;
    }


    class TeamHolder extends RecyclerView.ViewHolder {
        private TextView tvTeam;
        private RelativeLayout llTeamHolder;

        TeamHolder(@NonNull View itemView) {
            super(itemView);
            tvTeam = itemView.findViewById(R.id.tv_tag);
            llTeamHolder = itemView.findViewById(R.id.ll_team_holder);

            llTeamHolder.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(tagsList.get(position));
                }

            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Tags tag);
    }

    public void setOnItemClickListener(TeamAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}


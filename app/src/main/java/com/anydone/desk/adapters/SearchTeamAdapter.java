package com.anydone.desk.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.anydone.desk.R;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchTeamAdapter extends RecyclerView.Adapter<SearchTeamAdapter.TeamHolder>
        implements Filterable {
    private static final String TAG = "SearchTeamAdapter";
    private List<Tags> tagsList;
    private Context mContext;
    private OnItemClickListener listener;
    private List<Tags> tagsListFiltered;
    private boolean hasCheckedItems = false;
    private List<String> selectedTeams = new ArrayList<>();

    public SearchTeamAdapter(List<Tags> tagsList, Context mContext) {
        this.tagsList = tagsList;
        this.mContext = mContext;
        this.tagsListFiltered = tagsList;
    }

    public void setData(List<String> selectedTeams) {
        hasCheckedItems = true;
        this.selectedTeams = selectedTeams;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_team_row, parent, false);
        return new TeamHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamHolder holder, int position) {
        Tags tags = tagsListFiltered.get(position);

        holder.tvEmployeeName.setText(tags.getLabel());

        if (hasCheckedItems) {
            holder.cbCheck.setChecked(false);
            for (String tagId : selectedTeams
            ) {
                if (tagId.equalsIgnoreCase(tags.getTagId())) {
                    holder.cbCheck.setChecked(true);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (tagsListFiltered != null) {
            return tagsListFiltered.size();
        } else return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                ((Activity) mContext).runOnUiThread(() -> {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        tagsListFiltered = tagsList;
                    } else {
                        List<Tags> filteredList = new ArrayList<>();
                        if (!tagsList.isEmpty()) {
                            for (Tags row : tagsList) {
                                if (row.getLabel().toLowerCase()
                                        .contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                            }
                        }
                        tagsListFiltered = filteredList;
                    }

                    filterResults.values = tagsListFiltered;
                    filterResults.count = tagsListFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tagsListFiltered = (List<Tags>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class TeamHolder extends RecyclerView.ViewHolder {
        private TextView tvEmployeeName;
        private AppCompatCheckBox cbCheck;

        TeamHolder(@NonNull View itemView) {
            super(itemView);
            RelativeLayout rlTeamHolder = itemView.findViewById(R.id.rl_team_holder);
            tvEmployeeName = itemView.findViewById(R.id.tv_tag_name);
            cbCheck = itemView.findViewById(R.id.cb_check);

            rlTeamHolder.setOnClickListener(view -> {
                if (cbCheck.isChecked()) {
                    cbCheck.setChecked(false);
                } else {
                    cbCheck.setChecked(true);
                }

                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    if (cbCheck.isChecked()) {
                        listener.onItemAdd(tagsListFiltered.get(position));
                    } else {
                        listener.onItemRemove(tagsListFiltered.get(position));
                    }
                }

            });
        }
    }

    public interface OnItemClickListener {
        void onItemAdd(Tags tags);

        void onItemRemove(Tags tags);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
package com.treeleaf.anydone.serviceprovider.adapters;

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

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchLabelAdapter extends RecyclerView.Adapter<SearchLabelAdapter.LabelHolder>
        implements Filterable {
    private static final String TAG = "SearchLabelAdapter";
    private List<Label> labelList;
    private Context mContext;
    private OnItemClickListener listener;
    private OnFilterListEmptyListener filterListEmptyListener;
    private List<Label> labelListFiltered;
    private boolean hasCheckedItems = false;
    private List<Label> selectedLabels = new ArrayList<>();
    private boolean newLabelAdded = false;

    public SearchLabelAdapter(List<Label> labelsList, Context mContext) {
        this.labelList = labelsList;
        this.mContext = mContext;
        this.labelListFiltered = labelsList;
    }

    public void setData(List<Label> selectedLabels) {
        hasCheckedItems = true;
        this.selectedLabels = selectedLabels;
        notifyDataSetChanged();
    }

    public void setNewData(List<Label> labelList) {
        newLabelAdded = true;
        this.labelList = labelList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LabelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_team_row, parent, false);
        return new LabelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelHolder holder, int position) {
        Label label = labelListFiltered.get(position);

        holder.tvLabel.setText(label.getName());

        if (hasCheckedItems) {
            holder.cbCheck.setChecked(false);
            for (Label label1 : selectedLabels
            ) {
                if (label1.getName() != null)
                    if (label1.getName().equalsIgnoreCase(label.getName())) {
                        holder.cbCheck.setChecked(true);
                    }
            }
        }

        if (newLabelAdded) {
            if (label.getLabelId() == null || label.getLabelId().isEmpty()) {
                holder.cbCheck.setChecked(true);
                newLabelAdded = false;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (labelListFiltered != null) {
            return labelListFiltered.size();
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
                        labelListFiltered = labelList;
                    } else {
                        List<Label> filteredList = new ArrayList<>();
                        for (Label row : labelList) {
                            if (row.getName().toLowerCase()
                                    .contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        labelListFiltered = filteredList;
                    }

                    filterResults.values = labelListFiltered;
                    filterResults.count = labelListFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                labelListFiltered = (List<Label>) filterResults.values;
                notifyDataSetChanged();
                if (labelListFiltered.isEmpty())
                    if (filterListEmptyListener != null) {
                        filterListEmptyListener.showNewLabel();
                    }
            }
        };
    }


    class LabelHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;
        private AppCompatCheckBox cbCheck;

        LabelHolder(@NonNull View itemView) {
            super(itemView);
            RelativeLayout rlLabelHolder = itemView.findViewById(R.id.rl_team_holder);
            tvLabel = itemView.findViewById(R.id.tv_tag_name);
            cbCheck = itemView.findViewById(R.id.cb_check);

            rlLabelHolder.setOnClickListener(view -> {
                if (cbCheck.isChecked()) {
                    cbCheck.setChecked(false);
                } else {
                    cbCheck.setChecked(true);
                }

                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    if (cbCheck.isChecked()) {
                        listener.onItemAdd(labelListFiltered.get(position));
                    } else {
                        listener.onItemRemove(labelListFiltered.get(position));
                    }
                }

            });
        }
    }

    public interface OnItemClickListener {
        void onItemAdd(Label tags);

        void onItemRemove(Label tags);
    }

    public interface OnFilterListEmptyListener {
        void showNewLabel();
    }
    public void setOnFilterListEmptyListener(OnFilterListEmptyListener listener) {
        this.filterListEmptyListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}

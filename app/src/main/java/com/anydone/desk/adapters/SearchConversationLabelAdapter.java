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
import com.anydone.desk.realm.model.ConversationThreadLabel;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchConversationLabelAdapter extends RecyclerView.Adapter<SearchConversationLabelAdapter.LabelHolder>
        implements Filterable {
    private static final String TAG = "SearchConversationLabel";
    private List<ConversationThreadLabel> labelList;
    private Context mContext;
    private OnItemClickListener listener;
    private List<ConversationThreadLabel> labelsFiltered;
    private List<String> selected = new ArrayList<>();
    private boolean hasCheckedItems = false;


    public SearchConversationLabelAdapter(List<ConversationThreadLabel> labelList, Context mContext) {
        this.labelList = labelList;
        this.mContext = mContext;
        this.labelsFiltered = labelList;
    }

    public void setData(List<String> selected) {
        hasCheckedItems = true;
        this.selected = selected;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LabelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_conversation_label_row, parent, false);
        return new LabelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelHolder holder, int position) {
        ConversationThreadLabel label = labelsFiltered.get(position);
        holder.tvLabelName.setText(label.getName());

        if (hasCheckedItems) {
            holder.cbCheck.setChecked(false);
            for (String labelId : selected
            ) {
                if (labelId.equalsIgnoreCase(label.getLabelId())) {
                    holder.cbCheck.setChecked(true);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        if (labelsFiltered != null) {
            return labelsFiltered.size();
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
                        labelsFiltered = labelList;
                    } else {
                        List<ConversationThreadLabel> filteredList = new ArrayList<>();
                        for (ConversationThreadLabel row : labelList) {
                            if (row.getName().toLowerCase()
                                    .contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        labelsFiltered = filteredList;
                    }


                    filterResults.values = labelsFiltered;
                    filterResults.count = labelsFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                labelsFiltered = (List<ConversationThreadLabel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class LabelHolder extends RecyclerView.ViewHolder {
        private TextView tvLabelName;
        private AppCompatCheckBox cbCheck;

        LabelHolder(@NonNull View itemView) {
            super(itemView);
            RelativeLayout rlLabelHolder = itemView.findViewById(R.id.rl_label_holder);
            tvLabelName = itemView.findViewById(R.id.tv_label_name);
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
                    GlobalUtils.showLog(TAG, "click listened");
                    if (cbCheck.isChecked()) {
                        GlobalUtils.showLog(TAG, "check listened");
                        listener.onItemAdd(labelsFiltered.get(position).getLabelId());
                    } else {
                        GlobalUtils.showLog(TAG, "uncheck listened");
                        listener.onItemRemove(labelsFiltered.get(position).getLabelId());
                    }
                }
            });

        }
    }


    public interface OnItemClickListener {
        void onItemAdd(String labelId);

        void onItemRemove(String labelId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}


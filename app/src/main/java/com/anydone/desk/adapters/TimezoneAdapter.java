package com.anydone.desk.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anydone.desk.R;
import com.anydone.desk.model.TimezoneResult;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

public class TimezoneAdapter extends RecyclerView.Adapter<TimezoneAdapter.TimezoneHolder> implements
        Filterable {
    private static final String TAG = "BrowseServiceAdapter";
    private List<TimezoneResult> timezoneList;
    private List<TimezoneResult> timezoneListFiltered;
    private Context mContext;
    private OnItemClickListener listener;

    public TimezoneAdapter(List<TimezoneResult> timezoneList, Context mContext) {
        this.timezoneList = timezoneList;
        this.mContext = mContext;
        this.timezoneListFiltered = timezoneList;
    }

    @NonNull
    @Override
    public TimezoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_timezone_row, parent, false);
        return new TimezoneHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimezoneHolder holder, int position) {
        TimezoneResult timezoneResult = timezoneListFiltered.get(position);

        holder.tvZone.setText(timezoneResult.getZone());
        holder.tvTimezone.setText(timezoneResult.getTimezone());

    }

    @Override
    public int getItemCount() {
        return timezoneListFiltered.size();
    }


    class TimezoneHolder extends RecyclerView.ViewHolder {
        private TextView tvZone;
        private TextView tvTimezone;
        private RelativeLayout rlContainer;

        TimezoneHolder(@NonNull View itemView) {
            super(itemView);
            tvZone = itemView.findViewById(R.id.tv_zone);
            tvTimezone = itemView.findViewById(R.id.tv_timezone);
            rlContainer = itemView.findViewById(R.id.rl_timezone_holder);

            rlContainer.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(timezoneListFiltered.get(position));
                }

            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(TimezoneResult timezoneResult);
    }

    public void setOnItemClickListener(TimezoneAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    timezoneListFiltered = timezoneList;
                } else {
                    List<TimezoneResult> filteredList = new ArrayList<>();
                    for (TimezoneResult row : timezoneList) {
                        if (row.getZone().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    timezoneListFiltered = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = timezoneListFiltered;
                return filterResults;

            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                timezoneListFiltered = (ArrayList<TimezoneResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

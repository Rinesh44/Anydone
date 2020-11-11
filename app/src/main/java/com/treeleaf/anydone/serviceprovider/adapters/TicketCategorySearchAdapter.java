package com.treeleaf.anydone.serviceprovider.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

public class TicketCategorySearchAdapter extends RecyclerView.Adapter<TicketCategorySearchAdapter.TicketCategoryHolder>
        implements Filterable {
    private static final String TAG = "TicketCategorySearchAda";
    private List<TicketCategory> ticketTypeList;
    private List<TicketCategory> ticketTypeListFiltered;
    private Context mContext;
    private OnItemClickListener listener;

    public TicketCategorySearchAdapter(@NonNull Context context,
                                       @NonNull List<TicketCategory> ticketTypeList) {
        mContext = context;
        this.ticketTypeList = ticketTypeList;
        this.ticketTypeListFiltered = ticketTypeList;
    }

    public void setData(List<TicketCategory> ticketTypeList) {
        this.ticketTypeList = ticketTypeList;
    }


    @NonNull
    @Override
    public TicketCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_category_search_row, parent, false);
        return new TicketCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketCategoryHolder holder, int position) {
        TicketCategory category = ticketTypeListFiltered.get(position);
        holder.tvTicketCategory.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        if (ticketTypeListFiltered != null) {
            return ticketTypeListFiltered.size();
        } else return 0;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return ticketTypeFilter;
    }

    private Filter ticketTypeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ((Activity) mContext).runOnUiThread(() -> {
                List<TicketCategory> suggestions = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(ticketTypeList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (TicketCategory item : ticketTypeList) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            suggestions.add(item);
                        }
                    }
                }
                results.values = suggestions;
                results.count = suggestions.size();
            });
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ticketTypeListFiltered = (List<TicketCategory>) results.values;
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((TicketCategory) resultValue).getName();
        }
    };


    class TicketCategoryHolder extends RecyclerView.ViewHolder {
        private TextView tvTicketCategory;

        TicketCategoryHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketCategory = itemView.findViewById(R.id.tv_ticket_type);

            tvTicketCategory.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTicketTypeClick(ticketTypeListFiltered.get(position));
                }

            });
        }
    }

    public interface OnItemClickListener {
        void onTicketTypeClick(TicketCategory category);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}


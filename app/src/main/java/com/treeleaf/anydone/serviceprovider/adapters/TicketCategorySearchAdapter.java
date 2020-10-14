package com.treeleaf.anydone.serviceprovider.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;

import java.util.ArrayList;
import java.util.List;

public class TicketCategorySearchAdapter extends ArrayAdapter<TicketCategory> {
    private static final String TAG = "TicketCategorySearchAda";
    private List<TicketCategory> ticketTypeListFull;
    private Context mContext;

    public TicketCategorySearchAdapter(@NonNull Context context,
                                       @NonNull List<TicketCategory> ticketTypeList) {
        super(context, 0, ticketTypeList);
        mContext = context;
        ticketTypeListFull = new ArrayList<>(ticketTypeList);
    }

    public void setData(List<TicketCategory> ticketTypeList) {
        this.ticketTypeListFull = ticketTypeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.ticket_category_search_row, parent, false
            );
        }
        TextView tvTicketType = convertView.findViewById(R.id.tv_ticket_type);
        TicketCategory item = getItem(position);
        if (item != null) {
            tvTicketType.setText(item.getName());
        }
        return convertView;
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
                    suggestions.addAll(ticketTypeListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (TicketCategory item : ticketTypeListFull) {
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
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((TicketCategory) resultValue).getName();
        }
    };

}


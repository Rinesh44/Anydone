package com.anydone.desk.adapters;

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

import com.anydone.desk.R;
import com.anydone.desk.realm.model.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceFilterAdapter extends ArrayAdapter<Service> {
    private List<Service> serviceListFull;
    private Context mContext;

    public ServiceFilterAdapter(@NonNull Context context,
                                @NonNull List<Service> serviceList
    ) {
        super(context, 0, serviceList);
        mContext = context;
        serviceListFull = new ArrayList<>(serviceList);
    }

    public void setData(List<Service> serviceList) {
        this.serviceListFull = serviceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_ticket_type_row, parent, false
            );
        }

        TextView tvTag = convertView.findViewById(R.id.tv_ticket_type);
        Service serviceItem = getItem(position);
        if (serviceItem != null) {
            tvTag.setText(serviceItem.getName());
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return serviceFilter;
    }

    private Filter serviceFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ((Activity) mContext).runOnUiThread(() -> {
                List<Service> suggestions = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(serviceListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Service item : serviceListFull) {
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
            return ((Service) resultValue).getName();
        }
    };

}
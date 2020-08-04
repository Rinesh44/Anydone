package com.treeleaf.anydone.serviceprovider.adapters;

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
import com.treeleaf.anydone.serviceprovider.model.FilterObject;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends ArrayAdapter<FilterObject> {
    private static final String TAG = "FilterAdapter";
    private List<FilterObject> filterListFull;

    public FilterAdapter(@NonNull Context context, @NonNull List<FilterObject> filterList) {
        super(context, 0, filterList);
        filterListFull = new ArrayList<>(filterList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return serviceFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_search_row, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.search_result);
        FilterObject filterItem = getItem(position);
        if (filterItem != null) {
            textViewName.setText(filterItem.getFilterName());
        }
        return convertView;
    }

    private Filter serviceFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<FilterObject> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(filterListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (FilterObject item : filterListFull) {
                    if (item.getFilterName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
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
            return ((FilterObject) resultValue).getFilterName();
        }
    };
}


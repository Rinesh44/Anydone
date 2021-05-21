package com.anydone.desk.adapters;

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
import com.anydone.desk.model.AutocompleteLocation;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteLocationAdapter extends ArrayAdapter<AutocompleteLocation> {
    private List<AutocompleteLocation> locationListFull;

    public AutocompleteLocationAdapter(@NonNull Context context,
                                       @NonNull List<AutocompleteLocation> locationList) {
        super(context, 0, locationList);
        locationListFull = new ArrayList<>(locationList);
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return locationFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.location_autocomplete_row, parent, false
            );
        }
        TextView locationPrimary = convertView.findViewById(R.id.tv_primary);
        TextView locationSecondary = convertView.findViewById(R.id.tv_secondary);
        AutocompleteLocation locationItem = getItem(position);
        if (locationItem != null) {
            locationPrimary.setText(locationItem.getPrimary());
            locationSecondary.setText(locationItem.getSecondary());
        }
        return convertView;
    }

    private Filter locationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<AutocompleteLocation> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(locationListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AutocompleteLocation item : locationListFull) {
                    if (item.getPrimary().toLowerCase().contains(filterPattern)) {
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
            return ((AutocompleteLocation) resultValue).getPrimary();
        }
    };


}



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
import com.anydone.desk.model.AutocompleteResult;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteAdapter extends ArrayAdapter<AutocompleteResult> {
    private List<AutocompleteResult> resultListFull;

    public AutocompleteAdapter(@NonNull Context context,
                               @NonNull List<AutocompleteResult> resultList) {
        super(context, 0, resultList);
        resultListFull = new ArrayList<>(resultList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return resultFilter;
    }

    public void setData(List<AutocompleteResult> locationList) {
        this.resultListFull = locationList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_search_row, parent, false);
        }
        TextView textViewName = convertView.findViewById(R.id.search_result);
        AutocompleteResult resultItem = getItem(position);
        if (resultItem != null) {
            textViewName.setText(resultItem.getResult());
        }
        return convertView;
    }

    private final Filter resultFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<AutocompleteResult> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(resultListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AutocompleteResult item : resultListFull) {
                    if (item.getResult().toLowerCase().contains(filterPattern)) {
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
            return ((AutocompleteResult) resultValue).getResult();
        }
    };
}


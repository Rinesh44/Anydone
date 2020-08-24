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

import com.treeleaf.anydone.serviceprovider.realm.model.Tags;

import java.util.ArrayList;
import java.util.List;

public class TagSearchAdapter extends ArrayAdapter<Tags> {
    private List<Tags> tagsListFull;
    private Context mContext;

    public TagSearchAdapter(@NonNull Context context,
                            @NonNull List<Tags> tagsList) {
        super(context, 0, tagsList);
        mContext = context;
        tagsListFull = new ArrayList<>(tagsList);
    }

    public void setData(List<Tags> tagsList) {
        this.tagsListFull = tagsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    android.R.layout.simple_list_item_1, parent, false
            );
        }
        TextView tvTag = convertView.findViewById(android.R.id.text1);
        Tags tagItem = getItem(position);
        if (tagItem != null) {
            tvTag.setText(tagItem.getLabel());
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return tagFilter;
    }

    private Filter tagFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ((Activity) mContext).runOnUiThread(() -> {
                List<Tags> suggestions = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(tagsListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Tags item : tagsListFull) {
                        if (item.getLabel().toLowerCase().contains(filterPattern)) {
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
            return ((Tags) resultValue).getLabel();
        }
    };

}



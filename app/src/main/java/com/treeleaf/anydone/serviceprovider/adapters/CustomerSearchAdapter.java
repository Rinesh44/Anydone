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

import java.util.ArrayList;
import java.util.List;

public class CustomerSearchAdapter extends ArrayAdapter<Customer> {
    private static final String TAG = "CustomerSearchAdapter";
    private List<Customer> customerListFull;
    private Context mContext;

    public CustomerSearchAdapter(@NonNull Context context,
                                 @NonNull List<Customer> customerList) {
        super(context, 0, customerList);
        mContext = context;
        customerListFull = new ArrayList<>(customerList);
    }

    public void setData(List<Customer> customerList) {
        this.customerListFull = customerList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.customer_search_row, parent, false
            );
        }
        TextView tvCustomer = convertView.findViewById(R.id.tv_customer_details);
        Customer customerItem = getItem(position);
        if (customerItem != null) {
            if (customerItem.getPhone() != null && !customerItem.getPhone().isEmpty()) {
                tvCustomer.setText(customerItem.getFullName() + " (" + customerItem.getPhone() + ") ");
            } else {
                tvCustomer.setText(customerItem.getFullName());
            }
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
                List<Customer> suggestions = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(customerListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Customer item : customerListFull) {
                        if (item.getFullName().toLowerCase().contains(filterPattern) ||
                                item.getPhone().contains(filterPattern)) {
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
            return ((Customer) resultValue).getFullName();
        }
    };

}

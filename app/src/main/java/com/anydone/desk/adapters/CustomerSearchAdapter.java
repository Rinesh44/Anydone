package com.anydone.desk.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.anydone.desk.R;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.Employee;
import com.anydone.desk.realm.model.ServiceProvider;
import com.anydone.desk.realm.repo.EmployeeRepo;
import com.anydone.desk.realm.repo.ServiceProviderRepo;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerSearchAdapter extends RecyclerView.Adapter<CustomerSearchAdapter.CustomerHolder>
        implements Filterable {
    private static final String TAG = "CustomerSearchAdapter";
    private List<Customer> customerList;
    private List<Customer> customerListFiltered;
    private OnFilterListEmptyListener filterListEmptyListener;
    private Context mContext;
    private OnItemClickListener listener;
    private String selectedCustomerId = "";

    public CustomerSearchAdapter(@NonNull Context context,
                                 @NonNull List<Customer> customerList) {
        mContext = context;
        this.customerList = customerList;
        this.customerListFiltered = customerList;
    }

    public void setData(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public void setChecked(String customerId) {
        for (Customer customer : customerListFiltered
        ) {
            if (customerId.equalsIgnoreCase(customer.getCustomerId())) {
                selectedCustomerId = customerId;
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void removeCheckMark() {
        selectedCustomerId = "";
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_search_row, parent, false);
        return new CustomerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        Customer customer = customerListFiltered.get(position);

        Employee employee = EmployeeRepo.getInstance().getEmployee();
        String userId;
        if (employee != null) {
            userId = employee.getAccountId();
        } else {
            ServiceProvider serviceProvider = ServiceProviderRepo.getInstance().getServiceProvider();
            userId = serviceProvider.getAccountId();
        }

        if (userId != null && !customer.getCustomerId().equalsIgnoreCase(userId)) {
            GlobalUtils.showLog(TAG, "customer phone: " + customer.getPhone());
            if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
                holder.tvCustomer.setText(customer.getFullName() + " (" + customer.getPhone() + ") ");
            } else {
                holder.tvCustomer.setText(customer.getFullName());
            }

            if (customer.getProfilePic() != null) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .error(R.drawable.ic_empty_profile_holder_icon);

                Glide.with(mContext).load(customer.getProfilePic())
                        .apply(options).into(holder.ivCustomer);

            }
        }

        if (customer.getCustomerId().equalsIgnoreCase(selectedCustomerId)) {
            holder.ivTick.setVisibility(View.VISIBLE);
        } else holder.ivTick.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (customerListFiltered != null) {
            return customerListFiltered.size();
        } else return 0;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return customerFilter;
    }

    private Filter customerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ((Activity) mContext).runOnUiThread(() -> {
                List<Customer> suggestions = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(customerList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Customer item : customerList) {
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
            customerListFiltered = (List<Customer>) results.values;
            notifyDataSetChanged();
            if (customerListFiltered.isEmpty()) {
                if (filterListEmptyListener != null) {
                    filterListEmptyListener.showNewCustomer();
                }
            } else {
                if (filterListEmptyListener != null) {
                    filterListEmptyListener.hideNewCustomer();
                }
            }

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Customer) resultValue).getFullName();
        }
    };

    class CustomerHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomer;
        private CircleImageView ivCustomer;
        private ImageView ivTick;

        CustomerHolder(@NonNull View itemView) {
            super(itemView);
            LinearLayout llCustomerHolder = itemView.findViewById(R.id.ll_customer_container);
            tvCustomer = itemView.findViewById(R.id.tv_customer);
            ivCustomer = itemView.findViewById(R.id.civ_customer);
            ivTick = itemView.findViewById(R.id.iv_tick);

            llCustomerHolder.setOnClickListener(view -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onCustomerClick(customerListFiltered.get(position));
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onCustomerClick(Customer customer);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnFilterListEmptyListener {
        void showNewCustomer();

        void hideNewCustomer();
    }

    public void setOnFilterListEmptyListener(OnFilterListEmptyListener listener) {
        this.filterListEmptyListener = listener;
    }

}
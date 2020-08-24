package com.treeleaf.anydone.serviceprovider.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchEmployeeAdapter extends RecyclerView.Adapter<SearchEmployeeAdapter.EmployeeHolder>
        implements Filterable {
    private static final String TAG = "SearchEmployeeAdapter";
    private List<Employee> employeeList;
    private Context mContext;
    private OnItemClickListener listener;
    private List<Employee> assignEmployeeFiltered;

    public SearchEmployeeAdapter(List<Employee> employeeList, Context mContext) {
        this.employeeList = employeeList;
        this.mContext = mContext;
        this.assignEmployeeFiltered = employeeList;
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_employee_row, parent, false);
        return new EmployeeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeHolder holder, int position) {
        Employee employee = assignEmployeeFiltered.get(position);

        holder.tvEmployeeName.setText(employee.getName());

        if (employee.getEmployeeImageUrl() != null) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(mContext).load(employee.getEmployeeImageUrl())
                    .apply(options).into(holder.civEmployee);

        }

    }

    @Override
    public int getItemCount() {
        if (assignEmployeeFiltered != null) {
            return assignEmployeeFiltered.size();
        } else return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                ((Activity) mContext).runOnUiThread(() -> {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        assignEmployeeFiltered = employeeList;
                    } else {
                        List<Employee> filteredList = new ArrayList<>();
                        for (Employee row : employeeList) {
                            if (row.getName().toLowerCase()
                                    .contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        assignEmployeeFiltered = filteredList;
                    }


                    filterResults.values = assignEmployeeFiltered;
                    filterResults.count = assignEmployeeFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                assignEmployeeFiltered = (List<Employee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class EmployeeHolder extends RecyclerView.ViewHolder {
        private TextView tvEmployeeName;
        private CircleImageView civEmployee;
        private AppCompatCheckBox cbCheck;

        EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            RelativeLayout rlEmployeeHolder = itemView.findViewById(R.id.rl_employee_holder);
            tvEmployeeName = itemView.findViewById(R.id.tv_employee_name);
            civEmployee = itemView.findViewById(R.id.civ_employee);
            cbCheck = itemView.findViewById(R.id.cb_check);

            rlEmployeeHolder.setOnClickListener(view -> {
                if (cbCheck.isChecked()) {
                    cbCheck.setChecked(false);
                } else {
                    cbCheck.setChecked(true);
                }

                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    if (cbCheck.isChecked()) {
                        listener.onItemAdd(assignEmployeeFiltered.get(position).getEmployeeId());
                    } else {
                        listener.onItemRemove(assignEmployeeFiltered.get(position).getEmployeeId());
                    }
                }

            });
        }
    }


    public interface OnItemClickListener {
        void onItemAdd(String employeeId);

        void onItemRemove(String employeeId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
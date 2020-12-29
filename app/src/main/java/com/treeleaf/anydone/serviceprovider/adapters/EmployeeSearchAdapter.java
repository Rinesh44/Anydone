package com.treeleaf.anydone.serviceprovider.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeSearchAdapter extends RecyclerView.Adapter<EmployeeSearchAdapter.EmployeeHolder>
        implements Filterable {
    private static final String TAG = "EmployeeSearchAdapter";
    private List<AssignEmployee> assignEmployeeList;
    private List<AssignEmployee> assignEmployeeListFiltered;
    private Context mContext;
    private OnItemClickListener listener;
    private String selectedEmployeeId = "";

    public EmployeeSearchAdapter(List<AssignEmployee> assignEmployeeList, Context mContext) {
        this.assignEmployeeList = assignEmployeeList;
        this.assignEmployeeListFiltered = assignEmployeeList;
        this.mContext = mContext;
    }

    public void setData(List<AssignEmployee> assignEmployeeList) {
        this.assignEmployeeList = assignEmployeeList;
    }

    public void setChecked(String employeeId) {
        if (assignEmployeeListFiltered != null && !assignEmployeeListFiltered.isEmpty()) {
            for (AssignEmployee employee : assignEmployeeListFiltered
            ) {
                if (employeeId.equalsIgnoreCase(employee.getEmployeeId())) {
                    selectedEmployeeId = employeeId;
                    break;
                }
            }
            notifyDataSetChanged();
        }
    }

    public void removeCheckMark() {
        selectedEmployeeId = "";
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_search_row, parent, false);
        return new EmployeeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeHolder holder, int position) {
        AssignEmployee assignEmployee = assignEmployeeListFiltered.get(position);

        holder.tvEmployee.setText(assignEmployee.getName());

        String employeePic = assignEmployee.getEmployeeImageUrl();
        if (employeePic != null && !employeePic.isEmpty()) {

            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(mContext).load(employeePic).apply(options).into(holder.ivEmployee);
        }

        if (assignEmployee.getEmployeeId().equalsIgnoreCase(selectedEmployeeId)) {
            holder.ivTick.setVisibility(View.VISIBLE);
            holder.llEmployeeHolder.setClickable(false);
        } else {
            holder.ivTick.setVisibility(View.GONE);
            holder.llEmployeeHolder.setClickable(true);
        }
    }

    @Override
    public int getItemCount() {
        if (assignEmployeeListFiltered != null) {
            return assignEmployeeListFiltered.size();
        } else return 0;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return employeeFilter;
    }

    private Filter employeeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ((Activity) mContext).runOnUiThread(() -> {
                List<AssignEmployee> suggestions = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(assignEmployeeList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (AssignEmployee item : assignEmployeeList) {
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
            assignEmployeeListFiltered = (List<AssignEmployee>) results.values;
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((AssignEmployee) resultValue).getName();
        }
    };


    class EmployeeHolder extends RecyclerView.ViewHolder {
        private TextView tvEmployee;
        private CircleImageView ivEmployee;
        private RelativeLayout llEmployeeHolder;
        private ImageView ivTick;

        EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployee = itemView.findViewById(R.id.tv_employee);
            ivEmployee = itemView.findViewById(R.id.civ_employee);
            llEmployeeHolder = itemView.findViewById(R.id.ll_employee_holder);
            ivTick = itemView.findViewById(R.id.iv_tick);

            llEmployeeHolder.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(assignEmployeeListFiltered.get(position));
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(AssignEmployee employee);
    }

    public void setOnItemClickListener(EmployeeSearchAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}


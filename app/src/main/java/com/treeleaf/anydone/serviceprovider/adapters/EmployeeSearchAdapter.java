package com.treeleaf.anydone.serviceprovider.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeSearchAdapter extends RecyclerView.Adapter<EmployeeSearchAdapter.EmployeeHolder> {
    private static final String TAG = "EmployeeSearchAdapter";
    private List<AssignEmployee> assignEmployeeList;
    private Context mContext;
    private OnItemClickListener listener;

    public EmployeeSearchAdapter(List<AssignEmployee> assignEmployeeList, Context mContext) {
        this.assignEmployeeList = assignEmployeeList;
        this.mContext = mContext;
    }

    public void setData(List<AssignEmployee> assignEmployeeList) {
        this.assignEmployeeList = assignEmployeeList;
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
        AssignEmployee assignEmployee = assignEmployeeList.get(position);

        holder.tvEmployee.setText(assignEmployee.getName());

        String employeePic = assignEmployee.getEmployeeImageUrl();
        if (employeePic != null && !employeePic.isEmpty()) {

            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(mContext).load(employeePic).apply(options).into(holder.ivEmployee);
        }

    }

    @Override
    public int getItemCount() {
        if (assignEmployeeList != null) {
            return assignEmployeeList.size();
        } else return 0;
    }


    class EmployeeHolder extends RecyclerView.ViewHolder {
        private TextView tvEmployee;
        private CircleImageView ivEmployee;
        private RelativeLayout llEmployeeHolder;

        EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployee = itemView.findViewById(R.id.tv_employee);
            ivEmployee = itemView.findViewById(R.id.civ_employee);
            llEmployeeHolder = itemView.findViewById(R.id.ll_employee_holder);

            llEmployeeHolder.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(assignEmployeeList.get(position));
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


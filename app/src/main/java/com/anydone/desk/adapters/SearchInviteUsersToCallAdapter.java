package com.anydone.desk.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
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
import com.anydone.desk.R;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchInviteUsersToCallAdapter extends RecyclerView.Adapter<SearchInviteUsersToCallAdapter.ContributorHolder>
        implements Filterable {
    private static final String TAG = "SearchContributorAdapte";
    private List<AssignEmployee> contributorList;
    private Context mContext;
    private OnItemClickListenerOnCall listener;
    private List<AssignEmployee> contributorsFiltered;
    private SparseBooleanArray mSelectedFlags;
    private List<AssignEmployee> selected = new ArrayList<>();
    private boolean hasCheckedItems = false;


    public SearchInviteUsersToCallAdapter(List<AssignEmployee> contributorList, Context mContext) {
        this.contributorList = contributorList;
        this.mContext = mContext;
        this.contributorsFiltered = contributorList;
        mSelectedFlags = new SparseBooleanArray();
    }

    public void setData(List<AssignEmployee> selected) {
        hasCheckedItems = true;
        this.selected = selected;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContributorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_contributor_row, parent, false);
        return new ContributorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorHolder holder, int position) {
        AssignEmployee employee = contributorsFiltered.get(position);
        holder.tvEmployeeName.setText(employee.getName());

        if (employee.getEmployeeImageUrl() != null) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(mContext).load(employee.getEmployeeImageUrl())
                    .apply(options).into(holder.civEmployee);
        }

        if (hasCheckedItems) {
            holder.cbCheck.setChecked(false);
            for (AssignEmployee employeeId : selected
            ) {
                if (employeeId.getEmployeeId().equalsIgnoreCase(employee.getEmployeeId())) {
                    holder.cbCheck.setChecked(true);
                }
            }
        }

//        holder.cbCheck.setChecked(mSelectedFlags.get(holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        if (contributorsFiltered != null) {
            return contributorsFiltered.size();
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
                        contributorsFiltered = contributorList;
                    } else {
                        List<AssignEmployee> filteredList = new ArrayList<>();
                        for (AssignEmployee row : contributorList) {
                            if (row.getName().toLowerCase()
                                    .contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        contributorsFiltered = filteredList;
                    }


                    filterResults.values = contributorsFiltered;
                    filterResults.count = contributorsFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contributorsFiltered = (List<AssignEmployee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class ContributorHolder extends RecyclerView.ViewHolder {
        private TextView tvEmployeeName;
        private CircleImageView civEmployee;
        private AppCompatCheckBox cbCheck;

        ContributorHolder(@NonNull View itemView) {
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
                    GlobalUtils.showLog(TAG, "click listened");
                    if (cbCheck.isChecked()) {
                        GlobalUtils.showLog(TAG, "check listened");
                        listener.onItemAdd(contributorsFiltered.get(position));
                    } else {
                        GlobalUtils.showLog(TAG, "uncheck listened");
                        listener.onItemRemove(contributorsFiltered.get(position));
                    }
                }
            });

      /*      cbCheck.setOnCheckedChangeListener((buttonView, isChecked) ->
                    mSelectedFlags.put(getAdapterPosition(), isChecked));*/
        }
    }


    public interface OnItemClickListenerOnCall {
        void onItemAdd(AssignEmployee employeeId);

        void onItemRemove(AssignEmployee employeeId);
    }

    public void setOnItemClickListener(OnItemClickListenerOnCall listener) {
        this.listener = listener;
    }
}


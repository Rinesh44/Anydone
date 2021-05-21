package com.anydone.desk.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.anydone.desk.R;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.ActivityLog;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.AssignEmployeeRepo;
import com.anydone.desk.utils.GlobalUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ActivityLogAdapter";
    public static final int DESC = 0;
    public static final int STATUS = 2;
    public static final int TITLE = 4;
    public static final int EMPLOYEE = 6;
    public static final int CONTRIBUTOR = 10;
    public static final int EST_TIME = 11;
    public static final int PRIORITY = 12;
    public static final int LABEL = 13;

    private List<ActivityLog> logList;
    private Context mContext;

    public ActivityLogAdapter(List<ActivityLog> loglist, Context mContext) {
        this.logList = loglist;
        this.mContext = mContext;
    }

    public void setData(List<ActivityLog> logList) {
        this.logList = logList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GlobalUtils.showLog(TAG, "view type check: " + viewType);
        switch (viewType) {
            case DESC:
                View desc = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_desc_change, parent, false);
                return new DescriptionHolder(desc);

            case STATUS:
                View status = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_status_change, parent, false);
                return new StatusHolder(status);

            case TITLE:
                View title = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_title_change, parent, false);
                return new TitleHolder(title);

            case EMPLOYEE:
                View employee = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_emp_change, parent, false);
                return new EmployeeHolder(employee);

            case CONTRIBUTOR:
                View contributor = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_contributor_change, parent, false);
                return new ContributorHolder(contributor);

            case EST_TIME:
                View estTime = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_est_time_change, parent, false);
                return new EstTimeHolder(estTime);

            case PRIORITY:
                View priority = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_priority_change, parent, false);
                return new PriorityHolder(priority);

            case LABEL:
                View label = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_label_change, parent, false);
                return new LabelHolder(label);

            default:
                View defaultView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_desc_change, parent, false);
                return new DescriptionHolder(defaultView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ActivityLog log = logList.get(position);

        switch (holder.getItemViewType()) {
            case DESC:
                ((DescriptionHolder) holder).bind(log);
                break;

            case STATUS:
                ((StatusHolder) holder).bind(log);
                break;

            case TITLE:
                ((TitleHolder) holder).bind(log);
                break;

            case EMPLOYEE:
                ((EmployeeHolder) holder).bind(log);
                break;

            case CONTRIBUTOR:
                ((ContributorHolder) holder).bind(log);
                break;

            case EST_TIME:
                ((EstTimeHolder) holder).bind(log);
                break;

            case PRIORITY:
                ((PriorityHolder) holder).bind(log);
                break;

            case LABEL:
                ((LabelHolder) holder).bind(log);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return logList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ActivityLog log = logList.get(position);
        Account account = AccountRepo.getInstance().getAccount();

        GlobalUtils.showLog(TAG, "accontId: " + account.getAccountId());
        GlobalUtils.showLog(TAG, "message type check:" + log.getActivityType());
        switch (log.getActivityType()) {
            case "DESCRIPTION_CHANGED":
                return DESC;

            case "STATUS_CHANGED":
                return STATUS;

            case "TITLE_CHANGED":
                return TITLE;

            case "EMPLOYEE_CHANGED":
                return EMPLOYEE;

            case "CONTRIBUTER_ADDED":

            case "CONTRIBUTER_REMOVED":
                return CONTRIBUTOR;

            case "ESTIMATED_TIME_CHANGED":
                return EST_TIME;

            case "PRIORITY_CHANGED":
                return PRIORITY;

            case "LABEL_CHANGED":
                return LABEL;

        }

        return -1;
    }

    class DescriptionHolder extends RecyclerView.ViewHolder {
        private CircleImageView civAccount;
        private TextView tvAccountName;
        private TextView tvAddRemove;
        private TextView tvDesc;
        private TextView tvDate;

        DescriptionHolder(@NonNull View itemView) {
            super(itemView);
            civAccount = itemView.findViewById(R.id.civ_account);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            tvAddRemove = itemView.findViewById(R.id.tv_add_rem);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

        void bind(ActivityLog log) {
            Glide.with(mContext)
                    .load(log.getProfilePic())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .fitCenter()
                    .into(civAccount);

            tvAccountName.setText(log.getFullName());

            if ((log.getNewValue() == null || log.getNewValue().isEmpty()) && (log.getOldValue() == null ||
                    log.getOldValue().isEmpty())) {
                tvAddRemove.setText("removed the");
            } else if (!log.getOldValue().isEmpty() && !log.getNewValue().isEmpty()) {
                tvAddRemove.setText("changed the");
            } else {
                tvAddRemove.setText("added the");
            }

            showDateAndTime(log.getCreatedAt(), tvDate);
        }
    }

    class StatusHolder extends RecyclerView.ViewHolder {
        private CircleImageView civAccount;
        private TextView tvAccountName;
        private TextView tvAddRemove;
        private TextView tvFrom;
        private TextView tvTo;
        private TextView tvDate;

        StatusHolder(@NonNull View itemView) {
            super(itemView);
            civAccount = itemView.findViewById(R.id.civ_account);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            tvAddRemove = itemView.findViewById(R.id.tv_add_rem);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvTo = itemView.findViewById(R.id.tv_to);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        void bind(ActivityLog log) {
            Glide.with(mContext)
                    .load(log.getProfilePic())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .fitCenter()
                    .into(civAccount);

            String oldValue = log.getOldValue();
            String newValue = log.getNewValue();

            switch (newValue) {
                case "TICKET_CREATED":
                    tvTo.setTextColor(mContext.getResources().getColor(R.color.ticket_created_text));
                    tvTo.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.created_bg));
                    tvTo.setText("TODO");
                    break;

                case "TICKET_STARTED":
                    tvTo.setTextColor(mContext.getResources().getColor(R.color.ticket_started_text));
                    tvTo.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.started_bg));
                    tvTo.setText("STARTED");
                    break;

                case "TICKET_RESOLVED":
                    tvTo.setTextColor(mContext.getResources().getColor(R.color.ticket_resolved_text));
                    tvTo.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.resolved_bg));
                    tvTo.setText("RESOLVED");

                    break;

                case "TICKET_CLOSED":
                    tvTo.setTextColor(mContext.getResources().getColor(R.color.ticket_closed_text));
                    tvTo.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.closed_bg));
                    tvTo.setText("CLOSED");
                    break;

                case "TICKET_REOPENED":
                    tvTo.setTextColor(mContext.getResources().getColor(R.color.ticket_reopened_text));
                    tvTo.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.reopened_bg));
                    tvTo.setText("REOPENED");
                    break;
            }

            switch (oldValue) {
                case "TICKET_CREATED":
                    tvFrom.setTextColor(mContext.getResources().getColor(R.color.ticket_created_text));
                    tvFrom.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.created_bg));
                    tvFrom.setText("TODO");
                    break;

                case "TICKET_STARTED":
                    tvFrom.setTextColor(mContext.getResources().getColor(R.color.ticket_started_text));
                    tvFrom.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.started_bg));
                    tvFrom.setText("STARTED");
                    break;

                case "TICKET_RESOLVED":
                    tvFrom.setTextColor(mContext.getResources().getColor(R.color.ticket_resolved_text));
                    tvFrom.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.resolved_bg));
                    tvFrom.setText("RESOLVED");

                    break;

                case "TICKET_CLOSED":
                    tvFrom.setTextColor(mContext.getResources().getColor(R.color.ticket_closed_text));
                    tvFrom.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.closed_bg));
                    tvFrom.setText("CLOSED");
                    break;

                case "TICKET_REOPENED":
                    tvFrom.setTextColor(mContext.getResources().getColor(R.color.ticket_reopened_text));
                    tvFrom.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.reopened_bg));
                    tvFrom.setText("REOPENED");
                    break;
            }
            tvAccountName.setText(log.getFullName());
            showDateAndTime(log.getCreatedAt(), tvDate);


        }
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        private CircleImageView civAccount;
        private TextView tvAccountName;
        private TextView tvAddRemove;
        private TextView tvFrom;
        private TextView tvTo;
        private TextView tvDate;

        TitleHolder(@NonNull View itemView) {
            super(itemView);
            civAccount = itemView.findViewById(R.id.civ_account);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            tvAddRemove = itemView.findViewById(R.id.tv_add_rem);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvTo = itemView.findViewById(R.id.tv_to);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

        void bind(ActivityLog log) {
            Glide.with(mContext)
                    .load(log.getProfilePic())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .fitCenter()
                    .into(civAccount);

            tvAccountName.setText(log.getFullName());
            tvFrom.setText("\"" + log.getOldValue() + "\"");
            tvTo.setText("\"" + log.getNewValue() + "\"");
            showDateAndTime(log.getCreatedAt(), tvDate);
        }
    }

    class EmployeeHolder extends RecyclerView.ViewHolder {
        private CircleImageView civAccount;
        private TextView tvAccountName;
        private TextView tvAddRemove;
        private TextView tvFrom;
        private TextView tvTo;
        private CircleImageView civFrom;
        private CircleImageView civTo;
        private TextView tvDate;

        EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            civAccount = itemView.findViewById(R.id.civ_account);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            tvAddRemove = itemView.findViewById(R.id.tv_add_rem);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvTo = itemView.findViewById(R.id.tv_to);
            civFrom = itemView.findViewById(R.id.civ_from);
            civTo = itemView.findViewById(R.id.civ_to);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

        void bind(ActivityLog log) {
            Glide.with(mContext)
                    .load(log.getProfilePic())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .fitCenter()
                    .into(civAccount);

            tvAccountName.setText(log.getFullName());
            AssignEmployee fromEmp = AssignEmployeeRepo.getInstance().getAssignedEmployeeByAccountId(log.getOldValue());
            AssignEmployee newEmp = AssignEmployeeRepo.getInstance().getAssignedEmployeeByAccountId(log.getNewValue());

            if (fromEmp != null) {
                tvFrom.setText(fromEmp.getName());

                Glide.with(mContext)
                        .load(fromEmp.getEmployeeImageUrl())
                        .error(R.drawable.ic_empty_profile_holder_icon)
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .fitCenter()
                        .into(civFrom);
            }

            if (newEmp != null) {
                tvTo.setText(newEmp.getName());
                Glide.with(mContext)
                        .load(newEmp.getEmployeeImageUrl())
                        .error(R.drawable.ic_empty_profile_holder_icon)
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .fitCenter()
                        .into(civTo);
            }

            showDateAndTime(log.getCreatedAt(), tvDate);
        }
    }

    class ContributorHolder extends RecyclerView.ViewHolder {
        private CircleImageView civAccount;
        private TextView tvAccountName;
        private TextView tvAddRemove;
        private CircleImageView civFrom;
        private TextView tvContributor;
        private TextView tvJoin;
        private TextView tvDate;

        ContributorHolder(@NonNull View itemView) {
            super(itemView);
            civAccount = itemView.findViewById(R.id.civ_account);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            tvAddRemove = itemView.findViewById(R.id.tv_add_rem);
            civFrom = itemView.findViewById(R.id.civ_from);
            tvContributor = itemView.findViewById(R.id.tv_contributor);
            tvJoin = itemView.findViewById(R.id.tv_join);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

        void bind(ActivityLog log) {
            Glide.with(mContext)
                    .load(log.getProfilePic())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .fitCenter()
                    .into(civAccount);

            tvAccountName.setText(log.getFullName());

            if (log.getActivityType().equalsIgnoreCase("CONTRIBUTER_ADDED")) {
                tvAddRemove.setText("added");
                tvJoin.setText("as the");
            } else {
                tvAddRemove.setText("removed");
                tvJoin.setText("from the");
            }

            tvContributor.setText(log.getValue());
        /*    AssignEmployee contributor = AssignEmployeeRepo.getInstance().getAssignedEmployeeByAccountId(log.getNewValue());

            if (contributor != null) {
                tvContributor.setText(contributor.getName());

                Glide.with(mContext)
                        .load(contributor.getEmployeeImageUrl())
                        .error(R.drawable.ic_empty_profile_holder_icon)
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .fitCenter()
                        .into(civFrom);
            }*/

            showDateAndTime(log.getCreatedAt(), tvDate);
        }
    }

    class EstTimeHolder extends RecyclerView.ViewHolder {
        private CircleImageView civAccount;
        private TextView tvAccountName;
        private TextView tvAddRemove;
        private TextView tvEstTime;
        private TextView tvJoin;
        private TextView tvDate;

        EstTimeHolder(@NonNull View itemView) {
            super(itemView);
            civAccount = itemView.findViewById(R.id.civ_account);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            tvAddRemove = itemView.findViewById(R.id.tv_add_rem);
            tvEstTime = itemView.findViewById(R.id.tv_est_time);
            tvJoin = itemView.findViewById(R.id.tv_join);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

        void bind(ActivityLog log) {
            Glide.with(mContext)
                    .load(log.getProfilePic())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .fitCenter()
                    .into(civAccount);

            tvAccountName.setText(log.getFullName());

            if ((log.getNewValue() == null || log.getNewValue().isEmpty())
                    && (log.getOldValue() == null ||
                    log.getOldValue().isEmpty())) {
                tvAddRemove.setText("removed");
                tvJoin.setText("from");
            } else {
                tvAddRemove.setText("added");
                tvJoin.setText("as");
            }

            tvEstTime.setText(log.getNewValue());

            showDateAndTime(log.getCreatedAt(), tvDate);
        }
    }

    class PriorityHolder extends RecyclerView.ViewHolder {
        private CircleImageView civAccount;
        private TextView tvAccountName;
        private TextView tvAddRemove;
        private TextView tvFrom;
        private TextView tvTo;
        private ImageView ivFrom;
        private ImageView ivTo;
        private TextView tvDate;

        PriorityHolder(@NonNull View itemView) {
            super(itemView);
            civAccount = itemView.findViewById(R.id.civ_account);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            tvAddRemove = itemView.findViewById(R.id.tv_add_rem);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvTo = itemView.findViewById(R.id.tv_to);
            ivFrom = itemView.findViewById(R.id.iv_from);
            ivTo = itemView.findViewById(R.id.iv_to);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

        void bind(ActivityLog log) {
            Glide.with(mContext)
                    .load(log.getProfilePic())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .fitCenter()
                    .into(civAccount);

            tvAccountName.setText(log.getFullName());

            String oldValue = log.getOldValue();
            String newValue = log.getNewValue();

            switch (oldValue) {
                case "LOWEST_TICKET_PRIORITY":
                    ivFrom.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lowest));
                    tvFrom.setText("Lowest");
                    break;

                case "LOW_TICKET_PRIORITY":
                    ivFrom.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_low));
                    tvFrom.setText("Low");
                    break;

                case "MEDIUM_TICKET_PRIORITY":
                    ivFrom.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_medium));
                    tvFrom.setText("Medium");
                    break;

                case "HIGH_TICKET_PRIORITY":
                    ivFrom.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_high));
                    tvFrom.setText("High");
                    break;

                case "HIGHEST_TICKET_PRIORITY":
                    ivFrom.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_highest));
                    tvFrom.setText("Highest");
                    break;
            }

            switch (newValue) {
                case "LOWEST_TICKET_PRIORITY":
                    ivTo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lowest));
                    tvTo.setText("Lowest");
                    break;
                case "LOW_TICKET_PRIORITY":
                    ivTo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_low));
                    tvTo.setText("Low");
                    break;

                case "MEDIUM_TICKET_PRIORITY":
                    ivTo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_medium));
                    tvTo.setText("Medium");
                    break;

                case "HIGH_TICKET_PRIORITY":
                    ivTo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_high));
                    tvTo.setText("High");
                    break;

                case "HIGHEST_TICKET_PRIORITY":
                    ivTo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_highest));
                    tvTo.setText("Highest");
                    break;
            }

            showDateAndTime(log.getCreatedAt(), tvDate);
        }
    }

    class LabelHolder extends RecyclerView.ViewHolder {
        private CircleImageView civAccount;
        private TextView tvAccountName;
        private TextView tvAddRemove;
        private TextView tvLabel;
        private TextView tvJoin;
        private TextView tvDate;

        LabelHolder(@NonNull View itemView) {
            super(itemView);
            civAccount = itemView.findViewById(R.id.civ_account);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            tvAddRemove = itemView.findViewById(R.id.tv_add_rem);
            tvLabel = itemView.findViewById(R.id.tv_label);
            tvJoin = itemView.findViewById(R.id.tv_join);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

        void bind(ActivityLog log) {
            Glide.with(mContext)
                    .load(log.getProfilePic())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .fitCenter()
                    .into(civAccount);

            tvAccountName.setText(log.getFullName());

            if ((log.getNewValue() == null || log.getNewValue().isEmpty())
                    && (log.getOldValue() == null ||
                    log.getOldValue().isEmpty())) {
                tvAddRemove.setText("removed");
                tvLabel.setText("\"" + log.getNewValue() + "\"");
                tvJoin.setText("from");
            } else {
                tvAddRemove.setText("added");
                tvJoin.setText("as");
                tvLabel.setText("\"" + log.getNewValue() + "\"");
            }


            showDateAndTime(log.getCreatedAt(), tvDate);
        }
    }

    private void showDateAndTime(long sentAt, TextView tvDate) {
        String date;
        if (DateUtils.isToday(sentAt)) {
            date = "Today";
        } else if (DateUtils.isToday(sentAt + DateUtils.DAY_IN_MILLIS)) {//check for yesterday
            date = "Yesterday";
        } else {
            date = GlobalUtils.getDateAlternate(sentAt);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter =
                new SimpleDateFormat("hh:mm aa");
        String time = timeFormatter.format(new Date(sentAt));

        tvDate.setText(date + "  " + time);
    }


}

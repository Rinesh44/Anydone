package com.treeleaf.anydone.serviceprovider.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import io.realm.RealmList;

public class ServiceRequestAdapter extends RecyclerView.Adapter
        <ServiceRequestAdapter.ServicesHolder> {
    private static final String TAG = "ServiceRequestAdapter";
    private List<ServiceRequest> serviceList;
    private Context mContext;
    private OnItemClickListener listener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ServiceRequestAdapter.onDeleteListener deleteListener;

    public ServiceRequestAdapter(List<ServiceRequest> serviceList, Context mContext) {
        this.serviceList = serviceList;
        this.mContext = mContext;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    public void setData(List<ServiceRequest> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_service_request_row, parent, false);
        return new ServicesHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ServicesHolder holder, int position) {
        ServiceRequest service = serviceList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout,
                String.valueOf(service.getServiceOrderId()));

        GlobalUtils.showLog(TAG, "service name check: " + service.getServiceName());
        holder.tvServiceName.setText(service.getServiceName());
        boolean isDateRange = checkIfDateRange(service.getAttributeList());
        GlobalUtils.showLog(TAG, "date range check: " + isDateRange);
        if (isDateRange) {
            String fromDate = getFromDate(service);
            String toDate = getToDate(service);
            GlobalUtils.showLog(TAG, "from date: " + fromDate);
            GlobalUtils.showLog(TAG, "to date: " + toDate);
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                holder.tvTime.setText(fromDate + " to " + toDate);
                holder.tvTime.setVisibility(View.VISIBLE);
            }
        } else {
            String date = getDate(service);
            if (date != null && !date.isEmpty()) {
                holder.tvTime.setText(date);
                holder.tvTime.setVisibility(View.VISIBLE);
            }
        }

        String location = getLocation(service);

        GlobalUtils.showLog(TAG, "location check: " + location);
        if (location == null || location.isEmpty()) {
            GlobalUtils.showLog(TAG, "inside empty location");
            holder.tvLocation.setVisibility(View.GONE);
        } else {
            holder.tvLocation.setText(location);
            holder.tvLocation.setVisibility(View.VISIBLE);
        }
//        holder.tvTime.setText("Order date: " + date + " - " + time);

        setStatus(service.getStatus(), holder.tvStatus);
        holder.tvProblemStat.setText(service.getProblemStatement());
        holder.tvProblemStat.setVisibility(View.VISIBLE);
        holder.tvServiceName.setText(service.getServiceName().replace("_", " "));

        if (!service.getStatus().equalsIgnoreCase
                (OrderServiceProto.ServiceOrderState.PENDING_SERVICE_ORDER.name())) {
            viewBinderHelper.lockSwipe(String.valueOf(service.getServiceOrderId()));
            holder.ibDelete.setVisibility(View.GONE);
        }
        holder.ibDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClicked(String.valueOf(service.getServiceOrderId()),
                        serviceList.indexOf(service));
            }
        });

        if (service.isRemote()) holder.ivTag.setVisibility(View.VISIBLE);
        else holder.ivTag.setVisibility(View.GONE);
    }

    private boolean checkIfDateRange(RealmList<ServiceAttributes> attributeList) {
        for (ServiceAttributes attribute : attributeList
        ) {
            if (attribute.getName().equalsIgnoreCase("to")) {
                return true;
            }
        }

        return false;
    }

    private String getLocation(ServiceRequest service) {
        for (ServiceAttributes attribute : service.getAttributeList()
        ) {
            if (attribute.getName().equals("location")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private void setDate(TextView tvDateTop, TextView tvDateBottom, long createdAt) {
        String dateConverted = GlobalUtils.getDateForServiceReq(createdAt);
        String[] splitDate = dateConverted.split(",");
        String dateTop = splitDate[0].trim();
        String dateBottom = splitDate[1].trim();
        tvDateTop.setText(dateTop);
        tvDateTop.setVisibility(View.VISIBLE);
        tvDateBottom.setText(dateBottom);
        tvDateBottom.setVisibility(View.VISIBLE);
    }


    private String getDate(ServiceRequest serviceRequest) {
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getName().equals("from")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private String getFromDate(ServiceRequest serviceRequest) {
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getServiceAttributeType()
                    .equalsIgnoreCase(ServiceProto.ServiceAttributeType.DATE_TIME_ATTRIBUTE.name())
                    && attribute.getName().equals("from")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private String getToDate(ServiceRequest serviceRequest) {
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getServiceAttributeType()
                    .equalsIgnoreCase(ServiceProto.ServiceAttributeType.DATE_TIME_ATTRIBUTE.name())
                    && attribute.getName().equals("to")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private String getTime(ServiceRequest serviceRequest) {
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getName().equals("time")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private void setUpDateTime(String date, String time, TextView tvTime) {
        StringBuilder dateTimeBuilder = new StringBuilder("Order Date: ");
        if (date.isEmpty() && !time.isEmpty()) {
            dateTimeBuilder.append(time);
            tvTime.setText(dateTimeBuilder);
            tvTime.setVisibility(View.VISIBLE);
        } else if (time.isEmpty() && !date.isEmpty()) {
            dateTimeBuilder.append(time);
            tvTime.setText(dateTimeBuilder);
            tvTime.setVisibility(View.VISIBLE);
        } else if (time.isEmpty()) {
            tvTime.setVisibility(View.GONE);
        } else {
            dateTimeBuilder.append(date);
            dateTimeBuilder.append(" - ");
            dateTimeBuilder.append(time);
            tvTime.setText(dateTimeBuilder);
            tvTime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }


    class ServicesHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceName;
        private TextView tvTime;
        private TextView tvLocation;
        private TextView tvStatus;
        private TextView tvProblemStat;
        private ImageView ivTag;
        private SwipeRevealLayout swipeRevealLayout;
        private ImageButton ibDelete;
        private RelativeLayout rlLayoutHolder;
        private View separator;


        ServicesHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvStatus = itemView.findViewById(R.id.tv_status);
            ivTag = itemView.findViewById(R.id.iv_tag);
            ibDelete = itemView.findViewById(R.id.ib_delete);
            swipeRevealLayout = itemView.findViewById(R.id.srl_service_req1);
            tvProblemStat = itemView.findViewById(R.id.tv_problem_stat);
            rlLayoutHolder = itemView.findViewById(R.id.rl_layout_holder);
            separator = itemView.findViewById(R.id.view_separator);

            rlLayoutHolder.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                ServiceRequest serviceRequest = serviceList.get(position);
                if (listener != null && position != RecyclerView.NO_POSITION &&
                        !serviceRequest.getStatus().equalsIgnoreCase
                                (OrderServiceProto.ServiceOrderState.PENDING_SERVICE_ORDER.name())) {
                    listener.onItemClick(serviceRequest);
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(ServiceRequest service);
    }

    public void setOnItemClickListener(ServiceRequestAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("SetTextI18n")
    private void setStatus(String status, TextView tvStatus) {
        switch (status) {
            case "ACCEPTED_SERVICE_ORDER":
                tvStatus.setText("Accepted");
                tvStatus.setTextColor(mContext.getResources()
                        .getColor(R.color.service_status_blue));
//                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot, 0, 0, 0);
                tvStatus.setCompoundDrawablePadding(10);
                break;

            case "PENDING_SERVICE_ORDER":
                tvStatus.setText("Pending");
                tvStatus.setTextColor(mContext.getResources()
                        .getColor(R.color.service_status_yellow));
//                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_yellow, 0, 0, 0);
                tvStatus.setCompoundDrawablePadding(10);
                break;

            case "COMPLETED_SERVICE_ORDER":
                tvStatus.setText("Completed");
                tvStatus.setTextColor(mContext.getResources()
                        .getColor(R.color.service_status_green));
//                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot, 0, 0, 0);
                tvStatus.setCompoundDrawablePadding(10);
                break;

            case "CANCELLED_SERVICE_ORDER":
                tvStatus.setText("Cancelled");
                tvStatus.setTextColor(mContext.getResources()
                        .getColor(R.color.service_status_red));
//                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_red, 0, 0, 0);
                tvStatus.setCompoundDrawablePadding(10);
                break;

            case "STARTED_SERVICE_ORDER":
                tvStatus.setText("Started");
                tvStatus.setTextColor(mContext.getResources()
                        .getColor(R.color.service_status_blue));
//                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot, 0, 0, 0);
                tvStatus.setCompoundDrawablePadding(10);
                break;

            case "UNKNOWN_SERVICE_ORDER_STATE":
                tvStatus.setText("Unknown");
                tvStatus.setTextColor(mContext.getResources()
                        .getColor(R.color.service_status_red));
//                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_red, 0, 0, 0);
                tvStatus.setCompoundDrawablePadding(10);
                break;

            case "CLOSED_SERVICE_ORDER":
                tvStatus.setText("Closed");
                tvStatus.setTextColor(mContext.getResources()
                        .getColor(R.color.service_status_red));
//                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_red, 0, 0, 0);
                tvStatus.setCompoundDrawablePadding(10);
                break;
            default:
                break;
        }
    }


    public interface onDeleteListener {
        void onDeleteClicked(String id, int pos);
    }

    public void closeSwipeLayout(String layoutId) {
        viewBinderHelper.closeLayout(layoutId);
    }

    public void setOnDeleteListener(ServiceRequestAdapter.onDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void deleteItem(String id, int pos) {
        removeFromDb(id);
        serviceList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, serviceList.size());
    }

    private void removeFromDb(String id) {
//        ServiceRequestRepo.getInstance().(id);
    }

}

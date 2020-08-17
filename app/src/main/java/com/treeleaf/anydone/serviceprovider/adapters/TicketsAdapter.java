package com.treeleaf.anydone.serviceprovider.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketHolder> {
    private static final String TAG = "TicketsAdapter";
    public static final int ASSIGNED = 0;
    public static final int SUBSCRIBED = 1;
    public static final int CLOSED_RESOLVED = 2;
    public static final int ASSIGNABLE = 3;
    public static final int SUBSCRIBEABLE = 4;
    private List<Tickets> ticketsList;
    private Context mContext;
    private OnItemClickListener listener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private OnUnSubscribeListener unsubscribeListener;
    private OnReopenListener onReopenListener;
    private OnSubscribeListener subscribeListener;
    private OnAssignListener assignListener;

    public TicketsAdapter(List<Tickets> ticketsList, Context mContext) {
        this.ticketsList = ticketsList;
        this.mContext = mContext;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    public void setData(List<Tickets> ticketsList) {
        this.ticketsList = ticketsList;
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ASSIGNED:
                View itemViewAssigned = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_ticket_row_assigned, parent, false);
                return new TicketHolder(itemViewAssigned);

            case SUBSCRIBED:
                View itemViewSubscribed = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_ticket_row_subscribed, parent, false);
                return new TicketHolder(itemViewSubscribed);

            case CLOSED_RESOLVED:
                View itemViewClosedResolved = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_ticket_row_closed_resolved, parent, false);
                return new TicketHolder(itemViewClosedResolved);

            case ASSIGNABLE:
                View itemViewAssignable = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_ticket_row_assignable, parent, false);
                return new TicketHolder(itemViewAssignable);

            case SUBSCRIBEABLE:
                View itemViewSubscribeable = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_ticket_row_subscribable, parent, false);
                return new TicketHolder(itemViewSubscribeable);

            default:
                View itemViewDefault = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_ticket_row_assigned, parent, false);
                return new TicketHolder(itemViewDefault);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Tickets tickets = ticketsList.get(position);

        switch (tickets.getTicketType()) {
            case "ASSIGNED":
                return ASSIGNED;

            case "SUBSCRIBED":
                return SUBSCRIBED;

            case "CLOSED_RESOLVED":
                return CLOSED_RESOLVED;

            case "ASSIGNABLE":
                return ASSIGNABLE;

            case "SUBSCRIBEABLE":
                return SUBSCRIBEABLE;
        }

        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        Tickets tickets = ticketsList.get(position);
        if (holder.swipeRevealLayout != null) {
            viewBinderHelper.bind(holder.swipeRevealLayout,
                    String.valueOf(tickets.getTicketId()));
        }

        String date = GlobalUtils.getDateNormal(tickets.getCreatedAt());
        String[] dateSeparated = date.split("\\s+");
        holder.tvDate1.setText(dateSeparated[0]);
        holder.tvDate2.setText(dateSeparated[1] + " " + dateSeparated[2]);
        holder.ticketId.setText("#" + tickets.getTicketId());
        holder.summary.setText(tickets.getDescription());
        holder.customer.setText(tickets.getCustomer().getFullName());
        holder.ticketStatus.setText(tickets.getTicketStatus());
        GlobalUtils.showLog(TAG, "ticket status: " + tickets.getTicketStatus());
        switch (tickets.getTicketStatus()) {
            case "TICKET_ASSIGNED":
                holder.ticketStatus.setTextColor(mContext.getResources().getColor(R.color.ticket_assigned_text));
                holder.ticketStatus.setBackground(mContext.getResources().getDrawable(R.drawable.assigned_bg));

                break;

            case "TICKET_STARTED":
                holder.ticketStatus.setTextColor(mContext.getResources().getColor(R.color.ticket_started_text));
                holder.ticketStatus.setBackground(mContext.getResources().getDrawable(R.drawable.started_bg));
                break;

            case "TICKET_COMPLETED":
                holder.ticketStatus.setTextColor(mContext.getResources().getColor(R.color.ticket_resolved_text));
                holder.ticketStatus.setBackground(mContext.getResources().getDrawable(R.drawable.resolved_bg));
                break;

            case "TICKET_CLOSED":
                holder.ticketStatus.setTextColor(mContext.getResources().getColor(R.color.ticket_closed_text));
                holder.ticketStatus.setBackground(mContext.getResources().getDrawable(R.drawable.closed_bg));
                break;

            case "TICKET_REOPENED":
                holder.ticketStatus.setTextColor(mContext.getResources().getColor(R.color.ticket_reopened_text));
                holder.ticketStatus.setBackground(mContext.getResources().getDrawable(R.drawable.reopened_bg));
                break;
        }

        switch (tickets.getTicketType()) {
            //set click listener on swipe action
            case "ASSIGNED":

                break;

            case "SUBSCRIBED":
                holder.llUnsubscribe.setOnClickListener(v -> {
                    if (unsubscribeListener != null) {
                        unsubscribeListener.onUnsubscribeClicked(String.valueOf(tickets.getTicketId()),
                                ticketsList.indexOf(tickets));
                    }
                });
                break;

            case "CLOSED_RESOLVED":
                holder.llReopen.setOnClickListener(v -> {
                    if (onReopenListener != null) {
                        onReopenListener.onReopenClicked(String.valueOf(tickets.getTicketId()),
                                ticketsList.indexOf(tickets));
                    }
                });
                break;

            case "ASSIGNABLE":
                holder.llAssign.setOnClickListener(v -> {
                    if (assignListener != null) {
                        assignListener.onAssignClicked(String.valueOf(tickets.getTicketId()),
                                ticketsList.indexOf(tickets));
                    }
                });
                break;

            case "SUBSCRIBEABLE":
                holder.llSubscribe.setOnClickListener(v -> {
                    if (subscribeListener != null) {
                        subscribeListener.onSubscribeClicked(String.valueOf(tickets.getTicketId()),
                                ticketsList.indexOf(tickets));
                    }
                });
                break;
        }


        for (Tags tag : tickets.getTagsRealmList()
        ) {
            TextView tagView = (TextView) LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_tag, null);
            tagView.setText(tag.getLabel());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 20, 0);
            tagView.setLayoutParams(params);
            holder.tags.addView(tagView);
        }

        for (Employee assignedEmployee : tickets.getAssignedEmployee()
        ) {
            CircleImageView employeeImage = (CircleImageView) LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_assigned_employee, null);
            if (assignedEmployee.getEmployeeImageUrl() != null) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_assigned_emp_placeholder)
                        .error(R.drawable.ic_assigned_emp_placeholder);

                Glide.with(mContext).load(assignedEmployee.getEmployeeImageUrl())
                        .apply(options).into(employeeImage);
            }

            holder.assignedEmployeeHolder.addView(employeeImage);
        }
    }

    @Override
    public int getItemCount() {
        if (ticketsList != null) {
            return ticketsList.size();
        } else return 0;
    }

    class TicketHolder extends RecyclerView.ViewHolder {
        private TextView tvDate1;
        private TextView tvDate2;
        private LinearLayout assignedEmployeeHolder;
        private RelativeLayout rlTicketHolder;
        private TextView assignedTo;
        private TextView ticketId;
        private TextView summary;
        private TextView customer;
        private LinearLayout tags;
        private TextView ticketStatus;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout llUnsubscribe;
        private LinearLayout llSubscribe;
        private LinearLayout llAssign;
        private LinearLayout llReopen;

        TicketHolder(@NonNull View itemView) {
            super(itemView);
            rlTicketHolder = itemView.findViewById(R.id.rl_ticket_holder);
            tvDate1 = itemView.findViewById(R.id.date1);
            tvDate2 = itemView.findViewById(R.id.date2);
            assignedEmployeeHolder = itemView.findViewById(R.id.ll_assinged_employee_holder);
            assignedTo = itemView.findViewById(R.id.tv_assigned_to);
            ticketId = itemView.findViewById(R.id.tv_ticket_id_value);
            summary = itemView.findViewById(R.id.tv_summary);
            customer = itemView.findViewById(R.id.tv_customer_value);
            tags = itemView.findViewById(R.id.ll_tags);
            ticketStatus = itemView.findViewById(R.id.tv_ticket_status);
            swipeRevealLayout = itemView.findViewById(R.id.srl_tickets);
            llReopen = itemView.findViewById(R.id.ll_swipe_reopen);
            llUnsubscribe = itemView.findViewById(R.id.ll_swipe_unsubscribe);
            llAssign = itemView.findViewById(R.id.ll_swipe_assign);
            llSubscribe = itemView.findViewById(R.id.ll_swipe_subscribe);

            if (rlTicketHolder != null) {
                rlTicketHolder.setOnClickListener(view -> {
                    int position = getAdapterPosition();

                    GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(ticketsList.get(position));
                    }

                });
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Tickets tickets);
    }

    public void setOnItemClickListener(TicketsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void closeSwipeLayout(String layoutId) {
        viewBinderHelper.closeLayout(layoutId);
    }


    public interface OnUnSubscribeListener {
        void onUnsubscribeClicked(String id, int pos);
    }

    public void setOnUnsubscribeListener(OnUnSubscribeListener unsubscribeListener) {
        this.unsubscribeListener = unsubscribeListener;
    }

    public interface OnReopenListener {
        void onReopenClicked(String id, int pos);
    }

    public void setOnReopenListener(OnReopenListener reopenListener) {
        this.onReopenListener = reopenListener;
    }

    public interface OnSubscribeListener {
        void onSubscribeClicked(String id, int pos);
    }

    public void setOnSubscribeListener(OnSubscribeListener subscribeListener) {
        this.subscribeListener = subscribeListener;
    }

    public interface OnAssignListener {
        void onAssignClicked(String id, int pos);

    }

    public void setOnAssignListener(OnAssignListener assignListener) {
        this.assignListener = assignListener;
    }

    public void deleteItem(int pos) {
        ticketsList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, ticketsList.size());
    }

}

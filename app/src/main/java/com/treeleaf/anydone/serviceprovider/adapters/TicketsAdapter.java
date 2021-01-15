package com.treeleaf.anydone.serviceprovider.adapters;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketHolder> {
    private static final String TAG = "TicketsAdapter";
    public static final int PENDING = 0;
    public static final int IN_PROGRESS = 1;
    public static final int SUBSCRIBED = 2;
    public static final int CLOSED_RESOLVED = 3;
    public static final int ASSIGNABLE = 4;
    public static final int SUBSCRIBEABLE = 5;
    public static final int CONTRIBUTED = 6;
    public static final int ALL = 7;
    private List<Tickets> ticketsList;
    private Context mContext;
    private OnItemClickListener listener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private OnUnSubscribeListener unsubscribeListener;
    private OnReopenListener onReopenListener;
    private OnSubscribeListener subscribeListener;
    private OnAssignListener assignListener;
    private OnStartListener onStartListener;
    private OnCloseListener onCloseListener;
    private OnResolveListener onResolveListener;
    private RecyclerView recyclerView;
    private long mLastClickTime = 0;

    public TicketsAdapter(List<Tickets> ticketsList, Context mContext, RecyclerView recyclerView) {
        this.ticketsList = ticketsList;
        this.mContext = mContext;
        this.recyclerView = recyclerView;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    public void reflectTicketDataChange(Tickets tickets) {
        int index = ticketsList.indexOf(tickets);
        ticketsList.set(index, tickets);
        notifyItemChanged(index);
    }

    public void setData(List<Tickets> ticketsList) {
        this.ticketsList = ticketsList;
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
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

            case ALL:
                View itemViewAll = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_ticket_row_all, parent, false);
                return new TicketHolder(itemViewAll);

            case PENDING:
                View itemViewPending = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_pending_ticket, parent, false);
                return new TicketHolder(itemViewPending);

            case IN_PROGRESS:
                View itemViewInProgress = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_in_progress_ticket, parent, false);
                return new TicketHolder(itemViewInProgress);

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
            case "PENDING":
                return PENDING;

            case "IN_PROGRESS":
                return IN_PROGRESS;

            case "SUBSCRIBED":
                return SUBSCRIBED;

            case "CLOSED_RESOLVED":
                return CLOSED_RESOLVED;

            case "ASSIGNABLE":
                return ASSIGNABLE;

            case "SUBSCRIBEABLE":
                return SUBSCRIBEABLE;

            case "CONTRIBUTED":
                return CONTRIBUTED;

            case "ALL":
                return ALL;
        }

        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) holder.setIsRecyclable(false);
        Tickets tickets = ticketsList.get(position);
        if (holder.swipeRevealLayout != null) {
            viewBinderHelper.bind(holder.swipeRevealLayout,
                    String.valueOf(tickets.getTicketId()));
        }

        Locale localeByLanguageTag = Locale.forLanguageTag("np");
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(localeByLanguageTag).build();

        String relativeTime = TimeAgo.using(tickets.getCreatedAt(), messages);
//        String date = GlobalUtils.getDateDigits(tickets.getCreatedAt());
        holder.tvDate.setText(relativeTime);
        holder.ticketId.setText("#" + tickets.getTicketIndex());
        holder.summary.setText(tickets.getTitle());

        if (tickets.getTicketStatus().equals("TICKET_STARTED")) {
            if (holder.tvDueDate != null) {
                if (tickets.getEstimatedTimeStamp() != 0) {
                    String date = GlobalUtils.getDateDigits(tickets.getEstimatedTimeStamp());

                    String dueDateRelativeTime = TimeAgo.using(tickets.getEstimatedTimeStamp(), messages);

             /*   String dueDateRelativeTime = DateUtils.getRelativeTimeSpanString
                        (tickets.getEstimatedTimeStamp()).toString();*/

                    StringBuilder dueDateBuilder = new StringBuilder();
                    dueDateBuilder.append("Due on ");
                    dueDateBuilder.append(date);
                    dueDateBuilder.append(" (");
                    dueDateBuilder.append(dueDateRelativeTime);
                    dueDateBuilder.append(")");
                    holder.tvDueDate.setText(dueDateBuilder.toString());

                    long estTime = tickets.getEstimatedTimeStamp();
                    long current = System.currentTimeMillis();
                    if (estTime - current < 0) {
                        holder.tvDueDate.setTextColor(mContext.getResources().getColor(R.color.red));
                    }
                }
            }
        } else {
            if (holder.tvDueDate != null)
                holder.tvDueDate.setVisibility(View.GONE);
        }


//        holder.customer.setText(tickets.getCustomer().getFullName());

      /*  String customerPic = tickets.getCustomer().getProfilePic();
        if (customerPic != null && !customerPic.isEmpty()) {

            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(mContext).load(customerPic).apply(options).into(holder.civCustomer);
        }*/

        setPriority(tickets.getPriority(), holder);
        GlobalUtils.showLog(TAG, "ticket priority check: " + tickets.getPriority());
        GlobalUtils.showLog(TAG, "ticket status: " + tickets.getTicketStatus());


        //show hide assign and subscribe swipe button according to conditions.

        if (holder.tvAssign != null) {
            Account userAccount = AccountRepo.getInstance().getAccount();
            if (userAccount.getAccountId().equalsIgnoreCase(tickets.getCreatedById())
                    || userAccount.getAccountId().equalsIgnoreCase(tickets.getAssignedEmployee().getAccountId())
                    || userAccount.getAccountType().equalsIgnoreCase(AnydoneProto.AccountType.SERVICE_PROVIDER.name())) {
                holder.tvAssign.setVisibility(View.VISIBLE);
            } else {
                holder.tvAssign.setVisibility(View.GONE);
            }
        }
        //TODO: hide subscribe swipe action code

        switch (tickets.getTicketStatus()) {
            case "TICKET_CREATED":
                holder.ticketStatus.setTextColor
                        (mContext.getResources().getColor(R.color.ticket_created_text));
                holder.ticketStatus.setBackground
                        (mContext.getResources().getDrawable(R.drawable.created_bg));
                holder.ticketStatus.setText("TODO");
                break;

            case "TICKET_STARTED":
                holder.ticketStatus.setTextColor(mContext.getResources().getColor
                        (R.color.ticket_started_text));
                holder.ticketStatus.setBackground(mContext.getResources().getDrawable
                        (R.drawable.started_bg));
                holder.ticketStatus.setText("STARTED");
                break;

            case "TICKET_RESOLVED":
                holder.ticketStatus.setTextColor(mContext.getResources().getColor
                        (R.color.ticket_resolved_text));
                holder.ticketStatus.setBackground(mContext.getResources().getDrawable
                        (R.drawable.resolved_bg));
                holder.ticketStatus.setText("RESOLVED");
                break;

            case "TICKET_CLOSED":
                holder.ticketStatus.setTextColor(mContext.getResources().getColor
                        (R.color.ticket_closed_text));
                holder.ticketStatus.setBackground(mContext.getResources().getDrawable
                        (R.drawable.closed_bg));
                holder.ticketStatus.setText("CLOSED");
                break;

            case "TICKET_REOPENED":
                holder.ticketStatus.setTextColor(mContext.getResources().getColor
                        (R.color.ticket_reopened_text));
                holder.ticketStatus.setBackground(mContext.getResources().getDrawable
                        (R.drawable.reopened_bg));
                holder.ticketStatus.setText("REOPENED");
                break;
        }

        switch (tickets.getTicketType()) {
            //set click listener on swipe action
            case "PENDING":
                if (holder.tvStart != null) {
                    holder.tvStart.setOnClickListener(v -> {
                        if (onStartListener != null) {
                            onStartListener.onStartClicked(String.valueOf(tickets.getTicketId()),
                                    ticketsList.indexOf(tickets));
                        }
                    });
                }
                break;

            case "IN_PROGRESS":
                if (holder.tvResolve != null) {
                    holder.tvResolve.setOnClickListener(v -> {
                        if (onResolveListener != null) {
                            onResolveListener.onResolveClicked(String.valueOf(tickets.getTicketId()),
                                    ticketsList.indexOf(tickets));
                        }
                    });
                }

                if (holder.tvClosed != null) {
                    holder.tvClosed.setOnClickListener(v -> {
                        if (onCloseListener != null) {
                            onCloseListener.onCloseClicked(String.valueOf(tickets.getTicketId()),
                                    ticketsList.indexOf(tickets));
                        }
                    });
                }
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

            case "ALL":
                holder.tvSubscribe.setOnClickListener(v -> {
                    if (subscribeListener != null) {
                        subscribeListener.onSubscribeClicked(String.valueOf(tickets.getTicketId()),
                                ticketsList.indexOf(tickets));
                    }
                });

                holder.tvAssign.setOnClickListener(v -> {
                    if (assignListener != null) {
                        assignListener.onAssignClicked(String.valueOf(tickets.getTicketId()),
                                ticketsList.indexOf(tickets));
                    }
                });

                break;
        }

        holder.tags.removeAllViews();
        for (Label tag : tickets.getLabelRealmList()
        ) {
            TextView tagView = (TextView) LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_tag, null);
            tagView.setText(tag.getName());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 20, 0);
            tagView.setLayoutParams(params);
            holder.tags.addView(tagView);
        }

        if (tickets.getLabelRealmList().isEmpty()) {
            holder.hsvTags.setVisibility(View.GONE);
        } else {
            holder.hsvTags.setVisibility(View.VISIBLE);
        }

        GlobalUtils.showLog(TAG, "ticket id: " + tickets.getTicketId());
    }

    private void setPriority(int priority, TicketHolder holder) {
        switch (priority) {
            case 1:
                holder.ticketPriority.setText("Lowest");
                holder.ticketPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_lowest_small, 0, 0, 0);
                break;

            case 2:
                holder.ticketPriority.setText("Low");
                holder.ticketPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_low_small, 0, 0, 0);
                break;

            case 4:
                holder.ticketPriority.setText("High");
                holder.ticketPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_high_small, 0, 0, 0);
                break;

            case 5:
                holder.ticketPriority.setText("Highest");
                holder.ticketPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_highest_small, 0, 0, 0);
                break;

            default:
                holder.ticketPriority.setText("Medium");
                holder.ticketPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_medium_small, 0, 0, 0);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (ticketsList != null) {
            return ticketsList.size();
        } else return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class TicketHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private CircleImageView civCustomer;
        private RelativeLayout rlTicketHolder;
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
        private TextView ticketPriority;
        private HorizontalScrollView hsvTags;
        private RelativeLayout rlMain;
        private TextView tvSubscribe, tvAssign;
        private TextView tvStart, tvResolve, tvClosed;
        private TextView tvDueDate;
        private RelativeLayout rlDueDate;

        TicketHolder(@NonNull View itemView) {
            super(itemView);
            rlTicketHolder = itemView.findViewById(R.id.rl_ticket_holder);
            tvDate = itemView.findViewById(R.id.tv_date);
//            civCustomer = itemView.findViewById(R.id.civ_customer);
            ticketId = itemView.findViewById(R.id.tv_ticket_id_value);
            summary = itemView.findViewById(R.id.tv_summary);
//            customer = itemView.findViewById(R.id.tv_customer_value);
            tags = itemView.findViewById(R.id.ll_tags);
            ticketStatus = itemView.findViewById(R.id.tv_ticket_status);
            swipeRevealLayout = itemView.findViewById(R.id.srl_tickets);
            llReopen = itemView.findViewById(R.id.ll_swipe_reopen);
            llUnsubscribe = itemView.findViewById(R.id.ll_swipe_unsubscribe);
            llAssign = itemView.findViewById(R.id.ll_swipe_assign);
            llSubscribe = itemView.findViewById(R.id.ll_swipe_subscribe);
            ticketPriority = itemView.findViewById(R.id.tv_priority);
            hsvTags = itemView.findViewById(R.id.hsv_tags);
            rlMain = itemView.findViewById(R.id.rl_main);
            tvSubscribe = itemView.findViewById(R.id.tv_subscribe);
            tvAssign = itemView.findViewById(R.id.tv_assign);
            tvStart = itemView.findViewById(R.id.tv_start);
            tvResolve = itemView.findViewById(R.id.tv_resolve);
            tvClosed = itemView.findViewById(R.id.tv_close);
            rlDueDate = itemView.findViewById(R.id.rl_due_date);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);

            if (rlTicketHolder != null) {
                rlTicketHolder.setOnClickListener(view -> {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

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

    public interface OnStartListener {
        void onStartClicked(String id, int pos);
    }

    public void setOnStartListener(OnStartListener onStartListener) {
        this.onStartListener = onStartListener;
    }


    public interface OnCloseListener {
        void onCloseClicked(String id, int pos);
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }


    public interface OnResolveListener {
        void onResolveClicked(String id, int pos);
    }

    public void setOnResolveListener(OnResolveListener onResolveListener) {
        this.onResolveListener = onResolveListener;
    }

    public void deleteItem(int pos, long ticketId) {
        ticketsList.remove(pos);
        notifyDataSetChanged();
//        notifyItemRangeChanged(pos, ticketsList.size());
//        TicketRepo.getInstance().deleteTicket(ticketId);
    }

}

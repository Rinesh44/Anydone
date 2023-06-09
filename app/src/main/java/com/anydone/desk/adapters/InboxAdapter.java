package com.anydone.desk.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.treeleaf.anydone.entities.InboxProto;
import com.anydone.desk.R;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Inbox;
import com.anydone.desk.realm.model.Participant;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.InboxRepo;
import com.anydone.desk.realm.repo.ParticipantRepo;
import com.anydone.desk.utils.DetectHtml;
import com.anydone.desk.utils.GlobalUtils;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmList;

public class InboxAdapter extends ListAdapter<Inbox, RecyclerView.ViewHolder> implements Filterable {
    private static final String TAG = "InboxAdapter";
    private List<Inbox> inboxList;
    private List<Inbox> inboxListFiltered;
    private Context mContext;
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteClickListener;
    private OnMuteClickListener muteClickListener;
    private OnUnMuteClickListener unMuteClickListener;
    private OnJoinClickListener onJoinClickListener;
    private OnConvertToGroupClickListener convertToGroupClickListener;
    private long mLastClickTime = 0;
    private static final int SINGLE_IMAGE = 1;
    private static final int DOUBLE_IMAGE = 2;
    private static final int MULTIPLE_IMAGE = 3;
    private static final int LOADING = 4;
    private boolean isLoaderVisible = false;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public InboxAdapter(List<Inbox> inboxList, Context mContext) {
        super(DIFF_CALLBACK);
        List<Inbox> sorted = setSelfInboxOnTop(inboxList);
        this.inboxList = sorted;
        this.mContext = mContext;
        this.inboxListFiltered = sorted;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    private List<Inbox> setSelfInboxOnTop(List<Inbox> inboxList) {
        Inbox selfInbox = null;
        for (Inbox inbox : inboxList
        ) {
            if (inbox.isSelfInbox()) {
                selfInbox = inbox;
            }
        }

        inboxList.remove(selfInbox);
        if (selfInbox != null)
            inboxList.add(0, selfInbox);
        return inboxList;
    }

    private static final DiffUtil.ItemCallback<Inbox> DIFF_CALLBACK = new DiffUtil.ItemCallback<Inbox>() {
        @Override
        public boolean areItemsTheSame(@NonNull Inbox oldItem, @NonNull Inbox newItem) {
            return oldItem.getInboxId().equals(newItem.getInboxId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Inbox oldItem, @NonNull Inbox newItem) {
            return oldItem.getSubject().equals(newItem.getSubject()) &&
                    oldItem.getCreatedByUserAccountId().equals(newItem.getCreatedByUserAccountId())
                    && oldItem.getLastMsgDate() == newItem.getLastMsgDate() &&
                    oldItem.getLastMsgSenderId().equals(newItem.getLastMsgSenderId())
                    && oldItem.getCreatedAt() == newItem.getCreatedAt();
        }
    };

    public void updateInbox(Inbox inbox) {
        Inbox updatedInbox = InboxRepo.getInstance().getInboxById(inbox.getInboxId());
        int index = inboxListFiltered.indexOf(inbox);
        inboxListFiltered.set(index, updatedInbox);
//        notifyItemChanged(index);
        notifyDataSetChanged();
    }

    public void setData(List<Inbox> inboxList) {
//        this.inboxListFiltered.clear();
//        this.inboxListFiltered = inboxList;
        this.inboxListFiltered = setSelfInboxOnTop(inboxList);
        notifyDataSetChanged();
    }

    public void setSearchedData(List<Inbox> inboxList) {
        this.inboxListFiltered = inboxList;
        notifyDataSetChanged();
    }

    public void updateInbox(String inboxId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Inbox updatedInbox = InboxRepo.getInstance().getInboxById(inboxId);
            int index = inboxList.indexOf(updatedInbox);
            inboxList.set(index, updatedInbox);
            notifyItemChanged(index);
        });
    }


    @Override
    public int getItemViewType(int position) {
        GlobalUtils.showLog(TAG, "item view type position: " + position);
        if (isLoaderVisible) {
            if (position == inboxListFiltered.size() - 1) return LOADING;
        } else {
            Inbox inbox = inboxListFiltered.get(position);
            int participantSize = inbox.getParticipantList().size();
            if (participantSize <= 2) {
                return SINGLE_IMAGE;
            } else {
                return DOUBLE_IMAGE;
            } /*else if (participantSize > 3) {
                return MULTIPLE_IMAGE;
            }*/
        }
        return SINGLE_IMAGE;
    }

    public Inbox getInboxAt(int pos) {
        return inboxListFiltered.get(pos);
    }

    @io.reactivex.annotations.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DOUBLE_IMAGE:
                View doubleImageItemView = LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.layout_inbox_row_double,
                                parent, false);
                return new InboxAdapter.DoubleImageHolder(doubleImageItemView);

            case MULTIPLE_IMAGE:
                View multipleImageItemView = LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.layout_inbox_row_multiple,
                                parent, false);
                return new InboxAdapter.MultipleImageHolder(multipleImageItemView);

            case LOADING:
                View loadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading,
                        parent, false);
                return new LoadingHolder(loadingView);

            default:
                View singleImageItemView = LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.layout_inbox_row_single,
                                parent, false);
                return new InboxAdapter.SingleImageHolder(singleImageItemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Inbox inbox = inboxListFiltered.get(position);

        //remove already removed item from list
        if (!inbox.isExists()) inboxListFiltered.remove(inbox);

        switch (holder.getItemViewType()) {
            case SINGLE_IMAGE:
                ((SingleImageHolder) holder).bind(inbox);
                break;

            case DOUBLE_IMAGE:
                ((DoubleImageHolder) holder).bind(inbox);
                break;

            case MULTIPLE_IMAGE:
                ((MultipleImageHolder) holder).bind(inbox);
                break;

            case LOADING:
                ((LoadingHolder) holder).bind(inbox);
        }
    }

    public void addItems(List<Inbox> inboxItems) {
        inboxListFiltered.addAll(inboxItems);
        notifyDataSetChanged();
    }


    public void addLoading() {
        isLoaderVisible = true;
        inboxListFiltered.add(new Inbox());
        notifyItemInserted(inboxListFiltered.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = inboxListFiltered.size() - 1;
    /*    Inbox item = getItem(position);
        if (item != null) {
            inboxListFiltered.remove(position);
            notifyItemRemoved(position);
        }*/
    }

    public void clear() {
        inboxListFiltered.clear();
        notifyDataSetChanged();
    }


    private void showMessagedDateTime(TextView tvDate, Inbox inbox) {
        long lastMsgDate = inbox.getLastMsgDate();
        if (lastMsgDate == 0) {
            lastMsgDate = inbox.getCreatedAt();
        }
        GlobalUtils.showLog(TAG, "last msg date: " + lastMsgDate);
        Date date = new Date();
        date.setTime(lastMsgDate);
        if (DateUtils.isToday(lastMsgDate)) {
            tvDate.setText(GlobalUtils.getTimeExcludeMillis(lastMsgDate));

            //check for yesterday
        } else if (DateUtils.isToday(lastMsgDate + DateUtils.DAY_IN_MILLIS)) {
            tvDate.setText(R.string.yesterday);
        } else if (isDateInCurrentWeek(date) != -1) {
            switch (isDateInCurrentWeek(date)) {
                case 1:
                    tvDate.setText(R.string.sun);
                    break;

                case 2:
                    tvDate.setText(R.string.mon);
                    break;

                case 3:
                    tvDate.setText(R.string.tue);
                    break;

                case 4:
                    tvDate.setText(R.string.wed);
                    break;

                case 5:
                    tvDate.setText(R.string.thu);
                    break;

                case 6:
                    tvDate.setText(R.string.fri);
                    break;

                case 7:
                    tvDate.setText(R.string.sat);
                    break;

                default:
                    break;
            }

        } else {
            tvDate.setText(GlobalUtils.getDateShort(lastMsgDate));
        }
    }


    @Override
    public int getItemCount() {
        if (inboxListFiltered != null) {
            return inboxListFiltered.size();
        } else return 0;
    }

    public static int isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        int day = targetCalendar.get(Calendar.DAY_OF_WEEK);
        if (week == targetWeek && year == targetYear) {
            return day;
        }
        return -1;
    }

    class SingleImageHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName;
        private TextView tvLastMsg;
        private TextView tvDate;
        private RelativeLayout container;
        private ImageView ivMute;
        private SwipeRevealLayout swipeRevealLayout;
        private TextView tvUnMute, tvMute, tvDelete;
        private RelativeLayout rlSecondLine;
        private LinearLayout llSwipeActions;
        private ImageView ivParticipant;
        private TextView tvJoin;
        private TextView tvConvertToGroup;
        private TextView tvUnReadCount;

        SingleImageHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            tvDate = itemView.findViewById(R.id.tv_date);
            container = itemView.findViewById(R.id.rl_holder);
            ivMute = itemView.findViewById(R.id.iv_mute);
            tvUnMute = itemView.findViewById(R.id.tv_unmute);
            tvMute = itemView.findViewById(R.id.tv_mute);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            swipeRevealLayout = itemView.findViewById(R.id.srl_single);
            rlSecondLine = itemView.findViewById(R.id.rl_second_line);
            llSwipeActions = itemView.findViewById(R.id.ll_swipe_actions);
            ivParticipant = itemView.findViewById(R.id.iv_single_participant);
            tvJoin = itemView.findViewById(R.id.tv_join);
            tvConvertToGroup = itemView.findViewById(R.id.tv_convert_to_group);
            tvUnReadCount = itemView.findViewById(R.id.tv_unread_count);

            container.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(inboxListFiltered.get(position));
                }
            });

            tvMute.setOnClickListener(v -> {
                if (muteClickListener != null) {
                    muteClickListener.onMuteClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvUnMute.setOnClickListener(v -> {
                if (unMuteClickListener != null) {
                    unMuteClickListener.onUnMuteClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvConvertToGroup.setOnClickListener(v -> {
                if (convertToGroupClickListener != null) {
                    convertToGroupClickListener.onConvertToGroupClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvJoin.setOnClickListener(v -> {
                if (onJoinClickListener != null) {
                    onJoinClickListener.onJoinClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

        }

        @SuppressLint("SetTextI18n")
        void bind(Inbox inbox) {
            if (inbox.getParticipantList() != null) {
                if (swipeRevealLayout != null) {
                    viewBinderHelper.bind(swipeRevealLayout,
                            String.valueOf(inbox.getInboxId()));
                }

                if (inbox.isSelfInbox() || inbox.isLeftGroup()) {
                    viewBinderHelper.lockSwipe(inbox.getInboxId());
                }
                GlobalUtils.showLog(TAG, "seen status check: " + inbox.isSeen());


                GlobalUtils.showLog(TAG, "seen status check: " + inbox.isSeen());

                //set unread count
                if (!inbox.isSelfInbox() && inbox.getUnReadMessageCount() > 0) {
                    tvUnReadCount.setVisibility(View.VISIBLE);
                    tvUnReadCount.setText(String.valueOf(inbox.getUnReadMessageCount()));
                } else {
                    tvUnReadCount.setVisibility(View.GONE);
                }

                if (!inbox.isSeen() && !inbox.isSelfInbox()) {
                    tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.BOLD);
                    tvLastMsg.setTextColor(mContext.getResources().getColor(R.color.charcoal_text));
                    tvCustomerName.setTypeface(tvLastMsg.getTypeface(), Typeface.BOLD);
                    tvCustomerName.setTextColor(mContext.getResources().getColor(R.color.charcoal_text));
                    tvDate.setTextColor(mContext.getResources().getColor(R.color.charcoal_text));
                } else {
                    tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.NORMAL);
                    tvLastMsg.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    tvCustomerName.setTypeface(tvLastMsg.getTypeface(), Typeface.NORMAL);
                    tvCustomerName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    tvDate.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                }


                if (inbox.getInboxType() != null && inbox.getInboxType()
                        .equalsIgnoreCase(InboxProto.Inbox.InboxType.PUBLIC_GROUP.name())) {
                    Glide.with(mContext).load(R.drawable.ic_public_grp)
                            .fitCenter()
                            .into(ivParticipant);

                } else if (inbox.getInboxType() != null && inbox.getInboxType()
                        .equalsIgnoreCase(InboxProto.Inbox.InboxType.PRIVATE_GROUP.name())) {
                    Glide.with(mContext).load(R.drawable.ic_private_grp)
                            .fitCenter()
                            .into(ivParticipant);
                } else {
                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .placeholder(R.drawable.ic_empty_profile_holder_icon)
                            .error(R.drawable.ic_empty_profile_holder_icon);

                    //change swipe text to delete when not group
                    tvDelete.setText("Delete");

                    if (inbox.getParticipantList() != null && !inbox.getParticipantList().isEmpty() &&
                            inbox.getParticipantList().get(0) != null) {

                        if (inbox.getParticipantList().size() == 1) {
                            Glide.with(mContext).load(inbox.getParticipantList().get(0).getEmployee()
                                    .getEmployeeImageUrl())
                                    .apply(options).into(ivParticipant);
                        } else {
                            Account account = AccountRepo.getInstance().getAccount();
                            if (!inbox.getParticipantList().get(0).getEmployee().getAccountId()
                                    .equalsIgnoreCase(account.getAccountId())) {
                                Glide.with(mContext).load(inbox.getParticipantList().get(0).getEmployee()
                                        .getEmployeeImageUrl())
                                        .apply(options).into(ivParticipant);
                            } else {
                                Glide.with(mContext).load(inbox.getParticipantList().get(1).getEmployee()
                                        .getEmployeeImageUrl())
                                        .apply(options).into(ivParticipant);
                            }
                        }
                    }
                }

                Account account = AccountRepo.getInstance().getAccount();
                if (inbox.isSelfInbox()) {
                    tvCustomerName.setText(account.getFullName());
                    tvCustomerName.append(" (You)");
                } else if (inbox.getSubject() != null && !inbox.getSubject().isEmpty()) {
                    tvCustomerName.setText(inbox.getSubject());
                } else {
                    if (inbox.getParticipantList() != null && !inbox.getParticipantList().isEmpty()) {
                        if (inbox.getParticipantList().get(0) != null &&
                                !inbox.getParticipantList().get(0).getEmployee().getAccountId()
                                        .equalsIgnoreCase(account.getAccountId())) {
                            tvCustomerName.setText(inbox.getParticipantList().get(0).getEmployee().getName());
                        } else {
                            if (inbox.getParticipantList().size() > 1 && inbox.getParticipantList().get(1) != null) {
                                tvCustomerName.setText(inbox.getParticipantList().get(1).getEmployee().getName());
                            }
                        }
                    }
                }

                if (tvCustomerName.getText().toString().isEmpty()) {
                    tvCustomerName.setText(account.getFullName());
                }


                if (inbox.getLastMsg() != null && !inbox.getLastMsg().isEmpty()) {
                    String mentionPattern = "(?<=@)[\\w]+";
                    Pattern p = Pattern.compile(mentionPattern);
                    String msg = inbox.getLastMsg();

                    Matcher m = p.matcher(msg);
//                    String changed = m.replaceAll("");
                    while (m.find()) {
                        GlobalUtils.showLog(TAG, "found: " + m.group(0));
                        String employeeId = m.group(0);
                        Participant participant = ParticipantRepo.getInstance()
                                .getParticipantByEmployeeAccountId(employeeId);
                        if (employeeId != null && participant != null) {
                            SpannableString wordToSpan = new SpannableString(participant.getEmployee().getName());
                            wordToSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),
                                    0, wordToSpan.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            GlobalUtils.showLog(TAG, "before: " + msg);
                            msg = msg.replace(employeeId, wordToSpan);
                            GlobalUtils.showLog(TAG, "after: " + msg);
                        }
                    }

                    boolean isHtml = DetectHtml.isHtml(msg);
                    if (isHtml)
                        tvLastMsg.setText(Jsoup.parse(msg).text());
                    else tvLastMsg.setText(msg);
                } else {
                    tvLastMsg.setVisibility(View.GONE);
                    tvDate.setVisibility(View.INVISIBLE);
                }


                if (inbox.getNotificationType().equalsIgnoreCase(
                        InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION.name())) {
                    ivMute.setVisibility(View.GONE);
                    tvMute.setVisibility(View.VISIBLE);
                    tvUnMute.setVisibility(View.GONE);
                } else {
                    ivMute.setVisibility(View.VISIBLE);
                    tvUnMute.setVisibility(View.VISIBLE);
                    tvMute.setVisibility(View.GONE);
                }

                showMessagedDateTime(tvDate, inbox);
            }

            if (inbox.getInboxType() != null && inbox.getInboxType()
                    .equalsIgnoreCase(InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {
                tvConvertToGroup.setVisibility(View.VISIBLE);
            }

            if (inbox.getInboxType() != null && inbox.getInboxType()
                    .equalsIgnoreCase(InboxProto.Inbox.InboxType.PUBLIC_GROUP.name()) &&
                    !inbox.isMember()) {
                tvJoin.setVisibility(View.VISIBLE);
            }

        }
    }

    class DoubleImageHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName;
        private TextView tvLastMsg;
        private TextView tvDate;
        private RelativeLayout container;
        private SwipeRevealLayout swipeRevealLayout;
        private ImageView ivMute;
        private TextView tvMute, tvUnMute, tvDelete;
        private RelativeLayout rlSecondLine;
        private TextView tvConvertToGroup;
        private TextView tvJoin;
        private ImageView ivParticipantFirst;
        private ImageView ivParticipantSecond;
        private ImageView ivGroup;
        private FrameLayout flCardView;
        private TextView tvUnreadCount;

        DoubleImageHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            tvDate = itemView.findViewById(R.id.tv_date);
            container = itemView.findViewById(R.id.rl_holder);
            ivMute = itemView.findViewById(R.id.iv_mute);
            swipeRevealLayout = itemView.findViewById(R.id.srl_double);
            tvMute = itemView.findViewById(R.id.tv_mute);
            tvUnMute = itemView.findViewById(R.id.tv_unmute);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            rlSecondLine = itemView.findViewById(R.id.rl_second_line);
            tvConvertToGroup = itemView.findViewById(R.id.tv_convert_to_group);
            tvJoin = itemView.findViewById(R.id.tv_join);
            ivParticipantFirst = itemView.findViewById(R.id.iv_participant_first);
            ivParticipantSecond = itemView.findViewById(R.id.iv_participant_second);
            ivGroup = itemView.findViewById(R.id.iv_group);
            flCardView = itemView.findViewById(R.id.fl_card_view);
            tvUnreadCount = itemView.findViewById(R.id.tv_unread_count);

            container.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();

                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(inboxListFiltered.get(position));
                }
            });

            tvMute.setOnClickListener(v -> {
                if (muteClickListener != null) {
                    muteClickListener.onMuteClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvUnMute.setOnClickListener(v -> {
                if (unMuteClickListener != null) {
                    unMuteClickListener.onUnMuteClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvConvertToGroup.setOnClickListener(v -> {
                if (convertToGroupClickListener != null) {
                    convertToGroupClickListener
                            .onConvertToGroupClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvJoin.setOnClickListener(v -> {
                if (onJoinClickListener != null) {
                    onJoinClickListener.onJoinClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void bind(Inbox inbox) {
            if (inbox.getParticipantList() != null) {
                if (swipeRevealLayout != null) {
                    viewBinderHelper.bind(swipeRevealLayout,
                            String.valueOf(inbox.getInboxId()));
                }

                //set unread count
                if (inbox.getUnReadMessageCount() > 0) {
                    tvUnreadCount.setVisibility(View.VISIBLE);
                    tvUnreadCount.setText(String.valueOf(inbox.getUnReadMessageCount()));
                } else {
                    tvUnreadCount.setVisibility(View.GONE);
                }

                GlobalUtils.showLog(TAG, "seen status check: " + inbox.isSeen());
                if (!inbox.isSeen()) {
                    tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.BOLD);
                    tvLastMsg.setTextColor(mContext.getResources().getColor(R.color.charcoal_text));
                    tvCustomerName.setTypeface(tvLastMsg.getTypeface(), Typeface.BOLD);
                    tvCustomerName.setTextColor(mContext.getResources().getColor(R.color.charcoal_text));
                    tvDate.setTextColor(mContext.getResources().getColor(R.color.charcoal_text));
                } else {
                    tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.NORMAL);
                    tvLastMsg.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    tvCustomerName.setTypeface(tvLastMsg.getTypeface(), Typeface.NORMAL);
                    tvCustomerName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                    tvDate.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                }

                if (inbox.getInboxType() != null && inbox.getInboxType().equalsIgnoreCase(
                        InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {
                    tvDelete.setText("Delete");
                }

                if (inbox.getInboxType() != null && inbox.getInboxType()
                        .equalsIgnoreCase(InboxProto.Inbox.InboxType.PUBLIC_GROUP.name())) {
                    flCardView.setVisibility(View.GONE);
                    ivGroup.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.drawable.ic_public_grp)
                            .into(ivGroup);
                } else if (inbox.getInboxType() != null && inbox.getInboxType()
                        .equalsIgnoreCase(InboxProto.Inbox.InboxType.PRIVATE_GROUP.name())) {
                    flCardView.setVisibility(View.GONE);
                    ivGroup.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.drawable.ic_private_grp)
                            .into(ivGroup);
                } else {
                    flCardView.setVisibility(View.VISIBLE);
                    ivGroup.setVisibility(View.GONE);
                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .placeholder(R.drawable.ic_empty_profile_holder_icon)
                            .error(R.drawable.ic_empty_profile_holder_icon);

                    Account account = AccountRepo.getInstance().getAccount();
                    RealmList<Participant> participants = new RealmList<>();
                    participants.addAll(inbox.getParticipantList());
                    for (Participant participant : inbox.getParticipantList()
                    ) {
                        if (account.getAccountId().equalsIgnoreCase(participant.getEmployee().getAccountId())) {
                            participants.remove(participant);
                        }
                    }

                    if (participants.get(0) != null)
                        Glide.with(mContext).load(participants.get(0).getEmployee()
                                .getEmployeeImageUrl())
                                .apply(options).into(ivParticipantFirst);

                    if (participants.size() > 1) {
                        Glide.with(mContext).load(participants.get(1).getEmployee()
                                .getEmployeeImageUrl())
                                .apply(options).into(ivParticipantSecond);
                    }
                }


                if (inbox.getSubject() != null && !inbox.getSubject().isEmpty()) {
                    tvCustomerName.setText(inbox.getSubject());
                } else {
                    String participantsName = GlobalUtils.getAllParticipants(inbox);
                    String firstParticipant = inbox.getParticipantList().get(0).getEmployee().getName();
                    String secondParticipant = inbox.getParticipantList().get(1).getEmployee().getName();

                    tvCustomerName.setText(participantsName);
                }

                if (inbox.getLastMsg() != null && !inbox.getLastMsg().isEmpty()) {
                    String mentionPattern = "(?<=@)[\\w]+";
                    Pattern p = Pattern.compile(mentionPattern);
                    String msg = inbox.getLastMsg();

                    Matcher m = p.matcher(msg);
//                    String changed = m.replaceAll("");
                    while (m.find()) {
                        GlobalUtils.showLog(TAG, "ffound: " + m.group(0));
                        String employeeId = m.group(0);
                        Participant participant = ParticipantRepo.getInstance()
                                .getParticipantByEmployeeAccountId(employeeId);
                        if (employeeId != null && participant != null) {
                            SpannableString wordToSpan = new SpannableString(participant.getEmployee().getName());
                            wordToSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),
                                    0, wordToSpan.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            GlobalUtils.showLog(TAG, "before: " + msg);
                            msg = msg.replace(employeeId, wordToSpan);
                            GlobalUtils.showLog(TAG, "after: " + msg);
                        }
                    }
                    boolean isHtml = DetectHtml.isHtml(msg);
                    if (isHtml)
                        tvLastMsg.setText(Jsoup.parse(msg).text());
                    else tvLastMsg.setText(msg);
                } else {
                    tvLastMsg.setVisibility(View.GONE);
                    tvDate.setVisibility(View.INVISIBLE);
                }


                if (inbox.getNotificationType().equalsIgnoreCase(
                        InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION.name())) {
                    ivMute.setVisibility(View.GONE);
                    tvMute.setVisibility(View.VISIBLE);
                    tvUnMute.setVisibility(View.GONE);
                } else {
                    ivMute.setVisibility(View.VISIBLE);
                    tvUnMute.setVisibility(View.VISIBLE);
                    tvMute.setVisibility(View.GONE);
                }

                showMessagedDateTime(tvDate, inbox);
            }

            Account user = AccountRepo.getInstance().getAccount();
            String userId = user.getAccountId();
            if (inbox.getInboxType() != null && inbox.getInboxType()
                    .equalsIgnoreCase(InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {
                tvConvertToGroup.setVisibility(View.VISIBLE);
            }

            if (inbox.getInboxType() != null && inbox.getInboxType()
                    .equalsIgnoreCase(InboxProto.Inbox.InboxType.PUBLIC_GROUP.name()) &&
                    !inbox.isMember()) {
                tvJoin.setVisibility(View.VISIBLE);
            }
        }
    }

    class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Inbox inbox) {

        }
    }

    class MultipleImageHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName;
        private TextView tvLastMsg;
        private TextView tvDate;
        private RelativeLayout container;
        private ImageView ivMute;
        private TextView tvExtraParticipantNo;
        private SwipeRevealLayout swipeRevealLayout;
        private TextView tvMute, tvUnMute, tvDelete;
        private RelativeLayout rlSecondLine;
        private TextView tvConvertToGroup;
        private TextView tvJoin;

        MultipleImageHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            tvDate = itemView.findViewById(R.id.tv_date);
            container = itemView.findViewById(R.id.rl_holder);
            ivMute = itemView.findViewById(R.id.iv_mute);
            swipeRevealLayout = itemView.findViewById(R.id.srl_multiple);
            tvMute = itemView.findViewById(R.id.tv_mute);
            tvUnMute = itemView.findViewById(R.id.tv_unmute);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            rlSecondLine = itemView.findViewById(R.id.rl_second_line);
            tvConvertToGroup = itemView.findViewById(R.id.tv_convert_to_group);
            tvJoin = itemView.findViewById(R.id.tv_join);

            container.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(inboxListFiltered.get(position));
                }
            });

            tvMute.setOnClickListener(v -> {
                if (muteClickListener != null) {
                    muteClickListener.onMuteClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvUnMute.setOnClickListener(v -> {
                if (unMuteClickListener != null) {
                    unMuteClickListener.onUnMuteClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvConvertToGroup.setOnClickListener(v -> {
                if (convertToGroupClickListener != null) {
                    convertToGroupClickListener
                            .onConvertToGroupClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

            tvJoin.setOnClickListener(v -> {
                if (onJoinClickListener != null) {
                    onJoinClickListener
                            .onJoinClick(inboxListFiltered.get(getAdapterPosition()));
                }
            });

        }


        @SuppressLint("SetTextI18n")
        void bind(Inbox inbox) {
            if (swipeRevealLayout != null) {
                viewBinderHelper.bind(swipeRevealLayout,
                        String.valueOf(inbox.getInboxId()));
            }

            GlobalUtils.showLog(TAG, "seen status check: " + inbox.isSeen());
            GlobalUtils.showLog(TAG, "msg check for seen: " + inbox.getLastMsg());
            if (!inbox.isSeen()) {
                tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.BOLD);
                tvLastMsg.setTextColor(mContext.getResources().getColor(R.color.charcoal_text));
                tvCustomerName.setTypeface(tvLastMsg.getTypeface(), Typeface.BOLD);
                tvCustomerName.setTextColor(mContext.getResources().getColor(R.color.charcoal_text));
                tvDate.setTextColor(mContext.getResources().getColor(R.color.charcoal_text));
            } else {
                tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.NORMAL);
                tvLastMsg.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                tvCustomerName.setTypeface(tvLastMsg.getTypeface(), Typeface.NORMAL);
                tvCustomerName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                tvDate.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            }

       /*     if (inbox.getParticipantList() != null) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .error(R.drawable.ic_empty_profile_holder_icon);


                if (inbox.getParticipantList() != null && inbox.getParticipantList().get(0) != null) {
                    String firstEmployeeImage = inbox.getParticipantList().get(0).getEmployee().getEmployeeImageUrl();
                    if (firstEmployeeImage != null)
                        Glide.with(mContext)
                                .load(firstEmployeeImage)
                                .apply(options)
                                .into(ivParticipantFirst);
                }

                if (inbox.getParticipantList() != null && inbox.getParticipantList().size() > 1) {
                    String secondEmployeeImage = inbox.getParticipantList().get(1).getEmployee().getEmployeeImageUrl();

                    if (secondEmployeeImage != null)
                        Glide.with(mContext).load(secondEmployeeImage)
                                .apply(options).into(ivParticipantSecond);
                }

                int totalParticipant = inbox.getParticipantList().size();
                tvExtraParticipantNo.setText("+" + (totalParticipant - 2));*/

            String allParticipantName = GlobalUtils.getAllParticipants(inbox);
            if (inbox.getSubject() != null && !inbox.getSubject().isEmpty()) {
                tvCustomerName.setText(inbox.getSubject());
            } else {
                tvCustomerName.setText(allParticipantName);
            }

            if (inbox.getLastMsg() != null && !inbox.getLastMsg().isEmpty()) {
                String mentionPattern = "(?<=@)[\\w]+";
                Pattern p = Pattern.compile(mentionPattern);
                String msg = inbox.getLastMsg();

                Matcher m = p.matcher(msg);
//                    String changed = m.replaceAll("");
                while (m.find()) {
                    GlobalUtils.showLog(TAG, "found: " + m.group(0));
                    String employeeId = m.group(0);
                    Participant participant = ParticipantRepo.getInstance()
                            .getParticipantByEmployeeAccountId(employeeId);
                    if (employeeId != null && participant != null) {
                        SpannableString wordToSpan = new SpannableString(participant.getEmployee().getName());
                        wordToSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),
                                0, wordToSpan.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        GlobalUtils.showLog(TAG, "before: " + msg);
                        msg = msg.replace(employeeId, wordToSpan);
                        GlobalUtils.showLog(TAG, "after: " + msg);
                    }
                }

                boolean isHtml = DetectHtml.isHtml(msg);
                if (isHtml)
                    tvLastMsg.setText(Jsoup.parse(msg).text());
                else tvLastMsg.setText(msg);
            } else {
                tvLastMsg.setVisibility(View.GONE);
                tvDate.setVisibility(View.INVISIBLE);
            }


            if (inbox.getNotificationType().equalsIgnoreCase(
                    InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION.name())) {
                ivMute.setVisibility(View.GONE);
                tvMute.setVisibility(View.VISIBLE);
                tvUnMute.setVisibility(View.GONE);
            } else {
                ivMute.setVisibility(View.VISIBLE);
                tvUnMute.setVisibility(View.VISIBLE);
                tvMute.setVisibility(View.GONE);
            }

            showMessagedDateTime(tvDate, inbox);

            if (inbox.getInboxType() != null && inbox.getInboxType().equalsIgnoreCase(
                    InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {
                tvDelete.setText("Delete");
            }

            Account user = AccountRepo.getInstance().getAccount();
            String userId = user.getAccountId();
            if (inbox.getInboxType() != null && inbox.getInboxType()
                    .equalsIgnoreCase(InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name()) &&
                    userId.equalsIgnoreCase(inbox.getParticipantAdminId())) {
                tvConvertToGroup.setVisibility(View.VISIBLE);
            }

        }
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
                        inboxListFiltered = inboxList;
                    } else {
                        List<Inbox> filteredList = new ArrayList<>();
                        for (Inbox row : inboxList) {
                            String participants = GlobalUtils.getAllParticipants(row);
                            if (row.getSubject().toLowerCase()
                                    .contains(charString.toLowerCase()) ||
                                    participants.toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                     /*       if (row.getSubject().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }*/
                        }
                        inboxListFiltered = filteredList;
                    }

                    filterResults.values = inboxListFiltered;
                    filterResults.count = inboxListFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inboxListFiltered = (List<Inbox>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

    public interface OnItemClickListener {
        void onItemClick(Inbox inbox);
    }

    public void setOnItemClickListener(InboxAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnDeleteClickListener {
        void onDeleteClick(Inbox inbox);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public interface OnMuteClickListener {
        void onMuteClick(Inbox inbox);
    }

    public void setOnMuteClickListener(OnMuteClickListener listener) {
        this.muteClickListener = listener;
    }

    public interface OnUnMuteClickListener {
        void onUnMuteClick(Inbox inbox);

    }

    public void setOnUnMuteClickListener(OnUnMuteClickListener listener) {
        this.unMuteClickListener = listener;
    }


    public interface OnJoinClickListener {
        void onJoinClick(Inbox inbox);

    }

    public void setOnJoinClickListener(OnJoinClickListener listener) {
        this.onJoinClickListener = listener;
    }

    public interface OnConvertToGroupClickListener {
        void onConvertToGroupClick(Inbox inbox);
    }

    public void setOnConvertToGroupClickListener(OnConvertToGroupClickListener listener) {
        this.convertToGroupClickListener = listener;
    }

    public void closeSwipeLayout(String layoutId) {
        viewBinderHelper.closeLayout(layoutId);
    }

}

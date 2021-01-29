package com.treeleaf.anydone.serviceprovider.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.utils.DetectHtml;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.jsoup.Jsoup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "InboxAdapter";
    private List<Inbox> inboxList;
    private Context mContext;
    private OnItemClickListener listener;
    private long mLastClickTime = 0;
    private static final int SINGLE_IMAGE = 1;
    private static final int DOUBLE_IMAGE = 2;
    private static final int MULTIPLE_IMAGE = 3;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public InboxAdapter(List<Inbox> inboxList, Context mContext) {
        this.inboxList = inboxList;
        this.mContext = mContext;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    public void setData(List<Inbox> inboxList) {
        this.inboxList = inboxList;
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
        Inbox inbox = inboxList.get(position);
        int participantSize = inbox.getParticipantList().size();
        switch (participantSize) {
            case 1:
                return SINGLE_IMAGE;

            case 2:
                return DOUBLE_IMAGE;

            default:
                return MULTIPLE_IMAGE;
        }
    }

    @NonNull
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

            default:
                View singleImageItemView = LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.layout_inbox_row_single,
                                parent, false);
                return new InboxAdapter.SingleImageHolder(singleImageItemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Inbox inbox = inboxList.get(position);

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
        }
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
        return inboxList.size();
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
        private ImageView ivParticipant;
        private ImageView ivMute;
        private SwipeRevealLayout swipeRevealLayout;


        SingleImageHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            tvDate = itemView.findViewById(R.id.tv_date);
            container = itemView.findViewById(R.id.rl_holder);
            ivParticipant = itemView.findViewById(R.id.iv_single_participant);
            ivMute = itemView.findViewById(R.id.iv_mute);
            swipeRevealLayout = itemView.findViewById(R.id.srl_single);

            container.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(inboxList.get(position));
                }
            });
        }

        void bind(Inbox inbox) {
            if (swipeRevealLayout != null) {
                viewBinderHelper.bind(swipeRevealLayout,
                        String.valueOf(inbox.getInboxId()));
            }

            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(mContext).load(inbox.getParticipantList().get(0).getEmployee()
                    .getEmployeeImageUrl())
                    .apply(options).into(ivParticipant);

            if (inbox.getSubject() != null && !inbox.getSubject().isEmpty()) {
                tvCustomerName.setText(inbox.getSubject());
            } else {
                tvCustomerName.setText(inbox.getParticipantList().get(0).getEmployee().getName());
            }

            if (inbox.getLastMsg().isEmpty()) {
             /*   tvLastMsg.setText("Attachment");
                tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_attachment_24,
                        0, 0, 0);
                tvLastMsg.setCompoundDrawablePadding(20);*/
                tvLastMsg.setVisibility(View.GONE);
            } else {
                if (!inbox.getLastMsg().isEmpty()) {
                    boolean isHtml = DetectHtml.isHtml(inbox.getLastMsg());
                    if (isHtml) {
                        tvLastMsg.setText(Jsoup.parse(inbox.getLastMsg()).text());
                    } else {
                        tvLastMsg.setText(inbox.getLastMsg());
                    }
                }
                tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            if (inbox.getNotificationType().equalsIgnoreCase(InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION.name())) {
                ivMute.setVisibility(View.VISIBLE);
            } else {
                ivMute.setVisibility(View.GONE);
            }

            GlobalUtils.showLog(TAG, "seen status check: " + inbox.isSeen());
            if (!inbox.isSeen()) {
                tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.BOLD);
            } else {
                tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.NORMAL);
            }

            showMessagedDateTime(tvDate, inbox);
        }
    }

    class DoubleImageHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName;
        private TextView tvLastMsg;
        private TextView tvDate;
        private RelativeLayout container;
        private SwipeRevealLayout swipeRevealLayout;
        private ImageView ivParticipantFirst, ivParticipantSecond;
        private ImageView ivMute;

        DoubleImageHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            tvDate = itemView.findViewById(R.id.tv_date);
            container = itemView.findViewById(R.id.rl_holder);
            ivParticipantFirst = itemView.findViewById(R.id.iv_participant_first);
            ivParticipantSecond = itemView.findViewById(R.id.iv_participant_second);
            ivMute = itemView.findViewById(R.id.iv_mute);
            swipeRevealLayout = itemView.findViewById(R.id.srl_double);

            container.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(inboxList.get(position));
                }
            });
        }

        void bind(Inbox inbox) {
            if (swipeRevealLayout != null) {
                viewBinderHelper.bind(swipeRevealLayout,
                        String.valueOf(inbox.getInboxId()));
            }
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(mContext).load(inbox.getParticipantList().get(0).getEmployee()
                    .getEmployeeImageUrl())
                    .apply(options).into(ivParticipantFirst);

            Glide.with(mContext).load(inbox.getParticipantList().get(1).getEmployee()
                    .getEmployeeImageUrl())
                    .apply(options).into(ivParticipantSecond);

            if (inbox.getSubject() != null && !inbox.getSubject().isEmpty()) {
                tvCustomerName.setText(inbox.getSubject());
            } else {
                String firstParticipant = inbox.getParticipantList().get(0).getEmployee().getName();
                String secondParticipant = inbox.getParticipantList().get(1).getEmployee().getName();

                tvCustomerName.setText(firstParticipant + ", " + secondParticipant);
            }

            if (inbox.getLastMsg().isEmpty()) {
             /*   tvLastMsg.setText("Attachment");
                tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_attachment_24,
                        0, 0, 0);
                tvLastMsg.setCompoundDrawablePadding(20);*/
                tvLastMsg.setVisibility(View.GONE);
            } else {
                if (!inbox.getLastMsg().isEmpty()) {
                    boolean isHtml = DetectHtml.isHtml(inbox.getLastMsg());
                    if (isHtml) {
                        tvLastMsg.setText(Jsoup.parse(inbox.getLastMsg()).text());
                    } else {
                        tvLastMsg.setText(inbox.getLastMsg());
                    }
                }
                tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            if (inbox.getNotificationType().equalsIgnoreCase(
                    InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION.name())) {
                ivMute.setVisibility(View.VISIBLE);
            } else {
                ivMute.setVisibility(View.GONE);
            }

            GlobalUtils.showLog(TAG, "seen status check: " + inbox.isSeen());
            if (!inbox.isSeen()) {
                tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.BOLD);
            } else {
                tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.NORMAL);
            }

            showMessagedDateTime(tvDate, inbox);
        }
    }


    class MultipleImageHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName;
        private TextView tvLastMsg;
        private TextView tvDate;
        private RelativeLayout container;
        private ImageView ivParticipantFirst, ivParticipantSecond;
        private ImageView ivMute;
        private TextView tvExtraParticipantNo;
        private SwipeRevealLayout swipeRevealLayout;

        MultipleImageHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            tvDate = itemView.findViewById(R.id.tv_date);
            container = itemView.findViewById(R.id.rl_holder);
            ivParticipantFirst = itemView.findViewById(R.id.iv_participant_first);
            ivParticipantSecond = itemView.findViewById(R.id.iv_participant_second);
            tvExtraParticipantNo = itemView.findViewById(R.id.tv_extra_participant_number);
            ivMute = itemView.findViewById(R.id.iv_mute);
            swipeRevealLayout = itemView.findViewById(R.id.srl_multiple);

            container.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(inboxList.get(position));
                }
            });
        }

        void bind(Inbox inbox) {
            if (swipeRevealLayout != null) {
                viewBinderHelper.bind(swipeRevealLayout,
                        String.valueOf(inbox.getInboxId()));
            }

            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            String allParticipantName = GlobalUtils.getAllParticipants(inbox);
            String firstEmployeeImage = inbox.getParticipantList().get(0).getEmployee().getEmployeeImageUrl();
            String secondEmployeeImage = inbox.getParticipantList().get(1).getEmployee().getEmployeeImageUrl();

            if (firstEmployeeImage != null)
                Glide.with(mContext)
                        .load(firstEmployeeImage)
                        .apply(options)
                        .into(ivParticipantFirst);

            if (secondEmployeeImage != null)
                Glide.with(mContext).load(secondEmployeeImage)
                        .apply(options).into(ivParticipantSecond);

            int totalParticipant = inbox.getParticipantList().size();
            tvExtraParticipantNo.setText("+" + (totalParticipant - 2));

            if (inbox.getSubject() != null && !inbox.getSubject().isEmpty()) {
                tvCustomerName.setText(inbox.getSubject());
            } else {
                tvCustomerName.setText(allParticipantName);
            }

            if (inbox.getLastMsg().isEmpty()) {
            /*    tvLastMsg.setText("Attachment");
                tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_attachment_24,
                        0, 0, 0);
                tvLastMsg.setCompoundDrawablePadding(20);*/
                tvLastMsg.setVisibility(View.GONE);
            } else {
                if (!inbox.getLastMsg().isEmpty()) {
                    boolean isHtml = DetectHtml.isHtml(inbox.getLastMsg());
                    if (isHtml) {
                        tvLastMsg.setText(Jsoup.parse(inbox.getLastMsg()).text());
                    } else {
                        tvLastMsg.setText(inbox.getLastMsg());
                    }
                }
                tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            if (inbox.getNotificationType().equalsIgnoreCase(
                    InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION.name())) {
                ivMute.setVisibility(View.VISIBLE);
            } else {
                ivMute.setVisibility(View.GONE);
            }

            GlobalUtils.showLog(TAG, "seen status check: " + inbox.isSeen());
            if (!inbox.isSeen()) {
                tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.BOLD);
            } else {
                tvLastMsg.setTypeface(tvLastMsg.getTypeface(), Typeface.NORMAL);
            }

            showMessagedDateTime(tvDate, inbox);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Inbox inbox);

    }

    public void setOnItemClickListener(InboxAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void closeSwipeLayout(String layoutId) {
        viewBinderHelper.closeLayout(layoutId);
    }

}

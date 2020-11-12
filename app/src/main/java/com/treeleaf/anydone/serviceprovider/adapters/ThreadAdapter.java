package com.treeleaf.anydone.serviceprovider.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
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
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.realm.repo.ThreadRepo;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadHolder> {
    private static final String TAG = "ThreadAdapter";
    private List<Thread> threadList;
    private Context mContext;
    private OnItemClickListener listener;

    public ThreadAdapter(List<Thread> threadList, Context mContext) {
        this.threadList = threadList;
        this.mContext = mContext;
    }

    public void setData(List<Thread> threadList) {
        this.threadList = threadList;
        notifyDataSetChanged();
    }


    public void updateThread(String threadId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Thread updatedThread = ThreadRepo.getInstance().getThreadById(threadId);
            int index = threadList.indexOf(updatedThread);
            threadList.set(index, updatedThread);
            notifyItemChanged(index);
        });
    }

    @NonNull
    @Override
    public ThreadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_thread_row, parent,
                false);
        return new ThreadHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadHolder holder, int position) {
        Thread thread = threadList.get(position);

        if (thread.getCustomerImageUrl() != null) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(mContext).load(thread.getCustomerImageUrl())
                    .apply(options).into(holder.civCustomer);
        }

        holder.tvCustomerName.setText(thread.getCustomerName());
        holder.tvLastMsg.setText(thread.getFinalMessage());
        GlobalUtils.showLog(TAG, "seedn status check: " + thread.isSeen());
        if (!thread.isSeen()) {
            holder.tvLastMsg.setTypeface(holder.tvLastMsg.getTypeface(), Typeface.BOLD);
        } else {
            holder.tvLastMsg.setTypeface(holder.tvLastMsg.getTypeface(), Typeface.NORMAL);
        }

        GlobalUtils.showLog(TAG, "current date: " + thread.getLastMessageDate());
        setSourceImg(holder.ivSource, thread);
        showMessagedDateTime(holder.tvDate, thread);


    }

    private void setSourceImg(ImageView ivSource, Thread thread) {
        if (thread.getSource().equalsIgnoreCase(UserProto.ThirdPartySource
                .FACEBOOK_THIRD_PARTY_SOURCE.name())) {
            ivSource.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_messenger));
        } else if (thread.getSource().equalsIgnoreCase(UserProto.ThirdPartySource.VIBER_THIRD_PARTY_SOURCE.name())) {
            ivSource.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_viber));
        } else if (thread.getSource().equalsIgnoreCase(UserProto.ThirdPartySource.SLACK_THIRD_PARTY_SOURCE.name())) {
            ivSource.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_slack));
        }
    }

    private void showMessagedDateTime(TextView tvDate, Thread thread) {
        long lastMsgDate = thread.getLastMessageDate();
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
        return threadList.size();
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

    class ThreadHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName;
        private TextView tvLastMsg;
        private CircleImageView civCustomer;
        private TextView tvDate;
        private ImageView ivSource;
        private RelativeLayout container;

        ThreadHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            civCustomer = itemView.findViewById(R.id.civ_customer);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivSource = itemView.findViewById(R.id.iv_source);
            container = itemView.findViewById(R.id.rl_holder);

            container.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(threadList.get(position));
                }

            });


        }
    }


    public interface OnItemClickListener {
        void onItemClick(Thread thread);

    }

    public void setOnItemClickListener(ThreadAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}

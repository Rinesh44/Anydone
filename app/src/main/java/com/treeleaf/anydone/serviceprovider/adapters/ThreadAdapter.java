package com.treeleaf.anydone.serviceprovider.adapters;


import android.content.Context;
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
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Card;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
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

            holder.tvCustomerName.setText(thread.getCustomerName());
            holder.tvLastMsg.setText(thread.getFinalMessage());

            holder.ivSource.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_messenger));

            //todo logic to show date


        }
    }

    @Override
    public int getItemCount() {
        return threadList.size();
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

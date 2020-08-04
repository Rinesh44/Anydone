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
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

public class OtherServiceAdapter extends RecyclerView.Adapter<OtherServiceAdapter.ServicesHolder> {
    private static final String TAG = "BrowseServiceAdapter";
    private List<Service> otherServiceList;
    private Context mContext;
    private OnItemClickListener listener;

    public OtherServiceAdapter(List<Service> otherServiceList, Context mContext) {
        this.otherServiceList = otherServiceList;
        this.mContext = mContext;
    }

    public void setData(List<Service> otherServiceList) {
        this.otherServiceList = otherServiceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.other_services_row, parent, false);
        return new ServicesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesHolder holder, int position) {
        Service service = otherServiceList.get(position);

        holder.tvServiceName.setText(service.getName().replace("_", " "));

        if (service.getServiceIconUrl() != null) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(mContext).load(service.getServiceIconUrl())
                    .apply(options).into(holder.ivServiceIcon);

        }

    }

    @Override
    public int getItemCount() {
        return otherServiceList.size();
    }


    class ServicesHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceName;
        private RelativeLayout rlServiceHolder;
        private ImageView ivServiceIcon;

        ServicesHolder(@NonNull View itemView) {
            super(itemView);
            rlServiceHolder = itemView.findViewById(R.id.rl_service_holder);
            ivServiceIcon = itemView.findViewById(R.id.iv_service_icon);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);

            rlServiceHolder.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(otherServiceList.get(position));
                }

            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Service service);
    }

    public void setOnItemClickListener(OtherServiceAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}

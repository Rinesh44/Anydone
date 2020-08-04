package com.treeleaf.anydone.serviceprovider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.List;

public class BrowseServiceAdapter extends RecyclerView.Adapter<BrowseServiceAdapter.ServicesHolder>
        implements Filterable {
    private static final String TAG = "BrowseServiceAdapter";
    private List<Service> serviceList;
    private Context mContext;
    private OnItemClickListener listener;
    private List<Service> serviceListFiltered;

    public BrowseServiceAdapter(List<Service> serviceList, Context mContext) {
        this.serviceList = serviceList;
        this.mContext = mContext;
        this.serviceListFiltered = serviceList;
    }

    public void setData(List<Service> serviceList) {
        this.serviceList = serviceList;
        this.serviceListFiltered = serviceList;
    }

    @NonNull
    @Override
    public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_row, parent, false);
        return new ServicesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesHolder holder, int position) {
        Service service = serviceListFiltered.get(position);

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
        if (serviceListFiltered != null) {
            return serviceListFiltered.size();
        } else return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    serviceListFiltered = serviceList;
                } else {
                    List<Service> filteredList = new ArrayList<>();
                    for (Service row : serviceList) {
                        if (row.getName().toLowerCase()
                                .contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    serviceListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = serviceListFiltered;
                return filterResults;

            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                serviceListFiltered = (List<Service>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class ServicesHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceName;
        private ImageView ivServiceIcon;

        ServicesHolder(@NonNull View itemView) {
            super(itemView);
            RelativeLayout rlServiceHolder = itemView.findViewById(R.id.rl_service_holder);
            ivServiceIcon = itemView.findViewById(R.id.iv_service_icon);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);

            rlServiceHolder.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(serviceListFiltered.get(position));
                }

            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Service service);
    }

    public void setOnItemClickListener(BrowseServiceAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}

package com.treeleaf.anydone.serviceprovider.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchServiceAdapter extends RecyclerView.Adapter<SearchServiceAdapter.ServiceHolder>
        implements Filterable {
    private static final String TAG = "SearchServiceAdapter";
    private List<Service> serviceList;
    private Context mContext;
    private OnItemClickListener listener;
    private List<Service> serviceFiltered;

    public SearchServiceAdapter(List<Service> serviceList, Context mContext) {
        this.serviceList = serviceList;
        this.mContext = mContext;
        this.serviceFiltered = serviceList;
    }

    public void setData(List<Service> serviceList) {
        this.serviceList = serviceList;
        this.serviceFiltered = serviceList;
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_service_row, parent, false);
        return new ServiceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {
        Service service = serviceFiltered.get(position);

        holder.tvService.setText(service.getName().replace("_", " "));

        if (service.getServiceIconUrl() != null) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_service_ph)
                    .error(R.drawable.ic_service_ph);

            Glide.with(mContext).load(service.getServiceIconUrl())
                    .apply(options).into(holder.ivService);

        }

    }

    @Override
    public int getItemCount() {
        if (serviceFiltered != null) {
            return serviceFiltered.size();
        } else return 0;
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
                        serviceFiltered = serviceList;
                    } else {
                        List<Service> filteredList = new ArrayList<>();
                        for (Service row : serviceList) {
                            if (row.getName().toLowerCase()
                                    .contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        serviceFiltered = filteredList;
                    }

                    filterResults.values = serviceFiltered;
                    filterResults.count = serviceFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                serviceFiltered = (List<Service>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class ServiceHolder extends RecyclerView.ViewHolder {
        private TextView tvService;
        private ImageView ivService;

        ServiceHolder(@NonNull View itemView) {
            super(itemView);
            LinearLayout llServiceHolder = itemView.findViewById(R.id.ll_service_holder);
            tvService = itemView.findViewById(R.id.tv_service);
            ivService = itemView.findViewById(R.id.iv_service_icon);

            llServiceHolder.setOnClickListener(view -> {

                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onServiceClick(serviceFiltered.get(position));
                }

            });
        }
    }

    public interface OnItemClickListener {
        void onServiceClick(Service service);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
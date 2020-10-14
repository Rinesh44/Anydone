package com.treeleaf.anydone.serviceprovider.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchTicketTypeAdapter extends RecyclerView.Adapter<SearchTicketTypeAdapter.TicketTypeHolder>
        implements Filterable {
    private static final String TAG = "SearchTicketTypeAdapter";
    private List<TicketCategory> ticketTypeList;
    private Context mContext;
    private OnItemClickListener listener;
    private List<TicketCategory> ticketTypeFiltered;

    public SearchTicketTypeAdapter(List<TicketCategory> ticketTypeList, Context mContext) {
        this.ticketTypeList = ticketTypeList;
        this.mContext = mContext;
        this.ticketTypeFiltered = ticketTypeList;
    }

    @NonNull
    @Override
    public TicketTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ticket_type_row, parent, false);
        return new TicketTypeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketTypeHolder holder, int position) {
        TicketCategory ticketCategory = ticketTypeFiltered.get(position);

        holder.tvTicketType.setText(ticketCategory.getName());
    }

    @Override
    public int getItemCount() {
        if (ticketTypeFiltered != null) {
            return ticketTypeFiltered.size();
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
                        ticketTypeFiltered = ticketTypeList;
                    } else {
                        List<TicketCategory> filteredList = new ArrayList<>();
                        for (TicketCategory row : ticketTypeList) {
                            if (row.getName().toLowerCase()
                                    .contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        ticketTypeFiltered = filteredList;
                    }

                    filterResults.values = ticketTypeFiltered;
                    filterResults.count = ticketTypeFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ticketTypeFiltered = (List<TicketCategory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class TicketTypeHolder extends RecyclerView.ViewHolder {
        private TextView tvTicketType;

        TicketTypeHolder(@NonNull View itemView) {
            super(itemView);
            LinearLayout llTicketTypeHolder = itemView.findViewById(R.id.ll_ticket_type_holder);
            tvTicketType = itemView.findViewById(R.id.tv_ticket_type);

            llTicketTypeHolder.setOnClickListener(view -> {

                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onClick(ticketTypeFiltered.get(position));
                }

            });
        }
    }

    public interface OnItemClickListener {
        void onClick(TicketCategory ticketCategory);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

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

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.DependentTicket;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

public class DependentTicketSearchAdapter extends RecyclerView.Adapter<DependentTicketSearchAdapter.TicketHolder>
        implements Filterable {
    private static final String TAG = "DependentTicketSearchAd";
    private List<DependentTicket> dependentTicketList;
    private List<DependentTicket> dependentTicketListFiltered;
    private Context mContext;
    private OnItemClickListener listener;

    public DependentTicketSearchAdapter(@NonNull Context context,
                                        @NonNull List<DependentTicket> dependentTicketList) {
        mContext = context;
        this.dependentTicketList = dependentTicketList;
        this.dependentTicketListFiltered = dependentTicketList;
    }

    public void setData(List<DependentTicket> dependentTickets) {
        dependentTicketList = dependentTickets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dependent_ticket_search_row, parent, false);
        return new TicketHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        DependentTicket ticket = dependentTicketListFiltered.get(position);

        holder.tvIndex.setText("#" + ticket.getIndex());
        holder.tvSummary.setText(ticket.getSummary());
    }

    @Override
    public int getItemCount() {
        if (dependentTicketListFiltered != null) {
            return dependentTicketListFiltered.size();
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
                        dependentTicketListFiltered = dependentTicketList;
                    } else {
                        List<DependentTicket> filteredList = new ArrayList<>();
                        if (!dependentTicketList.isEmpty()) {
                            for (DependentTicket row : dependentTicketList) {
                                if (row.getSummary().toLowerCase()
                                        .contains(charString.toLowerCase()) ||
                                        String.valueOf(row.getIndex()).contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                            }
                        }
                        dependentTicketListFiltered = filteredList;
                    }

                    filterResults.values = dependentTicketListFiltered;
                    filterResults.count = dependentTicketListFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dependentTicketListFiltered = (List<DependentTicket>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class TicketHolder extends RecyclerView.ViewHolder {
        private TextView tvIndex, tvSummary;

        TicketHolder(@NonNull View itemView) {
            super(itemView);
            LinearLayout llTicketHolder = itemView.findViewById(R.id.ll_dependent_ticket_holder);
            tvIndex = itemView.findViewById(R.id.tv_ticket_index);
            tvSummary = itemView.findViewById(R.id.tv_summary);

            llTicketHolder.setOnClickListener(view -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDependentTicketClick(dependentTicketList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onDependentTicketClick(DependentTicket ticket);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
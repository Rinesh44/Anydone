package com.treeleaf.anydone.serviceprovider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

public class LinkedTicketAdapter extends RecyclerView.Adapter<LinkedTicketAdapter.TicketHolder> {
    private static final String TAG = "LinkedTicketAdapter";
    private List<Tickets> ticketsList;
    private Context mContext;
    private TicketsAdapter.OnItemClickListener listener;

    public LinkedTicketAdapter(List<Tickets> ticketsList, Context mContext) {
        this.ticketsList = ticketsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_linked_ticket_row, parent,
                        false);
        return new TicketHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        Tickets tickets = ticketsList.get(position);

        String date = GlobalUtils.getDateNormal(tickets.getCreatedAt());
        String[] dateSeparated = date.split("\\s+");
        holder.tvDate1.setText(dateSeparated[0]);
        holder.tvDate2.setText(dateSeparated[1] + " " + dateSeparated[2]);
        holder.ticketId.setText("#" + tickets.getTicketId());
        holder.summary.setText(tickets.getTitle());

        setStatus(tickets, holder);

        GlobalUtils.showLog(TAG, "ticket priority check: " + tickets.getPriority());

    }

    private void setStatus(Tickets tickets, TicketHolder holder) {
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
    }


    @Override
    public int getItemCount() {
        if (ticketsList != null) {
            return ticketsList.size();
        } else return 0;
    }

    class TicketHolder extends RecyclerView.ViewHolder {
        private TextView tvDate1;
        private TextView tvDate2;
        private RelativeLayout rlTicketHolder;
        private TextView ticketId;
        private TextView summary;
        private TextView ticketStatus;

        TicketHolder(@NonNull View itemView) {
            super(itemView);
            rlTicketHolder = itemView.findViewById(R.id.rl_ticket_holder);
            tvDate1 = itemView.findViewById(R.id.date1);
            tvDate2 = itemView.findViewById(R.id.date2);
            ticketId = itemView.findViewById(R.id.tv_ticket_id_value);
            summary = itemView.findViewById(R.id.tv_summary);
            ticketStatus = itemView.findViewById(R.id.tv_ticket_status);

            if (rlTicketHolder != null) {
                rlTicketHolder.setOnClickListener(view -> {
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

}

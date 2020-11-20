package com.treeleaf.anydone.serviceprovider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.model.Message;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

public class TicketHistoryAdapter extends RecyclerView.Adapter<TicketHistoryAdapter.MessageHolder> {
    private static final String TAG = "TicketHistoryAdapter";
    private List<Message> messageList;
    private Context mContext;
    private List<Message> messageListFiltered;
    private String messageId;

    public TicketHistoryAdapter(List<Message> messageList, String messageId,
                                Context mContext) {
        this.messageList = messageList;
        this.mContext = mContext;
        this.messageListFiltered = messageList;
        this.messageId = messageId;
    }


    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_preview_row, parent, false);
        return new MessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message message = messageListFiltered.get(position);

        holder.tvMessage.setText(message.getMessageText());
        String date = GlobalUtils.getDateNormal(message.getTimestamp());
        String time = GlobalUtils.getTimeExcludeMillis(message.getTimestamp());

        holder.tvSentAt.setText(date + " " + time);

        if (message.getMessageId().equalsIgnoreCase(messageId)) {
            holder.tvMessage.setTextColor(mContext.getResources().getColor(R.color.service_status_green));
            holder.tvMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ticket_highlight, 0);
            holder.tvMessage.setCompoundDrawablePadding(50);
        }

    }

    @Override
    public int getItemCount() {
        if (messageListFiltered != null) {
            return messageListFiltered.size();
        } else return 0;
    }


    class MessageHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage;
        private TextView tvSentAt;

        MessageHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvSentAt = itemView.findViewById(R.id.tv_sent_at);

        }
    }

}

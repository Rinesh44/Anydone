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
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;

import java.util.List;

public class SearchedListAdapter extends RecyclerView.Adapter<SearchedListAdapter.SearchedListHolder> {
    private static final String TAG = "SearchedListAdapter";
    private List<Conversation> conversationList;
    private Context mContext;

    public SearchedListAdapter(List<Conversation> conversationList, Context mContext) {
        this.conversationList = conversationList;
        this.mContext = mContext;
    }

    public void setData(List<Conversation> newList) {
        this.conversationList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_searched_results, parent, false);
        return new SearchedListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedListHolder holder, int position) {
        Conversation convo = conversationList.get(position);


    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }


    class SearchedListHolder extends RecyclerView.ViewHolder {
        private TextView tvSenderName;
        private TextView tvMsg;
        private TextView tvDate;
        private RelativeLayout rlHolder;

        SearchedListHolder(@NonNull View itemView) {
            super(itemView);

            tvSenderName = itemView.findViewById(R.id.tv_sender_name);
            tvMsg = itemView.findViewById(R.id.tv_msg);
            tvDate = itemView.findViewById(R.id.tv_date);
            rlHolder = itemView.findViewById(R.id.rl_text_holder);

            rlHolder.setOnClickListener(view -> {
       /*         int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(currencyListFiltered.get(position));
                }*/

            });
        }
    }
}

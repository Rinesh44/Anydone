package com.treeleaf.januswebrtc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class JoineeListAdapter extends RecyclerView.Adapter<JoineeListAdapter.ViewHolder> {

    private List<Joinee> joinees;
    private Context mContext;
    public static final Integer MAX_IN_A_ROW = 6;
    private LinkedHashMap<String, Joinee> mapRemainingJoinees;
    private LinkedHashMap<String, Joinee> mapTotalJoinees;
    private JoineeListToggleUpdate joineeListToggleUpdate;
    public JoineeListState joineeListState;
    private OnItemClickListener onItemClickListener;

    public JoineeListAdapter(Context context) {
        this.mContext = context;
        joinees = new ArrayList<>();
        mapRemainingJoinees = new LinkedHashMap<>();
        mapTotalJoinees = new LinkedHashMap<>();
        joineeListState = JoineeListState.CONTRACT;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Boolean isJoineePresent() {
        return mapTotalJoinees.size() > 0;
    }

    public void setJoineeListToggleUpdate(JoineeListToggleUpdate joineeListToggleUpdate) {
        this.joineeListToggleUpdate = joineeListToggleUpdate;
    }

    public void addNewJoinee(Joinee joinee, Boolean showFullList) {
        mapTotalJoinees.put(joinee.getAccountId(), joinee);
        if (showFullList) {
            joinees.clear();
            joinees.addAll(mapTotalJoinees.values());
        } else {
            if (!joineesCountExceededMax()) {
                joinees.clear();
                joinees.addAll(mapTotalJoinees.values());
            } else {
                mapRemainingJoinees.put(joinee.getAccountId(), joinee);
            }
        }
        notifyDataSetChanged();
        joineeListToggleUpdate.onShowHideJoineeList(mapTotalJoinees.size() > 0);
        joineeListToggleUpdate.onShowHideToggleIcon(mapTotalJoinees.size() > MAX_IN_A_ROW);
    }

    private void fillJoinees(Boolean showFullList) {
        if (showFullList) {
            joinees.clear();
            joinees.addAll(mapTotalJoinees.values());
        } else {
            Iterator<String> iterator = mapTotalJoinees.keySet().iterator();
            int i = 0;
            joinees.clear();
            if (mapTotalJoinees.size() > 0) {
                while (i < MAX_IN_A_ROW) {
                    joinees.add(mapTotalJoinees.get(iterator.next()));
                    i++;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeJoinee(String accountId, Boolean showFullList) {
        if (mapTotalJoinees != null && !mapTotalJoinees.isEmpty()) {
            Joinee joineeToRemove = mapTotalJoinees.get(accountId);
            if (joineeToRemove != null) {
                if (mapRemainingJoinees.containsValue(joineeToRemove))
                    mapRemainingJoinees.remove(accountId);
                if (mapTotalJoinees.containsValue(joineeToRemove))
                    mapTotalJoinees.remove(accountId);
            }
            fillJoinees(showFullList);

            if (mapTotalJoinees.size() == 0) {
                joineeListToggleUpdate.onShowHideJoineeList(false);
            }
            if (mapTotalJoinees.size() <= MAX_IN_A_ROW) {
                //call callback
                joineeListToggleUpdate.onShowHideToggleIcon(false);
            }
        }
    }

    public boolean joineesCountExceededMax() {
        return mapTotalJoinees.size() > MAX_IN_A_ROW;
    }

    public void toggleJoineeList(boolean showFullList) {
        fillJoinees(showFullList);
        joineeListState = showFullList ? JoineeListState.EXPAND : JoineeListState.CONTRACT;
        joineeListToggleUpdate.onListExpandContract(showFullList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joinee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Joinee joinee = joinees.get(position);
        if (joinee != null && !joinee.getProfileUrl().isEmpty()) {
            String imgUri = joinee.getProfileUrl();
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(mContext)
                    .load(imgUri)
                    .apply(options)
                    .circleCrop()
                    .into(holder.ivJoinee);
            if (position == (MAX_IN_A_ROW - 1) && joineeListState == (JoineeListState.CONTRACT) && joineesCountExceededMax()) {
                holder.tvCountAdditionalJoinees.setVisibility(View.VISIBLE);
                int remaining = (mapTotalJoinees.size() - (MAX_IN_A_ROW - 1));
                holder.tvCountAdditionalJoinees.setText("+" + remaining);
            } else {
                holder.tvCountAdditionalJoinees.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClicked(position, holder.itemView,
                                joinee.getAccountId(), joinee.getName());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return joinees.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ImageView ivJoinee;
        public TextView tvCountAdditionalJoinees;

        ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivJoinee = itemView.findViewById(R.id.iv_joinee);
            tvCountAdditionalJoinees = itemView.findViewById(R.id.tv_count_additional_joinees);
        }
    }

    public interface JoineeListToggleUpdate {

        void onListExpandContract(Boolean expand);

        void onShowHideToggleIcon(Boolean show);

        void onShowHideJoineeList(Boolean show);

    }

    public enum JoineeListState {
        EXPAND, CONTRACT
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, View v, String accountId, String accountName);
    }

}

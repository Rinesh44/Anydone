package com.treeleaf.anydone.serviceprovider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantSelectionAdapter extends RecyclerView.Adapter<ParticipantSelectionAdapter.ParticipantHolder> {
    private static final String TAG = "ParticipantSelectionAda";
    private List<String> participantList;
    private Context mContext;
    private OnItemClickListener listener;

    public ParticipantSelectionAdapter(List<String> participantList, Context mContext) {
        this.participantList = participantList;
        this.mContext = mContext;
    }

    public void setData(List<String> participantList) {
        this.participantList = participantList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParticipantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_participant_selected, parent, false);
        return new ParticipantHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantHolder holder, int position) {
        String participantId = participantList.get(position);
        GlobalUtils.showLog(TAG, "selected party: " + participantId);
        AssignEmployee employee = AssignEmployeeRepo.getInstance().getAssignedEmployeeById(participantId);

        holder.tvParticipantName.setText(employee.getName());

        Glide.with(mContext)
                .load(employee.getEmployeeImageUrl())
                .error(R.drawable.ic_empty_profile_holder_icon)
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .into(holder.civParticipant);

    }

    @Override
    public int getItemCount() {
        if (participantList != null) {
            return participantList.size();
        } else return 0;
    }


    class ParticipantHolder extends RecyclerView.ViewHolder {
        private TextView tvParticipantName;
        private CircleImageView civParticipant;
        private ImageView ivRemove;

        ParticipantHolder(@NonNull View itemView) {
            super(itemView);
            tvParticipantName = itemView.findViewById(R.id.tv_participant);
            civParticipant = itemView.findViewById(R.id.civ_participant);
            ivRemove = itemView.findViewById(R.id.iv_close);

            ivRemove.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(participantList.get(position));
                }

            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(String participantId);
    }

    public void setOnItemClickListener(ParticipantSelectionAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}


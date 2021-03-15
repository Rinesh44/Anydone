package com.treeleaf.anydone.serviceprovider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantHolder> {
    private static final String TAG = "ParticipantAdapter";
    private List<Participant> participantList;
    private Context mContext;
    private OnMuteClickListener listener;
    private OnUnMuteClickListener unMuteClickListener;
    private OnDeleteClickListener onDeleteClickListener;
    private String inboxType;
    private boolean hasLeft;

    public ParticipantAdapter(List<Participant> participantList, Context mContext, String inboxType,
                              boolean hasLeft) {
        this.participantList = participantList;
        this.mContext = mContext;
        this.inboxType = inboxType;
        this.hasLeft = hasLeft;
    }

    public void setData(List<Participant> newParticipants) {
        this.participantList = newParticipants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParticipantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_participant_row, parent, false);
        return new ParticipantHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantHolder holder, int position) {
        Participant participant = participantList.get(position);

        holder.tvParticipant.setText(participant.getEmployee().getName());
        Glide.with(mContext)
                .load(participant.getEmployee()
                        .getEmployeeImageUrl())
                .error(R.drawable.ic_empty_profile_holder_icon)
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .into(holder.civParticipant);

        if (participant.getRole().equalsIgnoreCase
                (InboxProto.InboxParticipant.InboxRole.INBOX_ADMIN.name())) {
            holder.tvAdminTag.setVisibility(View.VISIBLE);
        } else holder.tvAdminTag.setVisibility(View.GONE);


        Account account = AccountRepo.getInstance().getAccount();

        if (!account.getAccountId().equalsIgnoreCase(participant.getEmployee().getAccountId())) {
            holder.ivMore.setVisibility(View.VISIBLE);
            //creating a popup menu
            PopupMenu popup = new PopupMenu(mContext, holder.ivMore);
            //inflating menu from xml resource
            popup.inflate(R.menu.menu_participant);
            //adding click listener

            String role = participant.getRole();
            String participantNotification = participant.getNotificationType();
            if (role.equalsIgnoreCase(InboxProto.InboxParticipant.InboxRole.INBOX_ADMIN.name())) {
                //hide mute actions for admin
                popup.getMenu().getItem(2).setVisible(false);
            }

            if (participantNotification.equalsIgnoreCase(InboxProto.InboxNotificationType
                    .MUTED_INBOX_NOTIFICATION.name())) {
                holder.ivMute.setVisibility(View.VISIBLE);
                popup.getMenu().getItem(0).setVisible(false);
                popup.getMenu().getItem(1).setVisible(true);
            } else {
                holder.ivMute.setVisibility(View.GONE);
            }


            //hide delete option in case inbox is direct message
            if (inboxType.equalsIgnoreCase(InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {
                popup.getMenu().getItem(2).setVisible(false);
            }

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu1:
                        //handle menu1 click
                        if (listener != null) {
                            listener.onMuteClick(participant, participantList.indexOf(participant));
                        }
                        return true;
                    case R.id.menu2:
                        //handle menu2 click
                        if (unMuteClickListener != null) {
                            unMuteClickListener.onUnMuteClick(participant, participantList.indexOf(participant));
                        }
                        return true;
                    case R.id.menu3:
                        //handle menu3 click
                        if (onDeleteClickListener != null) {
                            onDeleteClickListener.onDeleteClicked(participant,
                                    participantList.indexOf(participant));
                        }
                        return true;
                    default:
                        return false;
                }
            });

            holder.ivMore.setOnClickListener(v -> popup.show());

        } else holder.ivMore.setVisibility(View.GONE);

        if (hasLeft) {
            holder.ivMore.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }


    class ParticipantHolder extends RecyclerView.ViewHolder {
        private CircleImageView civParticipant;
        private TextView tvParticipant;
        private TextView tvAdminTag;
        private ImageView ivMore;
        private ImageView ivMute;

        ParticipantHolder(@NonNull View itemView) {
            super(itemView);
            civParticipant = itemView.findViewById(R.id.civ_participant);
            tvParticipant = itemView.findViewById(R.id.tv_participant);
            tvAdminTag = itemView.findViewById(R.id.tv_admin_tag);
            ivMore = itemView.findViewById(R.id.iv_more);
            ivMute = itemView.findViewById(R.id.iv_mute);

            ivMore.setOnClickListener(view -> {
       /*         int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(currencyListFiltered.get(position));
                }*/

            });
        }
    }


    public interface OnMuteClickListener {
        void onMuteClick(Participant participant, int pos);
    }

    public void setOnMuteClickListener(OnMuteClickListener listener) {
        this.listener = listener;
    }

    public interface OnUnMuteClickListener {
        void onUnMuteClick(Participant participant, int pos);
    }

    public void setOnUnMuteClickListener(OnUnMuteClickListener listener) {
        this.unMuteClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClicked(Participant participant, int pos);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteListener) {
        this.onDeleteClickListener = onDeleteListener;
    }

}


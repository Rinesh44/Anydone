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
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonMentionAdapter extends RecyclerView.Adapter<PersonMentionAdapter.ParticipantHolder>
        implements Filterable {
    private static final String TAG = "PersonMentionAdapter";
    private List<Participant> participantList;
    private List<Participant> participantListFiltered;
    private Context mContext;
    private OnItemClickListener listener;

    public PersonMentionAdapter(List<Participant> participantList, Context mContext) {
        this.participantList = participantList;
        this.participantListFiltered = participantList;
        this.mContext = mContext;
    }

    public void setData(List<Participant> participantList) {
        this.participantList = participantList;
        this.participantListFiltered = participantList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParticipantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_mention_row, parent, false);
        return new ParticipantHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantHolder holder, int position) {
        Participant participant = participantListFiltered.get(position);
        holder.tvName.setText(participant.getEmployee().getName());

        Glide.with(mContext)
                .load(participant.getEmployee().getEmployeeImageUrl())
                .error(R.drawable.ic_empty_profile_holder_icon)
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .into(holder.civImage);
    }

    @Override
    public int getItemCount() {
        if (participantListFiltered != null) {
            return participantListFiltered.size();
        } else return 0;
    }


    class ParticipantHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private CircleImageView civImage;
        private LinearLayout llHolder;

        ParticipantHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_participant);
            civImage = itemView.findViewById(R.id.civ_participant);
            llHolder = itemView.findViewById(R.id.ll_holder);

            llHolder.setOnClickListener(view -> {
                int position = getAdapterPosition();

                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(participantListFiltered.get(position));
                }

            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charSequence.toString().contains("@"))
                    charString = charSequence.toString().replace("@", "");
                GlobalUtils.showLog(TAG, "char st checK: " + charSequence.toString());
                FilterResults filterResults = new FilterResults();
                String finalCharString = charString;
                ((Activity) mContext).runOnUiThread(() -> {
                    if (finalCharString.isEmpty()) {
                        participantListFiltered = participantList;
                    } else {
                        List<Participant> filteredList = new ArrayList<>();
                        if (!participantList.isEmpty()) {
                            for (Participant participant : participantList) {
                                if (participant.getEmployee().getName().toLowerCase()
                                        .contains(finalCharString.toLowerCase())) {
                                    GlobalUtils.showLog(TAG, "match found");
                                    filteredList.add(participant);
                                }
                            }
                        }
                        participantListFiltered = filteredList;
                    }

                    filterResults.values = participantListFiltered;
                    filterResults.count = participantListFiltered.size();
                });
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                participantListFiltered = (List<Participant>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface OnItemClickListener {
        void onItemClick(Participant participant);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

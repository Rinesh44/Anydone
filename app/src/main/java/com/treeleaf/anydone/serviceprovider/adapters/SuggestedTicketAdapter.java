package com.treeleaf.anydone.serviceprovider.adapters;


import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketSuggestion;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestedTicketAdapter extends RecyclerView.Adapter<SuggestedTicketAdapter.SuggestedTicketHolder> {
    private static final String TAG = "SuggestedTicketAdapter";
    private List<TicketSuggestion> suggestionList;
    private Context mContext;
    private OnItemClickListener listener;
    private List<TicketSuggestion> suggestionFiltered;
    private boolean selectAllItems = false;

    public SuggestedTicketAdapter(List<TicketSuggestion> suggestionList, Context mContext) {
        this.suggestionList = suggestionList;
        this.mContext = mContext;
        this.suggestionFiltered = suggestionList;
    }

    public void removeSuggestion(TicketSuggestion suggestion) {
        int index = suggestionFiltered.indexOf(suggestion);
        suggestionFiltered.remove(suggestion);
        notifyItemRemoved(index);
    }

    public void removeSuggestionList(List<TicketSuggestion> suggestionList) {
        suggestionFiltered.removeAll(suggestionList);
        notifyDataSetChanged();
    }

    public void selectAll(boolean selectAll) {
        GlobalUtils.showLog(TAG, "select all: " + selectAll);
        selectAllItems = selectAll;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SuggestedTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggested_ticket_row, parent, false);
        return new SuggestedTicketHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedTicketHolder holder, int position) {
        TicketSuggestion suggestion = suggestionFiltered.get(position);

        holder.tvCustomer.setText(suggestion.getCustomerName());
        holder.tvMessage.setText(suggestion.getMessageText());
        holder.cbMessage.setChecked(selectAllItems);

        if (suggestion.getCustomerImageUrl() != null) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(mContext).load(suggestion.getCustomerImageUrl())
                    .apply(options).into(holder.civCustomer);

        }

        setSourceImg(holder.ivSource, suggestion);
        showMessagedDateTime(holder.sentAt, suggestion);
    }

    @Override
    public int getItemCount() {
        if (suggestionFiltered != null) {
            return suggestionFiltered.size();
        } else return 0;
    }


    private void showMessagedDateTime(TextView tvDate, TicketSuggestion suggestion) {
        long lastMsgDate = suggestion.getMessageSentAt();
        Date date = new Date();
        date.setTime(lastMsgDate);
        if (DateUtils.isToday(lastMsgDate)) {
            tvDate.setText(GlobalUtils.getTimeExcludeMillis(lastMsgDate));

            //check for yesterday
        } else if (DateUtils.isToday(lastMsgDate + DateUtils.DAY_IN_MILLIS)) {
            tvDate.setText(R.string.yesterday);
        } else if (isDateInCurrentWeek(date) != -1) {
            switch (isDateInCurrentWeek(date)) {
                case 1:
                    tvDate.setText(R.string.sun);
                    break;

                case 2:
                    tvDate.setText(R.string.mon);
                    break;

                case 3:
                    tvDate.setText(R.string.tue);
                    break;

                case 4:
                    tvDate.setText(R.string.wed);
                    break;

                case 5:
                    tvDate.setText(R.string.thu);
                    break;

                case 6:
                    tvDate.setText(R.string.fri);
                    break;

                case 7:
                    tvDate.setText(R.string.sat);
                    break;

                default:
                    break;
            }

        } else {
            tvDate.setText(GlobalUtils.getDateShort(lastMsgDate));
        }
    }

    private void setSourceImg(ImageView ivSource, TicketSuggestion suggestion) {
        if (suggestion.getSource().equalsIgnoreCase(UserProto.ThirdPartySource
                .FACEBOOK_THIRD_PARTY_SOURCE.name())) {
            ivSource.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_messenger));
        } else if (suggestion.getSource().equalsIgnoreCase(UserProto.ThirdPartySource.VIBER_THIRD_PARTY_SOURCE.name())) {
            ivSource.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_viber));
        } else if (suggestion.getSource().equalsIgnoreCase(UserProto.ThirdPartySource.SLACK_THIRD_PARTY_SOURCE.name())) {
            ivSource.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_slack));
        } else if (suggestion.getSource().equalsIgnoreCase(UserProto.ThirdPartySource.SLACK_THIRD_PARTY_SOURCE.name())) {
            ivSource.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_link_email));
        }
    }

    public static int isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        int day = targetCalendar.get(Calendar.DAY_OF_WEEK);
        if (week == targetWeek && year == targetYear) {
            return day;
        }
        return -1;
    }


    class SuggestedTicketHolder extends RecyclerView.ViewHolder {
        private CheckBox cbMessage;
        private CircleImageView civCustomer;
        private TextView tvCustomer;
        private ImageView ivSource;
        private TextView sentAt;
        private ImageView ivAccept;
        private ImageView ivReject;
        private TextView tvMessage;
        private RelativeLayout messageHolder;

        SuggestedTicketHolder(@NonNull View itemView) {
            super(itemView);

            cbMessage = itemView.findViewById(R.id.cb_ticket);
            civCustomer = itemView.findViewById(R.id.civ_employee);
            tvCustomer = itemView.findViewById(R.id.tv_employee_name);
            ivSource = itemView.findViewById(R.id.iv_source);
            sentAt = itemView.findViewById(R.id.tv_sent_at);
            ivAccept = itemView.findViewById(R.id.iv_accept);
            ivReject = itemView.findViewById(R.id.iv_reject);
            tvMessage = itemView.findViewById(R.id.tv_message);
            messageHolder = itemView.findViewById(R.id.rl_message_holder);


            messageHolder.setOnClickListener(v -> {
                if (listener != null) {
                    TicketSuggestion suggestion = suggestionFiltered.get(getAdapterPosition());
                    listener.showHistory(suggestion.getSuggestionId(), suggestion.getMessageId(),
                            suggestion.getCustomerName(), suggestion.getCustomerImageUrl(),
                            suggestion.getMessageText(), suggestion.getMessageSentAt(),
                            suggestion.getSource());
                }
            });

//            cbMessage.setOnClickListener(view -> cbMessage.setChecked(!cbMessage.isChecked()));

            cbMessage.setOnClickListener(v -> {
                if (cbMessage.isChecked()) {
                    listener.onItemAdd(suggestionFiltered.get(getAdapterPosition()));
                } else {
                    listener.onItemRemove(suggestionFiltered.get(getAdapterPosition()));
                }
            });

         /*   cbMessage.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    GlobalUtils.showLog(TAG, "check listened");
                    listener.onItemAdd(suggestionFiltered.get(getAdapterPosition()));
                } else {
                    GlobalUtils.showLog(TAG, "uncheck listened");
                    listener.onItemRemove(suggestionFiltered.get(getAdapterPosition()));
                }
            });*/

            ivAccept.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onAccept(suggestionFiltered.get(getAdapterPosition()));
                }
            });

            ivReject.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onReject(suggestionFiltered.get(getAdapterPosition()));
                }
            });

        }
    }


    public interface OnItemClickListener {
        void onItemAdd(TicketSuggestion ticketSuggestion);

        void onItemRemove(TicketSuggestion ticketSuggestion);

        void onAccept(TicketSuggestion ticketSuggestion);

        void onReject(TicketSuggestion ticketSuggestion);

        void showHistory(String ticketSuggestionId, String msgId, String customerName, String
                customerImage, String messageText, long sentAt, String source);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}



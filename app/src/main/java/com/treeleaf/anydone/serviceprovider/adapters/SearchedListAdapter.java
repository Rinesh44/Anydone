package com.treeleaf.anydone.serviceprovider.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchedListAdapter extends RecyclerView.Adapter<SearchedListAdapter.SearchedListHolder> {
    private static final String TAG = "SearchedListAdapter";
    private List<Conversation> conversationList;
    private Context mContext;
    private long mLastClickTime = 0;
    private OnItemClickListener listener;
    private String searchText;


    public SearchedListAdapter(List<Conversation> conversationList, Context mContext) {
        this.conversationList = conversationList;
        this.mContext = mContext;
    }

    public void setData(List<Conversation> newList, String searchText) {
        this.searchText = searchText;
        this.conversationList = newList;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.conversationList.clear();
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

        holder.tvSenderName.setText(convo.getSenderName() + ": ");
        String htmlParsedMsg = Jsoup.parse(convo.getMessage()).text();
        holder.tvMsg.setText(htmlParsedMsg);

        showDateAndTime(convo.getSentAt(), holder.tvDate);

        if (searchText.length() > 0) {
            GlobalUtils.showLog(TAG, "inside search filter");
            //color your text here
            SpannableStringBuilder sb = new SpannableStringBuilder(htmlParsedMsg);
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(htmlParsedMsg.toLowerCase());

            while (match.find()) {
                ForegroundColorSpan fcs = new ForegroundColorSpan(
                        ContextCompat.getColor(mContext, R.color.colorPrimary)); //specify color here
                sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            holder.tvMsg.setText(sb);
        }
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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(conversationList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Conversation conversation);
    }

    public void setOnItemClickListener(SearchedListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("SetTextI18n")
    private void showDateAndTime(long sentAt, TextView tvSentAt) {
       /* @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter =
                new SimpleDateFormat("dd MMM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter =
                new SimpleDateFormat("hh:mm a");

        String dateString = dateFormatter.format(new Date(sentAt));
        String timeString = timeFormatter.format(new Date(sentAt));

        tvSentAt.setText(dateString + " At " + timeString);
        tvSentAt.setVisibility(View.VISIBLE);*/

        tvSentAt.setVisibility(View.GONE);
        if (DateUtils.isToday(sentAt)) {
            tvSentAt.setText(R.string.today);
            tvSentAt.setVisibility(View.VISIBLE);

        } else if (DateUtils.isToday(sentAt + DateUtils.DAY_IN_MILLIS)) {//check for yesterday
            tvSentAt.setText(R.string.yesterday);
            tvSentAt.setVisibility(View.VISIBLE);
        } else {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter =
                    new SimpleDateFormat("dd MMM");

            String dateString = dateFormatter.format(new Date(sentAt));
            tvSentAt.setText(dateString);
            tvSentAt.setVisibility(View.VISIBLE);
        }
    }
}

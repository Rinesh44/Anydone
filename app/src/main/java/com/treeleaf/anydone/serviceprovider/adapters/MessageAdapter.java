package com.treeleaf.anydone.serviceprovider.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.protobuf.InvalidProtocolBufferException;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.KGraphProto;
import com.treeleaf.anydone.serviceprovider.AnyDoneServiceProviderApplication;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.KGraph;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.DetectHtml;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
import io.realm.RealmList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MessageAdapter";
    public static final int MSG_TEXT_LEFT = 0;
    public static final int MSG_TEXT_RIGHT = 1;
    public static final int MSG_IMG_LEFT = 2;
    public static final int MSG_IMG_RIGHT = 3;
    public static final int MSG_LINK_LEFT = 4;
    public static final int MSG_LINK_RIGHT = 5;
    public static final int MSG_DOC_LEFT = 6;
    public static final int MSG_DOC_RIGHT = 7;
    public static final int MSG_ACCEPTED_TAG = 8;
    public static final int MSG_SERVICE_DOERS_TAG = 9;
    public static final int MSG_BOT_SUGGESTIONS = 10;
    public static final int INITIAL_SERVICE_DETAIL = 11;
    public static final int MSG_CALL_OUTGOING = 12;
    public static final int INITIAL_TICKET_DETAIL = 13;
    public static final int MSG_BOT_SUGGESTION_JSON = 14;


    private List<Conversation> conversationList;
    private Context mContext;
    private MessageAdapter.OnItemLongClickListener listener;
    private MessageAdapter.OnImageClickListener imageClickListener;
    private MessageAdapter.OnMessageNotDeliveredListener messageNotDeliveredListener;
    private MessageAdapter.OnSenderImageClickListener senderImageClickListener;
    private MessageAdapter.OnSuggestionClickListener suggestionClickListener;
    private MessageAdapter.OnBackClickListener onBackClickListener;

    public MessageAdapter(List<Conversation> conversationList, Context mContext) {
        this.conversationList = conversationList;
        this.mContext = mContext;
    }

    public void setData(Conversation conversation) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Conversation existingConversation = getConversationIfExists(conversation);
            GlobalUtils.showLog(TAG, "existing conversation status: " + existingConversation);
            if (existingConversation != null) {
                int index = conversationList.indexOf(existingConversation);
                conversationList.set(index, conversation);
                notifyItemChanged(index);
            } else {
                conversationList.add(0, conversation);
                notifyItemInserted(0);
            }

            if (!conversation.getMessageType().equals("MSG_BOT_SUGGESTIONS")) {
                if (conversationList.size() > 0) {
                    GlobalUtils.showLog(TAG, "prev index refreshed");
                    int prevIndex = conversationList.indexOf(conversation) + 1;
                    notifyItemChanged(prevIndex);
                }
            }

        });
    }

    public void setAcceptedTAG(Conversation conversation) {
        conversationList.add(conversation);
        Collections.sort(conversationList, (o1, o2) ->
                Long.compare(o2.getSentAt(), o1.getSentAt()));
        notifyDataSetChanged();
    }

    public void setAssignedEmployeesView(List<Conversation> conversations) {
        conversationList.addAll(conversations);
        Collections.sort(conversationList, (o1, o2) ->
                Long.compare(o2.getSentAt(), o1.getSentAt()));
        notifyDataSetChanged();
    }


    public void setInitialData(Conversation conversation) {
        new Handler(Looper.getMainLooper()).post(() -> {
            conversationList.add(conversationList.size(), conversation);
            notifyItemInserted(conversationList.size());
        });
    }


    public void setData(List<Conversation> newConversationList) {
        if (!CollectionUtils.isEmpty(newConversationList)) {
            GlobalUtils.showLog(TAG, "conversation list checkout: " +
                    newConversationList.size());
            conversationList.addAll(0, newConversationList);
            notifyItemRangeInserted(0, newConversationList.size());
        }
    }

    private Conversation getConversationIfExists(Conversation conversation) {
        for (Conversation existingConversation : conversationList
        ) {
            if (existingConversation.getClientId().equalsIgnoreCase(conversation.getClientId())) {
                return existingConversation;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GlobalUtils.showLog(TAG, "view type check: " + viewType);
        switch (viewType) {
            case MSG_BOT_SUGGESTION_JSON:
                View botSuggestionJSON = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_json, parent, false);
                return new BotSuggestionJSONHolder(botSuggestionJSON);

            case MSG_TEXT_LEFT:
                View leftTextView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_text_left, parent, false);
                return new LeftTextHolder(leftTextView);

            case MSG_TEXT_RIGHT:
                View rightTextView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_text_right, parent, false);
                return new RightTextHolder(rightTextView);

            case MSG_IMG_LEFT:
                View leftImageView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_image_left, parent, false);
                return new LeftImageHolder(leftImageView);

            case MSG_IMG_RIGHT:
                View rightImageView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_image_right, parent, false);
                return new RightImageHolder(rightImageView);

            case MSG_LINK_LEFT:
                View leftLinkView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_link_left, parent, false);
                return new LeftLinkHolder(leftLinkView);

            case MSG_LINK_RIGHT:
                View rightLinkView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_link_right, parent, false);
                return new RightLinkHolder(rightLinkView);

            case MSG_DOC_LEFT:
                View leftDocView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_doc_left, parent, false);
                return new LeftDocHolder(leftDocView);

            case MSG_DOC_RIGHT:
                View rightDocView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_doc_right, parent, false);
                return new RightDocHolder(rightDocView);

            case MSG_ACCEPTED_TAG:
                View acceptedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_accepted_tag, parent, false);
                return new AcceptedViewHolder(acceptedView);

            case MSG_SERVICE_DOERS_TAG:
                View serviceDoerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_service_doers, parent, false);
                return new ServiceDoerViewHolder(serviceDoerView);

            case MSG_BOT_SUGGESTIONS:
                View suggestionsView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_bot_suggestions, parent, false);
                return new BotSuggestionsHolder(suggestionsView);

            case INITIAL_SERVICE_DETAIL:
                View serviceDetailView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.initial_service_detail, parent, false);
                return new InitialServiceDetailHolder(serviceDetailView);

            case INITIAL_TICKET_DETAIL:
                View ticketDetailView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_initial_ticket_detail, parent, false);
                return new InitialTicketDetailHolder(ticketDetailView);

            case MSG_CALL_OUTGOING:
                View callView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_calls, parent, false);
                return new CallViewHolder(callView);

            default:
                View defaultView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_text_right, parent, false);
                return new RightTextHolder(defaultView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);

        GlobalUtils.showLog(TAG, "refid check: " + conversation.getRefId());

        boolean isContinuous = false;
        boolean isNewDay = false;
        boolean isShowTime = false;

        // If there is at least one item preceding the current one, check the previous message.
        if (position < conversationList.size() - 1) {
            Conversation prevMessage = conversationList.get(position + 1);

            long timeDiff = conversation.getSentAt() - prevMessage.getSentAt();
            // If the date of the previous message is different, display the date before the message,
            // and also set isContinuous to false to show information such as the sender's name
            // and profile image.
            if (!isSameDay(conversation.getSentAt(), prevMessage.getSentAt())) {
                isNewDay = true;
                isContinuous = false;
                GlobalUtils.showLog(TAG, "first");
            } else if (isSameDay(conversation.getSentAt(), prevMessage.getSentAt())
                    && timeDiff > 20 * 60 * 1000) {
                isShowTime = true;
                isContinuous = isContinuous(conversation, prevMessage);
                GlobalUtils.showLog(TAG, "second");
            } else {
                GlobalUtils.showLog(TAG, "third");
                isContinuous = isContinuous(conversation, prevMessage);
                GlobalUtils.showLog(TAG, "check result: " + isContinuous);
            }
        } else if (position == conversationList.size() - 1) {
            isNewDay = true;
        }

        switch (holder.getItemViewType()) {
            case MSG_BOT_SUGGESTION_JSON:
                try {
                    ((BotSuggestionJSONHolder) holder).bind(conversation, isNewDay, isShowTime,
                            false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case MSG_TEXT_LEFT:
                try {
                    ((LeftTextHolder) holder).bind(conversation, isNewDay, isShowTime,
                            isContinuous);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case MSG_TEXT_RIGHT:
                ((RightTextHolder) holder).bind(conversation, isNewDay, isShowTime, position,
                        isContinuous);
                break;
            case MSG_IMG_LEFT:
                ((LeftImageHolder) holder).bind(conversation, isNewDay, isShowTime,
                        isContinuous);
                break;

            case MSG_IMG_RIGHT:
                ((RightImageHolder) holder).bind(conversation, isNewDay, isShowTime, position,
                        isContinuous);
                break;

            case MSG_LINK_LEFT:
                ((LeftLinkHolder) holder).bind(conversation, isNewDay, isShowTime,
                        isContinuous);
                break;

            case MSG_LINK_RIGHT:
                ((RightLinkHolder) holder).bind(conversation, isNewDay, isShowTime, position,
                        isContinuous);
                break;

            case MSG_DOC_LEFT:
                ((LeftDocHolder) holder).bind(conversation, isNewDay, isShowTime,
                        isContinuous);
                break;

            case MSG_DOC_RIGHT:
                ((RightDocHolder) holder).bind(conversation, isNewDay, isShowTime, position,
                        isContinuous);
                break;

            case MSG_ACCEPTED_TAG:
                ((AcceptedViewHolder) holder).bind(conversation);
                break;

            case MSG_SERVICE_DOERS_TAG:
                ((ServiceDoerViewHolder) holder).bind(conversation);
                break;

            case MSG_BOT_SUGGESTIONS:
                ((BotSuggestionsHolder) holder).bind(conversation, isContinuous);
                break;

            case INITIAL_SERVICE_DETAIL:
                ((InitialServiceDetailHolder) holder).bind(conversation);
                break;

            case INITIAL_TICKET_DETAIL:
                ((InitialTicketDetailHolder) holder).bind(conversation);
                break;

            case MSG_CALL_OUTGOING:
                ((CallViewHolder) holder).bind(conversation, isNewDay, isShowTime, isContinuous);
        }
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

    private void showTime(long sentAt, TextView tvSentAt) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter =
                new SimpleDateFormat("hh:mm aa");
        String timeString = timeFormatter.format(new Date(sentAt));

        tvSentAt.setText(timeString);
        tvSentAt.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Conversation conversation = conversationList.get(position);
        Account account = AccountRepo.getInstance().getAccount();

        GlobalUtils.showLog(TAG, "accontId: " + account.getAccountId());
        GlobalUtils.showLog(TAG, "message type check:" + conversation.getMessageType());
        switch (conversation.getMessageType()) {
            case "TEXT_RTC_MESSAGE":
                if (conversation.getSenderId().equals(account.getAccountId()))
                    return MSG_TEXT_RIGHT;
                else {
                    boolean isReplyInJson = GlobalUtils.isJSONValid(conversation.getMessage());
                    if (isReplyInJson) return MSG_BOT_SUGGESTION_JSON;
                    else return MSG_TEXT_LEFT;
                }

            case "BOT_CONVERSATION_REQUEST":
                return MSG_TEXT_LEFT;

            case "LINK_RTC_MESSAGE":
                if (conversation.getSenderId().equals(account.getAccountId()))
                    return MSG_LINK_RIGHT;
                else return MSG_LINK_LEFT;

            case "IMAGE_RTC_MESSAGE":
                if (conversation.getSenderId().equals(account.getAccountId()))
                    return MSG_IMG_RIGHT;
                else return MSG_IMG_LEFT;

            case "DOC_RTC_MESSAGE":
                if (conversation.getSenderId().equals(account.getAccountId()))
                    return MSG_DOC_RIGHT;
                else return MSG_DOC_LEFT;

            case "MSG_BOT_SUGGESTIONS":
                return MSG_BOT_SUGGESTIONS;

            case "INITIAL_SERVICE_DETAIL":
                return INITIAL_SERVICE_DETAIL;

            case "INITIAL_TICKET_DETAIL":
                return INITIAL_TICKET_DETAIL;

            case "MSG_ACCEPTED_TAG":
                return MSG_ACCEPTED_TAG;

            case "MSG_SERVICE_DOERS_TAG":
                return MSG_SERVICE_DOERS_TAG;

            case "VIDEO_CALL_RTC_MESSAGE":
                return MSG_CALL_OUTGOING;
        }

        return -1;

    }

    public String getFileSize(int size) {
        String hrSize;
        double m = size / 1024.0;
        DecimalFormat dec = new DecimalFormat("0");

        if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return hrSize;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Conversation message);
    }

    public void setOnItemLongClickListener(MessageAdapter.OnItemLongClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageClickListener {
        void onImageClick(View view, int position);
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.imageClickListener = listener;
    }

    public interface OnMessageNotDeliveredListener {
        void onMessageNotDelivered(Conversation message);
    }

    public void setOnMessageNotDeliveredListener(MessageAdapter.OnMessageNotDeliveredListener
                                                         listener) {
        this.messageNotDeliveredListener = listener;
    }

    public interface OnSenderImageClickListener {
        void onSenderImageClick(Conversation conversation);
    }

    public void setOnSenderImageClickListener(MessageAdapter.OnSenderImageClickListener listener) {
        this.senderImageClickListener = listener;
    }

    public static String[] extractLinks(String text) {
        List<String> links = new ArrayList<>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();
            links.add(url);
        }

        return links.toArray(new String[0]);
    }

    public boolean isSameDay(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(date1);
        cal2.setTimeInMillis(date2);

        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }


    private class RightTextHolder extends RecyclerView.ViewHolder {
        TextView messageText, sentAt, notDelivered, sent;
        LinearLayout textHolder;
        ImageView resend;
        View spacing;

        RightTextHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.tv_text);
            sentAt = itemView.findViewById(R.id.tv_sent_at);
            notDelivered = itemView.findViewById(R.id.tv_not_delivered);
            sent = itemView.findViewById(R.id.tv_sent);
            textHolder = itemView.findViewById(R.id.ll_text_holder);
            resend = itemView.findViewById(R.id.iv_resend);
            spacing = itemView.findViewById(R.id.spacing);
        }

        @SuppressLint("SetTextI18n")
        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  final int pos, boolean isContinuous) {

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            messageText.setText(conversation.getMessage());
            // Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.VISIBLE);
                showDateAndTime(conversation.getSentAt(), sentAt);
            }
            if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }

            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }

            //resend handle and sent status
            if (!conversation.isSent() && conversation.isSendFail()) {
                //case when sending failed. Show resend layout
                resend.setVisibility(View.VISIBLE);
                notDelivered.setVisibility(View.VISIBLE);
                sent.setVisibility(View.GONE);
            } else {
                // case when message is sent. Disable progress and resend layout
                if (resend != null && notDelivered != null) {
                    resend.setVisibility(View.GONE);
                    notDelivered.setVisibility(View.GONE);
                }

                //handle sent visibility
                GlobalUtils.showLog(TAG, "conversation sent check: " + conversation.isSent());
                if (conversation.isSent() && pos == 0) {
                    sent.setVisibility(View.VISIBLE);
                } else {
                    if (sent != null) {
                        sent.setVisibility(View.GONE);
                    }
                }
            }

            //replace sent with seen
            if (conversationList.indexOf(conversation) == 0) {
                GlobalUtils.showLog(TAG, "check msg status: " + conversation.getMessageStatus());
                if (conversation.getMessageStatus() != null &&
                        conversation.getMessageStatus().equalsIgnoreCase("seen")) {
                    if (sent != null) {
                        sent.setVisibility(View.VISIBLE);
                        sent.setText("Seen");
                    }
                } else if (conversation.isSendFail()) {
                    sent.setVisibility(View.GONE);
                } else {
                    sent.setVisibility(View.VISIBLE);
                    sent.setText("Sent");
                }
            }

            //click listeners
            textHolder.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(conversationList.get(position));
                }
                return true;
            });

            if (resend != null) {
                resend.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (messageNotDeliveredListener != null && position !=
                            RecyclerView.NO_POSITION) {
                        messageNotDeliveredListener.onMessageNotDelivered
                                (conversationList.get(position));
                    }
                });
            }

        }
    }


    private class RightLinkHolder extends RecyclerView.ViewHolder {
        TextView urlText, sentAt, notDelivered, sent, urlTitle, urlDesc;
        LinearLayout urlHolder;
        ImageView resend, urlImage;
        View spacing;

        RightLinkHolder(@NonNull View itemView) {
            super(itemView);

            spacing = itemView.findViewById(R.id.spacing);
            urlText = itemView.findViewById(R.id.tv_url);
            sentAt = itemView.findViewById(R.id.tv_sent_at);
            notDelivered = itemView.findViewById(R.id.tv_not_delivered);
            sent = itemView.findViewById(R.id.tv_sent);
            urlHolder = itemView.findViewById(R.id.ll_url_preview);
            resend = itemView.findViewById(R.id.iv_resend);
            urlImage = itemView.findViewById(R.id.iv_url_image);
            urlTitle = itemView.findViewById(R.id.tv_url_title);
            urlDesc = itemView.findViewById(R.id.tv_url_desc);

        }

        @SuppressLint("SetTextI18n")
        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  final int pos, boolean isContinuous) {

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            RichPreview richPreview = new RichPreview(new ResponseListener() {
                @Override
                public void onData(MetaData metaData) {
                    //Implement your Layout

                    urlText.setText(conversation.getMessage());
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.ic_imageholder)
                            .error(R.drawable.ic_imageholder)
                            .fitCenter();

                    if ((metaData.getImageurl() != null && !metaData.getImageurl().isEmpty())) {
                        Glide.with(AnyDoneServiceProviderApplication.getContext())
                                .load(metaData.getImageurl())
                                .apply(options)
                                .into(urlImage);
                    }

                    urlTitle.setText(metaData.getTitle());
                    urlDesc.setText(metaData.getDescription());

                    urlHolder.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(
                                Intent.ACTION_VIEW, Uri.parse(metaData.getUrl()));
                        mContext.startActivity(browserIntent);
                    });
                }

                @Override
                public void onError(Exception e) {
                    //handle error
                    GlobalUtils.showLog(TAG, "url load error: " + e.toString());
                }
            });

            GlobalUtils.showLog(TAG, "show link message: " + conversation.getMessage());
            if (!conversation.getMessage().isEmpty()) {
                String[] extractedLink = extractLinks(conversation.getMessage());
//                GlobalUtils.showLog(TAG, "extracted: " + extractedLink[0]);

                if (extractedLink.length != 0 && !extractedLink[0].contains("https://")) {
                    String linkWithHttps = "https://" + extractedLink[0];
                    richPreview.getPreview(linkWithHttps);
                } else {
                    richPreview.getPreview(conversation.getMessage());
                }
            }


            // Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.VISIBLE);
                showDateAndTime(conversation.getSentAt(), sentAt);
            }
            if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }

            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }

            //resend handle and sent status
            if (!conversation.isSent() && conversation.isSendFail()) {
                //case when sending failed. Show resend layout
                resend.setVisibility(View.VISIBLE);
                notDelivered.setVisibility(View.VISIBLE);

            } else {
                // case when message is sent. Disable progress and resend layout
                if (resend != null && notDelivered != null) {
                    resend.setVisibility(View.GONE);
                    notDelivered.setVisibility(View.GONE);
                }

                //handle sent visibility
                GlobalUtils.showLog(TAG, "conversation sent check: " + conversation.isSent());
                if (conversation.isSent() && pos == 0) {
                    sent.setVisibility(View.VISIBLE);
                } else {
                    if (sent != null) {
                        sent.setVisibility(View.GONE);
                    }
                }
            }

            //replace sent with seen
            if (conversationList.indexOf(conversation) == 0) {
                if (conversation.getMessageStatus() != null &&
                        conversation.getMessageStatus().equalsIgnoreCase("seen")) {
                    if (sent != null) {
                        sent.setVisibility(View.VISIBLE);
                        sent.setText("Seen");
                    }
                }
            }

            //click listeners
            urlHolder.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(conversationList.get(position));
                }
                return true;
            });

            if (resend != null) {
                resend.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (messageNotDeliveredListener != null && position !=
                            RecyclerView.NO_POSITION) {
                        messageNotDeliveredListener.onMessageNotDelivered
                                (conversationList.get(position));
                    }
                });
            }

        }
    }

    private class RightImageHolder extends RecyclerView.ViewHolder {
        TextView messageText, sentAt, notDelivered, sent, resend;
        MaterialCardView imageHolder;
        ProgressBar progress;
        ImageView image;
        TextView imageDesc;
        View spacing;

        RightImageHolder(@NonNull View itemView) {
            super(itemView);

            spacing = itemView.findViewById(R.id.spacing);
            messageText = itemView.findViewById(R.id.tv_text);
            sentAt = itemView.findViewById(R.id.tv_sent_at);
            notDelivered = itemView.findViewById(R.id.tv_not_delivered);
            sent = itemView.findViewById(R.id.tv_sent);
            imageHolder = itemView.findViewById(R.id.cv_image_portrait);
            resend = itemView.findViewById(R.id.tv_retry_image_upload);
            progress = itemView.findViewById(R.id.pb_image);
            image = itemView.findViewById(R.id.iv_image);
            imageDesc = itemView.findViewById(R.id.tv_image_desc);
        }

        @SuppressLint("SetTextI18n")
        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  final int pos, boolean isContinuous) {

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            if (conversation.getImageBitmap() != null &&
                    conversation.getImageBitmap().length > 0) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_imageholder)
                        .error(R.drawable.ic_imageholder);
                Glide.with(AnyDoneServiceProviderApplication.getContext())
                        .load(conversation.getImageBitmap())
                        .apply(options.override(700, 620))
                        .centerCrop()
                        .into(image);
            } else {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_imageholder)
                        .error(R.drawable.ic_imageholder);
                Glide.with(AnyDoneServiceProviderApplication.getContext())
                        .load(conversation.getMessage())
                        .apply(options.override(700, 620))
                        .centerCrop()
                        .into(image);
            }


            // Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.VISIBLE);
                showDateAndTime(conversation.getSentAt(), sentAt);
            }
            if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }

            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }

            //resend handle and sent status
            if (!conversation.isSent() && conversation.isSendFail()) {
                //case when sending failed. Show resend layout
                resend.setVisibility(View.VISIBLE);
                notDelivered.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                sent.setVisibility(View.GONE);
            } else if (!conversation.isSent() && !conversation.isSendFail()) {
                progress.setVisibility(View.VISIBLE);
            } else {
                // case when message is sent. Disable progress and resend layout
                if (resend != null && notDelivered != null) {
                    resend.setVisibility(View.GONE);
                    notDelivered.setVisibility(View.GONE);
                }

                progress.setVisibility(View.GONE);
                //handle sent visibility
                GlobalUtils.showLog(TAG, "conversation sent check: " + conversation.isSent());
                GlobalUtils.showLog(TAG, "position check: " + pos);
                if (conversation.isSent() && pos == 0) {
                    sent.setVisibility(View.VISIBLE);
                } else {
                    if (sent != null) {
                        sent.setVisibility(View.GONE);
                    }
                }
            }

            //replace sent with seen
            if (conversationList.indexOf(conversation) == 0) {
                if (conversation.getMessageStatus() != null &&
                        conversation.getMessageStatus().equalsIgnoreCase("seen")) {
                    if (sent != null) {
                        sent.setVisibility(View.VISIBLE);
                        sent.setText("Seen");
                    }
                }
            }


            //click listeners
            imageHolder.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(conversationList.get(position));
                }
                return true;
            });

            if (resend != null) {
                resend.setOnClickListener(v -> {
                    progress.setVisibility(View.VISIBLE);
                    resend.setVisibility(View.GONE);
                    int position = getAdapterPosition();
                    if (messageNotDeliveredListener != null && position !=
                            RecyclerView.NO_POSITION) {
                        messageNotDeliveredListener.onMessageNotDelivered
                                (conversationList.get(position));
                    }
                });
            }

            //image preview
            imageHolder.setOnClickListener(v -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (imageClickListener != null && position != RecyclerView.NO_POSITION) {
                    imageClickListener.onImageClick(v, position);
                }
            });

            //show desc if available
            if (conversation.getImageDesc() != null && !conversation.getImageDesc().isEmpty()) {
             /*   if (conversation.getImageOrientation() != null &&
                        conversation.getImageOrientation().equalsIgnoreCase("portrait")) {*/
                imageDesc.setVisibility(View.VISIBLE);
                imageDesc.setText(conversation.getImageDesc());
//                }
            } else {
                imageDesc.setVisibility(View.GONE);
            }

        }
    }

    private class RightDocHolder extends RecyclerView.ViewHolder {
        TextView sentAt, notDelivered, sent, fileName, fileSize, resend;
        RelativeLayout docHolder;
        ImageView doc;
        ProgressBar progress;
        View spacing;

        RightDocHolder(@NonNull View itemView) {
            super(itemView);

            spacing = itemView.findViewById(R.id.spacing);
            sentAt = itemView.findViewById(R.id.tv_sent_at);
            notDelivered = itemView.findViewById(R.id.tv_not_delivered);
            sent = itemView.findViewById(R.id.tv_sent);
            docHolder = itemView.findViewById(R.id.rl_doc_holder);
            resend = itemView.findViewById(R.id.tv_resend_doc);
            progress = itemView.findViewById(R.id.pb_doc);
            doc = itemView.findViewById(R.id.iv_doc);
            fileName = itemView.findViewById(R.id.tv_doc_name);
            fileSize = itemView.findViewById(R.id.tv_doc_size);
        }

        @SuppressLint("SetTextI18n")
        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  final int pos, boolean isContinuous) {

            File docFile;

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            if (conversation.getFilePath() != null &&
                    !conversation.getFilePath().isEmpty()) {
                GlobalUtils.showLog(TAG, "file path not null");
                fileName.setText(conversation.getFileName());
                fileSize.setText(conversation.getFileSize());

                if (conversation.isSent()) {
                    fileName.setVisibility(View.VISIBLE);
                    fileSize.setVisibility(View.VISIBLE);
                }

                docFile = new File(conversation.getFilePath());

                docHolder.setOnClickListener(v -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    Uri docUri = FileProvider.getUriForFile(mContext,
                            mContext.getApplicationContext().getPackageName() +
                                    ".provider", docFile);

                    browserIntent.setDataAndType(docUri, "application/pdf");
                    Intent chooser = Intent.createChooser(browserIntent, "Open with");
                    browserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mContext.startActivity(chooser);
                });
            } else {
                String docUrl = conversation.getMessage();
                new Thread(() -> {
                    try {
                        URL url = new URL(docUrl);
                        URLConnection urlConnection = url.openConnection();
                        urlConnection.connect();
                        int file_size = urlConnection.getContentLength() / 1024;
                        String docFileSize = getFileSize(file_size);
                        fileSize.post(() -> fileSize.setText(docFileSize));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();


                fileName.setText(conversation.getFileName());
                fileName.setVisibility(View.VISIBLE);
                fileSize.setVisibility(View.VISIBLE);
                docHolder.setOnClickListener(v ->
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(conversation.getMessage()))));
            }

            // Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.VISIBLE);
                showDateAndTime(conversation.getSentAt(), sentAt);
            }
            if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }

            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }

            //resend handle and sent status
            if (!conversation.isSent() && conversation.isSendFail()) {
                //case when sending failed. Show resend layout
                resend.setVisibility(View.VISIBLE);
                notDelivered.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            } else if (!conversation.isSent() && !conversation.isSendFail()) {
                progress.setVisibility(View.VISIBLE);
            } else {
                // case when message is sent. Disable progress and resend layout
                if (resend != null && notDelivered != null) {
                    resend.setVisibility(View.GONE);
                    notDelivered.setVisibility(View.GONE);
                }
                progress.setVisibility(View.GONE);
                //handle sent visibility
                GlobalUtils.showLog(TAG, "conversation sent check: " + conversation.isSent());
                if (conversation.isSent() && pos == 0) {
                    sent.setVisibility(View.VISIBLE);
                } else {
                    if (sent != null) {
                        sent.setVisibility(View.GONE);
                    }
                }
            }

            //replace sent with seen
            if (conversationList.indexOf(conversation) == 0) {
                if (conversation.getMessageStatus() != null &&
                        conversation.getMessageStatus().equalsIgnoreCase("seen")) {
                    if (sent != null) {
                        sent.setVisibility(View.VISIBLE);
                        sent.setText("Seen");
                    }
                }
            }


            //click listeners
            docHolder.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(conversationList.get(position));
                }
                return true;
            });

            if (resend != null) {
                resend.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (messageNotDeliveredListener != null && position !=
                            RecyclerView.NO_POSITION) {
                        messageNotDeliveredListener.onMessageNotDelivered
                                (conversationList.get(position));
                    }
                });
            }
        }
    }

    private class LeftTextHolder extends RecyclerView.ViewHolder {
        TextView messageText, sentAt, senderTitle;
        LinearLayout textHolder;
        ImageView resend;
        CircleImageView civSender;
        View spacing;
        RelativeLayout rlMessageHolder;
        RelativeLayout rlKgraphHolder;
        View kgraphSpacing;
        RelativeLayout rlKgraphHolderAligned;
        CircleImageView civKgraphSender;
        LinearLayout llKgraphTextHolder;
        TextView tvBot;
        TextView tvKgraphTitle;
        CardView cvSuggestions;
        RecyclerView rvSuggestions;
        ImageView ivBack;

        LeftTextHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.tv_text);
            sentAt = itemView.findViewById(R.id.tv_sent_at);
            textHolder = itemView.findViewById(R.id.ll_text_holder);
            resend = itemView.findViewById(R.id.iv_resend);
            civSender = itemView.findViewById(R.id.civ_sender);
            senderTitle = itemView.findViewById(R.id.tv_title);
            spacing = itemView.findViewById(R.id.spacing);
            rlMessageHolder = itemView.findViewById(R.id.rl_message_holder);
            rlKgraphHolder = itemView.findViewById(R.id.rl_kgraph_holder);
            kgraphSpacing = itemView.findViewById(R.id.kgraph_spacing);
            rlKgraphHolderAligned = itemView.findViewById(R.id.rl_kgraph_holder_aligned);
            civKgraphSender = itemView.findViewById(R.id.civ_kgraph_sender);
            llKgraphTextHolder = itemView.findViewById(R.id.ll_kgraph_text_holder);
            tvBot = itemView.findViewById(R.id.tv_bot);
            tvKgraphTitle = itemView.findViewById(R.id.tv_kgraph_title);
            cvSuggestions = itemView.findViewById(R.id.cv_suggestions);
            rvSuggestions = itemView.findViewById(R.id.rv_suggestions);
            ivBack = itemView.findViewById(R.id.iv_back);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) throws JSONException {

            boolean isReplyInJson = GlobalUtils.isJSONValid(conversation.getMessage());

            //show messeage accoring to type of message(JSON or normal string)
            if (!isReplyInJson) {
                //reply is normal text message
                //show additional padding if not continuous
                rlMessageHolder.setVisibility(View.VISIBLE);
                rlKgraphHolder.setVisibility(View.GONE);
                if (!isContinuous) {
                    spacing.setVisibility(View.VISIBLE);
                } else {
                    spacing.setVisibility(View.GONE);
                    GlobalUtils.showLog(TAG, "spacing deleted");
                }

                boolean isHtml = DetectHtml.isHtml(conversation.getMessage());
                if (isHtml) messageText.setText(Jsoup.parse(conversation.getMessage()).text().trim());
                else messageText.setText(conversation.getMessage().trim());
                // Show the date if the message was sent on a different date than the previous message.
                if (isNewDay) {
                    sentAt.setVisibility(View.VISIBLE);
                    showDateAndTime(conversation.getSentAt(), sentAt);
                }
                if (showTime) {
                    sentAt.setVisibility(View.VISIBLE);
                    showTime(conversation.getSentAt(), sentAt);
                }
                if (!isNewDay && !showTime) {
                    sentAt.setVisibility(View.GONE);
                }


                // Hide profile image and name if the previous message was also sent by current sender.
                if (isContinuous && !showTime && !isNewDay) {
                    civSender.setVisibility(View.INVISIBLE);
                    senderTitle.setVisibility(View.GONE);
                } else {
                    //check for bot name and image
                    displayBotOrUserMessage(senderTitle, civSender, conversation);
                }

                if (civSender != null) {
                    civSender.setOnClickListener(v -> {
                        if (senderImageClickListener != null && getAdapterPosition() !=
                                RecyclerView.NO_POSITION) {
                            senderImageClickListener.onSenderImageClick(
                                    conversationList.get(getAdapterPosition()));
                        }
                    });
                }

                //click listeners
                textHolder.setOnLongClickListener(v -> {
                    int position = getAdapterPosition();
                    GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                    GlobalUtils.showLog(TAG, "isBot: " + conversationList.get(position)
                            .getSenderId());
                    if (listener != null && position != RecyclerView.NO_POSITION
                            && !conversationList.get(position).getSenderId().isEmpty()) {
                        listener.onItemLongClick(conversationList.get(position));
                    }
                    return true;
                });
            } else {
                rlMessageHolder.setVisibility(View.GONE);
                rlKgraphHolder.setVisibility(View.VISIBLE);
                //reply is in json, so bot suggestion
                if (!isContinuous) {
                    kgraphSpacing.setVisibility(View.VISIBLE);
                } else {
                    kgraphSpacing.setVisibility(View.GONE);
                }

                if (conversation.iskGraphBack()) {
                    ivBack.setVisibility(View.VISIBLE);
                } else {
                    ivBack.setVisibility(View.GONE);
                }

                JSONObject kGraphObj = new JSONObject(conversation.getMessage());
                JSONArray kGraphArray = kGraphObj.getJSONArray("knowledges");
                List<KGraph> kGraphList = new ArrayList<>();
                for (int i = 0; i < kGraphArray.length(); i++) {
                    JSONObject kGraphJSONObj = (JSONObject) kGraphArray.get(i);
                    KGraph kGraph = new KGraph();
                    kGraph.setAnswerType(kGraphJSONObj.getString("knowledgeType"));
                    kGraph.setNext(kGraphJSONObj.getString("knowledgeKey"));
                    kGraph.setId(kGraphJSONObj.getString("knowledgeId"));
                    kGraph.setTitle(kGraphJSONObj.getString("title"));
                    kGraphList.add(kGraph);
                }

                ivBack.setOnClickListener(v -> {
                /*    int position = getAdapterPosition();
                    if (onBackClickListener != null && position != RecyclerView.NO_POSITION) {
                        Conversation prevKGraph = null;
                        int prevIndex = position + 1;
                        GlobalUtils.showLog(TAG, "prev index;" + prevIndex);
                        //get prev k-graph message
                        for (int i = prevIndex; prevIndex >= 0; prevIndex++) {
                            if (conversationList.get(i).getMessageType()
                                    .equals("MSG_BOT_SUGGESTIONS")) {
                                GlobalUtils.showLog(TAG, "prev k-graph found");
                                prevKGraph = conversationList.get(i);
                                break;
                            }
                        }

                        if (prevKGraph != null)
                            onBackClickListener
                                    .onBackClick(Objects.requireNonNull(prevKGraph
                                                    .getkGraphList().get(0)).getPrev(),
                                            prevKGraph.getkGraphList().get(0).getPrevId());
                    }*/
                });

                if (conversation.getkGraphTitle() != null && conversation.getkGraphTitle().isEmpty()) {
                    tvKgraphTitle.setVisibility(View.GONE);
                    llKgraphTextHolder.setVisibility(View.GONE);
                } else {
                    tvKgraphTitle.setText(conversation.getkGraphTitle());
                    llKgraphTextHolder.setVisibility(View.GONE);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                rvSuggestions.setLayoutManager(layoutManager);
                GlobalUtils.showLog(TAG, "conversation kgraph: " +
                        kGraphList.size());
                KgraphAdapter adapter = new KgraphAdapter(kGraphList, mContext, false);
                adapter.setOnItemClickListener(kGraph -> {
                    GlobalUtils.showLog(TAG, "adapter click listened");
                    Hawk.put(Constants.KGRAPH_TITLE, kGraph.getTitle());
                    int position = getAdapterPosition();
                    if (suggestionClickListener != null && position != RecyclerView.NO_POSITION) {
                        GlobalUtils.showLog(TAG, "suggestion click listener not null");
                        suggestionClickListener.onSuggestionClick(kGraph);
                    }
                });
                rvSuggestions.setAdapter(adapter);
            }
        }
    }

    private class LeftLinkHolder extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle, urlTitle, urlDesc, url;
        LinearLayout urlHolder;
        ImageView resend, urlImage;
        CircleImageView civSender;
        View spacing;

        LeftLinkHolder(@NonNull View itemView) {
            super(itemView);

            sentAt = itemView.findViewById(R.id.tv_sent_at);
            urlHolder = itemView.findViewById(R.id.ll_url_preview);
            resend = itemView.findViewById(R.id.iv_resend);
            civSender = itemView.findViewById(R.id.civ_sender);
            senderTitle = itemView.findViewById(R.id.tv_url_user);
            url = itemView.findViewById(R.id.tv_url);
            urlTitle = itemView.findViewById(R.id.tv_url_title);
            urlDesc = itemView.findViewById(R.id.tv_url_desc);
            urlImage = itemView.findViewById(R.id.iv_url_image);
            spacing = itemView.findViewById(R.id.spacing);

        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) {

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            RichPreview richPreview = new RichPreview(new ResponseListener() {
                @Override
                public void onData(MetaData metaData) {
                    //Implement your Layout
                    url.setText(conversation.getMessage());
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.ic_imageholder)
                            .error(R.drawable.ic_imageholder)
                            .fitCenter();

                    if ((metaData.getImageurl() != null && !metaData.getImageurl().isEmpty())) {
                        Glide.with(AnyDoneServiceProviderApplication.getContext())
                                .load(metaData.getImageurl())
                                .apply(options)
                                .into(urlImage);
                    }

                    urlTitle.setText(metaData.getTitle());
                    urlDesc.setText(metaData.getDescription());

                    urlHolder.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(
                                Intent.ACTION_VIEW, Uri.parse(metaData.getUrl()));
                        mContext.startActivity(browserIntent);
                    });
                }

                @Override
                public void onError(Exception e) {
                    //handle error
                    GlobalUtils.showLog(TAG, "url load error: " + e.toString());
                }
            });

            GlobalUtils.showLog(TAG, "show link message: " + conversation.getMessage());
            if (!conversation.getMessage().isEmpty()) {
                String[] extractedLink = extractLinks(conversation.getMessage());
                GlobalUtils.showLog(TAG, "extracted: " + extractedLink[0]);

                if (!extractedLink[0].contains("https://")) {
                    String linkWithHttps = "https://" + extractedLink[0];
                    richPreview.getPreview(linkWithHttps);
                } else {
                    richPreview.getPreview(conversation.getMessage());
                }
            }

            //Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.VISIBLE);
                showDateAndTime(conversation.getSentAt(), sentAt);
            } else {
                sentAt.setVisibility(View.GONE);
            }
            if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            } else {
                sentAt.setVisibility(View.GONE);
            }

            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }

            // Hide profile image and name if the previous message was also sent by current sender.
            if (isContinuous) {
                civSender.setVisibility(View.INVISIBLE);
                senderTitle.setVisibility(View.GONE);
            } else {
                //check for bot name and image
                displayBotOrUserMessage(senderTitle, civSender, conversation);
            }

            if (civSender != null) {
                civSender.setOnClickListener(v -> {
                    if (senderImageClickListener != null && getAdapterPosition() !=
                            RecyclerView.NO_POSITION) {
                        senderImageClickListener.onSenderImageClick(
                                conversationList.get(getAdapterPosition()));
                    }
                });
            }

    /*        //click listeners
            textHolder.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(conversationList.get(position));
                }
                return true;
            });*/

        }
    }

    private class LeftImageHolder extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle, imageDesc;
        MaterialCardView imageHolder;
        ImageView resend, image;
        CircleImageView civSender;
        View spacing;

        LeftImageHolder(@NonNull View itemView) {
            super(itemView);

            sentAt = itemView.findViewById(R.id.tv_sent_at);
            imageHolder = itemView.findViewById(R.id.cv_image_portrait);
            resend = itemView.findViewById(R.id.iv_resend);
            civSender = itemView.findViewById(R.id.civ_sender);
            senderTitle = itemView.findViewById(R.id.tv_img_user);
            image = itemView.findViewById(R.id.iv_image);
            imageDesc = itemView.findViewById(R.id.tv_image_desc);
            spacing = itemView.findViewById(R.id.spacing);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) {

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            if (conversation.getImageBitmap() != null &&
                    conversation.getImageBitmap().length > 0) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_imageholder)
                        .error(R.drawable.ic_imageholder);
                Glide.with(AnyDoneServiceProviderApplication.getContext())
                        .load(conversation.getImageBitmap())
                        .apply(options.override(700, 620))
                        .centerCrop()
                        .into(image);
            } else {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_imageholder)
                        .error(R.drawable.ic_imageholder);
                Glide.with(AnyDoneServiceProviderApplication.getContext())
                        .load(conversation.getMessage())
                        .apply(options.override(700, 620))
                        .centerCrop()
                        .into(image);
            }

            // Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.VISIBLE);
                showDateAndTime(conversation.getSentAt(), sentAt);
            }
            if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }

            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }


            // Hide profile image and name if the previous message was also sent by current sender.
            if (isContinuous) {
                civSender.setVisibility(View.INVISIBLE);
                senderTitle.setVisibility(View.GONE);
            } else {
                //check for bot name and image
                displayBotOrUserMessage(senderTitle, civSender, conversation);
            }

            //image preview
            imageHolder.setOnClickListener(v -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (imageClickListener != null && position != RecyclerView.NO_POSITION) {
                    imageClickListener.onImageClick(v, position);
                }
            });

            //show desc if available
            if (conversation.getImageDesc() != null && !conversation.getImageDesc().isEmpty()) {
                if (conversation.getImageOrientation() != null &&
                        conversation.getImageOrientation()
                                .equalsIgnoreCase("portrait")) {
                    imageDesc.setVisibility(View.VISIBLE);
                    imageDesc.setText(conversation.getImageDesc());
                }
            } else {
                imageDesc.setVisibility(View.GONE);
            }

            if (civSender != null) {
                civSender.setOnClickListener(v -> {
                    if (senderImageClickListener != null && getAdapterPosition() !=
                            RecyclerView.NO_POSITION) {
                        senderImageClickListener.onSenderImageClick(
                                conversationList.get(getAdapterPosition()));
                    }
                });
            }

    /*        //click listeners
            textHolder.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(conversationList.get(position));
                }
                return true;
            });*/

        }
    }

    private class LeftDocHolder extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle, fileName, fileSize;
        RelativeLayout docHolder;
        ImageView doc;
        CircleImageView civSender;
        View spacing;

        LeftDocHolder(@NonNull View itemView) {
            super(itemView);

            sentAt = itemView.findViewById(R.id.tv_sent_at);
            docHolder = itemView.findViewById(R.id.rl_doc_holder);
            civSender = itemView.findViewById(R.id.civ_sender);
            senderTitle = itemView.findViewById(R.id.tv_doc_user);
            doc = itemView.findViewById(R.id.iv_doc);
            fileName = itemView.findViewById(R.id.tv_doc_name);
            fileSize = itemView.findViewById(R.id.tv_doc_size);
            spacing = itemView.findViewById(R.id.spacing);

        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) {

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            File docFile;

            if (conversation.getFilePath() != null &&
                    !conversation.getFilePath().isEmpty()) {
                GlobalUtils.showLog(TAG, "file path not null");
                fileName.setText(conversation.getFileName());
                fileSize.setText(conversation.getFileSize());

                if (conversation.isSent()) {
                    fileName.setVisibility(View.VISIBLE);
                    fileSize.setVisibility(View.VISIBLE);
                }

                docFile = new File(conversation.getFilePath());

                docHolder.setOnClickListener(v -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    Uri docUri = FileProvider.getUriForFile(mContext,
                            mContext.getApplicationContext().getPackageName() +
                                    ".provider", docFile);

                    browserIntent.setDataAndType(docUri, "application/pdf");
                    Intent chooser = Intent.createChooser(browserIntent, "Open with");
                    browserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mContext.startActivity(chooser);
                });
            } else {
                String docUrl = conversation.getMessage();
                new Thread(() -> {
                    try {
                        URL url = new URL(docUrl);
                        URLConnection urlConnection = url.openConnection();
                        urlConnection.connect();
                        int file_size = urlConnection.getContentLength() / 1024;
                        String docFileSize = getFileSize(file_size);
                        fileSize.post(() -> fileSize.setText(docFileSize));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();


                fileName.setText(conversation.getFileName());
                fileName.setVisibility(View.VISIBLE);
                fileSize.setVisibility(View.VISIBLE);
                docHolder.setOnClickListener(v ->
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(conversation.getMessage()))));
            }

            // Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.VISIBLE);
                showDateAndTime(conversation.getSentAt(), sentAt);
            }
            if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }

            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }


            // Hide profile image and name if the previous message was also sent by current sender.
            if (isContinuous) {
                civSender.setVisibility(View.INVISIBLE);
                senderTitle.setVisibility(View.GONE);
            } else {
                //check for bot name and image
                displayBotOrUserMessage(senderTitle, civSender, conversation);
            }

            if (civSender != null) {
                civSender.setOnClickListener(v -> {
                    if (senderImageClickListener != null && getAdapterPosition() !=
                            RecyclerView.NO_POSITION) {
                        senderImageClickListener.onSenderImageClick(
                                conversationList.get(getAdapterPosition()));
                    }
                });
            }

    /*        //click listeners
            textHolder.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                GlobalUtils.showLog(TAG, "position: " + getAdapterPosition());
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(conversationList.get(position));
                }
                return true;
            });*/

        }
    }

    private class BotSuggestionsHolder extends RecyclerView.ViewHolder {
        TextView suggestionTitle;
        RecyclerView suggestions;
        ImageView back;
        View spacing;

        BotSuggestionsHolder(@NonNull View itemView) {
            super(itemView);

            spacing = itemView.findViewById(R.id.spacing);
            suggestionTitle = itemView.findViewById(R.id.tv_title);
            suggestions = itemView.findViewById(R.id.rv_suggestions);
            back = itemView.findViewById(R.id.iv_back);
        }

        void bind(final Conversation conversation, boolean isContinuous) {
            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            if (conversation.iskGraphBack()) {
                back.setVisibility(View.VISIBLE);
            } else {
                back.setVisibility(View.GONE);
            }

            back.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onBackClickListener != null && position != RecyclerView.NO_POSITION) {
                    String nextId = Objects.requireNonNull(conversation.getkGraphList().get(0)).getId();
                    String nextKey = Objects.requireNonNull(conversation.getkGraphList().get(0)).getNext();
                    String prevId = Objects.requireNonNull(conversation.getkGraphList().get(0)).getPrevId();
                    String prevKey = Objects.requireNonNull(conversation.getkGraphList().get(0)).getPrev();

                    if (prevId != null && prevKey != null) {
                        onBackClickListener
                                .onBackClick(prevKey, prevId, nextId, nextKey);
                    } else {
                        Toast.makeText(mContext, "empty back data", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (conversation.getkGraphTitle().isEmpty()) {
                suggestionTitle.setVisibility(View.GONE);
            } else {
                suggestionTitle.setText(conversation.getkGraphTitle());
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            suggestions.setLayoutManager(layoutManager);
            GlobalUtils.showLog(TAG, "conversation kgraph: " +
                    conversation.getkGraphList().size());
            KgraphAdapter adapter = new KgraphAdapter(conversation.getkGraphList(), mContext,
                    false);
            adapter.setOnItemClickListener(kGraph -> {
                Hawk.put(Constants.KGRAPH_TITLE, kGraph.getTitle());
                int position = getAdapterPosition();
                if (suggestionClickListener != null && position != RecyclerView.NO_POSITION) {
                    suggestionClickListener.onSuggestionClick(kGraph);
                }
            });
            suggestions.setAdapter(adapter);
        }
    }

    private class InitialServiceDetailHolder extends RecyclerView.ViewHolder {
        TextView serviceName, problemStat, location, date;
        ImageView serviceIcon;

        InitialServiceDetailHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.tv_service_name);
            problemStat = itemView.findViewById(R.id.tv_problem_stat);
            location = itemView.findViewById(R.id.tv_location);
            date = itemView.findViewById(R.id.tv_date);
            serviceIcon = itemView.findViewById(R.id.iv_service_icon);
        }

        void bind(final Conversation conversation) {
            serviceName.setText(conversation.getServiceName());
            problemStat.setText(conversation.getProblemStat());
            if (conversation.getLocation() != null && !conversation.getLocation().isEmpty()) {
                location.setText(conversation.getLocation());
                location.setVisibility(View.VISIBLE);
            }

            if (conversation.getDate() != null && !conversation.getDate().isEmpty()) {
                date.setText(conversation.getDate());
            }

            String serviceImage = conversation.getServiceIconUrl();
            if (serviceImage != null && !serviceImage.isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .fitCenter();

                Glide.with(mContext).load(serviceImage).apply(options).into(serviceIcon);
            }

        }
    }


    private class InitialTicketDetailHolder extends RecyclerView.ViewHolder {
        TextView ticketId, ticketTitle, ticketDesc;
        LinearLayout llLabels;
        HorizontalScrollView hsvTags;

        InitialTicketDetailHolder(@NonNull View itemView) {
            super(itemView);

            ticketId = itemView.findViewById(R.id.tv_ticket_id);
            ticketTitle = itemView.findViewById(R.id.tv_ticket_title);
            ticketDesc = itemView.findViewById(R.id.tv_ticket_desc);
            llLabels = itemView.findViewById(R.id.ll_label_holder);
            hsvTags = itemView.findViewById(R.id.scv_label);
        }

        void bind(final Conversation conversation) {
            ticketId.setText("#" + conversation.getRefId());
            ticketTitle.setText(conversation.getTicketTitle());
            if (conversation.getTicketDesc() != null && !conversation.getTicketDesc().isEmpty()) {
                ticketDesc.setText(conversation.getTicketDesc());
            } else {
                ticketDesc.setVisibility(View.GONE);
            }

            if (!CollectionUtils.isEmpty(conversation.getTagsList())) {
                llLabels.removeAllViews();
                for (Label tag : conversation.getTagsList()
                ) {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    @SuppressLint("InflateParams") TextView tvTag = (TextView) inflater
                            .inflate(R.layout.layout_tag, null);
                    tvTag.setText(tag.getName());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMarginEnd(20);
                    tvTag.setLayoutParams(params);
                    llLabels.addView(tvTag);
                }
/*
                if (llLabels.getChildCount() >= 5) {
                    hsvTags.setGravity(Gravity.START);
                }*/

            }

        }
    }

    private class AcceptedViewHolder extends RecyclerView.ViewHolder {
        TextView acceptedBy;

        public AcceptedViewHolder(View itemView) {
            super(itemView);
            acceptedBy = itemView.findViewById(R.id.tv_accpeted_by_tag);
        }

        void bind(final Conversation conversation) {

            StringBuilder acceptedByBuilder = new StringBuilder();
            acceptedByBuilder.append("Request accepted by ");
            acceptedByBuilder.append(conversation.getAcceptedBy());
            acceptedBy.setText(acceptedByBuilder);
        }
    }

    private class BotSuggestionJSONHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlKgraphHolder;
        View kgraphSpacing;
        RelativeLayout rlKgraphHolderAligned;
        CircleImageView civKgraphSender;
        LinearLayout llKgraphTextHolder;
        TextView tvBot;
        TextView tvKgraphTitle;
        CardView cvSuggestions;
        RecyclerView rvSuggestions;
        ImageView ivBack;
        TextView tvBotName;

        BotSuggestionJSONHolder(@NonNull View itemView) {
            super(itemView);

            rlKgraphHolder = itemView.findViewById(R.id.rl_kgraph_holder);
            kgraphSpacing = itemView.findViewById(R.id.kgraph_spacing);
            rlKgraphHolderAligned = itemView.findViewById(R.id.rl_kgraph_holder_aligned);
            civKgraphSender = itemView.findViewById(R.id.civ_kgraph_sender);
            llKgraphTextHolder = itemView.findViewById(R.id.ll_kgraph_text_holder);
            tvBot = itemView.findViewById(R.id.tv_bot);
            tvKgraphTitle = itemView.findViewById(R.id.tv_kgraph_title);
            cvSuggestions = itemView.findViewById(R.id.cv_suggestions);
            rvSuggestions = itemView.findViewById(R.id.rv_suggestions);
            ivBack = itemView.findViewById(R.id.iv_back);
            tvBotName = itemView.findViewById(R.id.tv_kGraph_bot);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime, boolean
                isContinuous) throws JSONException {

            Glide.with(mContext).load(conversation.getSenderImageUrl()).into(civKgraphSender);
            tvBotName.setText(conversation.getSenderName());

            boolean isReplyInJson = GlobalUtils.isJSONValid(conversation.getMessage());
            GlobalUtils.showLog(TAG, "check if json: " + isReplyInJson);

            GlobalUtils.showLog(TAG, "json msg: " + conversation.getMessage());
            rlKgraphHolder.setVisibility(View.VISIBLE);

            kgraphSpacing.setVisibility(View.VISIBLE);

            if (conversation.iskGraphBack()) {
                ivBack.setVisibility(View.VISIBLE);
            } else {
                ivBack.setVisibility(View.GONE);
            }

            GlobalUtils.showLog(TAG, "check msg in json: " + conversation.getMessage());

            JSONObject kGraphObj = new JSONObject(conversation.getMessage());
            JSONArray kGraphArray = kGraphObj.getJSONArray("knowledges");
            JSONObject kGraphBack;
            JSONObject kGraphRoot;
            try {
                kGraphBack = kGraphObj.getJSONObject("backKnowledge");
                kGraphBack.getString("knowledgeId");
            } catch (JSONException e) {
                kGraphBack = null;
            }

            try {
                kGraphRoot = kGraphObj.getJSONObject("parentKnowledge");
                kGraphRoot.getString("knowledgeId");
            } catch (JSONException e) {
                kGraphRoot = null;
            }


            List<KGraph> kGraphList = new ArrayList<>();
            for (int i = 0; i < kGraphArray.length(); i++) {
                JSONObject kGraphJSONObj = (JSONObject) kGraphArray.get(i);
                KGraph kGraph = new KGraph();
                kGraph.setAnswerType(kGraphJSONObj.getString("knowledgeType"));
                kGraph.setNext(kGraphJSONObj.getString("knowledgeKey"));
                kGraph.setId(kGraphJSONObj.getString("knowledgeId"));
                kGraph.setTitle(kGraphJSONObj.getString("title"));
                if (kGraphRoot != null && !kGraphRoot.getString("knowledgeId").isEmpty()) {
                    kGraph.setPrevId(kGraphRoot.getString("knowledgeId"));
                    kGraph.setPrev(kGraphRoot.getString("knowledgeKey"));
                } else {
                    kGraph.setPrev("");
                    kGraph.setPrevId("");
                }
                if (kGraphBack != null && !kGraphBack.getString("knowledgeId").isEmpty()) {
                    kGraph.setBackId(kGraphBack.getString("knowledgeId"));
                    kGraph.setBackKey(kGraphBack.getString("knowledgeKey"));
                }
                kGraphList.add(kGraph);
            }

          /*  String backId = Objects.requireNonNull(conversation.getkGraphList().get(0)).getBackId();
            String backKey = Objects.requireNonNull(conversation.getkGraphList().get(0)).getBackKey();*/
            String backId = "";
            String backKey = "";
            if (kGraphBack != null) {
                backId = kGraphBack.getString("knowledgeId");
                backKey = kGraphBack.getString("knowledgeKey");
            }

            ivBack.setOnClickListener(v -> {

            });

            if (conversation.getkGraphTitle() != null && conversation.getkGraphTitle().isEmpty()) {
                tvKgraphTitle.setVisibility(View.GONE);
                llKgraphTextHolder.setVisibility(View.GONE);
            } else {
                tvKgraphTitle.setText(conversation.getkGraphTitle());
                llKgraphTextHolder.setVisibility(View.GONE);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            rvSuggestions.setLayoutManager(layoutManager);
            GlobalUtils.showLog(TAG, "conversation kgraph: " +
                    kGraphList.size());
            KgraphAdapter adapter = new KgraphAdapter(kGraphList, mContext, false);
            adapter.setOnItemClickListener(kGraph -> {

            });
            rvSuggestions.setAdapter(adapter);
        }

    }

    private class CallViewHolder extends RecyclerView.ViewHolder {
        TextView callDuration, callTime, sentAt;
        View spacing;

        public CallViewHolder(View itemView) {
            super(itemView);

            sentAt = itemView.findViewById(R.id.tv_sent_at);
            spacing = itemView.findViewById(R.id.spacing);
            callDuration = itemView.findViewById(R.id.tv_call_elapsed_time);
            callTime = itemView.findViewById(R.id.tv_call_time);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) {
            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            // Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.VISIBLE);
                showDateAndTime(conversation.getSentAt(), sentAt);
            }
            if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }

            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }

            callTime.setText(conversation.getCallInitiateTime());
            StringBuilder durationBuilder = new StringBuilder();
            durationBuilder.append(" (");
            durationBuilder.append(conversation.getCallDuration());
            durationBuilder.append(")");
            callDuration.setText(durationBuilder);
        }
    }

    private class ServiceDoerViewHolder extends RecyclerView.ViewHolder {
        LinearLayout serviceDoersHolder;
        TextView serviceDoersNames;

        public ServiceDoerViewHolder(View itemView) {
            super(itemView);

            serviceDoersNames = itemView.findViewById(R.id.tv_service_doer_names);
            serviceDoersHolder = itemView.findViewById(R.id.ll_service_doers_holder);
        }

        @SuppressLint("SetTextI18n")
        void bind(final Conversation conversation) {
            RealmList<ServiceDoer> serviceDoerList = conversation.getServiceDoerList();
            GlobalUtils.showLog(TAG, "service doers count: " +
                    serviceDoerList.size());
            if (!CollectionUtils.isEmpty(serviceDoerList)) {
                int remainingServiceDoers = serviceDoerList.size() - 1;

                serviceDoersHolder.removeAllViews();
                for (int i = 0; i < serviceDoerList.size(); i++) {
                    if (i < 4) {
                        LayoutInflater inflater = LayoutInflater.from(mContext);
                        @SuppressLint("InflateParams") CircleImageView viewServiceDoer =
                                (CircleImageView) inflater
                                        .inflate(R.layout.layout_service_doer_icon, null);

                        RequestOptions options = new RequestOptions()
                                .override(76, 76)
                                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                                .error(R.drawable.ic_empty_profile_holder_icon)
                                .fitCenter();

                        if (Objects.requireNonNull(serviceDoerList.get(i)).getProfileId() != null ||
                                !Objects.requireNonNull(serviceDoerList.get(i))
                                        .getProfilePic().isEmpty()) {
                            Glide.with(AnyDoneServiceProviderApplication.getContext())
                                    .load(Objects.requireNonNull(serviceDoerList.get(i))
                                            .getProfilePic())
                                    .apply(options)
                                    .into(viewServiceDoer);
                        }

                        serviceDoersHolder.addView(viewServiceDoer);
                    }
                }

                StringBuilder serviceDoerTextBuilder = new StringBuilder();
                if (serviceDoerList.size() > 4) {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    @SuppressLint("InflateParams") TextView textServiceDoer =
                            (TextView) inflater.
                                    inflate(R.layout.layout_service_doer_icon_more, null);

                    textServiceDoer.setText("+" + remainingServiceDoers);
                    serviceDoersHolder.addView(textServiceDoer);
                }

                if (serviceDoerList.size() <= 1) {
                    serviceDoerTextBuilder.append(Objects.requireNonNull(serviceDoerList.get(0))
                            .getFullName());
                    serviceDoerTextBuilder.append(" ");
                    serviceDoerTextBuilder.append("was");
                    serviceDoerTextBuilder.append(" ");
                    serviceDoerTextBuilder.append("added");
                } else {
                    serviceDoerTextBuilder.append(Objects.requireNonNull(serviceDoerList.get(0))
                            .getFullName());
                    serviceDoerTextBuilder.append(" ");
                    serviceDoerTextBuilder.append("and");
                    serviceDoerTextBuilder.append(" ");
                    serviceDoerTextBuilder.append(remainingServiceDoers);
                    serviceDoerTextBuilder.append(" ");
                    serviceDoerTextBuilder.append("other added");
                }

                serviceDoersNames.setText(serviceDoerTextBuilder);
            }
        }
    }


    private void displayBotOrUserMessage(TextView senderTitle, CircleImageView civSender,
                                         Conversation conversation) {
        senderTitle.setVisibility(View.VISIBLE);
        civSender.setVisibility(View.VISIBLE);
        if (conversation.getSenderType()
                .equalsIgnoreCase(RtcProto.MessageActor.ANYDONE_BOT_MESSAGE.name())) {
            senderTitle.setText(Constants.ANYDONE_BOT);

            Glide.with(AnyDoneServiceProviderApplication.getContext())
                    .load(R.drawable.ic_bot_icon)
                    .into(civSender);
        } else {
//            GlobalUtils.showLog(TAG, "sender name: " + conversation.getSenderName());
            if (conversation.getSenderName() != null && !conversation.getSenderName().isEmpty()) {
                senderTitle.setText(conversation.getSenderName());
            } else {
                senderTitle.setText(Hawk.get(Constants.SERVICE_PROVIDER_NAME));
            }

//            GlobalUtils.showLog(TAG, "sender image: " + );
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .fitCenter();

            Glide.with(AnyDoneServiceProviderApplication.getContext())
                    .load(conversation.getSenderImageUrl())
                    .apply(options)
                    .into(civSender);

            civSender.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks if the current message was sent by the same person that sent the preceding message.
     * <p>
     * This is done so that redundant UI, such as sender name and profile picture,
     * does not have to displayed when not necessary.
     */
    private boolean isContinuous(Conversation currentMsg, Conversation precedingMsg) {
        if (currentMsg == null || precedingMsg == null) {
            GlobalUtils.showLog(TAG, "both null");
            return false;
        }

        String currentUserID = currentMsg.getSenderId();
        String precedingUserId = precedingMsg.getSenderId();
        String currentSenderType = currentMsg.getSenderType();
        String precedingSenderType = precedingMsg.getSenderType();

        if (currentUserID != null && precedingUserId != null && currentSenderType != null
                && precedingSenderType != null) {
            GlobalUtils.showLog(TAG, "both not null");
            return !currentUserID.isEmpty() && !precedingUserId.isEmpty() &&
                    currentUserID.equalsIgnoreCase(precedingUserId) &&
                    currentSenderType.equals(precedingSenderType);
        } else {
            GlobalUtils.showLog(TAG, "last one");
            return false;
        }

    }

    public interface OnSuggestionClickListener {
        void onSuggestionClick(KGraph kGraph);
    }

    public void setOnSuggestionClickListener(MessageAdapter.OnSuggestionClickListener listener) {
        this.suggestionClickListener = listener;
    }

    public interface OnBackClickListener {
        void onBackClick(String prevQuestionKey, String prevQuestionId, String nextId, String nextKey);
    }

    public void setOnBackClickListener(MessageAdapter.OnBackClickListener listener) {
        this.onBackClickListener = listener;
    }
}


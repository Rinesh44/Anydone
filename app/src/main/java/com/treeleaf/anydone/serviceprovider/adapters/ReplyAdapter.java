package com.treeleaf.anydone.serviceprovider.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.card.MaterialCardView;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.serviceprovider.AnyDoneServiceProviderApplication;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ParticipantRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.DetectHtml;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.json.JSONException;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MessageAdapter";
    public static final int MSG_TEXT_LEFT = 0;
    public static final int MSG_IMG_LEFT = 2;
    public static final int MSG_LINK_LEFT = 4;
    public static final int MSG_DOC_LEFT = 6;
    public static final int MSG_TEXT_LEFT_HTML = 15;
    public static final int MSG_HEADER_TEXT = 8;
    public static final int MSG_HEADER_IMG = 10;
    public static final int MSG_HEADER_LINK = 12;
    public static final int MSG_HEADER_DOC = 14;


    private List<Conversation> conversationList;
    private Context mContext;
    private InboxMessageAdapter.OnItemLongClickListener listener;
    private InboxMessageAdapter.OnImageClickListener imageClickListener;
    private InboxMessageAdapter.OnMessageNotDeliveredListener messageNotDeliveredListener;
    private InboxMessageAdapter.OnSenderImageClickListener senderImageClickListener;
    private String headerId;

    public ReplyAdapter(List<Conversation> conversationList, Context mContext, String headerId) {
        this.conversationList = conversationList;
        this.mContext = mContext;
        this.headerId = headerId;
        GlobalUtils.showLog(TAG, "replies size: " + conversationList.size());
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
                GlobalUtils.showLog(TAG, "pre convo check: " + conversation.getMessage());
                conversationList.add(0, conversation);
                notifyItemInserted(0);
            }

        });
    }


    public void setData(List<Conversation> newConversationList) {
        if (!CollectionUtils.isEmpty(newConversationList)) {
            GlobalUtils.showLog(TAG, "conversation list checkout: " +
                    newConversationList.size());
         /*   conversationList.addAll(0, newConversationList);
            notifyItemRangeInserted(0, newConversationList.size());*/
            this.conversationList = newConversationList;
            notifyDataSetChanged();
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
            case MSG_HEADER_TEXT:
                View msgHeaderText = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_reply_header_text, parent, false);
                return new LeftTextHolderHtml(msgHeaderText);

            case MSG_HEADER_IMG:
                View msgHeaderImg = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_reply_header_img, parent, false);
                return new LeftImageHolder(msgHeaderImg);

            case MSG_HEADER_DOC:
                View msgHeaderDoc = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_reply_header_doc, parent, false);
                return new LeftDocHolder(msgHeaderDoc);

            case MSG_HEADER_LINK:
                View msgHeaderLink = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_reply_header_link, parent, false);
                return new LeftLinkHolder(msgHeaderLink);

            case MSG_TEXT_LEFT_HTML:
                View leftTextViewHtml = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_text_left_reply, parent, false);
                return new LeftTextHolderHtml(leftTextViewHtml);

            case MSG_IMG_LEFT:
                View leftImageView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_image_left_reply, parent, false);
                return new LeftImageHolder(leftImageView);

            case MSG_LINK_LEFT:
                View leftLinkView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_link_left_reply, parent, false);
                return new LeftLinkHolder(leftLinkView);

            case MSG_DOC_LEFT:
                View leftDocView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_doc_left_reply, parent, false);
                return new LeftDocHolder(leftDocView);

            default:
                View defaultView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_text_left_reply, parent, false);
                return new LeftTextHolder(defaultView);
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
//        if (position < conversationList.size() - 1) {
//            Conversation prevMessage = conversationList.get(position + 1);

        switch (holder.getItemViewType()) {
            case MSG_HEADER_TEXT:
                try {
                    ((LeftTextHolderHtml) holder).bind(conversation, isNewDay, isShowTime,
                            isContinuous);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case MSG_HEADER_LINK:
                ((LeftLinkHolder) holder).bind(conversation, isNewDay, isShowTime,
                        isContinuous);
                break;


            case MSG_HEADER_IMG:
                ((LeftImageHolder) holder).bind(conversation, isNewDay, isShowTime,
                        isContinuous);
                break;


            case MSG_HEADER_DOC:
                ((LeftDocHolder) holder).bind(conversation, isNewDay, isShowTime,
                        isContinuous);
                break;


            case MSG_TEXT_LEFT_HTML:
                try {
                    ((LeftTextHolderHtml) holder).bind(conversation, isNewDay, isShowTime,
                            isContinuous);
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

            case MSG_IMG_LEFT:
                ((LeftImageHolder) holder).bind(conversation, isNewDay, isShowTime,
                        isContinuous);
                break;

            case MSG_LINK_LEFT:
                ((LeftLinkHolder) holder).bind(conversation, isNewDay, isShowTime,
                        isContinuous);
                break;

            case MSG_DOC_LEFT:
                ((LeftDocHolder) holder).bind(conversation, isNewDay, isShowTime,
                        isContinuous);
                break;

        }
    }
//    }

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

        GlobalUtils.showLog(TAG, "conversaation client id: " + conversation.getConversationId());
        GlobalUtils.showLog(TAG, "conversaation header id: " + headerId);
        switch (conversation.getMessageType()) {
            case "TEXT_RTC_MESSAGE":
                if (conversation.getConversationId().equalsIgnoreCase(headerId)) {
                    return MSG_HEADER_TEXT;
                } else if (conversation.getMessage().contains("</p>") || conversation.getMessage().contains("</div>")) {
                    return MSG_TEXT_LEFT_HTML;
                } else return MSG_TEXT_LEFT;

            case "LINK_RTC_MESSAGE":
                if (conversation.getConversationId().equalsIgnoreCase(headerId)) {
                    return MSG_HEADER_LINK;
                } else
                    return MSG_LINK_LEFT;

            case "IMAGE_RTC_MESSAGE":
                if (conversation.getConversationId().equalsIgnoreCase(headerId)) {
                    return MSG_HEADER_IMG;
                } else
                    return MSG_IMG_LEFT;

            case "DOC_RTC_MESSAGE":
                if (conversation.getConversationId().equalsIgnoreCase(headerId)) {
                    return MSG_HEADER_DOC;
                } else
                    return MSG_DOC_LEFT;
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

    public void setOnItemLongClickListener(InboxMessageAdapter.OnItemLongClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageClickListener {
        void onImageClick(View view, int position);
    }

    public void setOnImageClickListener(InboxMessageAdapter.OnImageClickListener listener) {
        this.imageClickListener = listener;
    }

    public interface OnMessageNotDeliveredListener {
        void onMessageNotDelivered(Conversation message);
    }

    public void setOnMessageNotDeliveredListener
            (InboxMessageAdapter.OnMessageNotDeliveredListener
                     listener) {
        this.messageNotDeliveredListener = listener;
    }

    public interface OnSenderImageClickListener {
        void onSenderImageClick(Conversation conversation);
    }

    public void setOnSenderImageClickListener(InboxMessageAdapter.OnSenderImageClickListener
                                                      listener) {
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

    private class HeaderTextHolder extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle;
        TextView messageText;
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
        TextView tvTime;

        HeaderTextHolder(@NonNull View itemView) {
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
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) throws JSONException {

            GlobalUtils.showLog(TAG, "check msg left: " + conversation.getMessage());
            //show additional padding if not continuous
            rlMessageHolder.setVisibility(View.VISIBLE);
            rlKgraphHolder.setVisibility(View.GONE);
            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
                GlobalUtils.showLog(TAG, "spacing deleted");
            }

            //remove unnecessary line break
            int msgLength = conversation.getMessage().trim().length();
            if ((conversation.getMessage().trim().charAt(msgLength - 1) == 'n') &&
                    conversation.getMessage().trim().charAt(msgLength - 2) == '\"') {
                String escapeHtml = Jsoup.parse(conversation.getMessage()).toString();
                messageText.setText(escapeHtml.replace("\n", ""));
            } else messageText.setText(conversation.getMessage().trim());


            showTime(conversation.getSentAt(), tvTime);

            GlobalUtils.showLog(TAG, "inside replacement");
            String mentionPattern = "(?<=@)[\\w]+";
            Pattern p = Pattern.compile(mentionPattern);
            String msg = conversation.getMessage();
            Matcher m = p.matcher(msg);
//                    String changed = m.replaceAll("");
            while (m.find()) {
                GlobalUtils.showLog(TAG, "found: " + m.group(0));
                String employeeId = m.group(0);
                Participant participant = ParticipantRepo.getInstance()
                        .getParticipantByEmployeeAccountId(employeeId);
                if (participant != null && employeeId != null) {
                    Spannable wordToSpan = new SpannableString(participant.getEmployee().getName());
                    wordToSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),
                            0, wordToSpan.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    msg = msg.replace(employeeId, wordToSpan);
                }
            }


            messageText.setPadding(GlobalUtils.convertDpToPixel(mContext, 0),
                    GlobalUtils.convertDpToPixel(mContext, 0),
                    0,
                    GlobalUtils.convertDpToPixel(mContext, -38));


            boolean isHtml = DetectHtml.isHtml(conversation.getMessage());
            if (isHtml) {
                GlobalUtils.showLog(TAG, "is html true");
                messageText.setText(Html.fromHtml(msg));
            } else {
                messageText.setText(msg);
            }

            textHolder.setClickable(true);
            textHolder.setFocusable(true);

           /*     //check for bot name and image
                displayBotOrUserMessage(senderTitle, civSender, conversation);*/

            if (civSender != null) {
                civSender.setOnClickListener(v -> {
                    if (senderImageClickListener != null && getAdapterPosition() !=
                            RecyclerView.NO_POSITION) {
                        senderImageClickListener.onSenderImageClick(
                                conversationList.get(getAdapterPosition()));
                    }
                });

                displayBotOrUserMessage(senderTitle, civSender, conversation);
            } else {
                GlobalUtils.showLog(TAG, "civsender null");
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
        }
    }


    private class LeftTextHolderHtml extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle;
        TextView messageText;
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
        TextView tvTime;

        LeftTextHolderHtml(@NonNull View itemView) {
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
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) throws JSONException {

            GlobalUtils.showLog(TAG, "check msg left: " + conversation.getMessage());
            //show additional padding if not continuous
            rlMessageHolder.setVisibility(View.VISIBLE);
            rlKgraphHolder.setVisibility(View.GONE);
            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
                GlobalUtils.showLog(TAG, "spacing deleted");
            }

            //remove unnecessary line break
            int msgLength = conversation.getMessage().trim().length();
            if ((conversation.getMessage().trim().charAt(msgLength - 1) == 'n') &&
                    conversation.getMessage().trim().charAt(msgLength - 2) == '\"') {
                String escapeHtml = Jsoup.parse(conversation.getMessage()).toString();
                messageText.setText(escapeHtml.replace("\n", ""));
            } else messageText.setText(conversation.getMessage().trim());


            showTime(conversation.getSentAt(), tvTime);

            GlobalUtils.showLog(TAG, "inside replacement");
            String mentionPattern = "(?<=@)[\\w]+";
            Pattern p = Pattern.compile(mentionPattern);
            String msg = conversation.getMessage();
            Matcher m = p.matcher(msg);
//                    String changed = m.replaceAll("");
            while (m.find()) {
                GlobalUtils.showLog(TAG, "found: " + m.group(0));
                String employeeId = m.group(0);
                Participant participant = ParticipantRepo.getInstance()
                        .getParticipantByEmployeeAccountId(employeeId);
                if (participant != null && employeeId != null) {
                    Spannable wordToSpan = new SpannableString(participant.getEmployee().getName());
                    wordToSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),
                            0, wordToSpan.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    msg = msg.replace(employeeId, wordToSpan);
                }
            }


            messageText.setPadding(GlobalUtils.convertDpToPixel(mContext, 0),
                    GlobalUtils.convertDpToPixel(mContext, 0),
                    0,
                    GlobalUtils.convertDpToPixel(mContext, -38));


            boolean isHtml = DetectHtml.isHtml(conversation.getMessage());
            if (isHtml) {
                GlobalUtils.showLog(TAG, "is html true");
                messageText.setText(Html.fromHtml(msg));
            } else {
                messageText.setText(msg);
            }

            textHolder.setClickable(true);
            textHolder.setFocusable(true);

           /*     //check for bot name and image
                displayBotOrUserMessage(senderTitle, civSender, conversation);*/

            if (civSender != null) {
                civSender.setOnClickListener(v -> {
                    if (senderImageClickListener != null && getAdapterPosition() !=
                            RecyclerView.NO_POSITION) {
                        senderImageClickListener.onSenderImageClick(
                                conversationList.get(getAdapterPosition()));
                    }
                });

                displayBotOrUserMessage(senderTitle, civSender, conversation);
            } else {
                GlobalUtils.showLog(TAG, "civsender null");
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
        }
    }

    private class LeftTextHolder extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle;
        TextView messageText;
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
        TextView tvTime;

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
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) throws JSONException {

            GlobalUtils.showLog(TAG, "check msg left non html: " + conversation.getMessage());
            //show additional padding if not continuous
            rlMessageHolder.setVisibility(View.VISIBLE);
            rlKgraphHolder.setVisibility(View.GONE);
            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
                GlobalUtils.showLog(TAG, "spacing deleted");
            }

            showTime(conversation.getSentAt(), tvTime);

            //remove unnecessary line break
            int msgLength = conversation.getMessage().trim().length();
            if ((conversation.getMessage().trim().charAt(msgLength - 1) == 'n') &&
                    conversation.getMessage().trim().charAt(msgLength - 2) == '\"') {
                String escapeHtml = Jsoup.parse(conversation.getMessage()).toString();
                messageText.setText(escapeHtml.replace("\n", ""));
            } else messageText.setText(conversation.getMessage().trim());


            GlobalUtils.showLog(TAG, "inside replacement");
            String mentionPattern = "(?<=@)[\\w]+";
            Pattern p = Pattern.compile(mentionPattern);
            String msg = conversation.getMessage();
            Matcher m = p.matcher(msg);
//                    String changed = m.replaceAll("");
            while (m.find()) {
                GlobalUtils.showLog(TAG, "found: " + m.group(0));
                String employeeId = m.group(0);
                Participant participant = ParticipantRepo.getInstance()
                        .getParticipantByEmployeeAccountId(employeeId);
                if (participant != null && employeeId != null) {
                    Spannable wordToSpan = new SpannableString(participant.getEmployee().getName());
                    wordToSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),
                            0, wordToSpan.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    msg = msg.replace(employeeId, wordToSpan);
                }
            }
/*
            boolean isHtml = DetectHtml.isHtml(conversation.getMessage());
            if (isHtml) {
                GlobalUtils.showLog(TAG, "is html true");
                messageText.setText(Html.fromHtml(msg));
            } else {
                messageText.setText(msg);
            }*/

            messageText.setText(msg);
            textHolder.setClickable(true);
            textHolder.setFocusable(true);

           /*     //check for bot name and image
                displayBotOrUserMessage(senderTitle, civSender, conversation);*/

            if (civSender != null) {
                civSender.setOnClickListener(v -> {
                    if (senderImageClickListener != null && getAdapterPosition() !=
                            RecyclerView.NO_POSITION) {
                        senderImageClickListener.onSenderImageClick(
                                conversationList.get(getAdapterPosition()));
                    }
                });

                displayBotOrUserMessage(senderTitle, civSender, conversation);
            } else {
                GlobalUtils.showLog(TAG, "civsender null");
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
        }
    }


    private class LeftLinkHolder extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle, urlTitle, urlDesc, url;
        RelativeLayout urlHolder;
        ImageView resend, urlImage;
        CircleImageView civSender;
        View spacing;
        TextView tvTime;


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
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) {

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            url.setText(Jsoup.parse(conversation.getMessage()).text());
            if (conversation.getLinkImageUrl() != null && !conversation.getLinkImageUrl().isEmpty()) {

                urlDesc.setText(conversation.getLinkDesc());
                urlTitle.setText(conversation.getLinkTitle());

                Glide.with(mContext).load(conversation.getLinkImageUrl())
                        .error(R.drawable.ic_imageholder)
                        .placeholder(R.drawable.ic_imageholder)
                        .into(urlImage);

            } else {
                urlDesc.setVisibility(View.GONE);
                urlTitle.setVisibility(View.GONE);
                urlImage.setVisibility(View.GONE);
            }


   /*         try {
                RichPreview richPreview = new RichPreview(new ResponseListener() {
                    @Override
                    public void onData(MetaData metaData) {
                        //Implement your Layout
                        url.setText(Jsoup.parse(conversation.getMessage()).text());
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
                        if (!metaData.getDescription().isEmpty())
                            urlDesc.setText(metaData.getDescription());
                        else urlDesc.setVisibility(View.GONE);

                        if (Jsoup.parse(conversation.getMessage()).text().contains("google.com")) {
                            Glide.with(AnyDoneServiceProviderApplication.getContext())
                                    .load(R.drawable.google_logo)
                                    .into(urlImage);

                            urlDesc.setText("A search engine by Google LLC.");
                            urlDesc.setVisibility(View.VISIBLE);
                        }

                        urlHolder.setOnClickListener(v -> {
                            String url = metaData.getUrl();
                            if (!url.startsWith("https://")) {
                                url = "https://" + url;
                            }

                            GlobalUtils.showLog(TAG, "url check for link: " + metaData.getUrl());
                            Intent browserIntent = new Intent(
                                    Intent.ACTION_VIEW, Uri.parse(url));
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
                        richPreview.getPreview(Jsoup.parse(conversation.getMessage()).text());
                    }
                }

            } catch (Exception e) {
                GlobalUtils.showLog(TAG, "Exception occoured: " + e.getLocalizedMessage());
            }*/

            if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            } else {
                sentAt.setVisibility(View.GONE);
            }

            showTime(conversation.getSentAt(), tvTime);

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

            GlobalUtils.showLog(TAG, "check complete message link: " + conversation.getMessage());
            String[] links = extractLinks(conversation.getMessage());

            //click listeners
            urlHolder.setOnClickListener(v -> {
                if (links.length > 0) {
                    String url = links[0];
                    if (!url.startsWith("https://")) {
                        url = "https://" + url;
                    }


                    if (url != null) {
                        Intent browserIntent = new Intent(
                                Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(browserIntent);
                    }
                }
            });

        }
    }

    private class LeftImageHolder extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle, imageDesc;
        MaterialCardView imageHolder;
        ImageView resend, image;
        CircleImageView civSender;
        View spacing;
        ProgressBar progress;
        TextView tvTime;

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
            progress = itemView.findViewById(R.id.pb_image);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) {

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            senderTitle.setText(conversation.getSenderName());

            showTime(conversation.getSentAt(), tvTime);

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

            //resend handle and sent status
            if (!conversation.isSent() && conversation.isSendFail()) {
                //case when sending failed. Show resend layout
                resend.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            } else if (!conversation.isSent() && !conversation.isSendFail()) {
                progress.setVisibility(View.VISIBLE);
            } else {
                // case when message is sent. Disable progress and resend layout
                if (resend != null) {
                    resend.setVisibility(View.GONE);
                }

                progress.setVisibility(View.GONE);
                //handle sent visibility
                GlobalUtils.showLog(TAG, "conversation sent check: " + conversation.isSent());
            }

            if (civSender != null) {
                Glide.with(mContext)
                        .load(conversation.getSenderImageUrl())
                        .error(R.drawable.ic_empty_profile_holder_icon)
                        .error(R.drawable.ic_empty_profile_holder_icon)
                        .into(civSender);

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
        TextView tvTime;

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
            tvTime = itemView.findViewById(R.id.tv_time);

        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) {

            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }

            senderTitle.setText(conversation.getSenderName());
            showTime(conversation.getSentAt(), tvTime);
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

            if (civSender != null) {
                Glide.with(mContext)
                        .load(conversation.getSenderImageUrl())
                        .error(R.drawable.ic_empty_profile_holder_icon)
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .into(civSender);

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


    private void displayBotOrUserMessage(TextView senderTitle, CircleImageView civSender,
                                         Conversation conversation) {
        GlobalUtils.showLog(TAG, "display user details");
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


    private void showTime(long sentAt, TextView tvSentAt) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter =
                new SimpleDateFormat("h:mm aa");
        String timeString = timeFormatter.format(new Date(sentAt));

        tvSentAt.setText(timeString);
        tvSentAt.setVisibility(View.VISIBLE);
    }
}


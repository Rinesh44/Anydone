package com.treeleaf.anydone.serviceprovider.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chinalwb.are.AREditor;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.card.MaterialCardView;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.serviceprovider.AnyDoneServiceProviderApplication;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Attachment;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.KGraph;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
import java.util.UUID;
import java.util.regex.Matcher;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
import io.realm.Realm;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CommentAdapter";
    public static final int MSG_TEXT_LEFT = 0;
    public static final int MSG_IMG_LEFT = 2;
    public static final int MSG_LINK_LEFT = 4;
    public static final int MSG_DOC_LEFT = 6;
    public static final int MSG_BOT_SUGGESTIONS = 10;
    public static final int INITIAL_SERVICE_DETAIL = 11;
    public static final int MSG_CALL_OUTGOING = 12;
    public static final int INITIAL_TICKET_DETAIL = 13;
    public static final int MSG_BOT_SUGGESTION_JSON = 14;

    private List<Conversation> conversationList;
    private Context mContext;
    private CommentAdapter.OnItemLongClickListener listener;
    private CommentAdapter.OnImageClickListener imageClickListener;
    private CommentAdapter.OnMessageNotDeliveredListener messageNotDeliveredListener;
    private CommentAdapter.OnSenderImageClickListener senderImageClickListener;
    private CommentAdapter.OnSuggestionClickListener suggestionClickListener;
    private CommentAdapter.OnBackClickListener onBackClickListener;
    private CommentAdapter.OnAddAttachmentListener onAddAttachmentListener;
    private CommentAdapter.OnAttachmentImageClickListener onAttachmentImageClickListener;
    private CommentAdapter.OnAttachmentRemoveListener onAttachmentRemoveListener;

    public CommentAdapter(List<Conversation> conversationList, Context mContext) {
//        super(DIFF_CALLBACK);
        this.mContext = mContext;
        this.conversationList = conversationList;
    }

    public void removeAttachment(long ticketId, Attachment attachment) {
        Conversation commentHeader = conversationList.get(conversationList.size() - 1);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            commentHeader.getAttachmentRealmList().remove(attachment);
            notifyItemChanged(conversationList.size() - 1);
        });
        TicketRepo.getInstance().removeAttachment(ticketId, attachment);
    }

/*    private static final DiffUtil.ItemCallback<Conversation> DIFF_CALLBACK = new DiffUtil.ItemCallback<Conversation>() {
        @Override
        public boolean areItemsTheSame(@NonNull Conversation oldItem, @NonNull Conversation newItem) {
            return oldItem.getClientId().equalsIgnoreCase(newItem.getClientId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Conversation oldItem, @NonNull Conversation newItem) {
            return oldItem.getConversationId().equals(newItem.getConversationId()) &&
                    oldItem.getMessage().equals(newItem.getMessage()) &&
                    oldItem.getMessageType().equals(newItem.getMessageType()) &&
                    oldItem.getTicketTitle().equals(newItem.getTicketTitle()) &&
                    oldItem.getTicketDesc().equals(newItem.getTicketDesc());
        }
    };*/

    public void setData(Conversation conversation) {
        GlobalUtils.showLog(TAG, "set data called");
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
//                notifyItemRangeChanged(0, 3);
//                notifyDataSetChanged();
//                submitList(conversationList);
            }

     /*       if (!conversation.getMessageType().equals("MSG_BOT_SUGGESTIONS")) {
                GlobalUtils.showLog(TAG, "msg type not bot suggestions");
                if (conversationList.size() > 0) {
                    GlobalUtils.showLog(TAG, "prev index refreshed");
                    int prevIndex = conversationList.indexOf(conversation) + 1;
                    notifyItemChanged(prevIndex);
                }
            }*/

            //add comments header if first comment
            if (conversationList.size() == 2) {
                notifyItemChanged(1);
            }
        });
    }

    public void addAttachment(long ticketId) {
        Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);
        Conversation commentHeader = conversationList.get(conversationList.size() - 1);
        commentHeader.setAttachmentRealmList(tickets.getAttachmentList());
        notifyItemChanged(conversationList.size() - 1);
    }

    public void setCommentVisibility() {
        notifyItemChanged(conversationList.size());
    }

    public void setAcceptedTAG(Conversation conversation) {
        conversationList.add(conversation);
        Collections.sort(conversationList, (o1, o2) ->
                Long.compare(o2.getSentAt(), o1.getSentAt()));
//        notifyDataSetChanged();
//        submitList(conversationList);
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
           /* conversationList.addAll(0, newConversationList);
            notifyItemRangeInserted(0, newConversationList.size());*/
            this.conversationList = newConversationList;
            notifyDataSetChanged();
        }

        notifyItemChanged(conversationList.size() - 1);
        GlobalUtils.showLog(TAG, "conversation list size check: " + conversationList.size());
    }


    public void checkIfLastItemInComment() {
        if (conversationList.size() == 1) {
            notifyItemChanged(0);
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

            case MSG_IMG_LEFT:
                View leftImageView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_image, parent, false);
                return new LeftImageHolder(leftImageView);

            case MSG_LINK_LEFT:
                View leftLinkView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_link, parent, false);
                return new LeftLinkHolder(leftLinkView);

            case MSG_DOC_LEFT:
                View leftDocView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_doc, parent, false);
                return new LeftDocHolder(leftDocView);

            case MSG_BOT_SUGGESTIONS:
                View suggestionsView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_bot_suggestions, parent, false);
                return new BotSuggestionsHolder(suggestionsView);

            case INITIAL_TICKET_DETAIL:
                View ticketDetailView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_initial_ticket_detail, parent, false);
                return new InitialTicketDetailHolder(ticketDetailView);

            case MSG_CALL_OUTGOING:
                View callView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_call, parent, false);
                return new CallViewHolder(callView);

            default:
                View defaultView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_text, parent, false);
                return new LeftTextHolder(defaultView);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);

        GlobalUtils.showLog(TAG, "refid check: " + conversation.getRefId());

        boolean isNewDay = false;
        boolean isShowTime = false;

        GlobalUtils.showLog(TAG, "current pos: " + position);
        GlobalUtils.showLog(TAG, "current msg: " + conversation.getMessage());

        // If there is at least one item preceding the current one, check the previous message.
        if (position < conversationList.size() - 1) {
            GlobalUtils.showLog(TAG, "conversation list size: " + conversationList.size());
            GlobalUtils.showLog(TAG, "currnt pos" + position);
            Conversation prevMessage = conversationList.get(position + 1);
            GlobalUtils.showLog(TAG, "prev msg: " + prevMessage.getMessage());
            long timeDiff = conversation.getSentAt() - prevMessage.getSentAt();
            // If the date of the previous message is different, display the date before the message,
            // and also set isContinuous to false to show information such as the sender's name
            // and profile image.
            if (!isSameDay(conversation.getSentAt(), prevMessage.getSentAt())) {
                isNewDay = true;
                GlobalUtils.showLog(TAG, "first");
            } else if (isSameDay(conversation.getSentAt(), prevMessage.getSentAt())
                    && timeDiff > 20 * 60 * 1000) {
                isShowTime = true;
                GlobalUtils.showLog(TAG, "second");
            } else {
                GlobalUtils.showLog(TAG, "third");
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
                    ((LeftTextHolder) holder).bind(conversation, isNewDay, isShowTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case MSG_IMG_LEFT:
                ((LeftImageHolder) holder).bind(conversation, isNewDay, isShowTime,
                        false);
                break;

            case MSG_LINK_LEFT:
                ((LeftLinkHolder) holder).bind(conversation, isNewDay, isShowTime,
                        false);
                break;

            case MSG_DOC_LEFT:
                ((LeftDocHolder) holder).bind(conversation, isNewDay, isShowTime,
                        false);
                break;

            case MSG_BOT_SUGGESTIONS:
                ((BotSuggestionsHolder) holder).bind(conversation, false);
                break;

            case INITIAL_TICKET_DETAIL:
                ((InitialTicketDetailHolder) holder).bind(conversation);
                break;

            case MSG_CALL_OUTGOING:
                ((CallViewHolder) holder).bind(conversation, isNewDay, isShowTime, false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showDateAndTime(long sentAt, TextView tvSentAt, TextView tvDate) {
        tvSentAt.setVisibility(View.GONE);
        if (DateUtils.isToday(sentAt)) {
            tvSentAt.setText(R.string.today);
            tvSentAt.setVisibility(View.GONE);
            tvDate.setText(R.string.today);
        } else if (DateUtils.isToday(sentAt + DateUtils.DAY_IN_MILLIS)) {//check for yesterday
            tvSentAt.setText(R.string.yesterday);
            tvSentAt.setVisibility(View.GONE);
            tvDate.setText(R.string.yesterday);
        } else {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter =
                    new SimpleDateFormat("dd MMM");

            String dateString = dateFormatter.format(new Date(sentAt));
            tvSentAt.setText(dateString);
            tvSentAt.setVisibility(View.GONE);
            tvDate.setText(dateString);
        }
    }

    private void showTime(long sentAt, TextView tvSentAt) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter =
                new SimpleDateFormat("HH:mm a");
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
                boolean isReplyInJson = GlobalUtils.isJSONValid(conversation.getMessage());
                if (isReplyInJson) return MSG_BOT_SUGGESTION_JSON;
                else return MSG_TEXT_LEFT;

            case "BOT_CONVERSATION_REQUEST":
                return MSG_TEXT_LEFT;

            case "LINK_RTC_MESSAGE":
                return MSG_LINK_LEFT;

            case "IMAGE_RTC_MESSAGE":
                return MSG_IMG_LEFT;

            case "DOC_RTC_MESSAGE":
                return MSG_DOC_LEFT;

            case "MSG_BOT_SUGGESTIONS":
                return MSG_BOT_SUGGESTIONS;

            case "INITIAL_SERVICE_DETAIL":
                return INITIAL_SERVICE_DETAIL;

            case "INITIAL_TICKET_DETAIL":
                return INITIAL_TICKET_DETAIL;

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

    public void setOnItemLongClickListener(CommentAdapter.OnItemLongClickListener listener) {
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

    public void setOnMessageNotDeliveredListener(CommentAdapter.OnMessageNotDeliveredListener
                                                         listener) {
        this.messageNotDeliveredListener = listener;
    }

    public interface OnSenderImageClickListener {
        void onSenderImageClick(Conversation conversation);
    }

    public void setOnSenderImageClickListener(CommentAdapter.OnSenderImageClickListener listener) {
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

    private class LeftTextHolder extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle, time, date, tvTextPlain;
        AREditor messageText;
        RelativeLayout textHolder;
        ImageView resend;
        CircleImageView civSender;
        View spacing;
        CircleImageView civKgraphSender;
        TextView tvBotName;

        LeftTextHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tv_text);
            sentAt = itemView.findViewById(R.id.tv_sent_at);
            textHolder = itemView.findViewById(R.id.ll_text_holder);
            resend = itemView.findViewById(R.id.iv_resend);
            civSender = itemView.findViewById(R.id.civ_sender);
            senderTitle = itemView.findViewById(R.id.tv_title);
            spacing = itemView.findViewById(R.id.spacing);
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);
            tvTextPlain = itemView.findViewById(R.id.tv_text_plain);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime) throws JSONException {

//            if (!isReplyInJson) {
            //show additional padding if not continuous
            spacing.setVisibility(View.VISIBLE);
            // Show the date if the message was sent on a different date than the previous message.

         /*   messageText.setHtml(conversation.getMessage());
            messageText.setEditorFontSize(15);*/

            messageText.setHideToolbar(true);
            messageText.getARE().setTextSize(14);
            messageText.getARE().setLongClickable(false);

            if (conversation.getMessage() != null) {
                if (conversation.getMessage().toLowerCase().contains("<ol>") ||
                        conversation.getMessage().toLowerCase().contains("<ul>")) {
                    messageText.setPadding(GlobalUtils.convertDpToPixel(mContext, -18),
                            GlobalUtils.convertDpToPixel(mContext, -10),
                            0,
                            GlobalUtils.convertDpToPixel(mContext, -45));
                } else messageText.setPadding(GlobalUtils.convertDpToPixel(mContext, -8),
                        GlobalUtils.convertDpToPixel(mContext, -10),
                        0,
                        GlobalUtils.convertDpToPixel(mContext, -25));


                if (conversation.getMessage().contains("</p>")) {
                    GlobalUtils.showLog(TAG, "msg contains tags");
                    messageText.fromHtml(conversation.getMessage());
                    messageText.setVisibility(View.VISIBLE);
                    tvTextPlain.setVisibility(View.GONE);
                } else {
                    GlobalUtils.showLog(TAG, "msg doesnt contains tags");
                    GlobalUtils.showLog(TAG, "msg check: " + conversation.getMessage());
                    tvTextPlain.setText(conversation.getMessage());
                    messageText.setVisibility(View.GONE);
                    tvTextPlain.setVisibility(View.VISIBLE);
                }
            }

            textHolder.setClickable(true);
            textHolder.setFocusable(true);
            if (isNewDay) {
                sentAt.setVisibility(View.GONE);
            } else {
                sentAt.setVisibility(View.GONE);
            }

            showDateAndTime(conversation.getSentAt(), sentAt, date);
            time.setText(GlobalUtils.getTimeExcludeMillis(conversation.getSentAt()));
         /*   if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }*/
            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }

            // Hide profile image and name if the previous message was also sent by current sender.
            //check for bot name and image
            displayBotOrUserMessage(senderTitle, civSender, conversation);

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
        private long mLastClickTime = 0;

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
            //reply is in json, so bot suggestion
            if (!isContinuous) {
                kgraphSpacing.setVisibility(View.VISIBLE);
            } else {
                kgraphSpacing.setVisibility(View.GONE);
            }

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

            String finalBackId = backId;
            String finalBackKey = backKey;
            ivBack.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (onBackClickListener != null) {
                    onBackClickListener.onBackClick("", "", "",
                            "", finalBackId, "title", finalBackKey);
                }
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
            KgraphAdapter adapter = new KgraphAdapter(kGraphList, mContext, true);
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

    private class LeftLinkHolder extends RecyclerView.ViewHolder {
        TextView sentAt, senderTitle, urlTitle, urlDesc, url, time, date;
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
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);

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
                        String url = metaData.getUrl();
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
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
                String[] extractedLink = extractLinks(conversation.getMessage().trim());
//                GlobalUtils.showLog(TAG, "extracted: " + extractedLink[0]);

                if (extractedLink.length != 0) {
                    if (!extractedLink[0].contains("https://")) {
                        String linkWithHttps = "https://" + extractedLink[0].trim();
                        richPreview.getPreview(linkWithHttps);
                    } else {
                        richPreview.getPreview(conversation.getMessage().trim());
                    }
                }
            }

            //Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.GONE);
            } else {
                sentAt.setVisibility(View.GONE);
            }

            showDateAndTime(conversation.getSentAt(), sentAt, date);
            time.setText(GlobalUtils.getTimeExcludeMillis(conversation.getSentAt()));
        /*    if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            } else {
                sentAt.setVisibility(View.GONE);
            }*/

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
        TextView sentAt, senderTitle, imageDesc, time, date;
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
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);
        }

        void bind(final Conversation conversation, boolean isNewDay, boolean showTime,
                  boolean isContinuous) {
            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                spacing.setVisibility(View.GONE);
            }


            GlobalUtils.showLog(TAG, "check image url: " + conversation.getMessage());
            if (conversation.getMessage() != null && conversation.getMessage().contains("https://")) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_imageholder)
                        .error(R.drawable.ic_imageholder);
                Glide.with(AnyDoneServiceProviderApplication.getContext())
                        .load(conversation.getMessage())
                        .apply(options.override(700, 620))
                        .centerCrop()
                        .into(image);
            } else {
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
                }
            }

            // Show the date if the message was sent on a different date than the previous message.
            if (isNewDay) {
                sentAt.setVisibility(View.VISIBLE);
            } else {
                sentAt.setVisibility(View.GONE);
            }

            showDateAndTime(conversation.getSentAt(), sentAt, date);
            time.setText(GlobalUtils.getTimeExcludeMillis(conversation.getSentAt()));
         /*   if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }*/

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
        TextView sentAt, senderTitle, fileName, fileSize, time, date;
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
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);

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
            } else {
                sentAt.setVisibility(View.GONE);
            }

            showDateAndTime(conversation.getSentAt(), sentAt, date);
            time.setText(GlobalUtils.getTimeExcludeMillis(conversation.getSentAt()));
         /*   if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }*/

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
        CircleImageView civBot;
        TextView tvBotName;
        private long mLastClickTime = 0;

        BotSuggestionsHolder(@NonNull View itemView) {
            super(itemView);

            spacing = itemView.findViewById(R.id.spacing);
            suggestionTitle = itemView.findViewById(R.id.tv_title);
            suggestions = itemView.findViewById(R.id.rv_suggestions);
            back = itemView.findViewById(R.id.iv_back);
            civBot = itemView.findViewById(R.id.civ_sender);
            tvBotName = itemView.findViewById(R.id.tv_kGraph_bot);
        }

        void bind(final Conversation conversation, boolean isContinuous) {
            Glide.with(mContext)
                    .load(conversation.getSenderImageUrl())
                    .error(R.drawable.ic_bot_icon)
                    .placeholder(R.drawable.ic_bot_icon)
                    .into(civBot);

            if (conversation.getSenderName().isEmpty()) {
                tvBotName.setText("Anydone Bot");
            } else {
                tvBotName.setText(conversation.getSenderName());
            }
/*
            if (!isContinuous) {
                spacing.setVisibility(View.VISIBLE);
            } else {
                    spacing.setVisibility(View.GONE);
            }*/


            GlobalUtils.showLog(TAG, "bot name check: " + conversation.getSenderName());
            GlobalUtils.showLog(TAG, "bot image check: " + conversation.getSenderImageUrl());
            spacing.setVisibility(View.VISIBLE);

            if (conversation.iskGraphBack()) {
                back.setVisibility(View.VISIBLE);
            } else {
                back.setVisibility(View.GONE);
            }

            back.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                int position = getAdapterPosition();
                if (onBackClickListener != null && position != RecyclerView.NO_POSITION) {
                    String nextId = Objects.requireNonNull(conversation.getkGraphList().get(0)).getId();
                    String nextKey = Objects.requireNonNull(conversation.getkGraphList().get(0)).getNext();

                    String prevId = Objects.requireNonNull(conversation.getkGraphList().get(0)).getPrevId();
                    String prevKey = Objects.requireNonNull(conversation.getkGraphList().get(0)).getPrev();

                    String backId = Objects.requireNonNull(conversation.getkGraphList().get(0)).getBackId();
                    String backKey = Objects.requireNonNull(conversation.getkGraphList().get(0)).getBackKey();

                    String title = Objects.requireNonNull(conversation.getkGraphList().get(0)).getTitle();

                    if (prevId != null && prevKey != null) {
                        onBackClickListener.onBackClick(nextId, nextKey, prevKey, prevId, backId,
                                title, backKey);
                    } else {
                        Toast.makeText(mContext, "empty back data", Toast.LENGTH_SHORT).show();
                    }
                }
            });

      /*      if (conversation.getkGraphTitle() == null || conversation.getkGraphTitle().isEmpty()) {
                suggestionTitle.setVisibility(View.GONE);
            } else {
                suggestionTitle.setText(conversation.getkGraphTitle());
            }*/

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            suggestions.setLayoutManager(layoutManager);
            KgraphAdapter adapter = new KgraphAdapter(conversation.getkGraphList(), mContext, true);
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

/*    private class InitialServiceDetailHolder extends RecyclerView.ViewHolder {
        TextView serviceName, problemStat, location, date;
        ImageView serviceIcon;
        CircleImageView civSender;


        InitialServiceDetailHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.tv_service_name);
            problemStat = itemView.findViewById(R.id.tv_problem_stat);
            location = itemView.findViewById(R.id.tv_location);
            serviceIcon = itemView.findViewById(R.id.iv_service_icon);
            civSender = itemView.findViewById(R.id.civ_sender);
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
    }*/


    private class InitialTicketDetailHolder extends RecyclerView.ViewHolder {
        TextView ticketId, ticketTitle, ticketDesc;
        LinearLayout llLabels;
        HorizontalScrollView hsvTags;
        RelativeLayout rlCommentHolder;
        RecyclerView rvAttachments;

        InitialTicketDetailHolder(@NonNull View itemView) {
            super(itemView);

            ticketId = itemView.findViewById(R.id.tv_ticket_id);
            ticketTitle = itemView.findViewById(R.id.tv_ticket_title);
            ticketDesc = itemView.findViewById(R.id.tv_ticket_desc);
            llLabels = itemView.findViewById(R.id.ll_label_holder);
            hsvTags = itemView.findViewById(R.id.scv_label);
            rlCommentHolder = itemView.findViewById(R.id.rl_comments_title);
            rvAttachments = itemView.findViewById(R.id.rv_attachments);
        }

        void bind(final Conversation conversation) {
//            ticketId.setText("#" + conversation.getRefId());

            ticketTitle.setText(conversation.getTicketTitle());
            if (conversation.getTicketDesc() != null && !conversation.getTicketDesc().isEmpty()) {
                ticketDesc.setText(conversation.getTicketDesc());
            } else {
                ticketDesc.setVisibility(View.GONE);
            }

            if (conversationList.size() == 1) {
                rlCommentHolder.setVisibility(View.GONE);
            } else {
                rlCommentHolder.setVisibility(View.VISIBLE);
            }

            if (!CollectionUtils.isEmpty(conversation.getTagsList())) {
                llLabels.removeAllViews();
                for (Label tag : conversation.getTagsList()
                ) {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    @SuppressLint("InflateParams") TextView tvTag = (TextView) inflater
                            .inflate(R.layout.layout_blue_tag_bg, null);
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

            setupAttachmentRecyclerView(rvAttachments, conversation);
        }
    }

    private void setupAttachmentRecyclerView(RecyclerView rvAttachments, Conversation
            conversation) {
        GlobalUtils.showLog(TAG, "attachment setup");
        List<Attachment> attachmentList = conversation.getAttachmentRealmList();
        if (attachmentList == null || attachmentList.isEmpty()) {
            attachmentList = new ArrayList<>();
            Attachment addAttachment = new Attachment();
            addAttachment.setId(UUID.randomUUID().toString().replace("-", ""));
            addAttachment.setType(0);

            attachmentList.add(addAttachment);

            rvAttachments.setLayoutManager(new GridLayoutManager(mContext, 3));

            AttachmentAdapter adapter = new AttachmentAdapter(attachmentList, mContext);
            rvAttachments.setAdapter(adapter);

            adapter.setOnAddAttachmentClickListener(() -> {
                GlobalUtils.showLog(TAG, "comment adapter received add click");
                if (onAddAttachmentListener != null) {
                    onAddAttachmentListener.onAttachmentAdd();
                }
            });

            adapter.setOnAttachmentImageClickListener((pos, imagesList) -> {
                if (onAttachmentImageClickListener != null) {
                    GlobalUtils.showLog(TAG, "image click listened on comment adapter");
                    onAttachmentImageClickListener.onImageClick(pos, imagesList);
                }
            });

            adapter.setOnAttachmentRemoveListener(attachment -> {
                if (onAttachmentRemoveListener != null) {
                    GlobalUtils.showLog(TAG, "attachment remove listened on comment adapter");
                    onAttachmentRemoveListener.onAttachmentRemove(attachment);
                }
            });

            adapter.setOnDocClickListener(attachment -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(attachment.getUrl()));
                mContext.startActivity(browserIntent);
            });

        } else {
            rvAttachments.setLayoutManager(new GridLayoutManager(mContext, 3));
            AttachmentAdapter adapter = new AttachmentAdapter(attachmentList, mContext);
            rvAttachments.setAdapter(adapter);
            adapter.setData(attachmentList);

            adapter.setOnAddAttachmentClickListener(() -> {
                GlobalUtils.showLog(TAG, "comment adapter received add click");
                if (onAddAttachmentListener != null) {
                    onAddAttachmentListener.onAttachmentAdd();
                }
            });

            adapter.setOnAttachmentImageClickListener((pos, imagesList) -> {
                if (onAttachmentImageClickListener != null) {
                    GlobalUtils.showLog(TAG, "image click listened on comment adapter");
                    onAttachmentImageClickListener.onImageClick(pos, imagesList);
                }
            });

            adapter.setOnAttachmentRemoveListener(attachment -> {
                if (onAttachmentRemoveListener != null) {
                    GlobalUtils.showLog(TAG, "attachment remove listened on comment adapter");
                    onAttachmentRemoveListener.onAttachmentRemove(attachment);
                }
            });

            adapter.setOnDocClickListener(attachment -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(attachment.getUrl()));
                mContext.startActivity(browserIntent);
            });
        }
    }


    private class CallViewHolder extends RecyclerView.ViewHolder {
        TextView callDuration, callTime, sentAt, tvTitle, tvTime, date;
        View spacing;
        CircleImageView civSender;

        public CallViewHolder(View itemView) {
            super(itemView);

            sentAt = itemView.findViewById(R.id.tv_sent_at);
            spacing = itemView.findViewById(R.id.spacing);
            callDuration = itemView.findViewById(R.id.tv_call_elapsed_time);
            callTime = itemView.findViewById(R.id.tv_call_time);
            civSender = itemView.findViewById(R.id.civ_sender);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);
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
                sentAt.setVisibility(View.GONE);
            } else {
                sentAt.setVisibility(View.GONE);
            }

            showDateAndTime(conversation.getSentAt(), sentAt, date);

        /*    if (showTime) {
                sentAt.setVisibility(View.VISIBLE);
                showTime(conversation.getSentAt(), sentAt);
            }*/

            if (!isNewDay && !showTime) {
                sentAt.setVisibility(View.GONE);
            }

            callTime.setText("Call ended at " + conversation.getCallInitiateTime());
            StringBuilder durationBuilder = new StringBuilder();
            durationBuilder.append(" (");
            durationBuilder.append(conversation.getCallDuration());
            durationBuilder.append(")");
            callDuration.setText(durationBuilder);

            displayBotOrUserMessage(tvTitle, civSender, conversation);

            if (civSender != null) {
                civSender.setOnClickListener(v -> {
                    if (senderImageClickListener != null && getAdapterPosition() !=
                            RecyclerView.NO_POSITION) {
                        senderImageClickListener.onSenderImageClick(
                                conversationList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

    private void displayBotOrUserMessage(TextView senderTitle, CircleImageView civSender,
                                         Conversation conversation) {
        senderTitle.setVisibility(View.VISIBLE);
        civSender.setVisibility(View.VISIBLE);
        if (conversation.getSenderType()
                .equalsIgnoreCase(RtcProto.MessageActor.ANYDONE_BOT_MESSAGE.name())) {
            senderTitle.setText(conversation.getSenderName());

            Glide.with(AnyDoneServiceProviderApplication.getContext())
                    .load(conversation.getSenderImageUrl())
                    .fitCenter()
                    .into(civSender);
        } else {
            Account account = AccountRepo.getInstance().getAccount();
            if (conversation.getSenderId().equals(account.getAccountId())) {
                senderTitle.setText(account.getFullName());

                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .error(R.drawable.ic_empty_profile_holder_icon)
                        .fitCenter();

                Glide.with(AnyDoneServiceProviderApplication.getContext())
                        .load(account.getProfilePic())
                        .apply(options)
                        .into(civSender);

                civSender.setVisibility(View.VISIBLE);
                return;
            }
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


    public interface OnSuggestionClickListener {
        void onSuggestionClick(KGraph kGraph);
    }

    public void setOnSuggestionClickListener(CommentAdapter.OnSuggestionClickListener
                                                     listener) {
        this.suggestionClickListener = listener;
    }

    public interface OnBackClickListener {
        void onBackClick(String nextId, String nextKey, String prevQuestionKey, String prevId,
                         String backId, String clickedMsg, String backKey);
    }

    public void setOnBackClickListener(CommentAdapter.OnBackClickListener listener) {
        this.onBackClickListener = listener;
    }

    public interface OnAddAttachmentListener {
        void onAttachmentAdd();
    }

    public void setOnAddAttachmentClickListener(CommentAdapter.OnAddAttachmentListener
                                                        listener) {
        this.onAddAttachmentListener = listener;
    }

    public interface OnAttachmentRemoveListener {
        void onAttachmentRemove(Attachment attachment);
    }

    public void setOnAttachmentRemoveListener(CommentAdapter.OnAttachmentRemoveListener
                                                      listener) {
        this.onAttachmentRemoveListener = listener;
    }

    public interface OnAttachmentImageClickListener {
        void onImageClick(int pos, List<String> urlList);
    }

    public void setOnAttachmentImageClickListener
            (CommentAdapter.OnAttachmentImageClickListener listener) {
        this.onAttachmentImageClickListener = listener;
    }
}


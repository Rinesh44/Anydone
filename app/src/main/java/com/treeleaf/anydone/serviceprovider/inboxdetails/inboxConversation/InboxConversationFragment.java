package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxConversation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_UpdaterDefault;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.linkedin.android.spyglass.suggestions.SuggestionsResult;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.InboxMessageAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.PersonMentionAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.forwardMessage.ForwardMessageActivity;
import com.treeleaf.anydone.serviceprovider.inboxdetails.InboxDetailActivity;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConversationRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ParticipantRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceProviderRepo;
import com.treeleaf.anydone.serviceprovider.reply.ReplyActivity;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ImagesFullScreen;
import com.treeleaf.anydone.serviceprovider.utils.NetworkChangeReceiver;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.videocallreceive.OnVideoCallEventListener;
import com.vanniktech.emoji.EmojiPopup;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedkeyboardobserver.TedRxKeyboardObserver;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class InboxConversationFragment extends BaseFragment<InboxConversationPresenterImpl>
        implements InboxConversationContract.InboxConversationView, QueryTokenReceiver,
        SuggestionsResultListener, SuggestionsVisibilityManager,
        TreeleafMqttClient.OnMQTTConnected, InboxDetailActivity.OnOutsideClickListener,
        InboxDetailActivity.MqttDelegate {
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 6543;
    public static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 8776;
    public static final int PICK_FILE_REQUEST_CODE = 8997;
    public static final int REPLY_REQUEST = 8197;

    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.ll_search_container)
    LinearLayout llSearchContainer;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.rich_editor)
    AREditText etMessage;
    @BindView(R.id.rv_conversations)
    RecyclerView rvConversation;
    @BindView(R.id.bottom_sheet)
    LinearLayout llBottomSheetMessage;
    @BindView(R.id.rl_copy_holder)
    RelativeLayout ivCopy;
    @BindView(R.id.rl_delete_holder)
    RelativeLayout ivDelete;
    @BindView(R.id.ll_attach_options)
    LinearLayout llAttachOptions;
    @BindView(R.id.tv_files)
    TextView tvFiles;
    @BindView(R.id.tv_camera)
    TextView tvCamera;
    @BindView(R.id.tv_recorder)
    TextView tvGallery;
    @BindView(R.id.iv_attachment)
    ImageView ivAttachment;
    @BindView(R.id.et_image_desc)
    EditText etImageDesc;
    @BindView(R.id.iv_capture_view)
    ImageView ivCaptureView;
    @BindView(R.id.iv_send_desc)
    ImageView ivSendImage;
    @BindView(R.id.root)
    RelativeLayout rlRoot;
    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;
    @BindView(R.id.tv_connection_status)
    TextView tvConnectionStatus;
    @BindView(R.id.bottom_sheet_profile)
    LinearLayout mBottomSheet;
    @BindView(R.id.pb_load_data)
    ProgressBar pbLoadData;
    @BindView(R.id.ll_text_modifier)
    ARE_ToolbarDefault llTextModifier;
    /*    @BindView(R.id.ll_bottom_options)
        LinearLayout llBottomOptions;*/
    @BindView(R.id.ll_emoji)
    LinearLayout llEmoji;
    @BindView(R.id.ll_mentions)
    LinearLayout llMentions;
    @BindView(R.id.ll_text_modifier_container)
    LinearLayout llTextModifierContainer;
    /* @BindView(R.id.tv_emoji)
     TextView tvEmoji;*/
    @BindView(R.id.rv_mentions)
    RecyclerView rvMentions;
    @BindView(R.id.rl_reply_holder)
    RelativeLayout rlReplyHolder;
    @BindView(R.id.rich_editor_invisible)
    AREditText etMessageInvisible;

    public static CoordinatorLayout clCaptureView;
    private static final String TAG = "InboxCoversationFragmen";
    private String currentPhotoPath = "";
    private String inboxId;
    private BottomSheetBehavior messageSheetBehavior;
    private BottomSheetBehavior profileSheetBehavior;
    private Conversation longClickedMessage;
    private InboxMessageAdapter adapter;
    private PersonMentionAdapter mentionsAdapter;
    private List<Conversation> conversationList = new ArrayList<>();
    private boolean attachmentToggle = false;
    private Bitmap capturedBitmap;
    private Uri uri;
    private String imageOrientation = "portrait";
    private List<String> imagesList = new ArrayList<>();
    private Uri selectedFileUri;
    private NetworkChangeReceiver networkChangeReceiver;
    private boolean connectionFlag = false;
    //    private boolean fetchRemainingMessages = false;
    private String userAccountId;
    private boolean isScrolling = false;
    private int currentItems, scrollOutItems, totalItems;
    private boolean keyboardShown = false;
    private boolean emojiToggle = false;
    private String msgForApi;
    private Disposable keyboardObserver;
    private String finalMsg = "";
    private int screenHeight;
    private int replyIndex = -1;
    private OnVideoCallEventListener videoCallBackListener;
    private Account userAccount;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ClickableViewAccessibility", "CheckResult"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InboxDetailActivity activity = (InboxDetailActivity) getActivity();
        assert activity != null;
        activity.setOutSideTouchListener(this);
        userAccount = AccountRepo.getInstance().getAccount();

        Employee employeeAccount = EmployeeRepo.getInstance().getEmployee();
        if (employeeAccount != null) {
            userAccountId = employeeAccount.getAccountId();
        } else {
            ServiceProvider serviceProvider = ServiceProviderRepo.getInstance().getServiceProvider();
            userAccountId = serviceProvider.getAccountId();
        }
        etMessage.requestFocus();
        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        inboxId = i.getStringExtra("inbox_id");

        if (inboxId == null && i.getExtras() != null) {
            inboxId = i.getExtras().getString("inboxId");
        }

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        screenHeight = size.y;

        GlobalUtils.showLog(TAG, "screen height: " + screenHeight);

        setUpMentionsAdapter();
        initTextModifier();
        if (inboxId != null) {
            conversationList = ConversationRepo.getInstance()
                    .getConversationByOrderId(inboxId);

            GlobalUtils.showLog(TAG, "inbox id check:" + inboxId);
            Collections.reverse(conversationList);

            setUpConversationView();
            if (CollectionUtils.isEmpty(conversationList)) {
                pbLoadData.setVisibility(View.VISIBLE);
                presenter.getMessages(inboxId, 0, System.currentTimeMillis(),
                        100, true);
            } else {
//                fetchRemainingMessages = true;
                presenter.getMessages(inboxId, 0, System.currentTimeMillis(),
                        100, false);
            }

            try {
                presenter.subscribeSuccessMessage(inboxId, userAccountId);
                presenter.subscribeFailMessage();
                /**
                 * mqtt subscription for video call events
                 */
                presenter.subscribeSuccessMessageAVCall(inboxId, userAccount.getAccountId());
                presenter.subscribeFailMessageAVCall(inboxId);
            } catch (MqttException e) {
                e.printStackTrace();
            }


            presenter.getInbox(String.valueOf(inboxId));
        }

        llMentions.setOnClickListener(v -> {
            if (etMessage.getText() != null)
                etMessage.getText().insert(etMessage.getText().length(), "@");
            Objects.requireNonNull(etMessageInvisible.getText()).insert(etMessageInvisible.getText().length(), "@");
        });

        ivSend.setEnabled(false);
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GlobalUtils.showLog(TAG, "on text changed()");
                if (s.length() > 0) {
                    ivSend.setImageTintList(AppCompatResources.getColorStateList
                            (Objects.requireNonNull(getContext()), R.color.colorPrimary));
                    ivSend.setEnabled(true);
                 /*   List<Participant> searchResults = ParticipantRepo.getInstance().searchParticipant(inboxId,
                            s.toString().substring(index + 1));
                    mentionsAdapter.setData(searchResults);
                    rvMentions.setVisibility(View.VISIBLE);*/
//                    GlobalUtils.showLog(TAG, "search results: " + searchResults.size());

                    int index = etMessage.getText().toString().lastIndexOf("@");
                    mentionsAdapter.getFilter().filter(s.toString().substring(index + 1));
                    rvMentions.setVisibility(View.VISIBLE);
                } else {
                    ivSend.setImageTintList(AppCompatResources.getColorStateList
                            (Objects.requireNonNull(getContext()), R.color.selector_disabled));
                    ivSend.setEnabled(false);
                    rvMentions.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etMessage.getText() != null && !etMessage.getText().toString().isEmpty()) {
                    String last = Objects.requireNonNull(etMessage.getText()).toString();
                    last = last.substring(last.length() - 1);
                    GlobalUtils.showLog(TAG, "last char check: " + last);
                    if (last.equals("@")) {
                        GlobalUtils.showLog(TAG, "made suggestions visible");
                        rvMentions.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        etMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                llTextModifier.setVisibility(View.VISIBLE);
            }
        });

        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(clRoot).build(etMessage);
        llEmoji.setOnClickListener(v -> {
            if (emojiPopup.isShowing()) {
                etMessage.requestFocus();
                emojiPopup.dismiss();
            } else {
                if (keyboardShown) ((InboxDetailActivity) getActivity()).hideKeyBoard();
                llEmoji.setBackgroundColor(getContext().getColor(R.color.red));
                emojiPopup.show();
            }
        });


        clCaptureView = view.findViewById(R.id.cl_capture_view);
        messageSheetBehavior = BottomSheetBehavior.from(llBottomSheetMessage);
        profileSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        InboxDetailActivity mActivity = (InboxDetailActivity) getActivity();
        assert mActivity != null;
        mActivity.setOutSideTouchListener(this);
        TreeleafMqttClient.setOnMqttConnectedListener(this);

    }

    private void setUpMentionsAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvMentions.setLayoutManager(layoutManager);

        List<Participant> participantList = ParticipantRepo.getInstance().getParticipantsExcludingSelf(inboxId);
        mentionsAdapter = new PersonMentionAdapter(participantList, getActivity());
        rvMentions.setAdapter(mentionsAdapter);

        mentionsAdapter.setOnItemClickListener(participant -> {
            int cursorPos = etMessage.getSelectionEnd();
            Spannable wordToSpan = new SpannableString(participant.getEmployee().getName());
            Spannable wordToSpanInvisible = new SpannableString(participant.getEmployee().getAccountId());
            wordToSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                    0, wordToSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int index = etMessage.getText().toString().lastIndexOf("@");
            int spaceIndex = etMessage.getText().toString().lastIndexOf(" ");
            GlobalUtils.showLog(TAG, "space index check: " + spaceIndex);
            if (etMessage.getText() != null) {
                etMessageInvisible.setText(etMessage.getText());
                etMessage.getText().replace(index + 1, etMessage.getSelectionEnd(), wordToSpan);
                GlobalUtils.showLog(TAG, "check msg on invisible text: " + etMessageInvisible.getText().toString());
                etMessageInvisible.getText().replace(index + 1, etMessageInvisible.getText().length(), wordToSpanInvisible);
            } else {
                etMessage.setText(wordToSpan);
                etMessageInvisible.setText(wordToSpanInvisible);
            }
            rvMentions.setVisibility(View.GONE);
        });
    }

    private List<String> getImageList() {
        GlobalUtils.showLog(TAG, "conversation list size: " + conversationList.size());
        for (Conversation conversation : conversationList
        ) {
            if (conversation.getMessageType().equalsIgnoreCase("IMAGE_RTC_MESSAGE")) {
                imagesList.add(conversation.getMessage());
            }
        }
        return imagesList;
    }

    @OnClick(R.id.iv_send)
    void sendMessageClick() {
        if (!UiUtils.getString(etMessage).isEmpty()) {
            presenter.checkConnection(TreeleafMqttClient.mqttClient);
        }
    }

    @SuppressLint("CheckResult")
    private void sendMessage(Conversation conversation) {
        GlobalUtils.showLog(TAG, "post conversation id: " + conversation.getClientId());
        if (conversation.getParentId() == null || conversation.getParentId().isEmpty()) {
//            this.conversationList.add(conversation);
            adapter.setData(conversation);
            presenter.enterMessage(rvConversation, etMessageInvisible);
            GlobalUtils.showLog(TAG, "final msg print: "
                    + Objects.requireNonNull(etMessageInvisible.getText()).toString());
        } else {
            ((Activity) Objects.requireNonNull(getContext())).runOnUiThread(() -> {
                //update reply count if msg has same parent
                for (Conversation existingConversation : conversationList
                ) {
                    if (existingConversation.getConversationId().equals(conversation.getParentId())) {
                        ConversationRepo.getInstance().updateReplyCount(existingConversation,
                                1, new Repo.Callback() {
                                    @Override
                                    public void success(Object o) {
                                        GlobalUtils.showLog(TAG, "reply count updated");
                                        Conversation updateConversation = ConversationRepo.getInstance()
                                                .getConversationByMessageId(existingConversation.getConversationId());
                                        int index = conversationList.indexOf(updateConversation);
                                        if (index == -1) index = 0;
                                        adapter.replaceData(updateConversation, index);
                                    }

                                    @Override
                                    public void fail() {
                                        GlobalUtils.showLog(TAG, "failed to update reply count");
                                    }
                                });
                    }
                }
            });
            GlobalUtils.showLog(TAG, "parent id: " + conversation.getParentId());
        }
    }

    @OnClick(R.id.rl_delete_holder)
    void deleteMessage() {
        showDeleteConfirmation();
        toggleMessageBottomSheet();
    }


    @OnClick(R.id.rl_copy_holder)
    void copyMessage() {
        GlobalUtils.showLog(TAG, "get message id: " + longClickedMessage.getConversationId());
        ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getContext())
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copied_text",
                Html.fromHtml(longClickedMessage.getMessage()).toString().trim());
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        toggleMessageBottomSheet();
        Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rl_forward_holder)
    void forwardMessage() {
        toggleMessageBottomSheet();
        Intent i = new Intent(getContext(), ForwardMessageActivity.class);
        i.putExtra("msg_to_forward", longClickedMessage.getMessage());
        startActivity(i);
    }


    @OnClick(R.id.rl_reply_holder)
    void replyMessage() {
        toggleMessageBottomSheet();
        Intent i = new Intent(getContext(), ReplyActivity.class);
        i.putExtra("client_id", longClickedMessage.getConversationId());
        i.putExtra("inbox_id", inboxId);
        startActivityForResult(i, REPLY_REQUEST);
    }

    private void showDeleteConfirmation() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Delete message?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Cancel",
                (dialog, id) -> dialog.cancel());

        builder1.setNegativeButton(
                "Delete",
                (dialog, id) -> {
                    presenter.publishMessageDelete(longClickedMessage);
                    dialog.dismiss();
                });


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

        });
        alert11.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpConversationView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        ((SimpleItemAnimator) Objects.requireNonNull(rvConversation.getItemAnimator()))
                .setSupportsChangeAnimations(false);
        rvConversation.setLayoutManager(layoutManager);

        rvConversation.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager)
                    Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });

        Collections.reverse(conversationList);
        adapter = new InboxMessageAdapter(conversationList, getActivity());
        adapter.setOnItemLongClickListener(new InboxMessageAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Conversation message) {
                longClickedMessage = message;
                toggleMessageBottomSheet();
            }

            @Override
            public void onItemClick(Conversation message) {
                longClickedMessage = message;
                replyIndex = conversationList.indexOf(message);
                GlobalUtils.showLog(TAG, "check reply index: " + replyIndex);
                Intent i = new Intent(getContext(), ReplyActivity.class);
                i.putExtra("client_id", message.getConversationId());
                i.putExtra("inbox_id", inboxId);
                startActivityForResult(i, REPLY_REQUEST);
            }
        });

        adapter.setOnMessageNotDeliveredListener(this::resendByMessageType);
        adapter.setOnSenderImageClickListener(new InboxMessageAdapter.OnSenderImageClickListener() {
            @Override
            public void onSenderImageClick(Conversation conversation) {
                if (!conversation.getSenderType().equalsIgnoreCase(RtcProto
                        .MessageActor.ANYDONE_BOT_MESSAGE.name())) {
                    setUpProfileBottomSheet(conversation.getSenderName(),
                            conversation.getSenderImageUrl(),
                            4f);
                    toggleBottomSheet();
                }
            }

            @Override
            public void onSenderImageClick(Participant participant) {
                GlobalUtils.showLog(TAG, "mention clicked");
                setUpProfileBottomSheet(participant.getEmployee().getName(),
                        participant.getEmployee().getEmployeeImageUrl(),
                        4f);
                toggleBottomSheet();
            }
        });

        adapter.setOnImageClickListener((view, position) -> {
            GlobalUtils.showLog(TAG, "image click check");
            Conversation conversation = this.conversationList.get(position);
            if (conversation.getMessageType().equalsIgnoreCase("IMAGE_RTC_MESSAGE")) {
                imagesList.clear();
                imagesList = getImageList();
                Collections.reverse(imagesList);
                GlobalUtils.showLog(TAG, "imageList size:  " + imagesList.size());
                if (!CollectionUtils.isEmpty(imagesList)) {
                    for (String imageUrl : imagesList
                    ) {
                        if (imageUrl != null && imageUrl.equalsIgnoreCase(conversation.getMessage())) {
                            int imagePosition = imagesList.indexOf(imageUrl);

                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("images", (ArrayList<String>) imagesList);
                            bundle.putInt("position", imagePosition);

                            FragmentTransaction ft = Objects.requireNonNull(getActivity())
                                    .getSupportFragmentManager().beginTransaction();
                            ImagesFullScreen newFragment = ImagesFullScreen.newInstance();
                            newFragment.setArguments(bundle);
                            newFragment.show(ft, "slideshow");
                        }
                    }
                }
            }
        });

        rvConversation.setAdapter(adapter);
        rvConversation.scrollToPosition(0);
    }

    private void resendByMessageType(Conversation message) {
        switch (message.getMessageType()) {
            case "TEXT_RTC_MESSAGE":

            case "LINK_RTC_MESSAGE":
                presenter.resendMessage(message);
                break;

            case "IMAGE_RTC_MESSAGE":
                Uri uri = Uri.parse(message.getImageUri());
                presenter.uploadImage(uri, message, getActivity());
                break;

            case "DOC_RTC_MESSAGE":
                presenter.uploadDoc(selectedFileUri, message);
                break;

            case "AUDIO_RTC_MESSAGE":
                break;

            case "VIDEO_RTC_MESSAGE":
                break;

            case "VIDEO_CALL_RTC_MESSAGE":
                break;

            case "AUDIO_CALL_RTC_MESSAGE":
                break;

            case "UNRECOGNIZED":
                break;

            default:
                break;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_inbox_conversation;
    }


    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setUpProfileBottomSheet(String name, String imageUrl, float rating) {
        TextView ratingNumber = mBottomSheet.findViewById(R.id.tv_rate_number);
        RatingBar ratingBar = mBottomSheet.findViewById(R.id.rating);
        CircleImageView profileImage = mBottomSheet.findViewById(R.id.iv_profile_user_image);
        TextView profileName = mBottomSheet.findViewById(R.id.tv_profile_username);

        profileName.setText(name);
        ratingBar.setRating(rating);
        ratingNumber.setText("(" + rating + ")");
//        if (imageUrl != null && !imageUrl.isEmpty()) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(this)
                .load(imageUrl)
                .apply(options)
                .into(profileImage);
//        }

    }

    @Override
    public void onFailure(String message) {
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), Constants.SERVER_ERROR);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showToast(getActivity(), Constants.SERVER_ERROR);
    }


    @Override
    public void getInboxSuccess(Inbox inbox) {
        GlobalUtils.showLog(TAG, "inbox details: " + inbox);
    }

    @Override
    public void getInboxFail(String msg) {
        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void onUploadImageSuccess(String imageUrl, Uri imageUri,
                                     String clientId, String imageCaption) {
        try {
            capturedBitmap = MediaStore.Images.Media
                    .getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), imageUri);
            if (capturedBitmap.getWidth() > capturedBitmap.getHeight()) {
                imageOrientation = "landscape";
            } else {
                imageOrientation = "portrait";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        GlobalUtils.showLog(TAG, "Image orientation check: " + imageOrientation);
        presenter.publishImage(imageUrl, inboxId, clientId, imageCaption);
    }

    @Override
    public void onUploadImageFail(String msg, Conversation conversation) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        adapter.setData(conversation);
//        sendMessage(conversation);
    }

    @Override
    public void onDocUploadFail(String msg, Conversation conversation) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        adapter.setData(conversation);
//        sendMessage(conversation);
    }

    @Override
    public void onDocUploadSuccess(String docUrl, File file, String clientId) {
        presenter.publishDoc(docUrl, file, inboxId, clientId);
    }

    @Override
    public void onSubscribeSuccessMsg(Conversation conversation, boolean botReply) {
        GlobalUtils.showLog(TAG, "subscribe callback reached fragment");
        if (botReply) {
//            showBotReplying();
        } else {
            Hawk.put(Constants.BOT_REPLY, false);
      /*      Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                    llBotReplying.setVisibility(View.GONE));*/

//            ThreadRepo.getInstance().disableBotReply(threadId);
        }
        GlobalUtils.showLog(TAG, "parent id get: " + conversation.getParentId());
        sendMessage(conversation);
    }

    @Override
    public void onSubscribeFailMsg(Conversation conversation) {
        sendMessage(conversation);
    }

    @Override
    public void onConnectionSuccess() {
        if (!Objects.requireNonNull(etMessage.getText()).toString().contains("@")) {
            etMessageInvisible.setText(etMessage.getText());
        }

        if (isLink(Objects.requireNonNull(etMessageInvisible.getText()).toString().trim())) {
            GlobalUtils.showLog(TAG, "isLink true");
            presenter.publishTextOrUrlMessage(
                    Jsoup.parse(etMessageInvisible.getText().toString()).text(), inboxId, false);
        } else {
            GlobalUtils.showLog(TAG, "isLink false");
            String resultMsg = etMessage.getHtml();
            GlobalUtils.showLog(TAG, "resultMsg: " + resultMsg);
            presenter.publishTextOrUrlMessage(resultMsg, inboxId, false);
        }
    }

    private boolean isLink(String message) {
        String[] links = extractLinks(message);
        if (links.length != 0) {
            return true;
        } else {
            return false;
        }
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

    @Override
    public void onConnectionFail(String msg) {
     /*   Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();*/

        GlobalUtils.showLog(TAG, "mqtt not connected");
        String env = Hawk.get(Constants.BASE_URL);
        boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
        GlobalUtils.showLog(TAG, "prod env check: " + prodEnv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TreeleafMqttClient.start(
                    Objects.requireNonNull(getActivity()).getApplicationContext(), prodEnv,
                    new TreeleafMqttCallback() {
                        @Override
                        public void messageArrived(String topic, MqttMessage message) {
                            GlobalUtils.showLog(TAG, "mqtt topic: " + topic);
                            GlobalUtils.showLog(TAG, "mqtt message: " + message);
                        }
                    });
        }

    /*    tvConnectionStatus.setText(R.string.reconnecting);
        tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.green));
        tvConnectionStatus.setVisibility(View.VISIBLE);*/

        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.INFO, msg, Banner.TOP, 2000).show();


    }

    @Override
    public void getMessagesSuccess(List<Conversation> conversationList, boolean showProgress) {
        this.conversationList = conversationList;
        pbLoadData.setVisibility(View.GONE);
        //sort list in ascending order by time
        GlobalUtils.showLog(TAG, "get messages success");
        GlobalUtils.showLog(TAG, "new messages count: " + conversationList.size());

        Collections.sort(conversationList, (o1, o2) ->
                Long.compare(o2.getSentAt(), o1.getSentAt()));
        adapter.setData(conversationList);
        if (rvConversation != null && showProgress)
            rvConversation.postDelayed(() -> rvConversation.scrollToPosition(0), 100);
//        if (fetchRemainingMessages) {
        presenter.sendDeliveredStatusForMessages(conversationList);
//        }
    }

    @Override
    public void getMessageFail(String message) {
        pbLoadData.setVisibility(View.GONE);
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), message);
            onAuthorizationFailed(getActivity());
        }
    }

    @Override
    public void onImagePreConversationSuccess(Conversation conversation) {
        String imageFileClientId = conversation.getClientId();
        adapter.setData(conversation);
        rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition
                (0), 50);
        presenter.uploadImage(uri, conversation, getActivity());
    }

    @Override
    public void onDocPreConversationSuccess(Conversation conversation) {
        adapter.setData(conversation);
        Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition
                        (0), 50));
        presenter.uploadDoc(selectedFileUri, conversation);
    }

    @Override
    public void onTextPreConversationSuccess(Conversation conversation) {
        GlobalUtils.showLog(TAG, "before post check: " + conversation.isSent());
        adapter.setData(conversation);
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition
                    (0), 100);
            etMessage.setText("");
            etMessageInvisible.setText("");
        });

        if (conversation.getMessageType().equalsIgnoreCase("LINK_RTC_MESSAGE")) {
            String[] links = extractLinks(conversation.getMessage());
            presenter.getLinkDetails(links[0], conversation);
        } else {
            presenter.publishTextMessage(conversation.getMessage(), conversation.getRefId(),
                    userAccountId, conversation.getClientId());
        }

/*        if (conversation.getMessageType()
                .equalsIgnoreCase(RtcProto.RtcMessageType.LINK_RTC_MESSAGE.name())) {
            presenter.publishLinkMessage(conversation.getMessage(), conversation.getRefId(),
                    userAccountId, conversation.getClientId());
        } else {
            presenter.publishTextMessage(conversation.getMessage(), conversation.getRefId(),
                    userAccountId, conversation.getClientId());
        }*/
    }

    @Override
    public void onDeleteMessageSuccess() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            int index = conversationList.indexOf(longClickedMessage);
            GlobalUtils.showLog(TAG, "check deleted msg index: " + index);
            if (index != -1) {
                conversationList.remove(index);
                adapter.notifyItemRemoved(index);
            }
            ConversationRepo.getInstance().deleteConversationById(longClickedMessage.getClientId());
            hideProgressBar();

            if (index == 0 && conversationList.size() > 0) {
                Conversation prevMsg = conversationList.get(0);
                GlobalUtils.showLog(TAG, "prev msg after del: " + prevMsg.getMessage());
                if (prevMsg.getParentId() == null || prevMsg.getParentId().isEmpty()) {
                    InboxRepo.getInstance().updateLastMsg(inboxId, prevMsg, new Repo.Callback() {
                        @Override
                        public void success(Object o) {
                            GlobalUtils.showLog(TAG, "last msg updated");
                        }

                        @Override
                        public void fail() {
                            GlobalUtils.showLog(TAG, "failed to update last msg");
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void setSeenStatus(Conversation conversation) {
        adapter.setData(conversation);
    }

    @Override
    public void onSendDeliveredMsgFail(List<Conversation> conversationList) {
        //todo
    }

    private void openCamera() throws IOException {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getImageFile(); // 1
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 2
            uri = FileProvider.getUriForFile(getActivity(),
                    "com.treeleaf.anydone.serviceprovider.provider", file);
        } else {
            uri = Uri.fromFile(file); // 3
        }
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        File file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        GlobalUtils.showLog(TAG, "on activity result");
        GlobalUtils.showLog(TAG, "requestCode: " + requestCode);
        GlobalUtils.showLog(TAG, "resultCode: " + resultCode);

        if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            Objects.requireNonNull(getActivity()).getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Objects.requireNonNull(((InboxDetailActivity)
                    getActivity()).getSupportActionBar()).hide();

            uri = Uri.parse(currentPhotoPath);
            setupSingleImageView(uri);
        }

        if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE &&
                resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();

            if (clipData != null) {
                GlobalUtils.showLog(TAG, "images count: " + clipData.getItemCount());
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    uri = clipData.getItemAt(i).getUri();
                    Bitmap bitmap;
                    Bitmap convertedBitmap;
                    try {
                        bitmap = MediaStore.Images.Media
                                .getBitmap(Objects.requireNonNull(getActivity())
                                        .getContentResolver(), uri);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        convertedBitmap = UiUtils.getResizedBitmap(bitmap, 200);
                        convertedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, baos);

                        presenter.createPreConversationForImage(uri.toString(), inboxId,
                                "", convertedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                Objects.requireNonNull(getActivity()).getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                Objects.requireNonNull(((InboxDetailActivity)
                        getActivity()).getSupportActionBar()).hide();

                uri = data.getData();
                setupSingleImageView(uri);
            }
        }

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            GlobalUtils.showLog(TAG, "uri check: " + selectedFileUri);
            if (selectedFileUri != null) {
                File file = new File(Objects.requireNonNull(
                        GlobalUtils.getPath(selectedFileUri, getContext())));
                presenter.createPreConversationForDoc(inboxId, file);
            } else {
                GlobalUtils.showLog(TAG, "file is null");
            }
        } else if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Objects.requireNonNull(getActivity()).getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Objects.requireNonNull(((InboxDetailActivity)
                    getActivity()).getSupportActionBar()).show();
        }


        if (requestCode == REPLY_REQUEST && resultCode == 771 && data != null) {
            int count = data.getIntExtra("count", -1);
            GlobalUtils.showLog(TAG, "check count changes: " + count);
            if (count > 0) {
                ConversationRepo.getInstance().updateReplyCount(longClickedMessage, count,
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "reply count updated");
                                GlobalUtils.showLog(TAG, "conversation list size: " + conversationList.size());
//                                int index = conversationList.indexOf(longClickedMessage);
                           /*     Conversation conversation = ConversationRepo.getInstance()
                                        .getConversationByMessageId(longClickedMessage.getConversationId());
                                int index = conversationList.indexOf(conversation);*/
                                adapter.replaceData(longClickedMessage, replyIndex);
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to update reply count");
                            }
                        });
            }
        }

    }

    private void setupSingleImageView(Uri uri) {
        try {
            capturedBitmap = MediaStore.Images.Media.getBitmap(
                    Objects.requireNonNull(getContext()).getContentResolver(), uri);
            capturedBitmap = GlobalUtils.fixBitmapRotation(uri, capturedBitmap, getActivity());

            if (capturedBitmap.getWidth() > capturedBitmap.getHeight()) {
                imageOrientation = "landscape";
                ivCaptureView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                imageOrientation = "portrait";
                ivCaptureView.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            GlobalUtils.showLog(TAG, "Orientation: " + imageOrientation);
            ivCaptureView.setImageBitmap(capturedBitmap);
            clCaptureView.setVisibility(View.VISIBLE);
            etImageDesc.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_send_desc)
    void sendImage() {
        UiUtils.hideKeyboard(Objects.requireNonNull(getActivity()));
        clCaptureView.setVisibility(View.INVISIBLE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(((InboxDetailActivity)
                getActivity()).getSupportActionBar()).show();

        String imageCaption = UiUtils.getString(etImageDesc);
        etImageDesc.setText("");

        Bitmap bitmap;
        Bitmap convertedBitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            convertedBitmap = GlobalUtils.fixBitmapRotation(uri, bitmap, getActivity());
            convertedBitmap = UiUtils.getResizedBitmap(convertedBitmap, 200);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            convertedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        presenter.createPreConversationForImage(uri.toString(), inboxId,
                imageCaption, convertedBitmap);
    }
/*
    @OnClick(R.id.iv_clear)
    void clearText() {
        Objects.requireNonNull(etMessage.getText()).clear();
        ivClear.setVisibility(View.GONE);
        etMessage.requestFocus();
//        ivSpeech.setVisibility(View.VISIBLE);
    }*/

    @OnClick(R.id.tv_camera)
    void initCamera() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getContext(),
                                    "Camera and media/files access permissions are required",
                                    Toast.LENGTH_SHORT).show();
                            openAppSettings();
                        }

                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            try {
                                llAttachOptions.setVisibility(View.GONE);
                                openCamera();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @OnClick(R.id.iv_attachment)
    void showAttachmentOptions() {
        attachmentToggle = !attachmentToggle;
        if (attachmentToggle) {
            llAttachOptions.setVisibility(View.VISIBLE);
        } else {
            llAttachOptions.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_files)
    void openFiles() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getContext(),
                                    "Media/files access permissions are required",
                                    Toast.LENGTH_SHORT).show();
                            openAppSettings();
                        }

                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            gotoFiles();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @OnClick(R.id.tv_recorder)
    void initRecorder() {
        llAttachOptions.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_gallery)
    void showGallery() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getContext(),
                                    "Media/files access permissions are required",
                                    Toast.LENGTH_SHORT).show();
                            openAppSettings();
                        }

                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            openGallery();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void openGallery() {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pictureIntent.setType("image/*");  // 1
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);  // 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};  // 3
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent,
                "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);  // 4
    }

    private void toggleMessageBottomSheet() {
        if (messageSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            RelativeLayout copyHolder = llBottomSheetMessage.findViewById(R.id.rl_copy_holder);
            RelativeLayout deleteHolder = llBottomSheetMessage.findViewById(R.id.rl_delete_holder);
            LinearLayout root = llBottomSheetMessage.findViewById(R.id.bottom_sheet);

/*
            if (longClickedMessage.getMessageType()
                    .equalsIgnoreCase("IMAGE_RTC_MESSAGE")) {
                copyHolder.setVisibility(View.VISIBLE);
            } else {
                copyHolder.setVisibility(View.GONE);
            }
*/

            if (!longClickedMessage.getSenderId().equals(userAccountId)) {
                deleteHolder.setVisibility(View.GONE);
                root.setWeightSum(3);
            } else {
                deleteHolder.setVisibility(View.VISIBLE);
                root.setWeightSum(4);
            }
            messageSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            messageSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        TreeleafMqttClient.mqttClient.registerResources(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setUpNetworkBroadCastReceiver();
        }
        registerReceiver();
    }

    private void gotoFiles() {
        Uri selectedUri = Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS)));
        GlobalUtils.showLog(TAG, "selectedUri: " + selectedUri);
        llAttachOptions.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(selectedUri, "application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        TreeleafMqttClient.mqttClient.unregisterResources();
        unregisterReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardObserver.dispose();
        llSearchContainer.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();

        llSearchContainer.requestFocus();
        llTextModifier.setVisibility(View.GONE);
        try {
            presenter.subscribeSuccessMessage(inboxId, userAccountId);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        keyboardObserver = new TedRxKeyboardObserver(getActivity())
                .listen()
                .subscribe(isShow -> {
                    keyboardShown = !keyboardShown;
                    if (keyboardShown) {
                        if (screenHeight <= 1280) {
                            llTextModifierContainer.setVisibility(View.GONE);
                            ((RelativeLayout.LayoutParams) llSearchContainer.getLayoutParams())
                                    .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        } else {
                            GlobalUtils.showLog(TAG, "keyboard shown listened");
                            ((RelativeLayout.LayoutParams) llSearchContainer.getLayoutParams())
                                    .removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            llTextModifierContainer.setVisibility(View.VISIBLE);
                            rvConversation.postDelayed(() -> rvConversation.scrollToPosition(0), 50);
                            etMessage.postDelayed(() -> etMessage.requestFocus(), 50);
                        }
                    } else {
                        if (screenHeight <= 1280) {
                            llTextModifierContainer.setVisibility(View.VISIBLE);
                            ((RelativeLayout.LayoutParams) llSearchContainer.getLayoutParams())
                                    .removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            rvConversation.postDelayed(() -> rvConversation.scrollToPosition(0), 50);
                            etMessage.postDelayed(() -> etMessage.requestFocus(), 50);
                        } else {
                            llTextModifierContainer.setVisibility(View.GONE);
                            ((RelativeLayout.LayoutParams) llSearchContainer.getLayoutParams())
                                    .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        }
                    }
                }, Throwable::printStackTrace);
        if (

                getView() == null) {
            GlobalUtils.showLog(TAG, "get view is null");
            return;
        }

        getView().
                setOnKeyListener((v, keyCode, event) ->

                {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        GlobalUtils.showLog(TAG, "back key press listen");
                        if (clCaptureView.getVisibility() == View.VISIBLE) {
                            clCaptureView.setVisibility(View.GONE);
                            Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.
                                    LayoutParams.FLAG_FULLSCREEN);
                            Objects.requireNonNull(((InboxDetailActivity)
                                    getActivity()).getSupportActionBar()).show();
                        } else {
                            Objects.requireNonNull(getActivity()).finish();
                        }
                        return true;
                    }
                    return false;
                });
    }

    @Override
    public void onOutsideClick(MotionEvent event) {
        GlobalUtils.showLog(TAG, "on outside click first");

        if (llAttachOptions.getVisibility() == View.VISIBLE) {
            Rect outRect = new Rect();
            llAttachOptions.getGlobalVisibleRect(outRect);

            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                attachmentToggle = !attachmentToggle;
                llAttachOptions.setVisibility(View.GONE);
            }
        }
        if (messageSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            Rect outRect = new Rect();
            llBottomSheetMessage.getGlobalVisibleRect(outRect);

            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                messageSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if (profileSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            GlobalUtils.showLog(TAG, "profile sheet shown");
            Rect outRect = new Rect();
            mBottomSheet.getGlobalVisibleRect(outRect);

            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                GlobalUtils.showLog(TAG, "collpse called");
                profileSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpNetworkBroadCastReceiver() {
        networkChangeReceiver = new NetworkChangeReceiver(isConnected -> {
            GlobalUtils.showLog(TAG, "connection state: " + isConnected);
            switch (isConnected) {
                case 0:
                    connectionFlag = true;

                    tvConnectionStatus.setText(R.string.not_connected);
                    tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.red));
                    tvConnectionStatus.setVisibility(View.VISIBLE);
                    break;

                case 1:


                case 2:
                    if (connectionFlag) {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            if (tvConnectionStatus != null) {
                                tvConnectionStatus.setText("Connected");
                                tvConnectionStatus.setVisibility(View.GONE);
                            }
                        }, 3000);

                        tvConnectionStatus.setText(R.string.connecting);
                        tvConnectionStatus.setBackgroundColor(getResources()
                                .getColor(R.color.green));
                        tvConnectionStatus.setVisibility(View.VISIBLE);
                    }
                    break;

                default:
                    break;
            }
        });
    }

    private void unregisterReceiver() {
        if (networkChangeReceiver != null)
            Objects.requireNonNull(getActivity()).unregisterReceiver(networkChangeReceiver);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        Objects.requireNonNull(getActivity()).registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    public void mqttConnected() {
        GlobalUtils.showLog(TAG, "mqtt is now connected");
        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.SUCCESS, "Connected", Banner.TOP, 2000).show();
        if (tvConnectionStatus != null) {
       /*     tvConnectionStatus.setText(R.string.connected);
            tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.green));
            tvConnectionStatus.setVisibility(View.VISIBLE);*/



  /*          final Handler handler = new Handler();
            handler.postDelayed(() -> {

                //Do something after 2 secs
                if (tvConnectionStatus != null) {
                    tvConnectionStatus.setText("Connected");
                    tvConnectionStatus.setVisibility(View.GONE);
                }
            }, 2000);*/
        }
    }

    @Override
    public void mqttNotConnected() {
        GlobalUtils.showLog(TAG, "mqtt not connected");
        String env = Hawk.get(Constants.BASE_URL);
        boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
        GlobalUtils.showLog(TAG, "prod env check: " + prodEnv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TreeleafMqttClient.start(
                    Objects.requireNonNull(getActivity()).getApplicationContext(), prodEnv, new TreeleafMqttCallback() {
                        @Override
                        public void messageArrived(String topic, MqttMessage message) {
                            GlobalUtils.showLog(TAG, "mqtt topic: " + topic);
                            GlobalUtils.showLog(TAG, "mqtt message: " + message);
                        }
                    });
        }

    /*    tvConnectionStatus.setText(R.string.reconnecting);
        tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.green));
        tvConnectionStatus.setVisibility(View.VISIBLE);*/

        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.INFO, "Reconnecting...", Banner.TOP, 3000).show();
    }

    /**
     * manually opening / closing bottom sheet on button click
     */
    public void toggleBottomSheet() {
        if (profileSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            profileSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            profileSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

/*    private void showBotReplying() {
        llBotReplying.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(() -> llBotReplying.setVisibility(View.GONE), 10000);
    }*/

    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initTextModifier() {
        etMessage.setTextSize(15);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        ARE_ToolItem_UpdaterDefault boldUpdater = new
                ARE_ToolItem_UpdaterDefault(bold, 0Xffcccccc, 0X00000000);
        bold.setToolItemUpdater(boldUpdater);

        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        ARE_ToolItem_UpdaterDefault italicUpdater = new
                ARE_ToolItem_UpdaterDefault(italic, 0Xffcccccc, 0X00000000);
        italic.setToolItemUpdater(italicUpdater);

        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        ARE_ToolItem_UpdaterDefault underlineUpdater = new
                ARE_ToolItem_UpdaterDefault(underline, 0Xffcccccc, 0X00000000);
        underline.setToolItemUpdater(underlineUpdater);

        IARE_ToolItem strikeThrough = new ARE_ToolItem_Strikethrough();
        ARE_ToolItem_UpdaterDefault strikeThroughUpdater = new
                ARE_ToolItem_UpdaterDefault(strikeThrough, 0Xffcccccc, 0X00000000);
        strikeThrough.setToolItemUpdater(strikeThroughUpdater);

        IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
        ARE_ToolItem_UpdaterDefault listUpdater = new
                ARE_ToolItem_UpdaterDefault(listNumber, 0Xffcccccc, 0X00000000);
        listNumber.setToolItemUpdater(listUpdater);

        IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
        ARE_ToolItem_UpdaterDefault bulletUpdater = new
                ARE_ToolItem_UpdaterDefault(listBullet, 0Xffcccccc, 0X00000000);
        listBullet.setToolItemUpdater(bulletUpdater);

        llTextModifier.addToolbarItem(bold);
        llTextModifier.addToolbarItem(italic);
        llTextModifier.addToolbarItem(underline);
        llTextModifier.addToolbarItem(strikeThrough);
/*        llTextModifier.addToolbarItem(listNumber);
        llTextModifier.addToolbarItem(listBullet);*/

        etMessage.setToolbar(llTextModifier);
        bold.getStyle().getImageView().setImageDrawable(getResources().getDrawable(R.drawable.ic_bold_new));
        bold.getStyle().getImageView().setPadding(25, 25, 25, 25);
        italic.getStyle().getImageView().setImageDrawable(getResources().getDrawable(R.drawable.ic_italic));
        italic.getStyle().getImageView().setPadding(25, 25, 25, 25);
        strikeThrough.getStyle().getImageView().setImageDrawable(getResources().getDrawable(R.drawable.ic_crossthroug));
        strikeThrough.getStyle().getImageView().setPadding(25, 25, 25, 25);
        underline.getStyle().getImageView().setImageDrawable(getResources().getDrawable(R.drawable.ic_underline));
        underline.getStyle().getImageView().setPadding(25, 25, 25, 25);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;
//        int width = llTextModifier.getMeasuredWidth();
        GlobalUtils.showLog(TAG, "width check: " + width);
        int unitWidth = width / 6;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(unitWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        bold.getView(getContext()).setLayoutParams(layoutParams);
        italic.getView(getContext()).setLayoutParams(layoutParams);
        underline.getView(getContext()).setLayoutParams(layoutParams);
        strikeThrough.getView(getContext()).setLayoutParams(layoutParams);
        listBullet.getView(getContext()).setLayoutParams(layoutParams);
        listNumber.getView(getContext()).setLayoutParams(layoutParams);

        llEmoji.setPadding(GlobalUtils.convertDpToPixel(getContext(), 10),
                GlobalUtils.convertDpToPixel(getContext(), 10),
                GlobalUtils.convertDpToPixel(getContext(), 10),
                GlobalUtils.convertDpToPixel(getContext(), 10));

        llMentions.setPadding(GlobalUtils.convertDpToPixel(getContext(), 10),
                GlobalUtils.convertDpToPixel(getContext(), 10),
                GlobalUtils.convertDpToPixel(getContext(), 10),
                GlobalUtils.convertDpToPixel(getContext(), 10));
    }

    @Override
    public void onReceiveSuggestionsResult(@NonNull SuggestionsResult result, @NonNull String
            bucket) {

    }

    @Override
    public void displaySuggestions(boolean display) {

    }

    @Override
    public boolean isDisplayingSuggestions() {
        return false;
    }

    @NonNull
    @Override
    public List<String> onQueryReceived(@NonNull QueryToken queryToken) {
        return null;
    }

    @Override
    public void onVideoRoomInitiationSuccessClient(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                                   AnydoneProto.ServiceContext context) {
        videoCallBackListener.onVideoRoomInitiationSuccessClient(broadcastVideoCall, context);
    }

    @Override
    public void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                             boolean videoBroadcastPublish, AnydoneProto.ServiceContext context) {
        videoCallBackListener.onVideoRoomInitiationSuccess(broadcastVideoCall, videoBroadcastPublish, context);
    }

    @Override
    public void onHostHangUp(SignalingProto.VideoRoomHostLeft videoRoomHostLeft) {
        ((TicketDetailsActivity)
                Objects.requireNonNull(getActivity())).onHostHangUp(videoRoomHostLeft);
        String duration = GlobalUtils.getFormattedDuration(videoRoomHostLeft.getDuration());
        String time = GlobalUtils.getTime(videoRoomHostLeft.getStartedAt());
        GlobalUtils.showLog(TAG, "call duration: " + videoRoomHostLeft.getDuration());
        GlobalUtils.showLog(TAG, "call time: " + videoRoomHostLeft.getStartedAt());

        Conversation conversation = new Conversation();
        conversation.setClientId(videoRoomHostLeft.getClientId());
        conversation.setMessageType(RtcProto.RtcMessageType.VIDEO_CALL_RTC_MESSAGE.name());
        conversation.setCallDuration(duration);
        conversation.setCallInitiateTime(time);
        conversation.setSenderId(videoRoomHostLeft.getSenderAccount().getAccountId());
        conversation.setSenderType(RtcProto.MessageActor.ANYDONE_USER_MESSAGE.name());
        conversation.setSentAt(videoRoomHostLeft.getStartedAt());
        conversation.setRefId((videoRoomHostLeft.getRefId()));
        conversation.setSent(true);
        conversation.setSendFail(false);

        GlobalUtils.showLog(TAG, "video call ref id check: " + videoRoomHostLeft.getRefId());
        GlobalUtils.showLog(TAG, "video call ref id check2: " + inboxId);

        ConversationRepo.getInstance().saveConversation(conversation, new Repo.Callback() {
            @Override
            public void success(Object o) {
                adapter.setData(conversation);
                rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition
                        (0), 50);
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save video call message");
            }
        });

    }

    @Override
    public void onLocalVideoRoomJoinSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse) {
        ((InboxDetailActivity) Objects.requireNonNull(getActivity()))
                .onLocalVideoRoomJoinSuccess(videoCallJoinResponse);
    }

    @Override
    public void onRemoteVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse
                                                       videoCallJoinResponse) {
        ((InboxDetailActivity) Objects.requireNonNull(getActivity()))
                .onRemoteVideoRoomJoinedSuccess(videoCallJoinResponse);
    }

    @Override
    public void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft) {
        ((InboxDetailActivity) Objects.requireNonNull(getActivity()))
                .onParticipantLeft(participantLeft);
    }

    @Override
    public void onMqttResponseReceivedChecked(String mqttResponseType, boolean isLocalResponse) {
        videoCallBackListener.onMqttReponseArrived(mqttResponseType, isLocalResponse);
    }

    @Override
    public void onGetLinkDetailSuccess(Conversation conversation, RtcProto.LinkMessage linkMessage) {
        presenter.publishLinkMessage(conversation.getMessage(), conversation.getRefId(),
                userAccountId, conversation.getClientId(), linkMessage);
    }

    @Override
    public void onGetLinkDetailFail(Conversation conversation) {
        GlobalUtils.showLog(TAG, "get link detail failed");
        conversation.setGetLinkFail(true);
        adapter.removeItem(conversation);
        presenter.publishTextOrUrlMessage(conversation.getMessage(), inboxId, true);
    }

    public void setOnVideoCallBackListener(OnVideoCallEventListener listener) {
        this.videoCallBackListener = listener;
    }

    @Override
    public void unSubscribeMqttTopics() {
        try {
            presenter.unSubscribeAVCall(String.valueOf(inboxId), userAccount.getAccountId());
        } catch (MqttException exception) {
            exception.printStackTrace();
        }
    }

}

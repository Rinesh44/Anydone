package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.AttachmentAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.CommentAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Attachment;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConversationRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ImagesFullScreen;
import com.treeleaf.anydone.serviceprovider.utils.NetworkChangeReceiver;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.videocallreceive.OnVideoCallEventListener;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedkeyboardobserver.TedRxKeyboardObserver;
import io.realm.Realm;
import io.realm.RealmList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.treeleaf.januswebrtc.Const.MQTT_CONNECTED;
import static com.treeleaf.januswebrtc.Const.MQTT_DISCONNECTED;

public class TicketConversationFragment extends BaseFragment<TicketConversationPresenterImpl>
        implements TicketConversationContract.TicketConversationView,
        TreeleafMqttClient.OnMQTTConnected, TicketDetailsActivity.OnOutsideClickListener {
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 1212;
    public static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 2323;
    public static final int PICK_FILE_REQUEST_CODE = 3434;
    public static final int ATTACH_FILE_REQUEST_CODE = 3730;

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
    @BindView(R.id.tv_closed)
    TextView tvClosed;
    @BindView(R.id.tv_cancelled)
    TextView tvCancelled;
    @BindView(R.id.pb_load_data)
    ProgressBar pbLoadData;
    @BindView(R.id.ll_bot_replying)
    LinearLayout llBotReplying;
    /*    @BindView(R.id.btn_start_task)
        MaterialButton btnStartTask;*/
    @BindView(R.id.ll_text_modifier)
    ARE_ToolbarDefault llTextModifier;
    @BindView(R.id.view)
    View view;
    /*  @BindView(R.id.tv_ticket_id)
      TextView tvTicketId;*/
/*    @BindView(R.id.tv_ticket_title)
    TextView tvTicketTitle;
    @BindView(R.id.tv_ticket_desc)
    TextView tvTicketDesc;
    @BindView(R.id.ll_label_holder)
    LinearLayout llLabels;
    @BindView(R.id.scv_label)
    HorizontalScrollView hsvTags;*/

    public static CoordinatorLayout clCaptureView;
    private static final String TAG = "ServiceRequestDetailFra";
    private String currentPhotoPath = "";
    private long ticketId;
    private BottomSheetBehavior messageSheetBehavior;
    private BottomSheetBehavior profileSheetBehavior;
    private Conversation longClickedMessage;
    private CommentAdapter adapter;
    private List<Conversation> conversationList = new ArrayList<>();
    private boolean attachmentToggle = false;
    private Bitmap capturedBitmap;
    private Uri uri;
    private String imageOrientation = "portrait";
    private List<String> imagesList = new ArrayList<>();
    private Uri selectedFileUri;
    private NetworkChangeReceiver networkChangeReceiver;
    private boolean connectionFlag = false;
    private boolean fetchRemainingMessages = false;
    private String userAccountId;
    private boolean isScrolling = false;
    private int currentItems, scrollOutItems, totalItems;
    private String ticketType;
    private boolean contributed, subscribed;
    private boolean boldFlag = false, italicFlag = false, underlineFlag, strikeThroughFlag = false,
            bulletsFlag = false, numberFlag = false;
    private boolean keyboardShown = false;
    private OnStatusChangeListener listener;
    private OnVideoCallEventListener videoCallBackListener;
    private Account userAccount;
    private AttachmentAdapter attachmentAdapter;
    private Tickets ticket;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ClickableViewAccessibility", "CheckResult"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TicketDetailsActivity activity = (TicketDetailsActivity) getActivity();
        assert activity != null;
        activity.setOutSideTouchListener(this);

//        Employee userAccount = EmployeeRepo.getInstance().getEmployee();
        userAccount = AccountRepo.getInstance().getAccount();
        userAccountId = userAccount.getAccountId();

        GlobalUtils.showLog(TAG, "account idd check: " + userAccountId);

        UiUtils.hideKeyboardForced(getActivity());
        initTextModifier();
        ivSend.setEnabled(false);

     /*   etMessage.setContentTypeface(getContentFace());
        etMessage.setHeadingTypeface(getContentFace());*/
//        etMessage.render();


   /*     etMessage.setOnTextChangeListener(text -> {
            if (!text.isEmpty()) {
                editorScrollview.post(() -> editorScrollview.fullScroll(View.FOCUS_DOWN));
            } else {
                editorScrollview.post(() -> editorScrollview.fullScroll(View.FOCUS_UP));
            }
            if (text.length() > 0) {
                tvAddCommentHint.setVisibility(View.GONE);
            } else {
                tvAddCommentHint.setVisibility(View.VISIBLE);
            }
        });*/

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
                } else {
                    ivSend.setImageTintList(AppCompatResources.getColorStateList
                            (Objects.requireNonNull(getContext()), R.color.selector_disabled));
                    ivSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new TedRxKeyboardObserver(getActivity())
                .listen()
                .subscribe(isShow -> {
                    keyboardShown = !keyboardShown;
                    if (keyboardShown) {
                        llTextModifier.setVisibility(View.VISIBLE);
                        ((RelativeLayout.LayoutParams) llSearchContainer.getLayoutParams())
                                .removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        rvConversation.postDelayed(() -> rvConversation.scrollToPosition(0), 50);
                        etMessage.postDelayed(() -> etMessage.requestFocus(), 50);
                    } else {
                        llTextModifier.setVisibility(View.GONE);
                        ((RelativeLayout.LayoutParams) llSearchContainer.getLayoutParams())
                                .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    }
                }, Throwable::printStackTrace);

        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        ticketId = i.getLongExtra("selected_ticket_id", -1);
        ticketType = i.getStringExtra("selected_ticket_type");
        contributed = i.getBooleanExtra("contributed", false);
        subscribed = i.getBooleanExtra("subscribed", false);

        ticket = TicketRepo.getInstance().getTicketById(ticketId);
//        setTicketInitialDetail(ticket);
        setChatVisibility(ticket);
//        setAttachmentVisibility(ticket);

        GlobalUtils.showLog(TAG, "attachment display: " + ticket.getAttachmentList());

        if (ticketId != -1) {
            Hawk.put(Constants.CURRENT_SERVICE_ORDER_ID, ticketId);
            conversationList = ConversationRepo.getInstance()
                    .getConversationByOrderId(String.valueOf(ticketId));
            GlobalUtils.showLog(TAG, "ticket id check:" + ticketId);

       /*     Collections.sort(conversationList, (o1, o2) ->
                    Long.compare(o2.getSentAt(), o1.getSentAt()));*/

            GlobalUtils.showLog(TAG, "convo list size: " + conversationList.size());
            if (CollectionUtils.isEmpty(conversationList)) {
                pbLoadData.setVisibility(View.VISIBLE);
                presenter.getMessages(ticketId, 0, System.currentTimeMillis(),
                        100);
            } else {
                fetchRemainingMessages = true;
                Conversation lastMessage = conversationList.get(conversationList.size() - 1);
                presenter.getMessages(ticketId,
                        lastMessage.getSentAt() + 1, System.currentTimeMillis(), 100);
            }

//            setInitialTicketDetail(ticket);
            setUpConversationView();

            try {
                presenter.subscribeSuccessMessage(ticketId, userAccount.getAccountId());
                presenter.subscribeFailMessage();
            } catch (MqttException e) {
                e.printStackTrace();
            }

            presenter.getTicket(ticketId);
        }

        clCaptureView = view.findViewById(R.id.cl_capture_view);
        messageSheetBehavior = BottomSheetBehavior.from(llBottomSheetMessage);
        profileSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        TicketDetailsActivity mActivity = (TicketDetailsActivity) getActivity();
        assert mActivity != null;
//        mActivity.setOutSideTouchListener(this);
        TreeleafMqttClient.setOnMqttConnectedListener(this);

        adapter.setOnSuggestionClickListener((kGraph) -> {
            GlobalUtils.showLog(TAG, "suggestion click listened on fragment");
/*            Conversation selectedSuggestion = new Conversation();
            selectedSuggestion.setClientId(UUID.randomUUID().toString()
                    .replace("-", ""));
            selectedSuggestion.setSenderId(userAccountId);
            selectedSuggestion.setMessage(kGraph.getTitle());
            selectedSuggestion.setMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE.name());
            selectedSuggestion.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
            selectedSuggestion.setSentAt(System.currentTimeMillis());
            selectedSuggestion.setRefId(String.valueOf(ticketId));

            ConversationRepo.getInstance().saveConversation(selectedSuggestion,
                    new Repo.Callback() {
                        @Override
                        public void success(Object o) {
                        *//*    List<Conversation> conversationList = new ArrayList<>();
                            conversationList.add(selectedSuggestion);*//*
//                            adapter.submitList(conversationList);
                            adapter.setData(selectedSuggestion);
                            rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition
                                    (0), 50);
                        }

                        @Override
                        public void fail() {
                            GlobalUtils.showLog(TAG, "failed to save dummy user msg");
                        }
                    });

            GlobalUtils.showLog(TAG, "kgraph next check: " + kGraph.getNext());*/

            presenter.getSuggestions(kGraph.getId(), kGraph.getNext(), kGraph.getPrevId(),
                    kGraph.getPrev(), kGraph.getBackId(), kGraph.getBackKey(), kGraph.getTitle(),
                    ticketId, false);
        });

        adapter.setOnBackClickListener((nextId, nextKey, prevQuestionKey, prevId, backId,
                                        clickedMsg, backKey) -> presenter.getSuggestions(nextId,
                nextKey, prevId, prevQuestionKey,
                backId, backKey, clickedMsg, ticketId, true));

        adapter.setOnAddAttachmentClickListener(() -> {
            GlobalUtils.showLog(TAG, "attachment click listened on fragment");
            accessExternalStoragePermissions();
        });

        adapter.setOnAttachmentRemoveListener(this::showRemoveAttachmentDialog);

        adapter.setOnAttachmentImageClickListener((pos, imagesList) -> {
            GlobalUtils.showLog(TAG, "image click listened on top level");
            GlobalUtils.showLog(TAG, "url list size: " + imagesList.size());
//            Collections.reverse(imagesList);
            if (!CollectionUtils.isEmpty(imagesList)) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", (ArrayList<String>) imagesList);
                bundle.putInt("position", pos);

                FragmentTransaction ft = Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager().beginTransaction();
                ImagesFullScreen newFragment = ImagesFullScreen.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        });
    }

    private void showRemoveAttachmentDialog(Attachment attachment) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Remove attachment?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Cancel",
                (dialog, id) -> dialog.cancel());

        builder1.setNegativeButton(
                "Remove",
                (dialog, id) -> {
                    presenter.removeAttachment(ticketId, attachment);
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

/*    private void setAttachmentVisibility(Tickets tickets) {
//        if (tickets.getAttachmentList().isEmpty()) {
        //set attachment visibility
        if (userAccount.getAccountId().equalsIgnoreCase(tickets.getCreatedById())
                || userAccount.getAccountId().equalsIgnoreCase(tickets
                .getAssignedEmployee().getAccountId())
                || userAccount.getAccountType().equalsIgnoreCase
                (AnydoneProto.AccountType.SERVICE_PROVIDER.name())) {
            rlAttachments.setVisibility(View.VISIBLE);
            rvAttachments.setVisibility(View.VISIBLE);
        } else {
            rlAttachments.setVisibility(View.GONE);
            rvAttachments.setVisibility(View.GONE);
        }
//        } else {
        setupAttachmentRecyclerView(rvAttachments);
//        }
    }*/

    @Override
    public void onPause() {
        super.onPause();
        GlobalUtils.showLog(TAG, "onPause called()");
        TreeleafMqttClient.disconnectMQTT();
    }

  /*  private void setTicketInitialDetail(Tickets ticket) {
        tvTicketTitle.setText(ticket.getTitle());
        if (ticket.getDescription() != null && !ticket.getDescription().isEmpty()) {
            tvTicketDesc.setText(ticket.getDescription());
        } else {
            tvTicketDesc.setVisibility(View.GONE);
        }

        if (!CollectionUtils.isEmpty(ticket.getLabelRealmList())) {
            llLabels.removeAllViews();
            for (Label tag : ticket.getLabelRealmList()
            ) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                @SuppressLint("InflateParams") TextView tvTag = (TextView) inflater
                        .inflate(R.layout.layout_blue_tag_bg, null);
                tvTag.setText(tag.getName());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMarginEnd(20);
                tvTag.setLayoutParams(params);
                llLabels.addView(tvTag);
            }
        }
    }*/

    private void setChatVisibility(Tickets ticket) {
        if (subscribed && !ticket.getAssignedEmployee().getAccountId().equalsIgnoreCase(userAccountId)
                && !contributed) {
            llSearchContainer.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
//            btnStartTask.setVisibility(View.GONE);
        } else {
            llSearchContainer.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }
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
        int unitWidth = width / 4;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(unitWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        bold.getView(getContext()).setLayoutParams(layoutParams);
        italic.getView(getContext()).setLayoutParams(layoutParams);
        underline.getView(getContext()).setLayoutParams(layoutParams);
        strikeThrough.getView(getContext()).setLayoutParams(layoutParams);
        listBullet.getView(getContext()).setLayoutParams(layoutParams);
        listNumber.getView(getContext()).setLayoutParams(layoutParams);
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
        if (!etMessage.getHtml().isEmpty()) {

            Hawk.put(Constants.KGRAPH_TITLE, etMessage.getHtml());
            presenter.checkConnection(TreeleafMqttClient.mqttClient);
        }
    }

 /*   @OnClick(R.id.btn_start_task)
    void startTask() {
        GlobalUtils.showLog(TAG, "start task ticket id: " + ticketId);
        presenter.startTask(ticketId);
    }*/

    public Map<Integer, String> getContentFace() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/poppins.otf");
        typefaceMap.put(Typeface.BOLD, "fonts/bold.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/italic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/bold_italic.ttf");
        return typefaceMap;
    }

    @SuppressLint("CheckResult")
    private void sendMessage(Conversation conversation) {
        GlobalUtils.showLog(TAG, "post conversation id: " + conversation.getClientId());
      /*  List<Conversation> conversationList = new ArrayList<>();
        conversationList.add(conversation);
        adapter.submitList(conversationList);*/
        adapter.setData(conversation);
        presenter.enterMessage(rvConversation, etMessage);
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
                Html.fromHtml(longClickedMessage.getMessage()));
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        toggleMessageBottomSheet();
        Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
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

    private void setUpConversationView() {
        GlobalUtils.showLog(TAG, "setup conversation view called");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        ((SimpleItemAnimator) Objects.requireNonNull(rvConversation.getItemAnimator()))
                .setSupportsChangeAnimations(false);
        rvConversation.setLayoutManager(layoutManager);
//        Collections.reverse(conversationList);
        adapter = new CommentAdapter(conversationList, getContext());
//        adapter.submitList(conversationList);
        adapter.setOnItemLongClickListener(message -> {
            longClickedMessage = message;
            toggleMessageBottomSheet();
        });


        adapter.setOnMessageNotDeliveredListener(this::resendByMessageType);
        adapter.setOnSenderImageClickListener(conversation -> {
            if (!conversation.getSenderType().equalsIgnoreCase(RtcProto
                    .MessageActor.ANYDONE_BOT_MESSAGE.name())) {
                setUpProfileBottomSheet(conversation.getSenderName(),
                        conversation.getSenderImageUrl(),
                        4f);
                toggleBottomSheet();
            }
        });

        adapter.setOnImageClickListener((view, position) -> {
            GlobalUtils.showLog(TAG, "image click check");
            Conversation conversation = conversationList.get(position);
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

//        adapter.setOnAddAttachmentClickListener(this::accessExternalStoragePermissions);

        rvConversation.setAdapter(adapter);
//        rvConversation.scrollToPosition(conversationList.size() - 1);
     /*   rvConversation.postDelayed(() -> {
            if (rvConversation != null)
                rvConversation.scrollToPosition(conversationList.size() - 1);
        }, 100);*/
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
        return R.layout.fragment_ticket_conversation;
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
        if (imageUrl != null && !imageUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this)
                    .load(imageUrl)
                    .apply(options)
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .into(profileImage);
        }
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
    public void getTicketSuccess(Tickets tickets) {
        GlobalUtils.showLog(TAG, "ticket details: " + tickets);

        Hawk.put(Constants.SERVICE_PROVIDER_IMG, tickets.getServiceProvider()
                .getProfilePic());
        Hawk.put(Constants.SERVICE_PROVIDER_NAME, tickets.getServiceProvider()
                .getFullName());

        setInitialTicketDetail(tickets);
        setStatusViews(tickets);

   /*     if (tickets.getTicketStatus().equalsIgnoreCase(TicketProto.TicketState.)) {
            llSearchContainer.setVisibility(View.GONE);
            tvCancelled.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rvConversation.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.tv_cancelled);
        }*/
//        presenter.getServiceProviderInfo(tickets);
    }

    private void setStatusViews(Tickets tickets) {
        if (tickets.getTicketStatus().equalsIgnoreCase(TicketProto.TicketState.TICKET_CLOSED.name())) {
//            llSearchContainer.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            tvClosed.setVisibility(View.VISIBLE);
            tvClosed.setText("Closed");
         /*   LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                    rvConversation.getLayoutParams();
            params.gravity(RelativeLayout.ABOVE, R.id.tv_closed);*/
        }

        if (tickets.getTicketStatus().equalsIgnoreCase(TicketProto.TicketState.TICKET_RESOLVED.name())) {
//            llSearchContainer.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            tvClosed.setText("Resolved");
            tvClosed.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    rvConversation.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.tv_closed);
        }

        GlobalUtils.showLog(TAG, "ticket status checckkk: " + tickets.getTicketStatus());
        if (tickets.getTicketStatus().equalsIgnoreCase(TicketProto.TicketState.TICKET_CREATED.name())
                || tickets.getTicketStatus().equalsIgnoreCase
                (TicketProto.TicketState.TICKET_REOPENED.name())) {
//            llSearchContainer.setVisibility(View.GONE);
//            btnStartTask.setVisibility(View.VISIBLE);
            tvClosed.setVisibility(View.GONE);
            view.setVisibility(View.GONE);

        /*    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    rvConversation.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.btn_start_task);*/
        }

        setChatVisibility(tickets);

        boolean isCustomer = tickets.getCustomer().getCustomerId().equalsIgnoreCase(userAccountId);
        //enable chat if user is contributor or assigned
        if (userAccount.getAccountId().equalsIgnoreCase(tickets.getAssignedEmployee().getAccountId())
                || contributed || isCustomer) {
            llSearchContainer.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        } else {
            llSearchContainer.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }


    private void setInitialTicketDetail(Tickets tickets) {
        Conversation conversation = new Conversation();
        conversation.setClientId(UUID.randomUUID().toString().replace("-", ""));
        conversation.setRefId(String.valueOf(tickets.getTicketId()));
        conversation.setTicketTitle(tickets.getTitle());
        conversation.setTicketDesc(tickets.getDescription());
        conversation.setTagsList(tickets.getLabelRealmList());
        conversation.setMessageType("INITIAL_TICKET_DETAIL");
        GlobalUtils.showLog(TAG, "attachment list size ch: " + tickets.getAttachmentList().size());
        conversation.setAttachmentRealmList(tickets.getAttachmentList());
//        adapter.setInitialData(conversation);
        conversationList.add(conversationList.size(), conversation);
    }

    private RealmList<Conversation> getGroupedConversations(List<ServiceDoer> serviceDoerList) {
        RealmList<Conversation> conversationList = new RealmList<>();
        for (int i = 0; i < serviceDoerList.size(); i++) {
            for (int j = i + 1; j < serviceDoerList.size(); j++) {
                // compare list.get(i) and list.get(j)
                if (serviceDoerList.get(i).getAssignedAt() == serviceDoerList.get(j)
                        .getAssignedAt()) {
                    RealmList<ServiceDoer> serviceDoers = new RealmList<>();
                    Conversation conversation = new Conversation();
                    conversation.setClientId(UUID.randomUUID().toString()
                            .replace("-", ""));
                    conversation.setMessageType("MSG_SERVICE_DOERS_TAG");
                    conversation.setSentAt(serviceDoerList.get(i).getAssignedAt());
                    serviceDoers.add(serviceDoerList.get(j));
                    serviceDoers.add(serviceDoerList.get(i));
                    conversation.setServiceDoerList(serviceDoers);
                    conversation.setRefId(String.valueOf(ticketId));
                    conversationList.add(conversation);
                } else {
                    RealmList<ServiceDoer> serviceDoers = new RealmList<>();
                    Conversation conversation = new Conversation();
                    conversation.setSentAt(serviceDoerList.get(j).getAssignedAt());
                    conversation.setClientId(UUID.randomUUID().toString()
                            .replace("-", ""));
                    conversation.setMessageType("MSG_SERVICE_DOERS_TAG");
                    serviceDoers.add(serviceDoerList.get(j));
                    serviceDoers.add(serviceDoerList.get(i));
                    conversation.setServiceDoerList(serviceDoers);
                    conversation.setRefId(String.valueOf(ticketId));
                    conversationList.add(conversation);
                }
            }
        }
        return conversationList;
    }

    @Override
    public void getTicketFail(String msg) {
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
        presenter.publishImage(imageUrl, ticketId, clientId, imageCaption);
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
        presenter.publishDoc(docUrl, file, ticketId, clientId);
    }

    @Override
    public void getServiceDoerSuccess() {

    }

    @Override
    public void getServiceDoerFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showToast(getActivity(), msg);
    }

    @Override
    public void onSubscribeSuccessMsg(Conversation conversation, boolean botReply) {
        if (botReply) {
            showBotReplying();
        } else {
            Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                    llBotReplying.setVisibility(View.GONE));

        }
        sendMessage(conversation);
    }

    @Override
    public void onSubscribeFailMsg(Conversation conversation) {
        sendMessage(conversation);
    }

    @Override
    public void onConnectionSuccess() {
        if (isLink(Objects.requireNonNull(etMessage.getText()).toString().trim())) {
            presenter.publishTextOrUrlMessage(etMessage.getText().toString(), ticketId);
        } else {
            String resultMsg = etMessage.getHtml();
            GlobalUtils.showLog(TAG, "resultMsg: " + resultMsg);
        /*    if (resultMsg.contains("<p>")) {
                resultMsg = resultMsg.replace("<p>", "");
                resultMsg = resultMsg.replace("<html>", "");
                resultMsg = resultMsg.replace("</html>", "");
                resultMsg = resultMsg.replace("</body>", "");
                resultMsg = resultMsg.replace("<body>", "");
                GlobalUtils.showLog(TAG, "result msg check: " + resultMsg);
                resultMsg = resultMsg.replace("</p>", "");
            }*/
            GlobalUtils.showLog(TAG, "final msg check: " + resultMsg);
            presenter.publishTextOrUrlMessage(resultMsg, ticketId);
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
        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void getMessagesSuccess(List<Conversation> conversationList) {
        GlobalUtils.showLog(TAG, "comments list size: " + conversationList.size());
        pbLoadData.setVisibility(View.GONE);
        //sort list in ascending order by time
        GlobalUtils.showLog(TAG, "get messages success");
        GlobalUtils.showLog(TAG, "new messages count: " + conversationList.size());
        Collections.sort(conversationList, (o1, o2) ->
                Long.compare(o2.getSentAt(), o1.getSentAt()));
        adapter.setData(conversationList);
        if (rvConversation != null) {
       /*     rvConversation.postDelayed(() -> {
                if (rvConversation != null)
                    rvConversation.scrollToPosition(0);
            }, 100);*/
         /*   scrollview.postDelayed(() -> scrollview.fullScroll(View.FOCUS_DOWN),
                    100);*/
        }

        if (fetchRemainingMessages) {
            presenter.sendDeliveredStatusForMessages(conversationList);
        }
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
        });
//        etMessage.setText("");

        GlobalUtils.showLog(TAG, "message type qwer: " + conversation.getMessageType());
        if (conversation.getMessageType().
                equalsIgnoreCase(RtcProto.RtcMessageType.LINK_RTC_MESSAGE.name())) {
            presenter.publishLinkMessage(conversation.getMessage(), Long.parseLong(conversation.getRefId()),
                    userAccountId, conversation.getClientId());
        } else {
            presenter.publishTextMessage(conversation.getMessage(), Long.parseLong(conversation.getRefId()),
                    userAccountId, conversation.getClientId());
        }

    }

    @Override
    public void onDeleteMessageSuccess() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            int index = conversationList.indexOf(longClickedMessage);
            conversationList.remove(index);
            adapter.notifyItemRemoved(index);
            adapter.checkIfLastItemInComment();
            ConversationRepo.getInstance().deleteConversationById(longClickedMessage.getClientId());
            hideProgressBar();

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMqttResponseReceivedChecked(String mqttResponseType, boolean isLocalResponse) {
        videoCallBackListener.onMqttReponseArrived(mqttResponseType, isLocalResponse);
    }

    @Override
    public void onKGraphPreConversationSuccess(Conversation conversation) {
        GlobalUtils.showLog(TAG, "before post check: " + conversation.isSent());
        adapter.setData(conversation);
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition
                    (0), 100);
        });
//        etMessage.setText("");

        GlobalUtils.showLog(TAG, "message type qwer: " + conversation.getMessageType());
        if (conversation.getMessageType().
                equalsIgnoreCase(RtcProto.RtcMessageType.LINK_RTC_MESSAGE.name())) {
            presenter.publishLinkMessage(conversation.getMessage(), Long.parseLong(conversation.getRefId()),
                    userAccountId, conversation.getClientId());
        } else {
            presenter.publishTextMessage(conversation.getMessage(), Long.parseLong(conversation.getRefId()),
                    userAccountId, conversation.getClientId());
        }
    }

    @Override
    public void onVideoRoomInitiationSuccessClient(SignalingProto.BroadcastVideoCall broadcastVideoCall) {
        videoCallBackListener.onVideoRoomInitiationSuccessClient(broadcastVideoCall);
    }

    @Override
    public void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                             boolean videoBroadcastPublish) {
        videoCallBackListener.onVideoRoomInitiationSuccess(broadcastVideoCall, videoBroadcastPublish);
    }

    @Override
    public void onImageDrawDiscardRemote(String accountId, String imageId) {
        videoCallBackListener.onImageDrawDiscardRemote(accountId, imageId);
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
        conversation.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
        conversation.setSentAt(videoRoomHostLeft.getStartedAt());
        conversation.setRefId((videoRoomHostLeft.getRefId()));
        conversation.setSent(true);
        conversation.setSendFail(false);

        GlobalUtils.showLog(TAG, "video call ref id check: " + videoRoomHostLeft.getRefId());
        GlobalUtils.showLog(TAG, "video call ref id check2: " + ticketId);

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
    public void onLocalVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse
                                                      videoCallJoinResponse) {
        ((TicketDetailsActivity) Objects.requireNonNull(getActivity()))
                .onLocalVideoRoomJoinSuccess(videoCallJoinResponse);
    }

    @Override
    public void onRemoteVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse
                                                       videoCallJoinResponse) {
        ((TicketDetailsActivity) Objects.requireNonNull(getActivity()))
                .onRemoteVideoRoomJoinedSuccess(videoCallJoinResponse);
    }

    @Override
    public void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft) {
        ((TicketDetailsActivity) Objects.requireNonNull(getActivity()))
                .onParticipantLeft(participantLeft);
    }

    @Override
    public void onKgraphReply(Conversation conversation) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                llBotReplying.setVisibility(View.GONE));

        adapter.setData(conversation);
        rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition(
                0), 50);

/*        adapter.setOnSuggestionClickListener((kGraph) -> {
            Conversation selectedSuggestion = new Conversation();
            selectedSuggestion.setClientId(UUID.randomUUID().toString()
                    .replace("-", ""));
            selectedSuggestion.setSenderId(userAccountId);
            selectedSuggestion.setSenderImageUrl(userAccount.getProfilePic());
            selectedSuggestion.setMessage(kGraph.getTitle());
            selectedSuggestion.setMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE.name());
            selectedSuggestion.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
            selectedSuggestion.setSentAt(System.currentTimeMillis());
            selectedSuggestion.setRefId(String.valueOf(ticketId));

            ConversationRepo.getInstance().saveConversation(selectedSuggestion,
                    new Repo.Callback() {
                        @Override
                        public void success(Object o) {
                            adapter.setData(selectedSuggestion);
                            rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition
                                    (0), 50);
                        }

                        @Override
                        public void fail() {
                            GlobalUtils.showLog(TAG, "failed to save dummy user msg");
                        }
                    });

            GlobalUtils.showLog(TAG, "kgraph next check: " + kGraph.getNext());

            presenter.getSuggestions(kGraph.getId(), kGraph.getNext(), kGraph.getPrevId(),
                    kGraph.getPrev(), kGraph.getBackId(), kGraph.getBackKey(), kGraph.getTitle(),
                    ticketId, false);
        });*/

     /*   adapter.setOnBackClickListener((nextId, nextKey, prevQuestionKey, prevId, backId,
                                        clickedMsg, backKey) -> presenter.getSuggestions(nextId,
                nextKey, prevId, prevQuestionKey,
                backId, backKey, clickedMsg, ticketId, true));*/

    }

    @Override
    public void getSuggestionFail(String msg) {
        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void setAcceptedTag(ServiceProvider serviceProvider, long acceptedAt) {
        GlobalUtils.showLog(TAG, "accepted at check: " + acceptedAt);
        if (acceptedAt != 0) {
            Conversation conversation = new Conversation();
            conversation.setSentAt(acceptedAt);
            conversation.setAcceptedBy(serviceProvider.getFullName());
            conversation.setClientId(UUID.randomUUID().toString().replace("-", ""));
            conversation.setMessageType("MSG_ACCEPTED_TAG");
            conversation.setRefId(String.valueOf(ticketId));
            conversation.setSenderId(UUID.randomUUID().toString().replace("-", ""));
            adapter.setAcceptedTAG(conversation);
        }
    }

    @Override
    public void onTaskStartSuccess(long estTime) {
        listener.onTaskStarted();
//        btnStartTask.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        llSearchContainer.setVisibility(View.VISIBLE);
        TicketRepo.getInstance().changeTicketStatusToStart(ticketId);
      /*  if (onTicketStartListener != null)
            onTicketStartListener.onTicketStarted();*/
        Hawk.put(Constants.TICKET_STARTED, true);
        Hawk.put(Constants.TICKET_PENDING, true);
        Hawk.put(Constants.TICKET_IN_PROGRESS, true);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                rvConversation.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.ll_search_container);

        TicketRepo.getInstance().setTicketEstTime(ticketId, estTime);
    }

    @Override
    public void onTaskStartFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void onUploadImageAttachmentSuccess(String url, String title) {
        Attachment attachment = new Attachment();
        attachment.setId(UUID.randomUUID().toString().replace("-", ""));
        attachment.setType(1);
        attachment.setCreatedAt(System.currentTimeMillis());
        attachment.setTitle(title);
        attachment.setUpdatedAt(System.currentTimeMillis());
        attachment.setUrl(url);

        presenter.addAttachment(ticketId, attachment);
    }

    @Override
    public void onUploadImageAttachmentFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadFileAttachmentSuccess(String url, String title) {
        String extension = url.substring(url.lastIndexOf(".")).toLowerCase();
        GlobalUtils.showLog(TAG, "exe check: " + extension);
        Attachment attachment = new Attachment();
        switch (extension) {
            case ".pdf":
                GlobalUtils.showLog(TAG, "caught on pdf type");
                attachment.setType(2);
                break;

            case ".doc":
                attachment.setType(3);
                break;

            case ".xls":
                attachment.setType(4);
                break;

            case ".jpg":

            case ".png":

            case ".webp":
                attachment.setType(1);
                break;
        }
        attachment.setId(UUID.randomUUID().toString().replace("-", ""));
        attachment.setCreatedAt(System.currentTimeMillis());
        attachment.setTitle(title);
        attachment.setUpdatedAt(System.currentTimeMillis());
        attachment.setUrl(url);

        presenter.addAttachment(ticketId, attachment);
    }

    @Override
    public void onUploadFileAttachmentFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addAttachmentSuccess(Attachment attachment) {
/*        if (attachmentList.size() == 1) {
            attachmentList.add(attachment);
            Collections.reverse(attachmentList);
        } else {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {
                Collections.reverse(attachmentList);
                attachmentList.add(attachment);
                Collections.reverse(attachmentList);
            });
        }
        attachmentAdapter.setData(attachmentList);*/

        Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);
        RealmList<Attachment> existingAttachment = tickets.getAttachmentList();
        if (existingAttachment == null) existingAttachment = new RealmList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmList<Attachment> finalExistingAttachment = existingAttachment;
        realm.executeTransaction(realm1 -> finalExistingAttachment.add(attachment));

        TicketRepo.getInstance().addAttachments(ticketId, finalExistingAttachment);
        adapter.addAttachment(ticketId);
    }

    @Override
    public void addAttachmentFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), Constants.SERVER_ERROR);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showToast(getActivity(), msg);
    }

    @Override
    public void removeAttachmentSuccess(Attachment attachment) {
        adapter.removeAttachment(ticketId, attachment);
    }

    @Override
    public void removeAttachmentFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), Constants.SERVER_ERROR);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showToast(getActivity(), msg);
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
            uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),
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

        if (requestCode == ATTACH_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            ContentResolver cr = Objects.requireNonNull(getActivity()).getContentResolver();
            String mime = cr.getType(fileUri);

            GlobalUtils.showLog(TAG, "mime type check: " + mime);
            GlobalUtils.showLog(TAG, "filename check: " + fileUri.getLastPathSegment());
            String filePath = fileUri.getLastPathSegment();
//            File file = new File(Objects.requireNonNull(GlobalUtils.getPath(uri, getContext())));
            assert filePath != null;
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            GlobalUtils.showLog(TAG, "file name check: " + fileName);
            assert mime != null;
            if (mime.equalsIgnoreCase("image/jpeg")
                    || mime.equalsIgnoreCase("image/png")
                    || mime.equalsIgnoreCase("image/webp")) {
                presenter.uploadImageAttachment(fileUri, getActivity(), fileName);
            } else {
                presenter.uploadFileAttachment(fileUri, fileName);
            }
        }

        if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            Objects.requireNonNull(getActivity()).getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Objects.requireNonNull(((TicketDetailsActivity)
                    getActivity()).getSupportActionBar()).hide();

            uri = Uri.parse(currentPhotoPath);
            GlobalUtils.showLog(TAG, "camera image uri: " + uri);
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

                        presenter.createPreConversationForImage(uri.toString(), ticketId,
                                "", convertedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Objects.requireNonNull(getActivity()).getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                Objects.requireNonNull(((TicketDetailsActivity)
                        getActivity()).getSupportActionBar()).hide();

                uri = data.getData();
                GlobalUtils.showLog(TAG, "gallery image uri: " + uri);
                setupSingleImageView(uri);
            }
        }

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            GlobalUtils.showLog(TAG, "uri check: " + selectedFileUri);
            if (selectedFileUri != null) {
                File file = new File(Objects.requireNonNull(
                        GlobalUtils.getPath(selectedFileUri, getContext())));
                presenter.createPreConversationForDoc(ticketId, file);
            } else {
                GlobalUtils.showLog(TAG, "file is null");
            }
        } else if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Objects.requireNonNull(getActivity()).getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Objects.requireNonNull(((TicketDetailsActivity)
                    getActivity()).getSupportActionBar()).show();
        }

    }

    private void setupSingleImageView(Uri uri) {
    /*    capturedBitmap = GlobalUtils.decodeSampledBitmapFromResource(currentPhotoPath,
                150, 150);*/
        try {
            capturedBitmap = MediaStore.Images.Media.getBitmap(
                    Objects.requireNonNull(getContext()).getContentResolver(), uri);
            capturedBitmap = GlobalUtils.fixBitmapRotation(uri, capturedBitmap, getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    }

    @OnClick(R.id.iv_send_desc)
    void sendImage() {
        UiUtils.hideKeyboard(Objects.requireNonNull(getActivity()));
        clCaptureView.setVisibility(View.INVISIBLE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(((TicketDetailsActivity)
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
        presenter.createPreConversationForImage(uri.toString(), ticketId,
                imageCaption, convertedBitmap);
    }

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
                                    Toast.LENGTH_LONG).show();
                            openAppSettings();
                        }

                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            try {
                                openCamera();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                                   PermissionToken permissionToken) {
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
                    public void onPermissionsChecked(MultiplePermissionsReport
                                                             multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getContext(),
                                    "Media/files access permissions are required",
                                    Toast.LENGTH_LONG).show();
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
                                    Toast.LENGTH_LONG).show();
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
            String[] mimeTypes = new String[]{"image/jpeg", "image/png", "image/webp"};  // 3
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent,
                "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);  // 4
    }

    private void toggleMessageBottomSheet() {
        if (messageSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            RelativeLayout copyHolder = llBottomSheetMessage.findViewById(R.id.rl_copy_holder);
            if (!longClickedMessage.getMessageType()
                    .equalsIgnoreCase("TEXT_RTC_MESSAGE")) {
                copyHolder.setVisibility(View.GONE);
            } else {
                copyHolder.setVisibility(View.VISIBLE);
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

    @Override
    public void onDetach() {
        super.onDetach();
        TreeleafMqttClient.mqttClient.unregisterResources();
        unregisterReceiver();
        listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            GlobalUtils.showLog(TAG, "get view is null");
            return;
        }

        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                GlobalUtils.showLog(TAG, "back key press listen");
                if (clCaptureView.getVisibility() == View.VISIBLE) {
                    clCaptureView.setVisibility(View.GONE);
                    Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.
                            LayoutParams.FLAG_FULLSCREEN);
                    Objects.requireNonNull(((TicketDetailsActivity)
                            getActivity()).getSupportActionBar()).show();
                } else {
                    Objects.requireNonNull(getActivity()).finish();
                }
                return true;
            }
            return false;
        });

        boolean reFetchTicket = Hawk.get(Constants.REFETCH_TICKET, false);
        if (reFetchTicket) {
            Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);
            setStatusViews(tickets);
            Hawk.put(Constants.REFETCH_TICKET, false);
        }
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
        if (getActivity() != null) {
            videoCallBackListener.onMqttConnectionStatusChange(MQTT_CONNECTED);
            tvConnectionStatus.setText(R.string.connected);
            tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.green));
            tvConnectionStatus.setVisibility(View.VISIBLE);

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                //Do something after 2 secs
                tvConnectionStatus.setVisibility(View.GONE);
            }, 2000);

        }
    }

    @Override
    public void mqttNotConnected() {
        if (getActivity() != null) {
            videoCallBackListener.onMqttConnectionStatusChange(MQTT_DISCONNECTED);
            GlobalUtils.showLog(TAG, "failed to reconnect to mqtt");
            tvConnectionStatus.setText(R.string.not_connected);
            tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.red));
            tvConnectionStatus.setVisibility(View.VISIBLE);
        }
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

    private void showBotReplying() {
        llBotReplying.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(() -> llBotReplying.setVisibility(View.GONE), 10000);
    }

    @Override
    public void onDrawTouchDown(CaptureDrawParam captureDrawParam, String accountId, String imageId) {
        videoCallBackListener.onDrawTouchDown(captureDrawParam, accountId, imageId);
    }

    @Override
    public void onDrawTouchMove(CaptureDrawParam captureDrawParam, String accountId, String imageId) {
        videoCallBackListener.onDrawTouchMove(captureDrawParam, accountId, imageId);
    }

    @Override
    public void onDrawTouchUp(String accountId, String imageId) {
        videoCallBackListener.onDrawTouchUp(accountId, imageId);
    }

    @Override
    public void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId,
                                          String imageId, CaptureDrawParam captureDrawParam) {
        videoCallBackListener.onDrawReceiveNewTextField(x, y, editTextFieldId, accountId, imageId,
                captureDrawParam);
    }

    @Override
    public void onDrawReceiveNewTextChange(String text, String id, String accountId, String imageId) {
        videoCallBackListener.onDrawReceiveNewTextChange(text, id, accountId, imageId);
    }

    @Override
    public void onDrawReceiveEdiTextRemove(String editTextId, String accountId, String imageId) {
        videoCallBackListener.onDrawReceiveEdiTextRemove(editTextId, accountId, imageId);
    }

    @Override
    public void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId, String imageId) {
        videoCallBackListener.onDrawParamChanged(captureDrawParam, accountId, imageId);
    }

    @Override
    public void onDrawCanvasCleared(String accountId, String imageId) {
        videoCallBackListener.onDrawCanvasCleared(accountId, imageId);
    }

    @Override
    public void onDrawCollabInvite(SignalingProto.DrawCollab drawCollabResponse) {
        videoCallBackListener.onDrawCollabInvite(drawCollabResponse);
    }

    @Override
    public void onDrawMaximize(SignalingProto.DrawMaximize drawMaximize) {
        videoCallBackListener.onDrawMaximize(drawMaximize);
    }

    @Override
    public void onDrawMinimize(SignalingProto.DrawMinize drawMinize) {
        videoCallBackListener.onDrawMinimize(drawMinize);
    }

    @Override
    public void onDrawClose(SignalingProto.DrawClose drawClose) {
        videoCallBackListener.onDrawClose(drawClose);
    }

    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public void setOnTicketStartListener(OnStatusChangeListener listener) {
        this.listener = listener;
    }

    private void accessExternalStoragePermissions() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport
                                                             multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getContext(),
                                    "Media/files access permissions are required",
                                    Toast.LENGTH_LONG).show();
                            openAppSettings();
                        }

                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            openAttachmentOptions();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void openAttachmentOptions() {
        Uri selectedUri = Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS)));
        GlobalUtils.showLog(TAG, "selectedUri: " + selectedUri);
        llAttachOptions.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setDataAndType(selectedUri, "*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, ATTACH_FILE_REQUEST_CODE);
    }

    private void setupAttachmentRecyclerView(RecyclerView rvAttachments) {
/*        if (ticket.getAttachmentList().isEmpty()) {
            Attachment addAttachment = new Attachment();
            addAttachment.setId(UUID.randomUUID().toString().replace("-", ""));
            addAttachment.setType(0);

            attachmentList.add(addAttachment);
        } else {
            attachmentList = ticket.getAttachmentList();

            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {
                Attachment addAttachment = new Attachment();
                addAttachment.setId(UUID.randomUUID().toString().replace("-", ""));
                addAttachment.setType(0);
                attachmentList.add(addAttachment);
            });

        }

//        LinearLayoutManager layoutManager = new LinearLayoutManager(new GridLayoutManager());
      *//*  rvAttachments.setLayoutManager(new GridLayoutManager(getContext(), 3));

        attachmentAdapter = new AttachmentAdapter(attachmentList, getContext());
        rvAttachments.setAdapter(attachmentAdapter);
*//*
        attachmentAdapter.setOnAddAttachmentClickListener(this::accessExternalStoragePermissions);
        attachmentAdapter.setOnAttachmentRemoveListener(attachment -> presenter.removeAttachment(ticketId, attachment));*/
    }

    public void setOnVideoCallBackListener(OnVideoCallEventListener listener) {
        this.videoCallBackListener = listener;
    }

}

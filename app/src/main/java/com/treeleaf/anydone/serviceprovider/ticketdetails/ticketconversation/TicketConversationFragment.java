package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.MessageAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConversationRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.ImagesFullScreen;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivity;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.NetworkChangeReceiver;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TicketConversationFragment extends BaseFragment<TicketConversationPresenterImpl>
        implements TicketConversationContract.TicketConversationView,
        TreeleafMqttClient.OnMQTTConnected {
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 1212;
    public static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 2323;
    public static final int PICK_FILE_REQUEST_CODE = 3434;

    @BindView(R.id.ll_search_container)
    LinearLayout llSearchContainer;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.et_message)
    TextInputEditText etMessage;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
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
    @BindView(R.id.btn_start_task)
    MaterialButton btnStartTask;


    public static CoordinatorLayout clCaptureView;
    private ProgressDialog progress;
    private static final String TAG = "ServiceRequestDetailFra";
    private String currentPhotoPath = "";
    private long ticketId;
    private BottomSheetBehavior messageSheetBehavior;
    private BottomSheetBehavior profileSheetBehavior;
    private Conversation longClickedMessage;
    private MessageAdapter adapter;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Employee userAccount = EmployeeRepo.getInstance().getEmployee();
        userAccountId = userAccount.getAccountId();
        etMessage.requestFocus();
        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        ticketId = i.getLongExtra("selected_ticket_id", -1);
        if (ticketId != -1) {
            Hawk.put(Constants.CURRENT_SERVICE_ORDER_ID, ticketId);
            conversationList = ConversationRepo.getInstance()
                    .getConversationByOrderId(ticketId);
            GlobalUtils.showLog(TAG, "ticket id check:" + ticketId);

    /*        if (CollectionUtils.isEmpty(conversationList)) {
                presenter.getMessages(ticketId, 0, System.currentTimeMillis(),
                        100);
            } else {
                fetchRemainingMessages = true;
                Conversation lastMessage = conversationList.get(conversationList.size() - 1);
                presenter.getMessages(ticketId,
                        lastMessage.getSentAt() + 1, System.currentTimeMillis(), 100);
            }*/

            setUpConversationView();
        /*    presenter.subscribeSuccessMessage(ticketId, userAccount.getAccountId());
            presenter.subscribeFailMessage();*/
            presenter.getTicket(ticketId);
        }

        clCaptureView = view.findViewById(R.id.cl_capture_view);
        messageSheetBehavior = BottomSheetBehavior.from(llBottomSheetMessage);
        profileSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        TicketDetailsActivity mActivity = (TicketDetailsActivity) getActivity();
        assert mActivity != null;
//        mActivity.setOutSideTouchListener(this);
        TreeleafMqttClient.setOnMqttConnectedListener(this);

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                            ivClear.setVisibility(View.GONE));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
            Hawk.put(Constants.KGRAPH_TITLE,
                    UiUtils.getString(etMessage));
            presenter.checkConnection(TreeleafMqttClient.mqttClient);
        }
    }

    @OnClick(R.id.btn_start_task)
    void startTask() {
        presenter.startTask(ticketId);
    }

    @SuppressLint("CheckResult")
    private void sendMessage(Conversation conversation) {
        GlobalUtils.showLog(TAG, "post conversation id: " + conversation.getClientId());
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
        ClipData clip = ClipData.newPlainText("copied_text", longClickedMessage.getMessage());
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvConversation.setLayoutManager(layoutManager);
        Collections.reverse(conversationList);
        adapter = new MessageAdapter(conversationList, getActivity());
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
                        if (imageUrl.equalsIgnoreCase(conversation.getMessage())) {
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
        return R.layout.fragment_ticket_conversation;
    }


    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }


    @Override
    public void showProgressBar(String message) {
        progress = ProgressDialog.show(getActivity(), null, message, true);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.dismiss();
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
        if (!imageUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this)
                    .load(imageUrl)
                    .apply(options)
                    .into(profileImage);
        }

    }

    @Override
    public void onFailure(String message) {
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), message);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showToast(getActivity(), message);
    }


    @Override
    public void getTicketSuccess(Tickets tickets) {
        GlobalUtils.showLog(TAG, "ticket details: " + tickets);

        Hawk.put(Constants.SERVICE_PROVIDER_IMG, tickets.getServiceProvider()
                .getProfilePic());
        Hawk.put(Constants.SERVICE_PROVIDER_NAME, tickets.getServiceProvider()
                .getFullName());

        if (tickets.getTicketStatus().equalsIgnoreCase(TicketProto.TicketState.TICKET_CLOSED.name())) {
            llSearchContainer.setVisibility(View.GONE);
            tvClosed.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    rvConversation.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.tv_closed);
        }

        if (tickets.getTicketStatus().equalsIgnoreCase(TicketProto.TicketState.TICKET_RESOLVED.name())) {
            llSearchContainer.setVisibility(View.GONE);
            tvClosed.setText("Resolved");
            tvClosed.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    rvConversation.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.tv_closed);
        }

        if (tickets.getTicketStatus().equalsIgnoreCase(TicketProto.TicketState.TICKET_CREATED.name())) {
            llSearchContainer.setVisibility(View.GONE);
            btnStartTask.setVisibility(View.VISIBLE);
        }

   /*     if (tickets.getTicketStatus().equalsIgnoreCase(TicketProto.TicketState.)) {
            llSearchContainer.setVisibility(View.GONE);
            tvCancelled.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rvConversation.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.tv_cancelled);
        }*/
//        presenter.getServiceProviderInfo(tickets);
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
                    conversation.setRefId(ticketId);
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
                    conversation.setRefId(ticketId);
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
        presenter.publishTextOrUrlMessage(UiUtils.getString(etMessage), ticketId);
    }

    @Override
    public void onConnectionFail(String msg) {
        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void getMessagesSuccess(List<Conversation> conversationList) {
        //sort list in ascending order by time
        GlobalUtils.showLog(TAG, "get messages success");
        GlobalUtils.showLog(TAG, "new messages count: " + conversationList.size());
        Collections.sort(conversationList, (o1, o2) ->
                Long.compare(o2.getSentAt(), o1.getSentAt()));
        adapter.setData(conversationList);
        if (rvConversation != null)
            rvConversation.postDelayed(() -> rvConversation.scrollToPosition(0), 100);
        if (fetchRemainingMessages) {
            presenter.sendDeliveredStatusForMessages(conversationList);
        }
    }

    @Override
    public void getMessageFail(String message) {
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), message);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showToast(getActivity(), message);
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
        Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition
                        (0), 100));
        etMessage.setText("");
        if (conversation.getMessageType()
                .equalsIgnoreCase(RtcProto.RtcMessageType.LINK_RTC_MESSAGE.name())) {
            presenter.publishLinkMessage(conversation.getMessage(), conversation.getRefId(),
                    userAccountId, conversation.getClientId());
        } else {
            presenter.publishTextMessage(conversation.getMessage(), conversation.getRefId(),
                    userAccountId, conversation.getClientId());
        }
    }

    @Override
    public void onDeleteMessageSuccess() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            int index = conversationList.indexOf(longClickedMessage);
            conversationList.remove(index);
            adapter.notifyItemRemoved(index);
            ConversationRepo.getInstance().deleteConversationById(longClickedMessage.getClientId());
            hideProgressBar();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                             boolean videoBroadcastPublish) {
        ((ServiceRequestDetailActivity) getActivity())
                .onVideoRoomInitiationSuccess(broadcastVideoCall, videoBroadcastPublish);
    }

    @Override
    public void onHostHangUp(SignalingProto.VideoRoomHostLeft videoRoomHostLeft) {
        ((ServiceRequestDetailActivity)
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
        conversation.setSenderId(videoRoomHostLeft.getSenderAccountId());
        conversation.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
        conversation.setSentAt(videoRoomHostLeft.getStartedAt());
        conversation.setRefId(Long.parseLong(videoRoomHostLeft.getRefId()));
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
    public void onVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse) {
        ((ServiceRequestDetailActivity) Objects.requireNonNull(getActivity()))
                .onVideoRoomJoinSuccess(videoCallJoinResponse);
    }

    @Override
    public void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft) {
        ((ServiceRequestDetailActivity) Objects.requireNonNull(getActivity()))
                .onParticipantLeft(participantLeft);
    }

    @Override
    public void onKgraphReply(Conversation conversation) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                llBotReplying.setVisibility(View.GONE));

        adapter.setData(conversation);
        rvConversation.postDelayed(() -> rvConversation.smoothScrollToPosition
                (0), 50);
        adapter.setOnSuggestionClickListener(kGraph -> {
            Conversation selectedSuggestion = new Conversation();
            selectedSuggestion.setClientId(UUID.randomUUID().toString()
                    .replace("-", ""));
            selectedSuggestion.setSenderId(userAccountId);
            selectedSuggestion.setMessage(kGraph.getTitle());
            selectedSuggestion.setMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE.name());
            selectedSuggestion.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
            selectedSuggestion.setSentAt(System.currentTimeMillis());
            selectedSuggestion.setRefId(ticketId);

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
            presenter.getSuggestions(kGraph.getNext(), ticketId, false);
        });


        adapter.setOnBackClickListener(prevQuestionKey -> {
            GlobalUtils.showLog(TAG, "on back clicked");
            presenter.getSuggestions(prevQuestionKey, ticketId, true);
        });

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
            conversation.setClientId(UUID.randomUUID().toString().replace("", ""));
            conversation.setMessageType("MSG_ACCEPTED_TAG");
            conversation.setRefId(ticketId);
            conversation.setSenderId(UUID.randomUUID().toString().replace("-", ""));
            adapter.setAcceptedTAG(conversation);
        }
    }

    @Override
    public void onTaskStartSuccess() {
        btnStartTask.setVisibility(View.GONE);
        llSearchContainer.setVisibility(View.VISIBLE);
        TicketRepo.getInstance().changeTicketStatusToStart(ticketId);
    }

    @Override
    public void onTaskStartFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
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
            uri = FileProvider.getUriForFile(getActivity(),
                    "com.treeleaf.anydone.provider", file);
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

        if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            Objects.requireNonNull(getActivity()).getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Objects.requireNonNull(((ServiceRequestDetailActivity)
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
                Objects.requireNonNull(((ServiceRequestDetailActivity)
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
                presenter.createPreConversationForDoc(ticketId, file);
            } else {
                GlobalUtils.showLog(TAG, "file is null");
            }
        } else if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Objects.requireNonNull(getActivity()).getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Objects.requireNonNull(((ServiceRequestDetailActivity)
                    getActivity()).getSupportActionBar()).show();
        }

    }

    private void setupSingleImageView(Uri uri) {
        try {
            capturedBitmap = MediaStore.Images.Media.getBitmap(
                    Objects.requireNonNull(getContext()).getContentResolver(), uri);
            capturedBitmap = GlobalUtils.fixBitmapRotation(uri, getActivity());

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
        Objects.requireNonNull(((ServiceRequestDetailActivity)
                getActivity()).getSupportActionBar()).show();

        String imageCaption = UiUtils.getString(etImageDesc);
        etImageDesc.setText("");

        Bitmap bitmap;
        Bitmap convertedBitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            convertedBitmap = GlobalUtils.fixBitmapRotation(uri, getActivity());
            convertedBitmap = UiUtils.getResizedBitmap(convertedBitmap, 200);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            convertedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        presenter.createPreConversationForImage(uri.toString(), ticketId,
                imageCaption, convertedBitmap);
    }

    @OnClick(R.id.iv_clear)
    void clearText() {
        Objects.requireNonNull(etMessage.getText()).clear();
        ivClear.setVisibility(View.GONE);
        etMessage.requestFocus();
//        ivSpeech.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_camera)
    void initCamera() {
        try {
            llAttachOptions.setVisibility(View.GONE);
            openCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        llAttachOptions.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @OnClick(R.id.tv_recorder)
    void initRecorder() {
        llAttachOptions.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_gallery)
    void showGallery() {
        llAttachOptions.setVisibility(View.GONE);
        openGallery();
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
                    Objects.requireNonNull(((ServiceRequestDetailActivity)
                            getActivity()).getSupportActionBar()).show();
                } else {
                    Objects.requireNonNull(getActivity()).finish();
                }
                return true;
            }
            return false;
        });
    }

/*    @Override
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
    }*/

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
        tvConnectionStatus.setText(R.string.connected);
        tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.green));
        tvConnectionStatus.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            //Do something after 2 secs
            tvConnectionStatus.setVisibility(View.GONE);
        }, 2000);
    }

    @Override
    public void mqttNotConnected() {
        GlobalUtils.showLog(TAG, "failed to reconnect to mqtt");
        tvConnectionStatus.setText(R.string.not_connected);
        tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.red));
        tvConnectionStatus.setVisibility(View.VISIBLE);
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
}

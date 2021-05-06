package com.treeleaf.anydone.serviceprovider.reply;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.PersonMentionAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.ReplyAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConversationRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ParticipantRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceProviderRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ImagesFullScreen;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.vanniktech.emoji.EmojiPopup;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import butterknife.BindView;
import butterknife.OnClick;
import gun0912.tedkeyboardobserver.TedRxKeyboardObserver;

public class ReplyActivity extends MvpBaseActivity<ReplyPresenterImpl> implements
        ReplyContract.ReplyView, TreeleafMqttClient.OnMQTTConnected {
    private static final String TAG = "ReplyActivity";
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 6343;
    public static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 6476;
    public static final int PICK_FILE_REQUEST_CODE = 8007;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_connection_status)
    TextView tvConnectionStatus;
    @BindView(R.id.ll_search_container)
    LinearLayout llSearchContainer;
    @BindView(R.id.rv_reply_threads)
    RecyclerView rvReplyThreads;
    @BindView(R.id.rich_editor)
    AREditText etMessage;
    @BindView(R.id.ll_text_modifier)
    ARE_ToolbarDefault llTextModifier;
    @BindView(R.id.iv_capture_view)
    ImageView ivCaptureView;
    @BindView(R.id.et_image_desc)
    EditText etImageDesc;
    @BindView(R.id.rv_mentions)
    RecyclerView rvMentions;
    @BindView(R.id.ll_mentions)
    LinearLayout llMentions;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.ll_emoji)
    LinearLayout llEmoji;
    @BindView(R.id.rl_root)
    RelativeLayout clRoot;
    @BindView(R.id.ll_text_modifier_container)
    LinearLayout llTextModifierContainer;
    /*    @BindView(R.id.ll_bottom_options)
        LinearLayout llBottomOptions;*/
    @BindView(R.id.ll_attach_options)
    LinearLayout llAttachOptions;
    private boolean attachmentToggle = false;

    private ProgressDialog progress;
    private String parentId;
    private int count = 0;
    private List<String> imagesList = new ArrayList<>();
    private ReplyAdapter adapter;
    private List<Conversation> conversationList = new ArrayList<>();
    private String currentPhotoPath = "";
    private Uri uri;
    private String userAccountId;
    private Bitmap capturedBitmap;
    private String imageOrientation = "portrait";
    private Uri selectedFileUri;
    public static CoordinatorLayout clCaptureView;
    private PersonMentionAdapter mentionsAdapter;
    private boolean keyboardShown = false;
    private String inboxId;
    private int replyCount;

    @Override
    protected int getLayout() {
        return R.layout.layout_reply;
    }

    @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Employee employeeAccount = EmployeeRepo.getInstance().getEmployee();
        if (employeeAccount != null) {
            userAccountId = employeeAccount.getAccountId();
        } else {
            ServiceProvider serviceProvider = ServiceProviderRepo.getInstance().getServiceProvider();
            userAccountId = serviceProvider.getAccountId();
        }

        Intent i = getIntent();
        parentId = i.getStringExtra("client_id");
        inboxId = i.getStringExtra("inbox_id");

        try {
            presenter.subscribeSuccessMessage(inboxId, userAccountId, parentId);
            presenter.subscribeFailMessage();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        ivBack.setOnClickListener(v -> onBackPressed());
        Conversation conversation = ConversationRepo.getInstance().getConversationByMessageId(parentId);
        conversationList.add(conversation);
        List<Conversation> conversationListReplies = ConversationRepo.getInstance().getConversationByParentId(parentId);
        conversationList.addAll(conversationListReplies);
        setUpMentionsAdapter();
        initTextModifier();
        presenter.getReplyThreads(conversation.getConversationId(),
                conversationListReplies.isEmpty());
        setUpConversationView();

        llMentions.setOnClickListener(v -> {
            if (etMessage.getText() != null)
                etMessage.getText().insert(etMessage.getText().length(), "@");
        });

        rvReplyThreads.setOnTouchListener((v, event) -> {
            llAttachOptions.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
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
                    int index = etMessage.getText().toString().lastIndexOf("@");
                 /*   List<Participant> searchResults = ParticipantRepo.getInstance().searchParticipant(inboxId,
                            s.toString().substring(index + 1));
                    mentionsAdapter.setData(searchResults);
                    rvMentions.setVisibility(View.VISIBLE);*/
//                    GlobalUtils.showLog(TAG, "search results: " + searchResults.size());
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

        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(clRoot).build(etMessage);
        llEmoji.setOnClickListener(v -> {
            if (emojiPopup.isShowing()) {
                etMessage.requestFocus();
                emojiPopup.dismiss();
            } else {
                if (keyboardShown) hideKeyBoard();
                llEmoji.setBackgroundColor(getResources().getColor(R.color.red));
                emojiPopup.show();
            }
        });

        new TedRxKeyboardObserver(this)
                .listen()
                .subscribe(isShow -> {
                    keyboardShown = !keyboardShown;
                    if (keyboardShown) {
                        llTextModifierContainer.setVisibility(View.VISIBLE);
//                        llBottomOptions.setVisibility(View.VISIBLE);
                        ((RelativeLayout.LayoutParams) llSearchContainer.getLayoutParams())
                                .removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                     /*   rvConversation.setPadding(0, 0, 0,
                                GlobalUtils.convertDpToPixel(Objects.requireNonNull(getContext()), 38));*/
                        rvReplyThreads.postDelayed(() -> rvReplyThreads.scrollToPosition(0), 50);
                        etMessage.postDelayed(() -> etMessage.requestFocus(), 50);
                    } else {
                        llTextModifierContainer.setVisibility(View.GONE);
//                        llBottomOptions.setVisibility(View.GONE);
                        ((RelativeLayout.LayoutParams) llSearchContainer.getLayoutParams())
                                .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                      /*  rvConversation.setPadding(0, 0, 0,
                                GlobalUtils.convertDpToPixel(Objects.requireNonNull(getContext()), 25));*/
                    }
                }, Throwable::printStackTrace);

        clCaptureView = findViewById(R.id.cl_capture_view);
        TreeleafMqttClient.setOnMqttConnectedListener(this);
    }

    @OnClick(R.id.iv_send)
    void sendMessageClick() {
        if (!UiUtils.getString(etMessage).isEmpty()) {
            presenter.checkConnection(TreeleafMqttClient.mqttClient);
        }
    }

    @OnClick(R.id.iv_send_desc)
    void sendImage() {
        UiUtils.hideKeyboard(Objects.requireNonNull(this));
        clCaptureView.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        Objects.requireNonNull(getSupportActionBar()).hide();

        String imageCaption = UiUtils.getString(etImageDesc);
        etImageDesc.setText("");

        Bitmap bitmap;
        Bitmap convertedBitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            convertedBitmap = GlobalUtils.fixBitmapRotation(uri, bitmap, this);
            convertedBitmap = UiUtils.getResizedBitmap(convertedBitmap, 200);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            convertedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        presenter.createPreConversationForImage(uri.toString(), inboxId,
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

    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }


    @Override
    public void showProgressBar(String message) {
        pbProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        pbProgress.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void getReplyThreadsSuccess(List<Conversation> conversationList) {
        GlobalUtils.showLog(TAG, "realm reply size: " + conversationList.size());
        this.conversationList = conversationList;
        Conversation conversation = ConversationRepo.getInstance().getConversationByMessageId(parentId);
        conversationList.add(conversation);
        if (!conversationList.isEmpty()) {
            Collections.sort(conversationList, (o1, o2) ->
                    Long.compare(o2.getSentAt(), o1.getSentAt()));
            adapter.setData(conversationList);
        /*    if (rvReplyThreads != null)
                rvReplyThreads.postDelayed(() -> rvReplyThreads.scrollToPosition(0), 100);*/
        }
    }

    @Override
    public void getReplyThreadsFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }

        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    private void setUpMentionsAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMentions.setLayoutManager(layoutManager);

        List<Participant> participantList = ParticipantRepo.getInstance().getParticipantsExcludingSelf(inboxId);
        mentionsAdapter = new PersonMentionAdapter(participantList, this);
        rvMentions.setAdapter(mentionsAdapter);

        mentionsAdapter.setOnItemClickListener(participant -> {
            int cursorPos = etMessage.getSelectionEnd();
            Spannable WordtoSpan = new SpannableString(participant.getEmployee().getAccountId());
            WordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, WordtoSpan.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int index = etMessage.getText().toString().lastIndexOf("@");
            int spaceIndex = etMessage.getText().toString().lastIndexOf(" ");
            GlobalUtils.showLog(TAG, "space index check: " + spaceIndex);
            if (etMessage.getText() != null) {
                if (spaceIndex != -1)
                    etMessage.getText().replace(index + 1, etMessage.getSelectionEnd(), WordtoSpan);
                else
                    etMessage.getText().replace(index + 1, etMessage.getSelectionEnd(), WordtoSpan);
            } else {
                etMessage.setText(WordtoSpan);
            }
            rvMentions.setVisibility(View.GONE);
        });
    }

    @Override
    public void onSendDeliveredMsgFail(List<Conversation> conversationList) {

    }

    @Override
    public void onImagePreConversationSuccess(Conversation conversation) {
        adapter.setData(conversation);
        rvReplyThreads.postDelayed(() -> rvReplyThreads.smoothScrollToPosition
                (0), 50);
        presenter.uploadImage(uri, conversation, this);
    }

    @Override
    public void onDocPreConversationSuccess(Conversation conversation) {
        adapter.setData(conversation);
        runOnUiThread(() ->
                rvReplyThreads.postDelayed(() -> rvReplyThreads.smoothScrollToPosition
                        (0), 50));
        presenter.uploadDoc(selectedFileUri, conversation);
    }

    @Override
    public void onTextPreConversationSuccess(Conversation conversation) {
        adapter.setData(conversation);
        runOnUiThread(() -> {
            rvReplyThreads.postDelayed(() -> rvReplyThreads.smoothScrollToPosition
                    (0), 100);
            etMessage.setText("");
        });

        if (conversation.getMessageType()
                .equalsIgnoreCase(RtcProto.RtcMessageType.LINK_RTC_MESSAGE.name())) {
            presenter.publishLinkMessage(conversation.getMessage(), conversation.getRefId(),
                    userAccountId, conversation.getClientId(), parentId);
        } else {
            presenter.publishTextMessage(conversation.getMessage(), conversation.getRefId(),
                    userAccountId, conversation.getClientId(), parentId);
        }
    }

    @Override
    public void onConnectionSuccess() {
        if (isLink(Objects.requireNonNull(etMessage.getText()).toString().trim())) {
            presenter.publishTextOrUrlMessage(etMessage.getText().toString(), inboxId);
        } else {
            String resultMsg = etMessage.getHtml();
            GlobalUtils.showLog(TAG, "resultMsg: " + resultMsg);
            presenter.publishTextOrUrlMessage(resultMsg, inboxId);
        }
    }


    private void openCamera() throws IOException {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getImageFile(); // 1
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 2
            uri = FileProvider.getUriForFile(this,
                    "com.treeleaf.anydone.serviceprovider.provider", file);
        } else {
            uri = Uri.fromFile(file); // 3
        }
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    @Override
    public void onConnectionFail(String msg) {
        Banner.make(getWindow().getDecorView().getRootView(), this,
                Banner.INFO, msg, Banner.TOP, 3000).show();

        GlobalUtils.showLog(TAG, "mqtt not connected");
        String env = Hawk.get(Constants.BASE_URL);
        boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
        GlobalUtils.showLog(TAG, "prod env check: " + prodEnv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TreeleafMqttClient.start(getApplicationContext(), prodEnv,
                    new TreeleafMqttCallback() {
                        @Override
                        public void messageArrived(String topic, MqttMessage message) {
                            GlobalUtils.showLog(TAG, "mqtt topic: " + topic);
                            GlobalUtils.showLog(TAG, "mqtt message: " + message);
                        }
                    });
        }
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
    public void onSubscribeSuccessMsg(Conversation conversation, boolean botReply, int count) {
        sendMessage(conversation);
        replyCount = count;
    }

    @Override
    public void onBackPressed() {
        hideKeyBoard();
        GlobalUtils.showLog(TAG, "reply count: " + replyCount);
        if (replyCount != -1) {
            Intent i = new Intent();
            i.putExtra("count", replyCount);
            setResult(771, i);
        }
        finish();
    }


    @SuppressLint("CheckResult")
    private void sendMessage(Conversation conversation) {
        GlobalUtils.showLog(TAG, "post conversation id: " + conversation.getClientId());
        adapter.setData(conversation);
        presenter.enterMessage(rvReplyThreads, etMessage);
    }

    @Override
    public void onSubscribeFailMsg(Conversation conversation) {
        sendMessage(conversation);
    }

    @Override
    public void onDocUploadFail(String msg, Conversation conversation) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        adapter.setData(conversation);
    }

    @Override
    public void onDocUploadSuccess(String docUrl, File file, String clientId) {
        presenter.publishDoc(docUrl, file, inboxId, clientId, parentId);
    }

    @Override
    public void onUploadImageSuccess(String imageUrl, Uri imageUri, String clientId, String imageCaption) {
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
        presenter.publishImage(imageUrl, inboxId, clientId, imageCaption, parentId);
    }

    @Override
    public void onUploadImageFail(String msg, Conversation conversation) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        adapter.setData(conversation);
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

    private boolean isLink(String message) {
        String[] links = extractLinks(message);
        if (links.length != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            Objects.requireNonNull(getSupportActionBar()).hide();

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
                                .getBitmap(getContentResolver(), uri);
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
                getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                Objects.requireNonNull(getSupportActionBar()).hide();

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
            getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
    }

    private void setUpConversationView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        ((SimpleItemAnimator) Objects.requireNonNull(rvReplyThreads.getItemAnimator()))
                .setSupportsChangeAnimations(false);
        rvReplyThreads.setLayoutManager(layoutManager);
        Collections.reverse(conversationList);
        adapter = new ReplyAdapter(conversationList, this, parentId);
     /*   adapter.setOnItemLongClickListener(message -> {
            longClickedMessage = message;
            toggleMessageBottomSheet();
        });*/

        adapter.setOnMessageNotDeliveredListener(this::resendByMessageType);
   /*     adapter.setOnSenderImageClickListener(conversation -> {
            if (!conversation.getSenderType().equalsIgnoreCase(RtcProto
                    .MessageActor.ANYDONE_BOT_MESSAGE.name())) {
                setUpProfileBottomSheet(conversation.getSenderName(),
                        conversation.getSenderImageUrl(),
                        4f);
                toggleBottomSheet();
            }
        });*/

        adapter.setOnImageClickListener((view, position) -> {
            GlobalUtils.showLog(TAG, "image click check");
            Conversation conversation = this.conversationList.get(position);
            GlobalUtils.showLog(TAG, "convo msg type check: " + conversation.getMessageType());
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

                            FragmentTransaction ft = Objects.requireNonNull(this)
                                    .getSupportFragmentManager().beginTransaction();
                            ImagesFullScreen newFragment = ImagesFullScreen.newInstance();
                            newFragment.setArguments(bundle);
                            newFragment.show(ft, "slideshow");
                        }
                    }
                }
            }
        });

        rvReplyThreads.setAdapter(adapter);
//        rvReplyThreads.scrollToPosition(0);
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
        int unitWidth = width / 6;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(unitWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        bold.getView(getContext()).setLayoutParams(layoutParams);
        italic.getView(getContext()).setLayoutParams(layoutParams);
        underline.getView(getContext()).setLayoutParams(layoutParams);
        strikeThrough.getView(getContext()).setLayoutParams(layoutParams);
        listBullet.getView(getContext()).setLayoutParams(layoutParams);
        listNumber.getView(getContext()).setLayoutParams(layoutParams);
    }


    private void resendByMessageType(Conversation message) {
        switch (message.getMessageType()) {
            case "TEXT_RTC_MESSAGE":

            case "LINK_RTC_MESSAGE":
                presenter.resendMessage(parentId, message);
                break;

            case "IMAGE_RTC_MESSAGE":
                Uri uri = Uri.parse(message.getImageUri());
                presenter.uploadImage(uri, message, this);
                break;

            case "DOC_RTC_MESSAGE":
                presenter.uploadDoc(selectedFileUri, message);
                break;

            default:
                break;
        }
    }

    private void setupSingleImageView(Uri uri) {
        try {
            capturedBitmap = MediaStore.Images.Media.getBitmap(
                    Objects.requireNonNull(getContext()).getContentResolver(), uri);
            capturedBitmap = GlobalUtils.fixBitmapRotation(uri, capturedBitmap, this);

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

    @Override
    public void mqttConnected() {
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.SUCCESS, "Connected", Banner.TOP, 2000).show();

 /*       if (tvConnectionStatus != null) {
            tvConnectionStatus.setText(R.string.connected);
            tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.green));
            tvConnectionStatus.setVisibility(View.VISIBLE);

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                //Do something after 2 secs
                tvConnectionStatus.setVisibility(View.GONE);
            }, 2000);
        }*/
    }

    @Override
    public void mqttNotConnected() {
/*        GlobalUtils.showLog(TAG, "failed to reconnect to mqtt");
        tvConnectionStatus.setText(R.string.not_connected);
        tvConnectionStatus.setBackgroundColor(getResources().getColor(R.color.red));
        tvConnectionStatus.setVisibility(View.VISIBLE);*/

        GlobalUtils.showLog(TAG, "mqtt not connected");
        String env = Hawk.get(Constants.BASE_URL);
        boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
        GlobalUtils.showLog(TAG, "prod env check: " + prodEnv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TreeleafMqttClient.start(
                    Objects.requireNonNull(this).getApplicationContext(), prodEnv, new TreeleafMqttCallback() {
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

        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.INFO, "Reconnecting...", Banner.TOP, 3000).show();
    }
}
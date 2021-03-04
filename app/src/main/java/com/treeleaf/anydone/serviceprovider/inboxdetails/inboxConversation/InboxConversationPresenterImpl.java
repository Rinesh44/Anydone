package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxConversation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.webkit.MimeTypeMap;

import androidx.recyclerview.widget.RecyclerView;

import com.chinalwb.are.AREditText;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.model.Receiver;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConversationRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ParticipantRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceProviderRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

import static com.treeleaf.anydone.serviceprovider.utils.Constants.MQTT_LOG;

public class InboxConversationPresenterImpl extends BasePresenter<InboxConversationContract.InboxConversationView>
        implements InboxConversationContract.InboxConversationPresenter {
    private static final String TAG = "InboxConversationPresen";
    public final String PUBLISH_TOPIC = "anydone/rtc/relay";
    private InboxConversationRepository inboxConversationRepository;
    private Employee userAccount = EmployeeRepo.getInstance().getEmployee();
    private String userAccountId;
    private Account account = AccountRepo.getInstance().getAccount();

    @Inject
    public InboxConversationPresenterImpl(InboxConversationRepository
                                                  inboxConversationRepository) {
        this.inboxConversationRepository = inboxConversationRepository;
        if (userAccount == null) {
            ServiceProvider serviceProvider = ServiceProviderRepo.getInstance().getServiceProvider();
            userAccountId = serviceProvider.getAccountId();
        } else {
            userAccountId = userAccount.getAccountId();
        }

    }

    @Override
    public void getInbox(String id) {
        Inbox inbox = InboxRepo.getInstance().getInboxById(id);
        if (inbox != null) {
            getView().getInboxSuccess(inbox);
        } else {
            getView().getInboxFail("Failed to fetch inbox");
        }
    }

    @Override
    public void uploadImage(Uri uri, Conversation conversation, Activity activity) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(uri, "Uri cannot be null");

        Observable<UserRpcProto.UserBaseResponse> imageUploadObservable;

        try {
            Bitmap decodedBitmap = MediaStore.Images.Media.getBitmap(
                    Objects.requireNonNull(getContext()).getContentResolver(), uri);
            Bitmap bitmap = GlobalUtils.fixBitmapRotation(uri, decodedBitmap, activity);
            byte[] byteArray = getByteArrayFromBitmap(bitmap);
            String mimeType = getMimeType(uri);

            RequestBody imageReqBody = RequestBody.create(MediaType.parse(mimeType), byteArray);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image",
                            "image.jpg", imageReqBody);

            Retrofit retrofit = getRetrofitInstance();

            AnyDoneService service = retrofit.create(AnyDoneService.class);

            imageUploadObservable = service.imageUploadConversation(Hawk.get(Constants.TOKEN), body);
            String clientId = conversation.getClientId();

            addSubscription(imageUploadObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                        @Override
                        public void onNext(UserRpcProto.UserBaseResponse uploadPicResponse) {
                            GlobalUtils.showLog(TAG, "upload pic response: "
                                    + uploadPicResponse);

                            getView().hideProgressBar();
                            if (uploadPicResponse == null) {
                                setUpFailedConversation(clientId);
                                return;
                            }

                            if (uploadPicResponse.getError()) {
                                setUpFailedConversation(clientId);
                                return;
                            }

                            getView().onUploadImageSuccess(uploadPicResponse.getRefId(), uri,
                                    conversation.getClientId(), conversation.getImageDesc());
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().hideProgressBar();
                            setUpFailedConversation(clientId);
                        }

                        @Override
                        public void onComplete() {
                            getView().hideProgressBar();
                        }
                    })
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setUpFailedConversation(String clientId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Conversation conversation = ConversationRepo.getInstance()
                    .getConversationByClientId(clientId);
            if (conversation != null) {
                ConversationRepo.getInstance().onConversationSendFailed(conversation,
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "handled failed case");
                                getView().onUploadImageFail("Upload failed", conversation);
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to handle case");
                            }
                        });
            }
        });
    }

    private void setUpFailedDocConversation(String clientId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Conversation conversation = ConversationRepo.getInstance()
                    .getConversationByClientId(clientId);
            if (conversation != null) {
                ConversationRepo.getInstance().onConversationSendFailed(conversation,
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "handled failed case");
                                getView().onDocUploadFail("Upload failed", conversation);
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to handle case");
                            }
                        });
            }
        });
    }


    @Override
    public void uploadDoc(Uri uri, Conversation conversation) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(uri, "Uri cannot be null");

        Observable<UserRpcProto.UserBaseResponse> docUploadObservable;

        File file = new File(Objects.requireNonNull(GlobalUtils.getPath(uri, getContext())));
        String mimeType = getMimeType(uri);

        GlobalUtils.showLog(TAG, "file check: " + file.getName());
        RequestBody docReqBody = RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("doc", file.getName(), docReqBody);

        Retrofit retrofit = getRetrofitInstance();

        AnyDoneService service = retrofit.create(AnyDoneService.class);

        docUploadObservable = service.docUploadConversation(Hawk.get(Constants.TOKEN), body);
        String clientId = conversation.getClientId();

        addSubscription(docUploadObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(@NonNull UserRpcProto.UserBaseResponse uploadDocResponse) {
                        GlobalUtils.showLog(TAG, "upload doc response: " + uploadDocResponse);

                        getView().hideProgressBar();
                        if (uploadDocResponse == null) {
                            setUpFailedDocConversation(clientId);
                            return;
                        }

                        if (uploadDocResponse.getError()) {
                            setUpFailedDocConversation(clientId);
                            return;
                        }

                        getView().onDocUploadSuccess(uploadDocResponse.getRefId(), file, clientId);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        GlobalUtils.showLog(TAG, "doc upload fail 2");
                        GlobalUtils.showLog(TAG, e.getLocalizedMessage());
                        getView().hideProgressBar();
                        setUpFailedDocConversation(clientId);
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );

    }


    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream);
        return stream.toByteArray();
    }

    private Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

        String base_url = Hawk.get(Constants.BASE_URL);
        return new Retrofit.Builder()
                .baseUrl(base_url)
                .client(client)
                .addConverterFactory(ProtoConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private String getMimeType(Uri uri) {
        String mimeType;
        if (Objects.equals(uri.getScheme(), ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }


    @Override
    public void publishTextOrUrlMessage(String message, String inboxId) {
        GlobalUtils.showLog(TAG, "check if mentions: " + message);
        String messageType = getTextOrLink(message);
        if (messageType.equalsIgnoreCase(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE.name())) {
            createPreConversationForText(message, inboxId, false);
        } else {
            createPreConversationForText(message, inboxId, true);
        }
    }

    @Override
    public void publishLinkMessage(String message, String inboxId, String userAccountId,
                                   String clientId) {
        String[] links = extractLinks(message);
        RtcProto.LinkMessage linkMessage = RtcProto.LinkMessage.newBuilder()
                .setUrl((links[0]))
                .setTitle(message)
                .build();

        RtcProto.RtcMessage rtcMessage = RtcProto.RtcMessage.newBuilder()
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setLink(linkMessage)
                .setServiceId(Hawk.get(Constants.SELECTED_SERVICE))
                .setRtcMessageType(RtcProto.RtcMessageType.LINK_RTC_MESSAGE)
                .setRefId(String.valueOf(inboxId))
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RTC_MESSAGE_RELAY)
                .setRtcMessage(rtcMessage)
                .setContext(AnydoneProto.ServiceContext.INBOX_CONTEXT)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish response raw: " + message);
            }
        });
    }


    @Override
    public void publishTextMessage(String message,
                                   String inboxId,
                                   String userAccountId,
                                   String clientId) {

        String plainText;
        if (message.contains("</b>") || message.contains("</i>") || message.contains("</u>") ||
                message.contains("</strike>") || message.contains("style=\"text-decoration:line-through")) {
            plainText = message;
        } else {
            plainText = Jsoup.parse(message).text();
        }

        GlobalUtils.showLog(TAG, "plain text check: " + plainText);

        List<Participant> mentions = getMentionedMembers(message);
        List<RtcProto.RtcMessage.Mention> mentionList = createMentionsProto(mentions);

        GlobalUtils.showLog(TAG, "mentions count: " + mentionList.size());

        RtcProto.TextMessage textMessage = RtcProto.TextMessage.newBuilder()
                .setMessage((message))
                .build();

        RtcProto.RtcMessage rtcMessage;
        if (!mentionList.isEmpty()) {
            rtcMessage = RtcProto.RtcMessage.newBuilder()
                    .setSenderAccountId(userAccountId)
                    .setClientId(clientId)
                    .setText(textMessage)
                    .setHasMentions(true)
                    .addAllMention(mentionList)
//                .setServiceId(Hawk.get(Constants.SELECTED_SERVICE))
                    .setRtcMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE)
                    .setRefId(String.valueOf(inboxId))
                    .build();
        } else {
            rtcMessage = RtcProto.RtcMessage.newBuilder()
                    .setSenderAccountId(userAccountId)
                    .setClientId(clientId)
                    .setText(textMessage)
//                .setServiceId(Hawk.get(Constants.SELECTED_SERVICE))
                    .setRtcMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE)
                    .setRefId(String.valueOf(inboxId))
                    .build();
        }

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RTC_MESSAGE_RELAY)
                .setRtcMessage(rtcMessage)
                .setContext(AnydoneProto.ServiceContext.INBOX_CONTEXT)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(),
                new TreeleafMqttCallback() {
                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        GlobalUtils.showLog(TAG, "publish response raw: " + message);
                    }
                });
    }

    private List<RtcProto.RtcMessage.Mention> createMentionsProto(List<Participant> mentions) {
        List<RtcProto.RtcMessage.Mention> mentionList = new ArrayList<>();
        for (Participant participant : mentions
        ) {
            RtcProto.RtcMessage.Mention mention = RtcProto.RtcMessage.Mention.newBuilder()
                    .setFullName(participant.getEmployee().getName())
                    .setUserId(participant.getEmployee().getAccountId())
                    .setProfilePicture(participant.getEmployee().getEmployeeImageUrl())
                    .build();

            mentionList.add(mention);
        }

        return mentionList;
    }

    private List<Participant> getMentionedMembers(String message) {
        List<Participant> participantList = new ArrayList<>();
        String mentionPattern = "(?<=@)[\\w]+";
        Pattern p = Pattern.compile(mentionPattern);
        Matcher m = p.matcher(message);
//                    String changed = m.replaceAll("");
        while (m.find()) {
            GlobalUtils.showLog(TAG, "found: " + m.group(0));
            String employeeId = m.group(0);
            Participant participant = ParticipantRepo.getInstance()
                    .getParticipantByEmployeeAccountId(employeeId);
            if (participant != null && employeeId != null) {
                participantList.add(participant);
            }
        }

        return participantList;
    }

    @Override
    public void setConversationAsFailed(Conversation conversation) {
        if (conversation != null) {
            ConversationRepo.getInstance().onConversationSendFailed(conversation,
                    new Repo.Callback() {
                        @Override
                        public void success(Object o) {
                            GlobalUtils.showLog(TAG, "handled failed case");
                            getView().onSubscribeFailMsg(conversation);
                        }

                        @Override
                        public void fail() {
                            GlobalUtils.showLog(TAG,
                                    "failed to handle case");
                        }
                    });
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void enterMessage(RecyclerView conversation, AREditText etMessage) {
        //prevent array index out of bounds on text input
        Observable.create((ObservableOnSubscribe<Void>) emitter -> {
            conversation.smoothScrollToPosition(0);
            etMessage.setFocusableInTouchMode(true);
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Void>() {
                    @Override
                    public void onNext(@NonNull Void aVoid) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void publishImage(String imageUrl, String inboxId, String clientId, String imageCaption) {

        RtcProto.Image image = RtcProto.Image.newBuilder()
                .setUrl(imageUrl)
                .build();

        RtcProto.ImageMessage imageMessage = RtcProto.ImageMessage.newBuilder()
                .addImages((image))
                .setTitle(imageCaption)
                .build();

        RtcProto.RtcMessage rtcMessage = RtcProto.RtcMessage.newBuilder()
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setImage(imageMessage)
                .setServiceId(Hawk.get(Constants.SELECTED_SERVICE))
                .setRtcMessageType(RtcProto.RtcMessageType.IMAGE_RTC_MESSAGE)
                .setRefId(String.valueOf(inboxId))
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RTC_MESSAGE_RELAY)
                .setRtcMessage(rtcMessage)
                .setContext(AnydoneProto.ServiceContext.INBOX_CONTEXT)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish response raw: " + message);
            }
        });

    }

    @Override
    public void publishDoc(String docUrl, File file, String inboxId, String clientId) {

        RtcProto.AttachmentMessage attachmentMessage = RtcProto.AttachmentMessage.newBuilder()
                .setUrl(docUrl)
                .setTitle(file.getName())
                .build();

        RtcProto.RtcMessage rtcMessage = RtcProto.RtcMessage.newBuilder()
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setAttachment(attachmentMessage)
                .setServiceId(Hawk.get(Constants.SELECTED_SERVICE))
                .setRtcMessageType(RtcProto.RtcMessageType.DOC_RTC_MESSAGE)
                .setRefId(inboxId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RTC_MESSAGE_RELAY)
                .setRtcMessage(rtcMessage)
                .setContext(AnydoneProto.ServiceContext.INBOX_CONTEXT)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish response raw: " + message);
            }
        });
    }


    private String getTextOrLink(String message) {
        String[] links = extractLinks(message);
        if (links.length != 0) {
            return RtcProto.RtcMessageType.LINK_RTC_MESSAGE.name();
        } else {
            return RtcProto.RtcMessageType.TEXT_RTC_MESSAGE.name();
        }
    }

    @Override
    public void subscribeSuccessMessage(String inboxId, String userAccountId) throws MqttException {
        String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + userAccountId;
        GlobalUtils.showLog(TAG, "subscribe topic: " + SUBSCRIBE_TOPIC);

        TreeleafMqttClient.subscribe(SUBSCRIBE_TOPIC, new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message)
                    throws InvalidProtocolBufferException {
                RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse
                        .parseFrom(message.getPayload());

                GlobalUtils.showLog(TAG, "relay response check: " + relayResponse);

                GlobalUtils.showLog(TAG, "inbox id Check: " + inboxId);
                GlobalUtils.showLog(TAG, "ref id Check: " + relayResponse.getMessageDeliveredResponse().getRtcMessage().getRefId());
                GlobalUtils.showLog(TAG, "parent id check: " + relayResponse.getMessageDeliveredResponse().getRtcMessage().getParentMessageId());

                if (relayResponse.getResponseType().equals
                        (RtcProto.RelayResponse.RelayResponseType.DELIVERED_MSG_RESPONSE)) {
                    if (relayResponse.getMessageDeliveredResponse().getRtcMessage().getRefId()
                            .equalsIgnoreCase(String.valueOf(inboxId))
                            && relayResponse.getMessageDeliveredResponse().getRtcMessage().getParentMessageId().isEmpty()) {
                        GlobalUtils.showLog(TAG, "inside seen");
                        new Handler(Looper.getMainLooper()).post(() -> {
                            String rtcMessageId = relayResponse
                                    .getMessageDeliveredResponse().getRtcMessageId();
                            Conversation conversation = ConversationRepo.getInstance()
                                    .getConversationByMessageId(rtcMessageId);
                            GlobalUtils.showLog(TAG, "get msg by rtc id: " + conversation);
                            ConversationRepo.getInstance().updateSeenStatus(conversation,
                                    new Repo.Callback() {
                                        @Override
                                        public void success(Object o) {
                                            GlobalUtils.showLog(TAG, "seen status updated");
                                            getView().setSeenStatus(conversation);
                                        }

                                        @Override
                                        public void fail() {
                                            GlobalUtils.showLog(TAG,
                                                    "failed to update seen status");
                                        }
                                    });
                        });
                    }
                }

                //check for ref id , can be removed if not usable
                if (relayResponse.getRtcMessage().getRefId().equalsIgnoreCase(String.valueOf(inboxId))) {
                    String clientId = relayResponse.getRtcMessage().getClientId();

                    if (relayResponse.getResponseType().equals(RtcProto.RelayResponse
                            .RelayResponseType.RTC_MESSAGE_DELETE)) {
                        getView().onDeleteMessageSuccess();
                    }

                    GlobalUtils.showLog(TAG, "get message type: " + relayResponse.getResponseType().name());

                    if (relayResponse.getResponseType().equals(RtcProto
                            .RelayResponse.RelayResponseType.RTC_MESSAGE_RESPONSE)) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Conversation conversation = ConversationRepo.getInstance()
                                    .getConversationByClientId(clientId);
                            if (conversation == null) {
                                Conversation newConversation = createNewConversation(relayResponse);
                                ConversationRepo.getInstance().saveConversation(newConversation,
                                        new Repo.Callback() {
                                            @Override
                                            public void success(Object o) {
                                                GlobalUtils.showLog(TAG, "incoming message saved");
                                                if (getView() != null)
                                                    getView().onSubscribeSuccessMsg(newConversation,
                                                            false);
                                                else
                                                    GlobalUtils.showLog(TAG, "view null");
                                            }

                                            @Override
                                            public void fail() {
                                                GlobalUtils.showLog(TAG,
                                                        "failed to save incoming message");
                                            }
                                        });

                                if (newConversation.getParentId() == null || newConversation.getParentId().isEmpty()) {
                                    InboxRepo.getInstance().updateLastMsg(inboxId, newConversation, new Repo.Callback() {
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

                            } else {
                                updateConversation(conversation, relayResponse);

                                if (conversation.getParentId() == null || conversation.getParentId().isEmpty()) {
                                    InboxRepo.getInstance().updateLastMsg(inboxId, conversation, new Repo.Callback() {
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

                        GlobalUtils.showLog(TAG, "account id user account: " + userAccountId);
                        if (!relayResponse.getRtcMessage().getSenderAccountId()
                                .equalsIgnoreCase(userAccountId)) {
                            sendDeliveredMessage(relayResponse.getRtcMessage().getClientId(),
//                                    relayResponse.getRtcMessage().getSenderAccountId(),
                                    userAccountId,
                                    relayResponse.getRtcMessage().getRtcMessageId());
                        }

                    }
                }

            }
        });
    }

    private void sendDeliveredMessage(String clientId, String senderId, String messageId) {
        RtcProto.MessageDeliveredRequest deliveredRequest =
                RtcProto.MessageDeliveredRequest.newBuilder()
                        .setClientId(clientId)
                        .setSenderAccountId(senderId)
                        .setRtcMessageId(messageId)
                        .setTimestamp(System.currentTimeMillis())
                        .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setMessageDeliveredRequest(deliveredRequest)
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DELIVERED_MSG_RELAY)
                .setContext(AnydoneProto.ServiceContext.INBOX_CONTEXT)
                .build();

        GlobalUtils.showLog(TAG, "actual delivered object: " + relayRequest);
        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish response raw: " + message);
            }
        });

        GlobalUtils.showLog(TAG, "sent delivery message");
    }

    private Conversation createNewConversation(RtcProto.RelayResponse relayResponse) {
        RealmList<Receiver> receiverList = new RealmList<>();
        for (RtcProto.MsgReceiver receiverPb : relayResponse.getRtcMessage().getReceiversList()
        ) {
            Receiver receiver = new Receiver();
            receiver.setReceiverId(receiverPb.getReceiverId());
            receiver.setMessageStatus(receiverPb.getRtcMessageStatus().name());
            receiver.setReceiverType(receiverPb.getReceiverActor().name());
            receiver.setSenderId(receiverPb.getAccountId());
            receiverList.add(receiver);
        }

        Conversation conversation = new Conversation();
        conversation.setClientId(relayResponse.getRtcMessage().getClientId());
        conversation.setReplyCount((int) relayResponse.getRtcMessage().getRepliesCount());
        if (relayResponse.getRtcMessage().getSenderActor()
                .equals(RtcProto.MessageActor.ANYDONE_BOT_MESSAGE)) {
            conversation.setSenderId("Anydone bot 101");
        } else {
            conversation.setSenderId(relayResponse.getRtcMessage().getSenderAccountId());
        }
        switch (relayResponse.getRtcMessage().getRtcMessageType().name()) {
            case "TEXT_RTC_MESSAGE":
                String msg = relayResponse.getRtcMessage().getText().getMessage();

                int msgLength = msg.trim().length();
                if ((msg.trim().charAt(msgLength - 1) == 'n') &&
                        msg.trim().charAt(msgLength - 2) == '\"') {
                    conversation.setMessage(msg.replace("\n", ""));
                } else conversation.setMessage(msg.trim());
                break;

            case "LINK_RTC_MESSAGE":
                conversation.setMessage(relayResponse.getRtcMessage().getLink().getTitle());
                break;

            case "IMAGE_RTC_MESSAGE":
                conversation.setMessage(relayResponse.getRtcMessage().getImage()
                        .getImages(0).getUrl());
                conversation.setImageDesc(relayResponse.getRtcMessage().getImage().getTitle());
                break;

            case "DOC_RTC_MESSAGE":
                conversation.setMessage(relayResponse.getRtcMessage().getAttachment().getUrl());
                conversation.setFileName(relayResponse.getRtcMessage().getAttachment().getTitle());
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

        GlobalUtils.showLog(TAG, "create new conversation()");
        conversation.setMessageType(relayResponse.getRtcMessage()
                .getRtcMessageType().name());
        conversation.setSenderType(relayResponse.getRtcMessage().getSenderActor().name());
        conversation.setSenderName(relayResponse.getRtcMessage()
                .getSenderAccountObj().getFullName());
        conversation.setSenderImageUrl(relayResponse.getRtcMessage()
                .getSenderAccountObj().getProfilePic());
        conversation.setRefId((relayResponse.getRtcMessage().getRefId()));
        conversation.setSent(true);
        conversation.setSendFail(false);
        conversation.setConversationId(relayResponse.getRtcMessage().getRtcMessageId());
        conversation.setSentAt(relayResponse.getRtcMessage().getSentAt());
        conversation.setSavedAt(relayResponse.getRtcMessage().getSavedAt());
        conversation.setReceiverList(receiverList);
        conversation.setParentId(relayResponse.getRtcMessage().getParentMessageId());
        return conversation;
    }

    private void updateConversation(Conversation conversation,
                                    RtcProto.RelayResponse relayResponse) {
        RealmList<Receiver> receiverList = new RealmList<>();
        for (RtcProto.MsgReceiver receiverPb : relayResponse.getRtcMessage().getReceiversList()
        ) {
            Receiver receiver = new Receiver();
            receiver.setSenderId(receiverPb.getAccountId());
            receiver.setReceiverType(receiverPb.getReceiverActor().name());
            receiver.setMessageStatus(receiverPb.getRtcMessageStatus().name());
            receiver.setReceiverId(receiverPb.getReceiverId());
            receiverList.add(receiver);
        }

        String message = getMessageFromConversationType(relayResponse);

        new Handler(Looper.getMainLooper()).post(() -> ConversationRepo.getInstance()
                .updateConversation(conversation,
                        relayResponse.getRtcMessage().getRtcMessageId(),
                        relayResponse.getRtcMessage().getSentAt(),
                        relayResponse.getRtcMessage().getSavedAt(),
                        receiverList,
                        message,
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "conversation updated");
                                getView().onSubscribeSuccessMsg(conversation,
                                        relayResponse.getBotReply());
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to update conversation");
                            }
                        }));
    }

    private String getMessageFromConversationType(RtcProto.RelayResponse response) {
        switch (response.getRtcMessage().getRtcMessageType().name()) {
            case "TEXT_RTC_MESSAGE":
                return response.getRtcMessage().getText().getMessage();
            case "LINK_RTC_MESSAGE":
                return response.getRtcMessage().getLink().getTitle();

            case "IMAGE_RTC_MESSAGE":
                return response.getRtcMessage().getImage().getImages(0).getUrl();

         /*   case "DOC_RTC_MESSAGE":
                return response.getRtcMessage().getAttachment().getUrl();

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
                break;*/
        }

        return null;
    }


    @Override
    public void subscribeFailMessage() throws MqttException {
        getView().hideProgressBar();
        String ERROR_TOPIC = "anydone/rtc/relay/response/error/" + userAccountId;

        GlobalUtils.showLog(TAG, "error topic: " + ERROR_TOPIC);

        TreeleafMqttClient.subscribe(ERROR_TOPIC, new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message)
                    throws InvalidProtocolBufferException {
                GlobalUtils.showLog(TAG, "subscribe error response: " + message);

                RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse.parseFrom
                        (message.getPayload());
                GlobalUtils.showLog(TAG, "Msg publish fail: " + relayResponse);
                String clientId = relayResponse.getRelayError().getClientId();

                new Handler(Looper.getMainLooper()).post(() -> {
                    Conversation conversation = ConversationRepo.getInstance()
                            .getConversationByClientId(clientId);
                    setConversationAsFailed(conversation);
                });
            }
        });
    }

    @Override
    public void resendMessage(Conversation conversation) {
        switch (conversation.getMessageType()) {
            case "TEXT_RTC_MESSAGE":

            case "LINK_RTC_MESSAGE":
                publishTextOrUrlMessage(conversation.getMessage(), conversation.getRefId());
                break;

            case "IMAGE_RTC_MESSAGE":
                publishImage(conversation.getMessage(), conversation.getRefId(),
                        conversation.getClientId(), conversation.getImageDesc());
                break;

            case "DOC_RTC_MESSAGE":

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
    public void checkConnection(MqttAndroidClient client) {
        if (!GlobalUtils.isConnected(getContext())) {
            getView().onConnectionFail("No Internet Connection");
        } else if (!client.isConnected()) {
            getView().onConnectionFail("Connection not established");
        } else {
            getView().onConnectionSuccess();
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

    @Override
    public void getMessages(String refId, long from, long to, int pageSize, boolean showProgress) {
        if (showProgress)
            getView().showProgressBar("show progress");
        Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessagesObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        getMessagesObservable = service.getInboxMessages(token,
                refId, from, to, pageSize, AnydoneProto.ServiceContext.INBOX_CONTEXT_VALUE);
        addSubscription(getMessagesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onNext(@NonNull RtcServiceRpcProto.RtcServiceBaseResponse
                                               inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "messages inbox response: " +
                                inboxBaseResponse);

                        if (inboxBaseResponse.getError()) {
                            getView().getMessageFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        GlobalUtils.showLog(TAG, "messages response: " +
                                inboxBaseResponse.getRtcMessagesList());
                        if (!CollectionUtils.isEmpty(inboxBaseResponse.getRtcMessagesList())) {
                            saveConversations(inboxBaseResponse.getRtcMessagesList(), showProgress);
                        } else {
                            getView().getMessageFail("stop progress");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().getMessageFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    @Override
    public void createPreConversationForImage(String imageUri, String inboxId,
                                              String imageTitle, Bitmap bitmap) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        byte[] bitmapBytes = getBitmapBytesFromBitmap(bitmap);
        Conversation conversation = new Conversation();
        conversation.setClientId(clientId);
        conversation.setSenderId(userAccountId);
        conversation.setMessageType(RtcProto.RtcMessageType.IMAGE_RTC_MESSAGE.name());
        conversation.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
        conversation.setRefId(inboxId);
        conversation.setSent(false);
        conversation.setSendFail(false);
        conversation.setSentAt(System.currentTimeMillis());
        conversation.setImageDesc(imageTitle);
        conversation.setImageUri(imageUri);
        conversation.setImageBitmap(bitmapBytes);

        ConversationRepo.getInstance().saveConversation(conversation, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "pre coversation saved");
                getView().onImagePreConversationSuccess(conversation);
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save image pre conversation");
            }
        });
    }

    @Override
    public void createPreConversationForText(String message, String inboxId, boolean link) {
        String plainText;
        if (message.contains("</b>") || message.contains("</i>") || message.contains("</u>") ||
                message.contains("</strike>") || message.contains("style=\"text-decoration:line-through")) {
            plainText = message;
        } else {
            plainText = Jsoup.parse(message).text();
        }

        String clientId = UUID.randomUUID().toString().replace("-", "");
        GlobalUtils.showLog(TAG, "pre conversation text id: " + clientId);
        Conversation conversation = new Conversation();
        conversation.setClientId(clientId);
        conversation.setSenderId(userAccountId);
        conversation.setMessage(plainText);
        conversation.setSenderImageUrl(userAccount.getEmployeeImageUrl());
        conversation.setSenderName(userAccount.getName());
        if (link) conversation.setMessageType(RtcProto.RtcMessageType.LINK_RTC_MESSAGE.name());
        else conversation.setMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE.name());
        conversation.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
        conversation.setRefId(inboxId);
        conversation.setSent(false);
        conversation.setSendFail(false);
        conversation.setSentAt(System.currentTimeMillis());

        ConversationRepo.getInstance().saveConversation(conversation,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        GlobalUtils.showLog(TAG, "text pre coversation saved");
                        getView().onTextPreConversationSuccess(conversation);
                    }

                    @Override
                    public void fail() {
                        GlobalUtils.showLog(TAG,
                                "failed to save text pre conversation");
                    }
                });
    }

    @Override
    public void createPreConversationForDoc(String inboxId, File file) {
        String clientId = UUID.randomUUID().toString().replace("-", "");
        int fileLength = Integer.parseInt(String.valueOf(file.length() / 1024));
        String fileSizeFormatted = getFileSize(fileLength);

        Conversation conversation = new Conversation();
        conversation.setClientId(clientId);
        conversation.setSenderId(userAccountId);
        conversation.setMessageType(RtcProto.RtcMessageType.DOC_RTC_MESSAGE.name());
        conversation.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
        conversation.setRefId(inboxId);
        conversation.setSent(false);
        conversation.setSendFail(false);
        conversation.setFileName(file.getName());
        conversation.setFileSize(fileSizeFormatted);
        conversation.setFilePath(file.getPath());
        conversation.setSentAt(System.currentTimeMillis());

        ConversationRepo.getInstance().saveConversation(conversation, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "pre coversation saved");
                getView().onDocPreConversationSuccess(conversation);
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save image pre conversation");
            }
        });
    }

    @Override
    public void publishMessageDelete(Conversation message) {
        getView().showProgressBar("Please wait...");
        GlobalUtils.showLog(TAG, "message Id check: " + message.getConversationId());
        RtcProto.DeleteMessageReq deleteMessageReq = RtcProto.DeleteMessageReq.newBuilder()
                .setClientId(message.getClientId())
                .setMessageId(message.getConversationId())
                .setRefId(String.valueOf(message.getRefId()))
                .setSenderAccountId(message.getSenderId())
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RTC_MESSAGE_DELETE)
                .setDeleteMessageReq(deleteMessageReq)
                .setContext(AnydoneProto.ServiceContext.INBOX_CONTEXT)
                .build();

        GlobalUtils.showLog(TAG, "actual values: " + relayRequest);
        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(),
                new TreeleafMqttCallback() {
                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        GlobalUtils.showLog(TAG, "publish response raw: " + message);
                    }
                });
    }

    @Override
    public void sendDeliveredStatusForMessages(List<Conversation> conversationList) {
        Conversation lastConvo = conversationList.get(0);
        GlobalUtils.showLog(TAG, "last msg check for delivery: " + lastConvo.getMessage());
   /*     for (Conversation conversation : conversationList
        ) {
            sendDeliveredMessage(conversation.getClientId(), conversation.getSenderId(),
                    conversation.getConversationId());
        }*/

        sendDeliveredMessage(lastConvo.getClientId(), lastConvo.getSenderId(),
                lastConvo.getConversationId());
    }

    private byte[] getBitmapBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        return byteArray;
    }

    private void saveConversations(List<RtcProto.RtcMessage> rtcMessagesList, boolean showProgress) {
        RealmList<Conversation> conversations = ProtoMapper.transformConversation(rtcMessagesList, false);
        ConversationRepo.getInstance().saveConversationList(conversations, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "all conversations saved");
                getView().getMessagesSuccess(conversations, showProgress);
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save conversations");
            }
        });
    }

    @Override
    public void subscribeSuccessMessageAVCall(String ticketId, String userAccountId) throws MqttException {
        String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + userAccountId + "/avcall/" + ticketId;
        GlobalUtils.showLog(TAG, "subscribe topic: " + SUBSCRIBE_TOPIC);

        TreeleafMqttClient.subscribe(SUBSCRIBE_TOPIC, new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message)
                    throws InvalidProtocolBufferException {
                RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse
                        .parseFrom(message.getPayload());

                if (relayResponse.getRefId().equalsIgnoreCase(String.valueOf(ticketId))) {
                    if (true) {
                        //after click on kGraph

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .VIDEO_CALL_BROADCAST_RESPONSE)) {
                            SignalingProto.BroadcastVideoCall broadcastVideoCall =
                                    relayResponse.getBroadcastVideoCall();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + broadcastVideoCall.getSenderAccountId());
                            if (broadcastVideoCall != null) {
                                if (userAccountId.equals(broadcastVideoCall.getSenderAccountId())) {
                                    getView().onVideoRoomInitiationSuccessClient(broadcastVideoCall, relayResponse.getContext());
                                } else {
                                    getView().onVideoRoomInitiationSuccess(broadcastVideoCall, true, relayResponse.getContext());
                                }
                                sendMqttLog("BROADCAST", userAccountId.equals(broadcastVideoCall.getSenderAccountId()));
                            }
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .PARTICIPANT_LEFT_RESPONSE)) {
                            SignalingProto.ParticipantLeft participantLeft =
                                    relayResponse.getParticipantLeftResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + participantLeft.getSenderAccount().getAccountId());
                            if (participantLeft != null) {
                                getView().onParticipantLeft(participantLeft);
//                                if (userAccountId.equals(participantLeft.getSenderAccount().getAccountId()))
                            }
                            sendMqttLog("PARTICIPANT_LEFT", participantLeft.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .VIDEO_CALL_JOIN_RESPONSE)) {
                            SignalingProto.VideoCallJoinResponse videoCallJoinResponse =
                                    relayResponse.getVideoCallJoinResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + videoCallJoinResponse.getSenderAccount().getAccountId());
                            if (videoCallJoinResponse != null) {
                                if (!userAccountId.equals(videoCallJoinResponse.getSenderAccountId())) {
                                    getView().onRemoteVideoRoomJoinedSuccess(videoCallJoinResponse);
                                } else {
                                    getView().onLocalVideoRoomJoinSuccess(videoCallJoinResponse);
                                }
                                sendMqttLog("JOIN", videoCallJoinResponse.getSenderAccount().getAccountId().
                                        equals(userAccountId));
                            }
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .VIDEO_ROOM_HOST_LEFT_RESPONSE)) {
                            SignalingProto.VideoRoomHostLeft videoRoomHostLeft = relayResponse
                                    .getVideoRoomHostLeftResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + videoRoomHostLeft.getSenderAccount().getAccountId());
                            if (videoRoomHostLeft != null && !userAccountId.equals(videoRoomHostLeft.getSenderAccount().getAccountId())) {
                                getView().onHostHangUp(videoRoomHostLeft);
                            }
                            sendMqttLog("HOST_LEFT", videoRoomHostLeft.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                    }
                }
            }

        });
    }

    @Override
    public void subscribeFailMessageAVCall(String refId) throws MqttException {
        String ERROR_TOPIC = "anydone/rtc/relay/response/error/" + account.getAccountId() + "/avcall/" + refId;

        GlobalUtils.showLog(TAG, "error topic: " + ERROR_TOPIC);

        TreeleafMqttClient.subscribe(ERROR_TOPIC, new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message)
                    throws InvalidProtocolBufferException {
                GlobalUtils.showLog(TAG, "subscribe error response: " + message);

                RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse.parseFrom
                        (message.getPayload());
                GlobalUtils.showLog(TAG, "Msg publish fail: " + relayResponse);
                String clientId = relayResponse.getRelayError().getClientId();

                new Handler(Looper.getMainLooper()).post(() -> {
                    Conversation conversation = ConversationRepo.getInstance()
                            .getConversationByClientId(clientId);
                    setConversationAsFailed(conversation);
                });
            }
        });
    }

    @Override
    public void unSubscribeAVCall(String ticketId, String accountId) throws MqttException {//TODO: ask rinesh how to unsubscribe
        Log.d(MQTT_LOG, "unsubscribe av call mqtt");
        String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + accountId + "/avcall/" + ticketId;
        String ERROR_TOPIC = "anydone/rtc/relay/response/error/" + accountId + "/avcall/" + ticketId;//TODO: ask rinesh/kshitij error topic for video call
        TreeleafMqttClient.unsubscribe(SUBSCRIBE_TOPIC);
        TreeleafMqttClient.unsubscribe(ERROR_TOPIC);
    }

    public void sendMqttLog(String eventName, boolean ownResponse) {
        if (true)
            getView().onMqttResponseReceivedChecked(eventName, ownResponse);
    }

}

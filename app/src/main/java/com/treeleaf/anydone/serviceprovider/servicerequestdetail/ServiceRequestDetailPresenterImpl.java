package com.treeleaf.anydone.serviceprovider.servicerequestdetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.webkit.MimeTypeMap;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.entities.BotConversationProto;
import com.treeleaf.anydone.entities.KGraphProto;
import com.treeleaf.anydone.entities.NLUProto;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.KGraph;
import com.treeleaf.anydone.serviceprovider.realm.model.Receiver;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConversationRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceOrderEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceRequestRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.BotConversationRpcProto;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.CANCEL_DRAWING_MESSAGE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.CAPTURE_IMAGE_RECEIVED_RESPONSE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.IMAGE_CAPTURE_MESSAGE_RESPONSE;

public class ServiceRequestDetailPresenterImpl extends
        BasePresenter<ServiceRequestDetailContract.ServiceRequestDetailView>
        implements ServiceRequestDetailContract.ServiceRequestDetailPresenter {
    private static final String TAG = "ServiceRequestDetailPre";
    public final String PUBLISH_TOPIC = "anydone/rtc/relay";
    private ServiceRequestDetailRepository serviceRequestDetailRepository;
    private Account userAccount = AccountRepo.getInstance().getAccount();

    @Inject
    public ServiceRequestDetailPresenterImpl(ServiceRequestDetailRepository
                                                     serviceRequestDetailRepository) {
        this.serviceRequestDetailRepository = serviceRequestDetailRepository;
    }

    @Override
    public void checkAttributes(List<ServiceAttributes> serviceAttributeList) {
        for (ServiceAttributes attributes : serviceAttributeList
        ) {
            switch (attributes.getName()) {
                case "location":
                    getView().setLocationAttribute(attributes.getValue());
                    break;

                case "from":
                    getView().setFromDateAttribute(attributes.getValue());
                    break;

                case "to":
                    getView().setToDateAttribute(attributes.getValue());
                    break;
            }
        }
    }

    @Override
    public void getServiceRequest(long id) {
        ServiceRequest serviceRequest = ServiceRequestRepo.getInstance().getServiceRequestById(id);
        if (serviceRequest != null) {
            getView().getServiceRequestSuccess(serviceRequest);
        } else {
            getView().getServiceRequestFail("Failed to fetch service request");
        }

    }

    @Override
    public void uploadImage(Uri uri, Conversation conversation, Activity activity) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(uri, "Uri cannot be null");

        Observable<UserRpcProto.UserBaseResponse> imageUploadObservable;

        try {
            Bitmap bitmap = GlobalUtils.fixBitmapRotation(uri, activity);
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
                    public void onNext(UserRpcProto.UserBaseResponse uploadDocResponse) {
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
                    public void onError(Throwable e) {
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

        return new Retrofit.Builder()
                .baseUrl(AnyDoneService.API_BASE_URL)
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
    public void getServiceDoers(long requestId) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(requestId, "Request id cannot be null");

        String token = Hawk.get(Constants.TOKEN);
        Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getServiceDoersObservable;
        getServiceDoersObservable = serviceRequestDetailRepository.getServiceDoers(token, requestId);

        addSubscription(getServiceDoersObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver
                        <OrderServiceRpcProto.OrderServiceBaseResponse>() {
                    @Override
                    public void onNext(OrderServiceRpcProto.OrderServiceBaseResponse
                                               getServiceDoersResponse) {
                        GlobalUtils.showLog(TAG, "service doers response: " +
                                getServiceDoersResponse);

                        getView().hideProgressBar();
                        if (getServiceDoersResponse == null) {
                            getView().getServiceDoerFail("Failed to get service doers");
                            return;
                        }

                        if (getServiceDoersResponse.getError()) {
                            getView().getServiceDoerFail(getServiceDoersResponse.getMsg());
                            return;
                        }

                        saveServiceOrderEmployee(getServiceDoersResponse.getServiceOrder());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().getServiceDoerFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );

    }

    @Override
    public void publishTextOrUrlMessage(String message, long orderId) {
        String messageType = getTextOrLink(message);
        if (messageType.equalsIgnoreCase(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE.name())) {
            createPreConversationForText(message, orderId, false);
        } else {
            createPreConversationForText(message, orderId, true);
        }
    }

    @Override
    public void publishLinkMessage(String message, long orderId, String userAccountId,
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
                .setRtcMessageType(RtcProto.RtcMessageType.LINK_RTC_MESSAGE)
                .setRefId(String.valueOf(orderId))
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RTC_MESSAGE_RELAY)
                .setRtcMessage(rtcMessage)
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
                                   long orderId,
                                   String userAccountId,
                                   String clientId) {
        RtcProto.TextMessage textMessage = RtcProto.TextMessage.newBuilder()
                .setMessage((message))
                .build();

        RtcProto.RtcMessage rtcMessage = RtcProto.RtcMessage.newBuilder()
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setText(textMessage)
                .setRtcMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE)
                .setRefId(String.valueOf(orderId))
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RTC_MESSAGE_RELAY)
                .setRtcMessage(rtcMessage)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish response raw: " + message);
            }
        });
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

    @Override
    public void getSuggestions(String nextMessageId, long refId, boolean backClicked) {
        Preconditions.checkNotNull(nextMessageId, "Message id cannot be null");

        String token = Hawk.get(Constants.TOKEN);
        Observable<BotConversationRpcProto.BotConversationBaseResponse> getBotConversationObservable;

        BotConversationProto.ConversationRequest conversationRequest = BotConversationProto
                .ConversationRequest.newBuilder()
                .setMessageId(nextMessageId)
                .build();
        getBotConversationObservable = serviceRequestDetailRepository
                .getSuggestions(token, conversationRequest);

        addSubscription(getBotConversationObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver
                        <BotConversationRpcProto.BotConversationBaseResponse>() {
                    @Override
                    public void onNext(BotConversationRpcProto.BotConversationBaseResponse
                                               botConversationBaseResponse) {
                        GlobalUtils.showLog(TAG, "bot conversation response: " +
                                botConversationBaseResponse);

                        getView().hideProgressBar();
                        if (botConversationBaseResponse == null) {
                            getView().getSuggestionFail("Failed to get suggestions");
                            return;
                        }

                        if (botConversationBaseResponse.getError()) {
                            getView().getSuggestionFail(botConversationBaseResponse.getMsg());
                            return;
                        }

                        RealmList<KGraph> kGraphList = getSuggestionList(botConversationBaseResponse
                                .getKgraphResponse().getAnswersList());
                        Conversation conversation = new Conversation();
                        String kgraphId = UUID.randomUUID().toString().replace("-",
                                "");
                        conversation.setClientId(kgraphId);
                        conversation.setMessageType("MSG_BOT_SUGGESTIONS");
                        conversation.setkGraphList(kGraphList);
                        conversation.setSenderId("Anydone bot 101");
                        conversation.setkGraphBack(true);
                        conversation.setSentAt(System.currentTimeMillis());
                        conversation.setRefId(refId);
                        if (!backClicked)
                            conversation.setkGraphTitle(Hawk.get(Constants.KGRAPH_TITLE));
                        else
                            conversation.setkGraphTitle("");

                        ConversationRepo.getInstance().saveConversation(conversation,
                                new Repo.Callback() {
                                    @Override
                                    public void success(Object o) {
                                        getView().onKgraphReply(conversation);
                                    }

                                    @Override
                                    public void fail() {
                                        GlobalUtils.showLog(TAG, "failed to save k-graph conversation");
                                    }
                                });

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().getSuggestionFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    @Override
    public void getAvailableAttributes(ServiceRequest serviceRequest) {
        HashMap<String, String> availableAttributeMap = new HashMap<>();
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getValue() != null && !attribute.getValue().isEmpty()) {
                availableAttributeMap.put(attribute.getName(), attribute.getValue());
            }
        }
        getView().setAttributesOnConversation(availableAttributeMap, serviceRequest);
    }

    @Override
    public void getServiceProviderInfo(ServiceRequest serviceRequest) {
        getView().setAcceptedTag(serviceRequest.getServiceProvider(),
                serviceRequest.getAcceptedAt());
    }

    @SuppressLint("CheckResult")
    @Override
    public void enterMessage(RecyclerView conversation, TextInputEditText etMessage) {
        //prevent array index out of bounds on text input
        Observable.create((ObservableOnSubscribe<Void>) emitter -> {
            conversation.smoothScrollToPosition(0);
            etMessage.setFocusableInTouchMode(true);
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Void>() {
                    @Override
                    public void onNext(Void aVoid) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private RealmList<KGraph> getSuggestionList(List<KGraphProto.Answer> answersList) {
        RealmList<KGraph> kGraphList = new RealmList<>();
        for (KGraphProto.Answer answer : answersList
        ) {
            KGraph kGraph = new KGraph();
            kGraph.setTitle(answer.getTitle());
            kGraph.setId(answer.getAnswerId());
            kGraph.setAnswerType(answer.getAnswerType().name());
            kGraph.setNext(answer.getOutgoing().getQuestionKey());
            kGraph.setPrev(answer.getIncoming().getQuestionKey());
            kGraphList.add(kGraph);
        }

        return kGraphList;
    }


    public void publishImage(String imageUrl, long orderId, String clientId, String imageCaption) {

        RtcProto.Image image = RtcProto.Image.newBuilder()
                .setUrl(imageUrl)
                .build();

        RtcProto.ImageMessage imageMessage = RtcProto.ImageMessage.newBuilder()
                .addImages((image))
                .setTitle(imageCaption)
                .build();

        RtcProto.RtcMessage rtcMessage = RtcProto.RtcMessage.newBuilder()
                .setSenderAccountId(userAccount.getAccountId())
                .setClientId(clientId)
                .setImage(imageMessage)
                .setRtcMessageType(RtcProto.RtcMessageType.IMAGE_RTC_MESSAGE)
                .setRefId(String.valueOf(orderId))
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RTC_MESSAGE_RELAY)
                .setRtcMessage(rtcMessage)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish response raw: " + message);
            }
        });

    }

    @Override
    public void publishDoc(String docUrl, File file, long orderId, String clientId) {

        RtcProto.AttachmentMessage attachmentMessage = RtcProto.AttachmentMessage.newBuilder()
                .setUrl(docUrl)
                .setTitle(file.getName())
                .build();

        RtcProto.RtcMessage rtcMessage = RtcProto.RtcMessage.newBuilder()
                .setSenderAccountId(userAccount.getAccountId())
                .setClientId(clientId)
                .setAttachment(attachmentMessage)
                .setRtcMessageType(RtcProto.RtcMessageType.DOC_RTC_MESSAGE)
                .setRefId(String.valueOf(orderId))
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RTC_MESSAGE_RELAY)
                .setRtcMessage(rtcMessage)
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
    public void subscribeSuccessMessage(long orderId, String userAccountId) {
        String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + orderId + "/" + userAccountId;
        GlobalUtils.showLog(TAG, "subscribe topic: " + SUBSCRIBE_TOPIC);

        TreeleafMqttClient.subscribe(SUBSCRIBE_TOPIC, new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message)
                    throws InvalidProtocolBufferException {
                RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse
                        .parseFrom(message.getPayload());

                GlobalUtils.showLog(TAG, "relay response check: " + relayResponse);
                GlobalUtils.showLog(TAG, "relay response type check: " + relayResponse.getResponseType().name());

                if (!CollectionUtils.isEmpty(relayResponse.getRtcMessage().getKGraphReply()
                        .getKGraphResultsList())) {
                    RealmList<KGraph> kGraphList = getkGraphList(relayResponse.getRtcMessage()
                            .getKGraphReply().getKGraphResultsList());
                    Conversation conversation = new Conversation();
                    String kgraphId = UUID.randomUUID().toString().replace("-",
                            "");
                    conversation.setClientId(kgraphId);
                    conversation.setMessageType("MSG_BOT_SUGGESTIONS");
                    conversation.setkGraphList(kGraphList);
                    conversation.setSenderId("Anydone bot 101");
                    conversation.setSentAt(System.currentTimeMillis());
                    conversation.setkGraphBack(false);
                    conversation.setkGraphTitle(relayResponse.getRtcMessage()
                            .getText().getMessage());
                    conversation.setRefId(Long.parseLong(relayResponse
                            .getRtcMessage().getRefId()));

                    ConversationRepo.getInstance().saveConversation(conversation,
                            new Repo.Callback() {
                                @Override
                                public void success(Object o) {
                                    getView().onKgraphReply(conversation);
                                }

                                @Override
                                public void fail() {
                                    GlobalUtils.showLog(TAG, "failed to save k-graph message");
                                }
                            });

                    return;
                }

                String clientId = relayResponse.getRtcMessage().getClientId();

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse
                        .RelayResponseType.RTC_MESSAGE_DELETE)) {
                    getView().onDeleteMessageSuccess();
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .VIDEO_CALL_BROADCAST_RESPONSE)) {
                    SignalingProto.BroadcastVideoCall broadcastVideoCall =
                            relayResponse.getBroadcastVideoCall();
                    if (broadcastVideoCall != null) {
                        getView().onVideoRoomInitiationSuccess(broadcastVideoCall, true);
                    }
                }

                if (relayResponse.getResponseType().equals(IMAGE_CAPTURE_MESSAGE_RESPONSE)) {
                    SignalingProto.StartDraw startDraw = relayResponse.getStartDrawResponse();
                    String accountId = startDraw.getSenderAccount().getAccountId();
                    if (startDraw != null) {
                        if (userAccountId.equals(accountId)) {
                            //sent and received id is same
                            getView().onImageCaptured();
                        } else {
                            //sent and received id is different
                            ByteString imageByteString = startDraw.getCapturedImage();
                            int width = startDraw.getBitmapWidth();
                            int height = startDraw.getBitmapHeight();
                            long captureTime = startDraw.getCapturedTime();
                            byte[] convertedBytes = imageByteString.toByteArray();
                            getView().onImageReceivedFromConsumer(width, height, captureTime, convertedBytes);
                        }
                    }
                }

                if (relayResponse.getResponseType().equals(CANCEL_DRAWING_MESSAGE_RESPONSE)) {
                    SignalingProto.CancelDrawing cancelDrawing = relayResponse.getCancelDrawResponse();
                    if (cancelDrawing != null) {
                        if (cancelDrawing.getSenderAccount().getAccountId().
                                equals(userAccountId)) {
                            getView().onImageDrawDiscardLocal();
                        } else
                            getView().onImageDrawDiscardRemote();
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .PARTICIPANT_LEFT_RESPONSE)) {
                    SignalingProto.ParticipantLeft participantLeft =
                            relayResponse.getParticipantLeftResponse();
                    if (participantLeft != null) {
                        getView().onParticipantLeft(participantLeft);
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .VIDEO_CALL_JOIN_RESPONSE)) {
                    SignalingProto.VideoCallJoinResponse videoCallJoinResponse =
                            relayResponse.getVideoCallJoinResponse();
                    if (videoCallJoinResponse != null) {
                        getView().onVideoRoomJoinedSuccess(videoCallJoinResponse);
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .VIDEO_ROOM_HOST_LEFT_RESPONSE)) {
                    SignalingProto.VideoRoomHostLeft videoRoomHostLeft = relayResponse
                            .getVideoRoomHostLeftResponse();
                    if (videoRoomHostLeft != null) {
                        getView().onHostHangUp(videoRoomHostLeft);
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .DRAW_TOUCH_DOWN_RESPONSE)) {
                    SignalingProto.DrawTouchDown drawTouchDown = relayResponse
                            .getDrawTouchDownResponse();
                    if (drawTouchDown != null &&
                            !drawTouchDown.getSenderAccount().getAccountId().equals(userAccountId)) {
                        CaptureDrawParam captureDrawParam = new CaptureDrawParam();
                        captureDrawParam.setXCoordinate(drawTouchDown.getX());
                        captureDrawParam.setYCoordinate(drawTouchDown.getY());
                        getView().onDrawTouchDown(captureDrawParam);
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .DRAW_TOUCH_MOVE_RESPONSE)) {
                    SignalingProto.DrawTouchMove drawTouchMove = relayResponse
                            .getDrawTouchMoveResponse();
                    if (drawTouchMove != null &&
                            !drawTouchMove.getSenderAccount().getAccountId().equals(userAccountId)) {
                        CaptureDrawParam captureDrawParam = new CaptureDrawParam();
                        captureDrawParam.setXCoordinate(drawTouchMove.getX());
                        captureDrawParam.setYCoordinate(drawTouchMove.getY());
                        getView().onDrawTouchMove(captureDrawParam);
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .DRAW_TOUCH_UP_RESPONSE)) {
                    SignalingProto.DrawTouchUp drawTouchUp = relayResponse
                            .getDrawTouchUpResponse();
                    if (drawTouchUp != null &&
                            !drawTouchUp.getSenderAccount().getAccountId().equals(userAccountId)) {
                        getView().onDrawTouchUp();
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .RECEIVE_NEW_TEXT_FIELD_RESPONSE)) {
                    SignalingProto.ReceiveNewTextField receiveNewTextField = relayResponse
                            .getReceiveNewTextFieldResponse();
                    if (receiveNewTextField != null &&
                            !receiveNewTextField.getSenderAccount().getAccountId().equals(userAccountId)) {
                        getView().onDrawReceiveNewTextField(receiveNewTextField.getX(),
                                receiveNewTextField.getY(), receiveNewTextField.getTextId());
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .TEXT_FIELD_CHANGE_RESPONSE)) {
                    SignalingProto.TextFieldChange textFieldChange = relayResponse
                            .getTextFieldChangeResponse();
                    if (textFieldChange != null &&
                            !textFieldChange.getSenderAccount().getAccountId().equals(userAccountId)) {
                        getView().onDrawReceiveNewTextChange(textFieldChange.getText(),
                                textFieldChange.getTextId());
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .TEXT_FIELD_REMOVE_RESPONSE)) {
                    SignalingProto.TextFieldRemove textFieldRemove = relayResponse
                            .getTextFieldRemoveResponse();
                    if (textFieldRemove != null &&
                            !textFieldRemove.getSenderAccount().getAccountId().equals(userAccountId)) {
                        getView().onDrawReceiveEdiTextRemove(textFieldRemove.getTextId());
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .DRAW_META_DATA_CHANGE_RESPONSE)) {
                    SignalingProto.DrawMetaDataChange drawMetaDataChange = relayResponse
                            .getDrawMetaDataChangeResponse();
                    if (drawMetaDataChange != null &&
                            !drawMetaDataChange.getSenderAccount().getAccountId().equals(userAccountId)) {
                        CaptureDrawParam captureDrawParam = new CaptureDrawParam();
                        captureDrawParam.setXCoordinate(drawMetaDataChange.getX());
                        captureDrawParam.setYCoordinate(drawMetaDataChange.getY());
                        captureDrawParam.setBrushWidth(drawMetaDataChange.getBrushWidth());
                        captureDrawParam.setBrushOpacity((int) drawMetaDataChange.getBrushOpacity());
                        captureDrawParam.setBrushColor(drawMetaDataChange.getBrushColor());
                        getView().onDrawParamChanged(captureDrawParam);
                    }
                }

                if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                        .DRAW_CANVAS_CLEAR_RESPONSE)) {
                    SignalingProto.DrawCanvasClear drawCanvasClear = relayResponse
                            .getDrawCanvasClearResponse();
                    if (drawCanvasClear != null &&
                            !drawCanvasClear.getSenderAccount().getAccountId().equals(userAccountId)) {
                        getView().onDrawCanvasCleared();
                    }
                }

                GlobalUtils.showLog(TAG, "deleted message response: " +
                        relayResponse.getDeletedMsgResponse().getClientId());
                if (relayResponse.getResponseType().equals
                        (RtcProto.RelayResponse.RelayResponseType.DELIVERED_MSG_RESPONSE)) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        String deliveryClientId = relayResponse
                                .getMessageDeliveredResponse().getClientId();
                        Conversation conversation = ConversationRepo.getInstance()
                                .getConversationByClientId(deliveryClientId);
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
                                        }

                                        @Override
                                        public void fail() {
                                            GlobalUtils.showLog(TAG,
                                                    "failed to save incoming message");
                                        }
                                    });
                        } else {
                            updateConversation(conversation, relayResponse);
                        }
                    });

                    GlobalUtils.showLog(TAG, "account id user account: " + userAccountId);
                    if (!relayResponse.getRtcMessage().getSenderAccountId()
                            .equalsIgnoreCase(userAccountId)) {
                        sendDeliveredMessage(relayResponse.getRtcMessage().getClientId(),
                                relayResponse.getRtcMessage().getSenderAccountId(),
                                relayResponse.getRtcMessage().getRtcMessageId());
                    }

                }
            }

        });
    }

    private RealmList<KGraph> getkGraphList(List<NLUProto.KGraphResult> kGraphResultsList) {
        RealmList<KGraph> kGraphRealmList = new RealmList<>();
        for (NLUProto.KGraphResult result : kGraphResultsList
        ) {
            KGraph kGraph = new KGraph();
            kGraph.setId(result.getId());
            kGraph.setPrev(result.getPrev());
            kGraph.setNext(result.getNext());
            kGraph.setTitle(result.getTitle());
            kGraph.setTraverse(result.getTraversable());
            kGraphRealmList.add(kGraph);
        }
        return kGraphRealmList;
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
        if (relayResponse.getRtcMessage().getSenderActor()
                .equals(RtcProto.MessageActor.ANYDONE_BOT_MESSAGE)) {
            conversation.setSenderId("Anydone bot 101");
        } else {
            conversation.setSenderId(relayResponse.getRtcMessage().getSenderAccountId());
        }
        switch (relayResponse.getRtcMessage().getRtcMessageType().name()) {
            case "TEXT_RTC_MESSAGE":
                conversation.setMessage(relayResponse.getRtcMessage().getText().getMessage());
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
        conversation.setRefId(Long.parseLong(relayResponse.getRtcMessage().getRefId()));
        conversation.setSent(true);
        conversation.setSendFail(false);
        conversation.setConversationId(relayResponse.getRtcMessage().getRtcMessageId());
        conversation.setSentAt(relayResponse.getRtcMessage().getSentAt());
        conversation.setSavedAt(relayResponse.getRtcMessage().getSavedAt());
        conversation.setReceiverList(receiverList);
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
    public void subscribeFailMessage() {
        getView().hideProgressBar();
        String ERROR_TOPIC = "anydone/rtc/relay/response/error/" + userAccount.getAccountId();

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

    private void saveServiceOrderEmployee(OrderServiceProto.ServiceOrder serviceOrder) {
        ServiceOrderEmployeeRepo.getInstance().saveServiceOrderEmployee(serviceOrder,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getServiceDoerSuccess();
                    }

                    @Override
                    public void fail() {
                        GlobalUtils.showLog(TAG, "Failed to save assigned employees");
                    }
                });
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
    public void getMessages(long refId, long from, long to, int pageSize) {
        Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessagesObservable;
        String token = Hawk.get(Constants.TOKEN);

        getMessagesObservable = serviceRequestDetailRepository.getMessages(token,
                refId, from, to, pageSize);
        addSubscription(getMessagesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onNext(RtcServiceRpcProto.RtcServiceBaseResponse
                                               rtcServiceBaseResponse) {
                        GlobalUtils.showLog(TAG, "messages service response: " +
                                rtcServiceBaseResponse);

                        if (rtcServiceBaseResponse == null) {
                            getView().getMessageFail("Failed to get messages");
                            return;
                        }

                        if (rtcServiceBaseResponse.getError()) {
                            getView().getMessageFail(rtcServiceBaseResponse.getMsg());
                            return;
                        }

                        GlobalUtils.showLog(TAG, "messages response: " +
                                rtcServiceBaseResponse.getRtcMessagesList());
                        if (!CollectionUtils.isEmpty(rtcServiceBaseResponse.getRtcMessagesList())) {
                            saveConversations(rtcServiceBaseResponse.getRtcMessagesList());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
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
    public void createPreConversationForImage(String imageUri, long orderId,
                                              String imageTitle, Bitmap bitmap) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        byte[] bitmapBytes = getBitmapBytesFromBitmap(bitmap);
        Conversation conversation = new Conversation();
        conversation.setClientId(clientId);
        conversation.setSenderId(userAccount.getAccountId());
        conversation.setMessageType(RtcProto.RtcMessageType.IMAGE_RTC_MESSAGE.name());
        conversation.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
        conversation.setRefId(orderId);
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
    public void createPreConversationForText(String message, long orderId, boolean link) {
        String clientId = UUID.randomUUID().toString().replace("-", "");
        GlobalUtils.showLog(TAG, "pre conversation text id: " + clientId);
        Conversation conversation = new Conversation();
        conversation.setClientId(clientId);
        conversation.setSenderId(userAccount.getAccountId());
        conversation.setMessage(message);
        if (link) conversation.setMessageType(RtcProto.RtcMessageType.LINK_RTC_MESSAGE.name());
        else conversation.setMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE.name());
        conversation.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
        conversation.setRefId(orderId);
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
    public void createPreConversationForDoc(long orderId, File file) {
        String clientId = UUID.randomUUID().toString().replace("-", "");
        int fileLength = Integer.parseInt(String.valueOf(file.length() / 1024));
        String fileSizeFormatted = getFileSize(fileLength);

        Conversation conversation = new Conversation();
        conversation.setClientId(clientId);
        conversation.setSenderId(userAccount.getAccountId());
        conversation.setMessageType(RtcProto.RtcMessageType.DOC_RTC_MESSAGE.name());
        conversation.setSenderType(RtcProto.MessageActor.ANDDONE_USER_MESSAGE.name());
        conversation.setRefId(orderId);
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
        for (Conversation conversation : conversationList
        ) {
            sendDeliveredMessage(conversation.getClientId(), conversation.getSenderId(),
                    conversation.getConversationId());
        }
    }

    private byte[] getBitmapBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        return byteArray;
    }

    private void saveConversations(List<RtcProto.RtcMessage> rtcMessagesList) {
        RealmList<Conversation> conversations = ProtoMapper.transformConversation(rtcMessagesList);
        ConversationRepo.getInstance().saveConversationList(conversations, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "all conversations saved");
                getView().getMessagesSuccess(conversations);
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save conversations");
            }
        });
    }
}

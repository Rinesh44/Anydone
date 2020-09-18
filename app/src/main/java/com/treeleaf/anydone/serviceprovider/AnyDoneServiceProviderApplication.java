package com.treeleaf.anydone.serviceprovider;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.injection.component.DaggerApplicationComponent;
import com.treeleaf.anydone.serviceprovider.injection.module.ApplicationModule;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Receiver;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConversationRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.LocaleHelper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import io.realm.RealmList;

public class AnyDoneServiceProviderApplication extends Application {
    private static final String TAG = "AnyDoneConsumerApplicat";
    private static Context context;

    private ApplicationComponent applicationComponent;

    public static AnyDoneServiceProviderApplication get(Context context) {
        return (AnyDoneServiceProviderApplication) context.getApplicationContext();
    }

    /**
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    /**
     * Provides the application context
     *
     * @return {@link Context Application Context}
     */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Hawk.init(this).build();

    /*    RealmInspectorModulesProvider realmInspectorModulesProvider = RealmInspectorModulesProvider.builder(this)
                .withDeleteIfMigrationNeeded(true)
                .build();

        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(realmInspectorModulesProvider)
                    .build());
        }*/

        initializeRealm();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TreeleafMqttClient.start(this, new TreeleafMqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    GlobalUtils.showLog(TAG, "mqtt topic: " + topic);
                    GlobalUtils.showLog(TAG, "mqtt message: " + message);
                }
            });
        }

        listenConversationMessages();


    }

    private void listenConversationMessages() {

        Employee userAccount = EmployeeRepo.getInstance().getEmployee();
        if (userAccount != null) {
            String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + userAccount.getAccountId();

            //listen for conversation thread messages
            TreeleafMqttClient.subscribe(SUBSCRIBE_TOPIC, new TreeleafMqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse
                            .parseFrom(message.getPayload());
                    String clientId = relayResponse.getRtcMessage().getClientId();

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

                    }
                }
            });
        }


    }

    public Application getApplicationObject() {
        return this;
    }

    public ApplicationComponent getApplicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }


    private void initializeRealm() {
        RealmUtils.init(this);
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
        conversation.setRefId((relayResponse.getRtcMessage().getRefId()));
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
                           /*     getView().onSubscribeSuccessMsg(conversation,
                                        relayResponse.getBotReply());*/
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


}

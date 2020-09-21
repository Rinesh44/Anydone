package com.treeleaf.anydone.serviceprovider.mqtt;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.serviceprovider.AnyDoneServiceProviderApplication;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Receiver;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConversationRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.InputStream;
import java.util.UUID;

import io.realm.RealmList;

/**
 * Created by Dipak Malla
 * Email dpakmalla@gmail.com
 * Created on 2/15/19.
 */
public class TreeleafMqttClient {
    private static final String TAG = "TreeleafMqttClient";
    private static final String MQTT_URI = Constants.MQTT_URI;

    private static final InputStream MQTT_CA_CERT = AnyDoneServiceProviderApplication.getContext()
            .getResources().openRawResource(AnyDoneServiceProviderApplication.getContext().getResources().
                    getIdentifier("cacert", "raw",
                            AnyDoneServiceProviderApplication.getContext().getPackageName()));
    private static final InputStream MQTT_CLIENT_CERT = AnyDoneServiceProviderApplication.getContext().
            getResources().openRawResource(AnyDoneServiceProviderApplication.getContext().getResources().
            getIdentifier("client_cert", "raw",
                    AnyDoneServiceProviderApplication.getContext().getPackageName()));
    public static final InputStream MQTT_SSL_KEY = AnyDoneServiceProviderApplication.getContext().
            getResources().openRawResource(AnyDoneServiceProviderApplication.getContext().getResources().
            getIdentifier("client_key", "raw",
                    AnyDoneServiceProviderApplication.getContext().getPackageName()));

    private static final String MQTT_SSL_KEY_PASSWORD = Constants.MQTT_SSL_KEY_PASSWORD;
    private static final String MQTT_USER = Constants.MQTT_USER;
    private static final String MQTT_PASSWORD = Constants.MQTT_PASSWORD;
    private static final int MAX_INFLIGHT = Constants.MAX_INFLIGHT;
    public static MqttClientPersistence mqttClientPersistence;
    public static MqttAndroidClient mqttClient;
    private static final int DEFAULT_QOS = 1;
    public static OnMQTTConnected mqttListener;
//    private static Map<String, Processor> processors = new ConcurrentHashMap<>();

    public TreeleafMqttClient() {
        //Empty on purpose
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean start(final Context context, TreeleafMqttCallback callback) {
        try {
            GlobalUtils.showLog(TAG, "Connecting to mqtt with URI : {}" + MQTT_URI);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(false);
            connectOptions.setAutomaticReconnect(true);
            connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            if (MQTT_URI.contains("ssl://")) {
                connectOptions.setSocketFactory(TreeleafSSL.getSocketFactory(MQTT_CA_CERT,
                        MQTT_CLIENT_CERT, MQTT_SSL_KEY, MQTT_SSL_KEY_PASSWORD));
            }
            if (null != MQTT_USER && null != MQTT_PASSWORD) {
                connectOptions.setUserName(MQTT_USER);
                connectOptions.setPassword(MQTT_PASSWORD.toCharArray());
                GlobalUtils.showLog(TAG,
                        "Connecting mqtt with provided username and password");
            }
            mqttClientPersistence = new MemoryPersistence();
            mqttClient = new MqttAndroidClient(context, MQTT_URI,
                    UUID.randomUUID().toString().replace("-", ""),
                    mqttClientPersistence);
            if (null != callback) {
                mqttClient.setCallback(callback);
            }
            connectOptions.setMaxInflight(MAX_INFLIGHT);
            final IMqttToken token = mqttClient.connect(connectOptions);
            GlobalUtils.showLog(TAG, "Initializing connection to mqtt on URI : {}" + MQTT_URI);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    GlobalUtils.showLog(TAG, "MQTT connected");
                    if (mqttListener != null)
                        mqttListener.mqttConnected();


                    listenConversationMessages();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    GlobalUtils.showLog(TAG, "Failed to connect MQTT: " +
                            exception.toString());
                    if (mqttListener != null)
                        mqttListener.mqttNotConnected();
                }
            });

            return true;
        } catch (Exception e) {
            GlobalUtils.showLog(TAG, "Error while connecting mqtt client " + e);
            return false;
        }
    }

    public static MqttAndroidClient getMqttClient() {
        return mqttClient;
    }

    public void stop() throws MqttException {
        if (null != mqttClient) {
            try {
                mqttClient.disconnect();
            } catch (MqttException ignore) {
                //Empty on purpose
            }
            mqttClient.close();
        }
        if (null != mqttClientPersistence) {
            try {
                mqttClientPersistence.clear();
            } catch (MqttPersistenceException ignore) {
                //Empty on purpose
            }
            try {
                mqttClientPersistence.close();
            } catch (MqttPersistenceException ignore) {
                //Empty on purpose
            }
        }
    }

    public static void listenConversationMessages() {

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

    public static Conversation createNewConversation(RtcProto.RelayResponse relayResponse) {
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

    public static void updateConversation(Conversation conversation,
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

    public static void publish(String topic, byte[] payload, TreeleafMqttCallback callback) {
        try {
            if (!mqttClient.isConnected()) {
                GlobalUtils.showLog(TAG, "Mqtt is not connected.");
                return;
            }

            IMqttToken pubToken = mqttClient.publish(topic, payload, DEFAULT_QOS, false);

            pubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    GlobalUtils.showLog(TAG, "message publish success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    GlobalUtils.showLog(TAG, "failed to publish msg" + exception.toString());
                }
            });

            if (null != callback) {
                mqttClient.setCallback(callback);
            }

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void subscribe(String topic, TreeleafMqttCallback callback) {
        try {
            mqttClient.subscribe(topic, DEFAULT_QOS, callback::messageArrived);
            mqttClient.setCallback(callback);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean unsubscribe(String topic) {
        try {
            IMqttToken unSubToken = mqttClient.unsubscribe(topic);
            unSubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    GlobalUtils.showLog(TAG, "Unsubscribe success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    GlobalUtils.showLog(TAG, "failed to unsubscribe" + exception.toString());
                }
            });
//            processors.remove(topic);

            return true;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void disconnect() {
        try {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
            IMqttToken disToken = mqttClient.disconnect();
            disToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    GlobalUtils.showLog(TAG, "Disconnected gracefully from : " + MQTT_URI);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    GlobalUtils.showLog(TAG, "Failed to disconnect from : " + MQTT_URI);
                    GlobalUtils.showLog(TAG, exception.toString());
                }
            });
//            processors.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public interface Processor {
        void process(MqttClient.Payload payload);
    }*/

    public interface OnMQTTConnected {
        void mqttConnected();

        void mqttNotConnected();
    }

    public static void setOnMqttConnectedListener(OnMQTTConnected listener) {
        mqttListener = listener;
    }

    public static String getMessageFromConversationType(RtcProto.RelayResponse response) {
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
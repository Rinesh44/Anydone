package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxConversation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import com.chinalwb.are.AREditText;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.util.List;

public class InboxConversationContract {
    public interface InboxConversationView extends BaseView {

        void getInboxSuccess(Inbox Inbox);

        void getInboxFail(String msg);

        void onUploadImageSuccess(String imageUrl,
                                  Uri imageUri,
                                  String clientId,
                                  String imageCaption);

        void onUploadImageFail(String msg, Conversation conversation);

        void onDocUploadFail(String msg, Conversation conversation);

        void onDocUploadSuccess(String docUrl, File file, String clientId);

        void onSubscribeSuccessMsg(Conversation conversation, boolean botReply);

        void onSubscribeFailMsg(Conversation conversation);

        void onConnectionSuccess();

        void onConnectionFail(String msg);

        void getMessagesSuccess(List<Conversation> conversationList, boolean showProgress);

        void getMessageFail(String msg);

        void onImagePreConversationSuccess(Conversation conversation);

        void onDocPreConversationSuccess(Conversation conversation);

        void onTextPreConversationSuccess(Conversation conversation);

        void onDeleteMessageSuccess();

        void setSeenStatus(Conversation conversation);

        void onSendDeliveredMsgFail(List<Conversation> conversationList);

        void onRemoteVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse);

        void onLocalVideoRoomJoinSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse);

        void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft);

        void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                          boolean b, AnydoneProto.ServiceContext context);

        void onVideoRoomInitiationSuccessClient(SignalingProto.BroadcastVideoCall broadcastVideoCall, AnydoneProto.ServiceContext context);

        void onHostHangUp(SignalingProto.VideoRoomHostLeft videoRoomHostLeft);

        void onMqttResponseReceivedChecked(String mqttResponseType, boolean localResponse);

        void onGetLinkDetailSuccess(Conversation conversation, RtcProto.LinkMessage linkMessage);

        void onGetLinkDetailFail(Conversation conversation);

    }

    public interface InboxConversationPresenter extends Presenter<InboxConversationContract.InboxConversationView> {

        void getInbox(String id);

        void uploadImage(Uri uri, Conversation conversation, Activity activity);

        void uploadDoc(Uri uri, Conversation conversation);

        void publishTextOrUrlMessage(String message, String InboxId, boolean linkFailCase);

        void publishImage(String imageUrl, String InboxId, String clientId, String imageCaption);

        void publishDoc(String docUrl, File file, String InboxId, String clientId);

        void subscribeSuccessMessage(String InboxId, String userAccountId) throws MqttException;

        void subscribeFailMessage() throws MqttException;

        void resendMessage(Conversation conversation);

        void checkConnection(MqttAndroidClient client);

        void getMessages(String refId, long from, long to, int pageSize, boolean showProgress);

        void createPreConversationForImage(String imageUri, String InboxId,
                                           String imageTitle, Bitmap bitmap);

        void createPreConversationForText(String message, String InboxId, boolean link);

        void createPreConversationForDoc(String InboxId, File file);

        void publishMessageDelete(Conversation message);

        void sendDeliveredStatusForMessages(List<Conversation> conversationList);

        void publishTextMessage(String message, String InboxId,
                                String userAccountId, String clientId);

        void publishLinkMessage(String message, String InboxId,
                                String userAccountId, String clientId, RtcProto.LinkMessage linkMessage);

        void setConversationAsFailed(Conversation conversation);

        void enterMessage(RecyclerView conversation, AREditText etMessage);

        void subscribeSuccessMessageAVCall(String ticketId, String userAccountId) throws MqttException;

        void subscribeFailMessageAVCall(String refId) throws MqttException;

        void unSubscribeAVCall(String ticketId, String accountId) throws MqttException;

        void getLinkDetails(String url, Conversation conversation);

    }
}

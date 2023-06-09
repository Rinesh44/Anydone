package com.anydone.desk.ticketdetails.ticketconversation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import com.chinalwb.are.AREditText;
import com.treeleaf.anydone.entities.SignalingProto;
import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.Attachment;
import com.anydone.desk.realm.model.Conversation;
import com.anydone.desk.realm.model.ServiceProvider;
import com.anydone.desk.realm.model.Tickets;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.util.List;

public class TicketConversationContract {

    public interface TicketConversationView extends BaseView {

        void getTicketSuccess(Tickets tickets);

        void getTicketFail(String msg);

        void onUploadImageSuccess(String imageUrl,
                                  Uri imageUri,
                                  String clientId,
                                  String imageCaption);

        void onUploadImageFail(String msg, Conversation conversation);

        void onDocUploadFail(String msg, Conversation conversation);

        void onDocUploadSuccess(String docUrl, File file, String clientId);

        void getServiceDoerSuccess();

        void getServiceDoerFail(String msg);

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

        void onLocalVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse);













        void onKgraphReply(Conversation conversation);

        void getSuggestionFail(String msg);

        void setAcceptedTag(ServiceProvider serviceProvider, long acceptedAt);

        /*void onImageDrawDiscardRemote(String accountId, String imageId);

        void onDrawTouchDown(CaptureDrawParam captureDrawParam, String accountId, String imageId);

        void onDrawTouchMove(CaptureDrawParam captureDrawParam, String accountId, String imageId);

        void onDrawTouchUp(String accountId, String imageId);

        void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId, String imageId, CaptureDrawParam captureDrawParam);

        void onDrawReceiveNewTextChange(String text, String id, String accountId, String imageId);

        void onDrawReceiveEdiTextRemove(String editTextId, String accountId, String imageId);

        void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId, String imageId);

        void onDrawCanvasCleared(String accountId, String imageId);

        void onDrawCollabInvite(SignalingProto.DrawCollab drawCollabResponse);

        void onDrawMaximize(SignalingProto.DrawMaximize drawMaximize);

        void onDrawMinimize(SignalingProto.DrawMinize drawMinize);

        void onDrawClose(SignalingProto.DrawClose drawClose);*/


        void onTaskStartSuccess(long estTime);

        void onTaskStartFail(String msg);

        void onUploadImageAttachmentSuccess(String url, String title);

        void onUploadImageAttachmentFail(String msg);

        void onUploadFileAttachmentSuccess(String url, String title);

        void onUploadFileAttachmentFail(String msg);

        void addAttachmentSuccess(Attachment attachment);

        void addAttachmentFail(String msg);

        void removeAttachmentSuccess(Attachment attachment);

        void removeAttachmentFail(String msg);

        void onKGraphPreConversationSuccess(Conversation conversation);

    }

    public interface TicketConversationPresenter extends Presenter<TicketConversationView> {

        void getTicket(long id);

        void uploadImage(Uri uri, Conversation conversation, Activity activity);

        void uploadDoc(Uri uri, Conversation conversation);

        void publishTextOrUrlMessage(String message, long orderId);

        void publishImage(String imageUrl, long orderId, String clientId, String imageCaption);

        void publishDoc(String docUrl, File file, long orderId, String clientId);

        void subscribeSuccessMessage(long orderId, String userAccountId) throws MqttException;

        void subscribeFailMessage() throws MqttException;

        void unSubscribeTicketConversation(String ticketId, String accountId) throws MqttException;

        void resendMessage(Conversation conversation);

        void checkConnection(MqttAndroidClient client);

        void getMessages(long refId, long from, long to, int pageSize, boolean showProgress);

        void createPreConversationForImage(String imageUri, long orderId,
                                           String imageTitle, Bitmap bitmap);

        void createPreConversationForText(String message, long orderId, boolean link);

        void createPreConversationForDoc(long orderId, File file);

        void publishMessageDelete(Conversation message);

        void sendDeliveredStatusForMessages(List<Conversation> conversationList);

        void publishTextMessage(String message, long orderId,
                                String userAccountId, String clientId);

        void publishLinkMessage(String message, long orderId,
                                String userAccountId, String clientId);

        void setConversationAsFailed(Conversation conversation);

        void getSuggestions(String nextMessageId, String knowledgeKey, String prevId, String prevKey,
                            String backId, String backKey, String clickedMsg,
                            long refId, boolean backClicked);

        void getServiceProviderInfo(Tickets tickets);

        void enterMessage(RecyclerView conversation, AREditText etMessage);

        void startTask(long ticketId);

        void uploadImageAttachment(Uri uri, Activity activity, String title);

        void uploadFileAttachment(Uri uri, String title);

        void addAttachment(long ticketId, Attachment attachment);

        void removeAttachment(long ticketId, Attachment attachment);

        void createPreConversationForKGraph(String message, long orderId);

    }
}


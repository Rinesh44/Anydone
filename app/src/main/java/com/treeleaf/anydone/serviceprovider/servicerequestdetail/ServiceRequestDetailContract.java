package com.treeleaf.anydone.serviceprovider.servicerequestdetail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class ServiceRequestDetailContract {

    public interface ServiceRequestDetailView extends BaseView {
//        void setDateTimeAttribute(String dateTime);

        void setFromDateAttribute(String date);

        void setToDateAttribute(String date);

        void setTimeAttribute(String time);

        void setLocationAttribute(String location);

        void getServiceRequestSuccess(ServiceRequest serviceRequest);

        void getServiceRequestFail(String msg);

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

        void getMessagesSuccess(List<Conversation> conversationList);

        void getMessageFail(String msg);

        void onImagePreConversationSuccess(Conversation conversation);

        void onDocPreConversationSuccess(Conversation conversation);

        void onTextPreConversationSuccess(Conversation conversation);

        void onDeleteMessageSuccess();

        void setSeenStatus(Conversation conversation);

        void onSendDeliveredMsgFail(List<Conversation> conversationList);

        void onLocalVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse);

        void onRemoteVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse);

        void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft);

        void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                          boolean b);

        void onVideoRoomInitiationSuccessClient(SignalingProto.BroadcastVideoCall broadcastVideoCall);

        void onHostHangUp(SignalingProto.VideoRoomHostLeft videoRoomHostLeft);

        void onKgraphReply(Conversation conversation);

        void getSuggestionFail(String msg);

        void setAttributesOnConversation(HashMap<String, String> availableAttributes,
                                         ServiceRequest serviceRequest);

        void setAcceptedTag(ServiceProvider serviceProvider, long acceptedAt);

        void onImageReceivedFromConsumer(int width, int height, long captureTime, byte[] convertedBytes, String accountId);

        void onImageCaptured();

        void onImageAckSent(String accountId);

        void onImageDrawDiscardLocal();

        void onImageDrawDiscardRemote(String accountId);

        void onDrawTouchDown(CaptureDrawParam captureDrawParam, String accountId);

        void onDrawTouchMove(CaptureDrawParam captureDrawParam, String accountId);

        void onDrawTouchUp(String accountId);

        void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId);

        void onDrawReceiveNewTextChange(String text, String id, String accountId);

        void onDrawReceiveEdiTextRemove(String editTextId, String accountId);

        void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId);

        void onDrawCanvasCleared(String accountId);

        void onRemoteDeviceConfigReceived(SignalingProto.StartDrawAcknowledgement startDrawAckResponse, String accountId);
    }

    public interface ServiceRequestDetailPresenter extends Presenter<ServiceRequestDetailView> {
        void checkAttributes(List<ServiceAttributes> serviceAttributes);

        void getServiceRequest(long id);

        void uploadImage(Uri uri, Conversation conversation, Activity activity);

        void uploadDoc(Uri uri, Conversation conversation);

        void getServiceDoers(long requestId);

        void publishTextOrUrlMessage(String message, long orderId);

        void publishImage(String imageUrl, long orderId, String clientId, String imageCaption);

        void publishDoc(String docUrl, File file, long orderId, String clientId);

        void subscribeSuccessMessage(long orderId, String userAccountId);

        void subscribeFailMessage();

        void resendMessage(Conversation conversation);

        void checkConnection(MqttAndroidClient client);

        void getMessages(long refId, long from, long to, int pageSize);

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

        void getSuggestions(String nextMessageId, long refId, boolean backClicked);

        void getAvailableAttributes(ServiceRequest serviceRequest);

        void getServiceProviderInfo(ServiceRequest serviceRequest);

        void enterMessage(RecyclerView conversation, TextInputEditText etMessage);

    }
}

package com.treeleaf.anydone.serviceprovider.threaddetails.threadconversation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.io.File;
import java.util.List;

public class ThreadConversationContract {

    public interface ThreadConversationView extends BaseView {

        void getThreadSuccess(Thread thread);

        void getThreadFail(String msg);

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

        void onKgraphReply(Conversation conversation);

        void getSuggestionFail(String msg);

        void setAcceptedTag(ServiceProvider serviceProvider, long acceptedAt);

        void onTaskStartSuccess();

        void onTaskStartFail(String msg);
    }

    public interface ThreadConversationPresenter extends Presenter<ThreadConversationView> {

        void getThread(long id);

        void uploadImage(Uri uri, Conversation conversation, Activity activity);

        void uploadDoc(Uri uri, Conversation conversation);

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

        void getServiceProviderInfo(Tickets tickets);

        void enterMessage(RecyclerView conversation, TextInputEditText etMessage);

        void startTask(long ticketId);

    }
}


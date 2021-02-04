package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxConversation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;

import com.chinalwb.are.AREditText;
import com.google.android.material.textfield.TextInputEditText;
import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

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

        void getMessagesSuccess(List<Conversation> conversationList);

        void getMessageFail(String msg);

        void onImagePreConversationSuccess(Conversation conversation);

        void onDocPreConversationSuccess(Conversation conversation);

        void onTextPreConversationSuccess(Conversation conversation);

        void onDeleteMessageSuccess();

        void setSeenStatus(Conversation conversation);

        void onSendDeliveredMsgFail(List<Conversation> conversationList);

    }

    public interface InboxConversationPresenter extends Presenter<InboxConversationContract.InboxConversationView> {

        void getInbox(String id);

        void uploadImage(Uri uri, Conversation conversation, Activity activity);

        void uploadDoc(Uri uri, Conversation conversation);

        void publishTextOrUrlMessage(String message, String InboxId);

        void publishImage(String imageUrl, String InboxId, String clientId, String imageCaption);

        void publishDoc(String docUrl, File file, String InboxId, String clientId);

        void subscribeSuccessMessage(String InboxId, String userAccountId) throws MqttException;

        void subscribeFailMessage() throws MqttException;

        void resendMessage(Conversation conversation);

        void checkConnection(MqttAndroidClient client);

        void getMessages(String refId, long from, long to, int pageSize);

        void createPreConversationForImage(String imageUri, String InboxId,
                                           String imageTitle, Bitmap bitmap);

        void createPreConversationForText(String message, String InboxId, boolean link);

        void createPreConversationForDoc(String InboxId, File file);

        void publishMessageDelete(Conversation message);

        void sendDeliveredStatusForMessages(List<Conversation> conversationList);

        void publishTextMessage(String message, String InboxId,
                                String userAccountId, String clientId);

        void publishLinkMessage(String message, String InboxId,
                                String userAccountId, String clientId);

        void setConversationAsFailed(Conversation conversation);

        void enterMessage(RecyclerView conversation, AREditText etMessage);

    }
}
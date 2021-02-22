package com.treeleaf.anydone.serviceprovider.reply;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import com.chinalwb.are.AREditText;
import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.util.List;

public class ReplyContract {
    public interface ReplyView extends BaseView {
        void getReplyThreadsSuccess(List<Conversation> conversationList);

        void getReplyThreadsFail(String msg);

        void onSendDeliveredMsgFail(List<Conversation> conversationList);

        void onImagePreConversationSuccess(Conversation conversation);

        void onDocPreConversationSuccess(Conversation conversation);

        void onTextPreConversationSuccess(Conversation conversation);

        void onConnectionSuccess();

        void onConnectionFail(String msg);

        void onSubscribeSuccessMsg(Conversation conversation, boolean botReply, int count);

        void onSubscribeFailMsg(Conversation conversation);

        void onDocUploadFail(String msg, Conversation conversation);

        void onDocUploadSuccess(String docUrl, File file, String clientId);

        void onUploadImageSuccess(String imageUrl,
                                  Uri imageUri,
                                  String clientId,
                                  String imageCaption);

        void onUploadImageFail(String msg, Conversation conversation);
    }

    public interface ReplyPresenter extends Presenter<ReplyContract.ReplyView> {
        void getReplyThreads(String msgId, boolean showProgress);

        void uploadImage(Uri uri, Conversation conversation, Activity activity);

        void uploadDoc(Uri uri, Conversation conversation);

        void publishTextOrUrlMessage(String message, String InboxId);

        void publishImage(String imageUrl, String InboxId, String clientId, String imageCaption,
                          String parentId);

        void publishDoc(String docUrl, File file, String InboxId, String clientId,
                        String parentId);

        void subscribeSuccessMessage(String InboxId, String userAccountId, String parentId) throws MqttException;

        void subscribeFailMessage() throws MqttException;

        void resendMessage(String parentId, Conversation conversation);

        void checkConnection(MqttAndroidClient client);

        void createPreConversationForImage(String imageUri, String InboxId,
                                           String imageTitle, Bitmap bitmap);

        void createPreConversationForText(String message, String InboxId, boolean link);

        void createPreConversationForDoc(String InboxId, File file);

        void publishTextMessage(String message, String InboxId,
                                String userAccountId, String clientId,
                                String parentId);

        void publishLinkMessage(String message, String InboxId,
                                String userAccountId, String clientId,
                                String parentId);

        void setConversationAsFailed(Conversation conversation);

        void enterMessage(RecyclerView conversation, AREditText etMessage);
    }
}

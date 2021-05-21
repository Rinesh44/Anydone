package com.anydone.desk.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Receiver extends RealmObject {

    @PrimaryKey
    private String receiverId;
    private String senderId;
    private String receiverType;
    private String messageStatus;

    public Receiver() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}

package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Conversation extends RealmObject {
    @PrimaryKey
    private String clientId;
    private String conversationId;
    private String senderId;
    private String message;
    private String messageType;
    private String senderType;
    private String senderImageUrl;
    private String messageStatus;
    private String imageDesc;
    private String imageOrientation;
    private String ticketTitle;
    private String ticketDesc;
    private long sentAt;
    private long savedAt;
    private String refId;
    private boolean sent;
    private boolean sendFail;
    private String fileName;
    private String fileSize;
    private String filePath;
    private String senderName;
    private String imageUri;
    private String serviceIconUrl;
    private String serviceName;
    private String problemStat;
    private String location;
    private String date;
    private String acceptedBy;
    private byte[] imageBitmap;
    private String callInitiateTime;
    private String callDuration;
    private boolean kGraphBack;
    private String kGraphTitle;
    private RealmList<ServiceDoer> serviceDoerList;
    private RealmList<KGraph> kGraphList;
    private RealmList<Receiver> receiverList;
    private RealmList<Tags> tagsList;


    public Conversation() {
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public String getTicketDesc() {
        return ticketDesc;
    }

    public void setTicketDesc(String ticketDesc) {
        this.ticketDesc = ticketDesc;
    }

    public RealmList<Tags> getTagsList() {
        return tagsList;
    }

    public void setTagsList(RealmList<Tags> tagsList) {
        this.tagsList = tagsList;
    }

    public String getkGraphTitle() {
        return kGraphTitle;
    }

    public void setkGraphTitle(String kGraphTitle) {
        this.kGraphTitle = kGraphTitle;
    }

    public RealmList<KGraph> getkGraphList() {
        return kGraphList;
    }

    public void setkGraphList(RealmList<KGraph> kGraphList) {
        this.kGraphList = kGraphList;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public boolean isSendFail() {
        return sendFail;
    }

    public byte[] getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(byte[] imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public void setSendFail(boolean sendFail) {
        this.sendFail = sendFail;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public RealmList<Receiver> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(RealmList<Receiver> receiverList) {
        this.receiverList = receiverList;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getImageOrientation() {
        return imageOrientation;
    }

    public void setImageOrientation(String imageOrientation) {
        this.imageOrientation = imageOrientation;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getSenderImageUrl() {
        return senderImageUrl;
    }

    public void setSenderImageUrl(String senderImageUrl) {
        this.senderImageUrl = senderImageUrl;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public long getSentAt() {
        return sentAt;
    }

    public void setSentAt(long sentAt) {
        this.sentAt = sentAt;
    }

    public long getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(long savedAt) {
        this.savedAt = savedAt;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getServiceIconUrl() {
        return serviceIconUrl;
    }

    public void setServiceIconUrl(String serviceIconUrl) {
        this.serviceIconUrl = serviceIconUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProblemStat() {
        return problemStat;
    }

    public void setProblemStat(String problemStat) {
        this.problemStat = problemStat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public RealmList<ServiceDoer> getServiceDoerList() {
        return serviceDoerList;
    }

    public void setServiceDoerList(RealmList<ServiceDoer> serviceDoerList) {
        this.serviceDoerList = serviceDoerList;
    }

    public String getCallInitiateTime() {
        return callInitiateTime;
    }

    public void setCallInitiateTime(String callInitiateTime) {
        this.callInitiateTime = callInitiateTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public boolean iskGraphBack() {
        return kGraphBack;
    }

    public void setkGraphBack(boolean kGraphBack) {
        this.kGraphBack = kGraphBack;
    }
}

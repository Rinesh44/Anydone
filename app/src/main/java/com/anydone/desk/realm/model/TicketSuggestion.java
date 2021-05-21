package com.anydone.desk.realm.model;

import android.os.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketSuggestion extends RealmObject {
    @PrimaryKey
    private String suggestionId;
    private long estimatedTime;
    private String conversationId;
    private long createdAt;
    private String customerId;
    private String customerName;
    private String customerImageUrl;
    private String messageId;
    private String messageText;
    private long messageSentAt;
    private String serviceId;
    private String source;
    private String status;
    private long updatedAt;

    public TicketSuggestion() {
    }

    protected TicketSuggestion(Parcel in) {
        suggestionId = in.readString();
        estimatedTime = in.readLong();
        conversationId = in.readString();
        createdAt = in.readLong();
        customerId = in.readString();
        customerName = in.readString();
        customerImageUrl = in.readString();
        messageId = in.readString();
        messageText = in.readString();
        messageSentAt = in.readLong();
        serviceId = in.readString();
        source = in.readString();
        status = in.readString();
        updatedAt = in.readLong();
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerImageUrl() {
        return customerImageUrl;
    }

    public void setCustomerImageUrl(String customerImageUrl) {
        this.customerImageUrl = customerImageUrl;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getMessageSentAt() {
        return messageSentAt;
    }

    public void setMessageSentAt(long messageSentAt) {
        this.messageSentAt = messageSentAt;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(String suggestionId) {
        this.suggestionId = suggestionId;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

package com.anydone.desk.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Thread extends RealmObject {
    @PrimaryKey
    String threadId;
    String customerName;
    String serviceId;
    String serviceProviderId;
    String customerId;
    String customerEmail;
    String customerPhone;
    String customerType;
    long createdAt;
    long updatedAt;
    long lastMessageDate;
    String customerImageUrl;
    String finalMessage;
    String source;
    String defaultLabelId;
    String defaultLabel;
    AssignEmployee assignedEmployee;
    boolean isImportant;
    boolean isFollowUp;
    long followUpDate;
    boolean botEnabled;
    boolean seen;
    RealmList<ConversationThreadLabel> labelRealmList;

    public Thread() {
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerImageUrl() {
        return customerImageUrl;
    }

    public void setCustomerImageUrl(String customerImageUrl) {
        this.customerImageUrl = customerImageUrl;
    }

    public String getFinalMessage() {
        return finalMessage;
    }

    public void setFinalMessage(String finalMessage) {
        this.finalMessage = finalMessage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(long lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getDefaultLabelId() {
        return defaultLabelId;
    }

    public void setDefaultLabelId(String defaultLabelId) {
        this.defaultLabelId = defaultLabelId;
    }

    public AssignEmployee getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(AssignEmployee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public boolean isBotEnabled() {
        return botEnabled;
    }

    public void setBotEnabled(boolean botEnabled) {
        this.botEnabled = botEnabled;
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public boolean isFollowUp() {
        return isFollowUp;
    }

    public void setFollowUp(boolean followUp) {
        isFollowUp = followUp;
    }

    public long getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(long followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public RealmList<ConversationThreadLabel> getLabelRealmList() {
        return labelRealmList;
    }

    public void setLabelRealmList(RealmList<ConversationThreadLabel> labelRealmList) {
        this.labelRealmList = labelRealmList;
    }
}

package com.anydone.desk.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Inbox extends RealmObject {
    @PrimaryKey
    String inboxId;
    String serviceId;
    String subject;
    String inboxType;
    RealmList<Participant> participantList;
    String createdByUserAccountId;
    String createdByUserEmail;
    String createdByUserPhone;
    String createdByUserAccountType;
    String createdByUserProfilePic;
    String createdByUserFullName;
    String createdByAccountType;
    String lastMsg;
    String notificationType;
    String lastMsgSender;
    String lastMsgSenderId;
    String lastMsgType;
    String participantAdminId;
    long lastMsgDate;
    long createdAt;
    long updatedAt;
    int unReadMessageCount;
    boolean seen;
    boolean selfInbox;
    boolean leftGroup;
    boolean isMember;
    boolean exists;

    public Inbox() {
    }

    public String getInboxId() {
        return inboxId;
    }

    public void setInboxId(String inboxId) {
        this.inboxId = inboxId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public RealmList<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(RealmList<Participant> participantList) {
        this.participantList = participantList;
    }

    public String getCreatedByUserAccountId() {
        return createdByUserAccountId;
    }

    public void setCreatedByUserAccountId(String createdByUserAccountId) {
        this.createdByUserAccountId = createdByUserAccountId;
    }

    public String getCreatedByUserEmail() {
        return createdByUserEmail;
    }

    public void setCreatedByUserEmail(String createdByUserEmail) {
        this.createdByUserEmail = createdByUserEmail;
    }

    public String getCreatedByUserPhone() {
        return createdByUserPhone;
    }

    public void setCreatedByUserPhone(String createdByUserPhone) {
        this.createdByUserPhone = createdByUserPhone;
    }

    public String getCreatedByUserAccountType() {
        return createdByUserAccountType;
    }

    public void setCreatedByUserAccountType(String createdByUserAccountType) {
        this.createdByUserAccountType = createdByUserAccountType;
    }

    public String getCreatedByUserProfilePic() {
        return createdByUserProfilePic;
    }

    public void setCreatedByUserProfilePic(String createdByUserProfilePic) {
        this.createdByUserProfilePic = createdByUserProfilePic;
    }

    public String getCreatedByUserFullName() {
        return createdByUserFullName;
    }

    public void setCreatedByUserFullName(String createdByUserFullName) {
        this.createdByUserFullName = createdByUserFullName;
    }

    public String getCreatedByAccountType() {
        return createdByAccountType;
    }

    public void setCreatedByAccountType(String createdByAccountType) {
        this.createdByAccountType = createdByAccountType;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public long getLastMsgDate() {
        return lastMsgDate;
    }

    public void setLastMsgDate(long lastMsgDate) {
        this.lastMsgDate = lastMsgDate;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getLastMsgSender() {
        return lastMsgSender;
    }

    public void setLastMsgSender(String lastMsgSender) {
        this.lastMsgSender = lastMsgSender;
    }

    public String getLastMsgSenderId() {
        return lastMsgSenderId;
    }

    public void setLastMsgSenderId(String lastMsgSenderId) {
        this.lastMsgSenderId = lastMsgSenderId;
    }

    public String getLastMsgType() {
        return lastMsgType;
    }

    public void setLastMsgType(String lastMsgType) {
        this.lastMsgType = lastMsgType;
    }

    public boolean isSelfInbox() {
        return selfInbox;
    }

    public void setSelfInbox(boolean selfInbox) {
        this.selfInbox = selfInbox;
    }

    public boolean isLeftGroup() {
        return leftGroup;
    }

    public void setLeftGroup(boolean leftGroup) {
        this.leftGroup = leftGroup;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getInboxType() {
        return inboxType;
    }

    public void setInboxType(String inboxType) {
        this.inboxType = inboxType;
    }

    public String getParticipantAdminId() {
        return participantAdminId;
    }

    public void setParticipantAdminId(String participantAdminId) {
        this.participantAdminId = participantAdminId;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public int getUnReadMessageCount() {
        return unReadMessageCount;
    }

    public void setUnReadMessageCount(int unReadMessageCount) {
        this.unReadMessageCount = unReadMessageCount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Inbox)) {
            return false;
        }

        Inbox otherMember = (Inbox) o;
        return otherMember.getInboxId().equals(getInboxId());
    }
}

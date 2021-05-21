package com.anydone.desk.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Participant extends RealmObject {
    @PrimaryKey
    String participantId;
    String inboxId;
    String role;
    AssignEmployee employee;
    String accountType;
    String notificationType;

    public Participant() {
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AssignEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(AssignEmployee employee) {
        this.employee = employee;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getInboxId() {
        return inboxId;
    }

    public void setInboxId(String inboxId) {
        this.inboxId = inboxId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Participant)) {
            return false;
        }

        Participant otherMember = (Participant) o;
        return otherMember.getParticipantId().equals(getParticipantId());
    }
}

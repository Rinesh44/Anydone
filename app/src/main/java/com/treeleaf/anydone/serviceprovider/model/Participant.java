package com.treeleaf.anydone.serviceprovider.model;

public class Participant {
    String participantId;
    String inboxId;
    String role;
    com.treeleaf.anydone.serviceprovider.model.AssignEmployee employee;
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

    public com.treeleaf.anydone.serviceprovider.model.AssignEmployee getEmployee() {
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
        if (!(o instanceof com.treeleaf.anydone.serviceprovider.realm.model.Participant)) {
            return false;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Participant otherMember = (com.treeleaf.anydone.serviceprovider.realm.model.Participant) o;
        return otherMember.getParticipantId().equals(getParticipantId());
    }
}
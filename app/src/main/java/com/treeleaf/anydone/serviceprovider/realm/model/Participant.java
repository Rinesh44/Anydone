package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Participant extends RealmObject {
    @PrimaryKey
    String participantId;
    String role;
    AssignEmployee employee;
    String accountType;

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
}
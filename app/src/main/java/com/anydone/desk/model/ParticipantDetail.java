package com.anydone.desk.model;

import com.anydone.desk.realm.model.Participant;

import io.realm.RealmList;

public class ParticipantDetail {

    private RealmList<Participant> participants;
    private String participantAdminId;

    public ParticipantDetail() {
    }

    public ParticipantDetail(RealmList<Participant> participants, String participantAdminId) {
        this.participants = participants;
        this.participantAdminId = participantAdminId;
    }

    public RealmList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(RealmList<Participant> participants) {
        this.participants = participants;
    }

    public String getParticipantAdminId() {
        return participantAdminId;
    }

    public void setParticipantAdminId(String participantAdminId) {
        this.participantAdminId = participantAdminId;
    }
}

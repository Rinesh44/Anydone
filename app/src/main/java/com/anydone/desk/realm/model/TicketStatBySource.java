package com.anydone.desk.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketStatBySource extends RealmObject {
    @PrimaryKey
    String id;
    int thirdParty;
    int manual;
    int phoneCall;
    int bot;

    public TicketStatBySource() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(int thirdParty) {
        this.thirdParty = thirdParty;
    }

    public int getManual() {
        return manual;
    }

    public void setManual(int manual) {
        this.manual = manual;
    }

    public int getPhoneCall() {
        return phoneCall;
    }

    public void setPhoneCall(int phoneCall) {
        this.phoneCall = phoneCall;
    }

    public int getBot() {
        return bot;
    }

    public void setBot(int bot) {
        this.bot = bot;
    }
}

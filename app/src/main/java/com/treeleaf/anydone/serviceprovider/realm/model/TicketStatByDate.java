package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketStatByDate extends RealmObject {
    @PrimaryKey
    String id;
    RealmList<TicketStatByStatus> ticketStatByStatusRealmList;

    public TicketStatByDate() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<TicketStatByStatus> getTicketStatByStatusRealmList() {
        return ticketStatByStatusRealmList;
    }

    public void setTicketStatByStatusRealmList(RealmList<TicketStatByStatus> ticketStatByStatusRealmList) {
        this.ticketStatByStatusRealmList = ticketStatByStatusRealmList;
    }
}

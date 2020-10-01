package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketStatByStatus extends RealmObject {
    @PrimaryKey
    String id;
    int closedTickets;
    int resolvedTickets;
    int unResolvedTickets;
    int newTickets;
    int totalTickets;
    int reOpenedTickets;
    String statType;
    long timestamp;

    public TicketStatByStatus() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getClosedTickets() {
        return closedTickets;
    }

    public void setClosedTickets(int closedTickets) {
        this.closedTickets = closedTickets;
    }

    public int getResolvedTickets() {
        return resolvedTickets;
    }

    public void setResolvedTickets(int resolvedTickets) {
        this.resolvedTickets = resolvedTickets;
    }

    public int getUnResolvedTickets() {
        return unResolvedTickets;
    }

    public void setUnResolvedTickets(int unResolvedTickets) {
        this.unResolvedTickets = unResolvedTickets;
    }

    public int getNewTickets() {
        return newTickets;
    }

    public void setNewTickets(int newTickets) {
        this.newTickets = newTickets;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getReOpenedTickets() {
        return reOpenedTickets;
    }

    public void setReOpenedTickets(int reOpenedTickets) {
        this.reOpenedTickets = reOpenedTickets;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }
}

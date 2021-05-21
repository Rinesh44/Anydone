package com.anydone.desk.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketStatByPriority extends RealmObject {
    @PrimaryKey
    String id;
    int highest;
    int high;
    int medium;
    int low;
    int lowest;

    public TicketStatByPriority() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHighest() {
        return highest;
    }

    public void setHighest(int highest) {
        this.highest = highest;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getLowest() {
        return lowest;
    }

    public void setLowest(int lowest) {
        this.lowest = lowest;
    }
}

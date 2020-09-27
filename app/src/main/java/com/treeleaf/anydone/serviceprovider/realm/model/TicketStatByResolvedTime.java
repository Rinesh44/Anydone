package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketStatByResolvedTime extends RealmObject {
    @PrimaryKey
    String id;
    long max;
    long avg;
    long min;

    public TicketStatByResolvedTime() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getAvg() {
        return avg;
    }

    public void setAvg(long avg) {
        this.avg = avg;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }
}

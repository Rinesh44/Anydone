package com.anydone.desk.realm.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MessageFilterData extends RealmObject {

    @PrimaryKey
    String serviceId;
    String searchQuery;
    long from;
    long to;
    boolean isImportant;
    boolean isFollowUp;
    RealmList<String> labels;
    RealmList<String> sources;

    public MessageFilterData() {
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public boolean isFollowUp() {
        return isFollowUp;
    }

    public void setFollowUp(boolean followUp) {
        isFollowUp = followUp;
    }

    public RealmList<String> getLabels() {
        return labels;
    }

    public void setLabels(RealmList<String> labels) {
        this.labels = labels;
    }

    public RealmList<String> getSources() {
        return sources;
    }

    public void setSources(RealmList<String> sources) {
        this.sources = sources;
    }
}

package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Label extends RealmObject {
    @PrimaryKey
    String labelId;
    long createdAt;
    String name;
    String serviceId;
    String spAccountId;
    long updatedAt;

    public Label() {
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSpAccountId() {
        return spAccountId;
    }

    public void setSpAccountId(String spAccountId) {
        this.spAccountId = spAccountId;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

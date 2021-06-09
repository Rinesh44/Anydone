package com.anydone.desk.realm.model;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ConversationThreadLabel extends RealmObject {
    @PrimaryKey
    private String labelId;
    private long createdAt;
    private String name;
    private String serviceId;
    private long updatedAt;

    public ConversationThreadLabel() {
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

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationThreadLabel label = (ConversationThreadLabel) o;
        return Objects.equals(labelId, label.labelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelId, createdAt, name, serviceId, updatedAt);
    }
}

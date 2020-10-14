package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketCategory extends RealmObject {
    @PrimaryKey
    String categoryId;
    long createdAt;
    String name;
    String serviceId;
    String spAccountId;
    long updatedAt;

    public TicketCategory() {
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

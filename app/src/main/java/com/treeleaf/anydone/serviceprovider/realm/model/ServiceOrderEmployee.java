package com.treeleaf.anydone.serviceprovider.realm.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ServiceOrderEmployee extends RealmObject {

    @PrimaryKey
    private long orderId;
    private String serviceId;
    private long createdAt;
    private long updatedAt;
    private RealmList<ServiceDoer> serviceDoerList;

    public ServiceOrderEmployee() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ServiceDoer> getServiceDoerList() {
        return serviceDoerList;
    }

    public void setServiceDoerList(RealmList<ServiceDoer> serviceDoerList) {
        this.serviceDoerList = serviceDoerList;
    }
}

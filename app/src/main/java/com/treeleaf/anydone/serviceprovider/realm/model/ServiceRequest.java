package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ServiceRequest extends RealmObject {
    @PrimaryKey
    private long serviceOrderId;
    private String serviceId;
    private String serviceName;
    private String problemStatement;
    private String language;
    private String status;
    private String serviceIconUrl;
    private String type;
    private long createdAt;
    private long acceptedAt;
    private long startedAt;
    private long completedAt;
    private long closedAt;
    private boolean isRemote;
    private ServiceProvider serviceProvider;
    private RealmList<ServiceAttributes> attributeList;

    public ServiceRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRemote() {
        return isRemote;
    }

    public void setRemote(boolean remote) {
        isRemote = remote;
    }

    public long getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(long acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public long getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(long closedAt) {
        this.closedAt = closedAt;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getServiceIconUrl() {
        return serviceIconUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setServiceIconUrl(String serviceIconUrl) {
        this.serviceIconUrl = serviceIconUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProblemStatement() {
        return problemStatement;
    }

    public void setProblemStatement(String problemStatement) {
        this.problemStatement = problemStatement;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public RealmList<ServiceAttributes> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(RealmList<ServiceAttributes> attributeList) {
        this.attributeList = attributeList;
    }
}